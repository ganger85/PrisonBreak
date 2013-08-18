package com.example.prisonbreak;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Pad implements ActiveObject {

	private int screenWide;

	// PadÉTÉCÉY
	public static final int HEIGHT = 10;
	public static final int WIDE = 200;
	public static final int WIDE_BLOCK = WIDE / 5;
	public static final int HALF_WIDE = WIDE / 2;
	
	private int color = Color.YELLOW;

	// PadÇÃåªç›èÓïÒ
	private float positionX;
	private float positionY = 1000;

	public float mTouchX;
	
	public float getPositionX() {
		return positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public float getlx() {
		return (positionX + WIDE);
	}

	public float getly() {
		return (positionY + HEIGHT);
	}
	
	public float getcx() {
		return positionX + WIDE/2;
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
			positionX = screenWide - WIDE;
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(color);
		canvas.drawRect(positionX, positionY, positionX + WIDE, positionY + HEIGHT, paint);
	}

	@Override
	public Rect getRect() {
		return null;
	}

}
