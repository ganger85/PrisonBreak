package kazu.prisonbreak;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * �@���Ă����Ȃ��u���b�N
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
