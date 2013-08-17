package com.example.prisonbreak;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Ball implements ActiveObject {

	private int mScreenWide;

	// ボールサイズ
	private int size = 16;
	private int statusBarHeight ;

	// ボールの四隅（衝突判定に利用する）
	public static final int LEFT_TOP = 0;
	public static final int RIGHT_TOP = 1;
	public static final int LEFT_DOWN = 2;
	public static final int RIGHT_DOWN = 3;

	// ボールスピード
	public float xSpeed;
	private float ySpeed;
	public float maxYSpeed = 8f;
	public float maxXSpeed = 3f;

	// ボールの現在情報
	public float positionX;
	private float positionY;
	public float acceleration = 0.15f;
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public float getPositionY() {
		return positionY;
	}
	
	public float getySpeed() {
		return ySpeed;
	}

	public Ball(float x, float y, float xSpeed, float ySpeed, int w, int h) {
		this.positionX = x;
		this.positionY = y;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.mScreenWide = w;
		this.statusBarHeight = PrisonBreakView.STATUS_BAR_HEIGHT;
	}

	public float getlx() {
		return positionX + size;
	}

	public float getcx() {
		return positionX + size / 2;
	}

	public float getly() {
		return positionY + size;
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
			if (lx + xSpeed >= mScreenWide) {
				positionX = getAfterCrashPoint(xSpeed, lx, mScreenWide) - size;
				xSpeed *= -1.01f;
				xSpeed = getNewSpeed(xSpeed, maxXSpeed);
			} else {
				positionX += xSpeed;
			}
		}
		if (positionY + ySpeed <= statusBarHeight) {
			positionY = getAfterCrashPoint(ySpeed, positionY, statusBarHeight);
			ySpeed *= -0.9f;
			ySpeed = getNewSpeed(ySpeed, maxYSpeed);
		} else {
			positionY += ySpeed;
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		int radius = size / 2;
		canvas.drawCircle(positionX + radius, positionY + radius, radius, paint);
	}

	public void topCrash(int index) {
		positionY += ySpeed;
		ySpeed = Math.abs(ySpeed);
	}

	public void downCrash(int index) {
		positionY += ySpeed;
		ySpeed = -Math.abs(ySpeed);
	}

	public void leftCrash(int index) {
		positionX += xSpeed;
		xSpeed = Math.abs(xSpeed);
	}

	public void rightCrash(int index) {
		positionX += xSpeed;
		xSpeed = -Math.abs(xSpeed);
	}

}
