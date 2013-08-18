package kazu.prisonbreak;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

abstract class Block implements ActiveObject {

	private int x;
	private int y;
	private int lx;
	private int ly;

	public static final int HEIGHT = 20;
	public static final int WIDE = 40;

	public Block(int x, int y) {
		this.x = x * WIDE;
		this.y = y * HEIGHT + PrisonBreakView.STATUS_BAR_HEIGHT;
		this.ly = this.y + HEIGHT;
		this.lx = this.x + WIDE;
	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawRect(x, y, lx - 1, ly - 1, paint);
	}

	@Override
	public Rect getRect() {
		return (new Rect(x, y, lx, ly));
	}
	
	abstract public boolean isClashPossible();

}
