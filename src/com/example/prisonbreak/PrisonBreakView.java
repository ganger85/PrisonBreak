package com.example.prisonbreak;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


public class PrisonBreakView extends View {

	private int mMode = READY;
	public static final int PAUSE = 0; // 一時停止中
	public static final int READY = 1; // スタート画面
	public static final int RUNNING = 2;// 実行中
	public static final int LOSE = 3; // ゲームオーバー
	public static final int CLEAR = 4; // クリア
	public static final int BRICK_ROW = 10;
	public static final int BRICK_COL = 19;
	public static final int STATUS_BAR_HEIGHT = 50;
	
	
	
	// 最大リフレッシュレート
	private static final long DELAY_MILLIS = 1000 / 60;

	// 画面表示用のメッセージ
	private TextView mMessage;
	private TextView mPoint;
	private RefreshHandler mFieldHandler = new RefreshHandler();
	private Paint mPaint = new Paint();
	private Pad mPad;
	private ArrayList<Ball> mBalls = new ArrayList<Ball>();
	private Block[][] mBricks = new Block[BRICK_COL][BRICK_ROW];
	private int mBallsCount = 0;
	private int mStockBallCount = 0;
	private int mBricksCount = 0;

	private MotionEvent mTouchEvent;

	// コンストラクタ
	public PrisonBreakView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialProcess();
	}

	public PrisonBreakView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialProcess();
	}

	/**
	 * 初期処理
	 */
	private void initialProcess() {
		setFocusable(true);
	}

	/**
	 * 新しいゲームの作成
	 */
	private void newGame() {
		mBalls.clear();
		mBricksCount = 0;
		mStockBallCount = 5;
		mBallsCount = 0;
		for (int i = 0; i < BRICK_COL; i++) {
			for (int j = 0; j < BRICK_ROW; j++) {
				mBricksCount++;
				if(i % 5 == 1 && j % 5 == 1){
					mBricks[i][j] = new BlockUnClashable(i, j);
				} else if (i % 11 == 1 && j % 11 == 1){
					mBricks[i][j] = new BlockAddBall(i, j);
				} else if (i % 13 == 1 && j % 9 == 8){
					mBricks[i][j] = new BlockBallBigger(i, j);
				} else{
					mBricks[i][j] = new BlockNormal(i, j);
				}
			}
		}
		PrisonBreakView.this.invalidate();
	}

	public boolean onTouchEvent(MotionEvent event) {
		mTouchEvent = event;
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			switch (mMode) {
			case READY:
				setMode(RUNNING);
				addBall();
				break;
			case PAUSE:
				setMode(RUNNING);
				break;
			case RUNNING:
				if(mBallsCount == 0){
					addBall();
				}
				break;
			case LOSE:
				setMode(READY);
				break;
			case CLEAR:
				setMode(READY);
				break;
			}
		}
		return true;
	}

	public void setMode(int newMode) {
		int oldMode = mMode;
		mMode = newMode;

		if (newMode == RUNNING) {
			if (oldMode == READY) {
				newGame();

				Resources resource = getContext().getResources();
				CharSequence newMessage = resource
						.getText(R.string.new_ball_help);
				mMessage.setText(newMessage);
				mMessage.setVisibility(View.VISIBLE);
			} else if (oldMode == PAUSE) {
				if (mBallsCount == 0) {
					Resources resource = getContext().getResources();
					CharSequence newMessage = resource
							.getText(R.string.new_ball_help);
					mMessage.setText(newMessage);
				} else {
					mMessage.setVisibility(View.INVISIBLE);
				}
			}
			if (oldMode != RUNNING) {
				update();
			}
			return;
		}
		CharSequence newMessage = "";
		Resources resource = getContext().getResources();
		switch (newMode) {
		case PAUSE:
			newMessage = resource.getText(R.string.pause_message);
			break;
		case READY:
			newMessage = resource.getText(R.string.ready_message);
			break;
		case LOSE:
			newMessage = resource.getText(R.string.game_over_message);
			break;
		case CLEAR:
			newMessage = resource.getText(R.string.game_clear_message);
			break;
		}
		mMessage.setText(newMessage);
		mMessage.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mPad = new Pad(w, h);
		setMode(READY);
	}

	private boolean isBlockCrash(int xIndex, int yIndex) {
		if (yIndex >= BRICK_ROW || xIndex >= BRICK_COL) {
			return false;
		}
		if (mBricks[xIndex][yIndex] != null) {
			return true;
		}
		return false;
	}

	private void crashBlock(int xIndex, int yIndex) {
		if (yIndex >= BRICK_ROW || xIndex >= BRICK_COL
				|| mBricks[xIndex][yIndex] == null) {
			return;
		} 
		if(!mBricks[xIndex][yIndex].isClashPossible()){
			return;
		}
		PrisonBreakView.this.invalidate(mBricks[xIndex][yIndex].getRect());
		
		//ボールを増やすブロックに当たったので、ボールを増やす
		if (mBricks[xIndex][yIndex].getClass() == BlockAddBall.class){
			mStockBallCount++;
			addBall();
		}
		
		//ボールのサイズを倍にするブロックに当たったので、ボールのサイズを倍にする
		if (mBricks[xIndex][yIndex].getClass() == BlockBallBigger.class){
			for (int i = 0; i < this.mBallsCount; i++) {
				mBalls.get(i).setSize(mBalls.get(i).getSize() * 2);
			}
		}
		
		mBricks[xIndex][yIndex] = null;
		mBricksCount--;
		
		if (mBricksCount <= 0) {
			setMode(CLEAR);
		}
	}

	public void update() {

		invalidate();
		mFieldHandler.sleep(DELAY_MILLIS);

		if (mMode == RUNNING) {

			if (mTouchEvent != null) {
				PrisonBreakView.this.invalidate(mPad.getRect());
				mPad.onTouchEvent(mTouchEvent);
				mPad.update();
				PrisonBreakView.this.invalidate(mPad.getRect());
			}
			int xCrash;
			int yCrash;
			for (int i = 0; i < this.mBallsCount; i++) {
				xCrash = 0;
				yCrash = 0;
				Ball ball = this.mBalls.get(i);

				PrisonBreakView.this.invalidate(ball.getRect());
				ball.update();
				PrisonBreakView.this.invalidate(ball.getRect());

				int xIndex = (int) (ball.positionX / Block.WIDE);
				int yIndex = (int) ((ball.getPositionY() - STATUS_BAR_HEIGHT) / Block.HEIGHT);
				int lxIndex = (int) (ball.getlx() / Block.WIDE);
				int lyIndex = (int) ((ball.getly() - STATUS_BAR_HEIGHT) / Block.HEIGHT);

				if (isBlockCrash(xIndex, yIndex)) {
					xCrash++;
					yCrash++;
				}
				if (isBlockCrash(lxIndex, yIndex)) {
					xCrash--;
					yCrash++;
				}
				if (isBlockCrash(xIndex, lyIndex)) {
					xCrash++;
					yCrash--;
				}
				if (isBlockCrash(lxIndex, lyIndex)) {
					xCrash--;
					yCrash--;
				}
				crashBlock(xIndex, yIndex);
				crashBlock(xIndex, lyIndex);
				crashBlock(lxIndex, yIndex);
				crashBlock(lxIndex, lyIndex);

				if (yCrash > 0) {
					ball.topCrash(yIndex);
				} else if (yCrash < 0) {
					ball.downCrash(lyIndex);
				}
				if (xCrash > 0) {
					ball.leftCrash(xIndex);
				} else if (xCrash < 0) {
					ball.rightCrash(lxIndex);
				}

				if (mPad.y <= ball.getly()
						&& mPad.getly() >= ball.getPositionY()
						&& mPad.x <= ball.getlx()
						&& mPad.getlx() >= ball.positionX) { // ボールがパッドに当たった
					float newXSpeed;
					float newYSpeed;

					newXSpeed = ball.xSpeed + (ball.getcx() - mPad.getcx()) / 5;
					newYSpeed = -(ball.getySpeed() - Math.abs(ball.getcx()
							- mPad.getcx()) * 1.2f);
					if (newYSpeed > -10) {
						newYSpeed = -10;
					}

					ball.setXSpeed(newXSpeed);
					ball.setYSpeed(newYSpeed);

					if (ball.maxYSpeed < 15) {
						ball.maxYSpeed += 0.1f;
					}
				} else if (ball.getPositionY() > this.getHeight()) { // ボールが下に落ちた
					this.mBalls.remove(i);
					mBallsCount--;
					if (mBallsCount == 0) {

						if (mStockBallCount > 0) {
							Resources resource = getContext().getResources();
							CharSequence newMessage = resource
									.getText(R.string.new_ball_help);
							mMessage.setText(newMessage);
							mMessage.setVisibility(View.VISIBLE);

						} else {

							setMode(LOSE);
							return;
						}
					}
				}

			}
			mFieldHandler.sleep(DELAY_MILLIS);
		} else {
			PrisonBreakView.this.invalidate();
		}
	}

	@Override
	public void onDraw(Canvas canvas) {

		canvas.drawColor(Color.rgb(120, 140, 160));
		mPaint.setColor(Color.BLACK);
		canvas.drawRect(0, STATUS_BAR_HEIGHT, this.getWidth(),
				this.getHeight(), mPaint);

		mPad.draw(canvas, mPaint);

		for (int i = 0; i < this.mBallsCount; i++) {
			this.mBalls.get(i).draw(canvas, mPaint);
		}

		for (int i = 0; i < BRICK_COL; i++) {
			for (int j = 0; j < BRICK_ROW; j++) {
				if (mBricks[i][j] != null) {
					mBricks[i][j].draw(canvas, mPaint);
				}
			}
		}
	}

	public void setTextView(TextView message) {
		this.mMessage = message;
	}

	public void setPointTextView(TextView point) {
		this.mPoint = point;
	}

	private void addBall() {
		if (mMode == RUNNING && mStockBallCount > 0) {
			if (mBallsCount == 0) {
				mMessage.setVisibility(View.INVISIBLE);
			}
			mBalls.add(new Ball(this.getWidth() / 2, 300, -0.2f, -5, this
					.getWidth(), this.getHeight()));
			mBallsCount++;
			mStockBallCount--;
			Resources resource = getContext().getResources();
			CharSequence newMessage = resource
					.getText(R.string.stock_ball_count);
			mPoint.setText(newMessage.toString() + Integer.toString(mStockBallCount));
		}
	}

	// State load
	public void restoreState(Bundle icicle) {
		setMode(PAUSE);
		mMode = icicle.getInt("mode");
		mBalls = flaotsToBalls(icicle.getFloatArray("balls"));
	}

	private ArrayList<Ball> flaotsToBalls(float[] rawArray) {
		ArrayList<Ball> balls = new ArrayList<Ball>();

		int coordCount = rawArray.length;
		for (int index = 0; index < coordCount; index += 4) {
			Ball ball = new Ball(rawArray[index], rawArray[index + 1],
					rawArray[index + 2], rawArray[index + 3], this.getWidth(),
					this.getHeight());
			balls.add(ball);
		}
		return balls;
	}

	// State save
	public Bundle saveState(Bundle icicle) {
		icicle.putInt("mode", mMode);
		icicle.putFloatArray("balls", ballsToFloats(mBalls));
		return icicle;

	}

	private float[] ballsToFloats(ArrayList<Ball> cvec) {
		int count = cvec.size();
		float[] rawArray = new float[count * 4];
		for (int index = 0; index < count; index++) {
			Ball setBall = (Ball) cvec.get(index);
			rawArray[4 * index] = setBall.positionX;
			rawArray[4 * index + 1] = setBall.getPositionY();
			rawArray[4 * index + 2] = setBall.xSpeed;
			rawArray[4 * index + 3] = setBall.getySpeed();
		}
		return rawArray;
	}

	public void setText(TextView message) {
		this.mMessage = message;
	}

	// 一定時間待機後Updateを実行させる。 Updateは再度Sleepを呼ぶ
	class RefreshHandler extends Handler {
		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}

		@Override
		public void handleMessage(Message msg) {
			PrisonBreakView.this.update();
		}
	}
}
