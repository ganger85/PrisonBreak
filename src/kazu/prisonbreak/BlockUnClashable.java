package kazu.prisonbreak;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * ’@‚¢‚Ä‚à‰ó‚ê‚È‚¢ƒuƒƒbƒN
 */
public class BlockUnClashable extends Block {

	public BlockUnClashable(int x, int y) {
		super(x, y);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(Color.GRAY);
		super.draw(canvas, paint);
	}

	@Override
	public boolean isClashPossible() {
		return false;
	}

}
