package kazu.prisonbreak.object;

import kazu.prisonbreak.util.WindowInfo;
import kazu.prisonbreak.view.PrisonBreakView;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class Block implements ActiveObject {

	private int x;
	private int y;
	private int lx;
	private int ly;

	public static int height ;
	public static int width ;
	

	public Block(int x, int y) {
		this.x = x * width ;
		this.y = y * height + PrisonBreakView.statusBarHeight;
		this.lx = this.x + width ; 
		this.ly = this.y + height;
	}
	
	static {
		WindowInfo windowInfo = WindowInfo.getInstance();
		height = windowInfo.getWindowHeight() / 40;
		width = windowInfo.getWindowWidth() /10 ;
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
