package kazu.prisonbreak.object;

import kazu.prisonbreak.util.WindowInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Pad implements ActiveObject {

	private int screenWide;

	// PadÉTÉCÉY
	public static final int HEIGHT = 10;
	public static int width ;
	public static final int WIDE_BLOCK = width / 5;
	public static final int HALF_WIDE = width / 2;
	
	private int color = Color.YELLOW;

	// PadÇÃåªç›èÓïÒ
	private float positionX;
	private static float positionY ;

	public float mTouchX;
	
	static {
		WindowInfo windowInfo = WindowInfo.getInstance();
		width = windowInfo.getWindowWidth() /5;
		positionY = windowInfo.getWindowHeight() - windowInfo.getWindowHeight() / 6;
	}
	
	public float getPositionX() {
		return positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public float getlx() {
		return (positionX + width);
	}

	public float getly() {
		return (positionY + HEIGHT);
	}
	
	public float getcx() {
		return positionX + width/2;
	}

	public int colArea(float pointX) {
		if (pointX < positionX) {
			return 0;
		} else {
			return (int) (pointX - positionX) / WIDE_BLOCK + 1;
		}
	}

	public void onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			mTouchX = event.getX();
		}
	}

	public Pad(int screenWidth) {
		screenWide = screenWidth;
	}

	@Override
	public void update() {
		positionX = mTouchX - HALF_WIDE;
		if (positionX < 0) {
			positionX = 0;
		} else if (getlx() > screenWide) {
			positionX = screenWide - width;
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(color);
		canvas.drawRect(positionX, positionY, positionX + width, positionY + HEIGHT, paint);
	}

	@Override
	public Rect getRect() {
		return null;
	}

}
