package kazu.prisonbreak.object;

import kazu.prisonbreak.view.PrisonBreakView;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Ball implements ActiveObject {

	private int diameter = 16;
	private int color = Color.WHITE;
	
	private int screenWidth;

	// ボールの四隅（衝突判定に利用する）
	public static final int LEFT_TOP = 0;
	public static final int RIGHT_TOP = 1;
	public static final int LEFT_DOWN = 2;
	public static final int RIGHT_DOWN = 3;

	// ボールスピード
	private float xSpeed;
	private float ySpeed;
	private float maxYSpeed = 8f;
	private float maxXSpeed = 3f;

	// ボールの現在情報
	private float positionX;
	private float positionY;
//	private float acceleration = 0.15f;
	
	public int getDiameter() {
		return diameter;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}
	
	public float getxSpeed() {
		return xSpeed;
	}

	public float getMaxYSpeed() {
		return maxYSpeed;
	}

	public void setMaxYSpeed(float maxYSpeed) {
		this.maxYSpeed = maxYSpeed;
	}
	
	public float getPositionX() {
		return positionX;
	}
	
	public float getPositionY() {
		return positionY;
	}
	
	public float getySpeed() {
		return ySpeed;
	}
	
	public Ball(float x, float y, float xSpeed, float ySpeed, int screenWidth) {
		this.positionX = x;
		this.positionY = y;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.screenWidth = screenWidth;
	}

	public float getlx() {
		return positionX + diameter;
	}

	public float getcx() {
		return positionX + diameter / 2;
	}

	public float getly() {
		return positionY + diameter;
	}

	/*
	 * 画面の再描画範囲を返す
	 */
	@Override
	public Rect getRect() {
		return new Rect((int) this.positionX - 1, (int) this.positionY - 1,
				(int) this.getlx() + 1, (int) this.getly() + 1);
	}

	public void setXSpeed(float speed) {
		this.xSpeed = getNewSpeed(speed, maxXSpeed);
	}

	public void setYSpeed(float speed) {
		this.ySpeed = getNewSpeed(speed, maxYSpeed);
	}

	private float getNewSpeed(float newSpeed, float maxSpeed) {
		if (maxSpeed < Math.abs(newSpeed)) {
			if (newSpeed > 0) {
				return maxSpeed;
			} else {
				return -maxSpeed;
			}
		}
		return newSpeed;
	}

	private int getAfterCrashPoint(float speed, float position, int wall) {
		int intPosition = (int) position;
		int intSpeed = (int) speed;
		int newPoint = ((wall * 2) - intPosition - intSpeed);
		// 壁のめりこみ対策
		if ((speed > 0 && newPoint > wall) || (speed < 0 && newPoint < wall)) {
			// newPoint = wall;
		}
		return (newPoint);
	}

	@Override
	public void update() {
//		ySpeed += acceleration;
		if (positionX + xSpeed <= 0) {
			positionX = getAfterCrashPoint(xSpeed, positionX, 0);
			xSpeed *= -1.01f;
			xSpeed = getNewSpeed(xSpeed, maxXSpeed);
		} else {
			float lx = getlx();
			if (lx + xSpeed >= screenWidth) {
				positionX = getAfterCrashPoint(xSpeed, lx, screenWidth) - diameter;
				xSpeed *= -1.01f;
				xSpeed = getNewSpeed(xSpeed, maxXSpeed);
			} else {
				positionX += xSpeed;
			}
		}
		if (positionY + ySpeed <= PrisonBreakView.STATUS_BAR_HEIGHT) {
			positionY = getAfterCrashPoint(ySpeed, positionY, PrisonBreakView.STATUS_BAR_HEIGHT);
			ySpeed *= -0.9f;
			ySpeed = getNewSpeed(ySpeed, maxYSpeed);
		} else {
			positionY += ySpeed;
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(color);
		paint.setAntiAlias(true);
		int radius = diameter / 2;
		canvas.drawCircle(positionX + radius, positionY + radius, radius, paint);
	}

	public void topCrash() {
		positionY += ySpeed;
		ySpeed = Math.abs(ySpeed);
	}

	public void downCrash() {
		positionY += ySpeed;
		ySpeed = -Math.abs(ySpeed);
	}

	public void leftCrash() {
		positionX += xSpeed;
		xSpeed = Math.abs(xSpeed);
	}

	public void rightCrash() {
		positionX += xSpeed;
		xSpeed = -Math.abs(xSpeed);
	}

}
