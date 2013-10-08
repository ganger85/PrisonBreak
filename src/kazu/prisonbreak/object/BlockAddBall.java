package kazu.prisonbreak.object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 叩くとボールが増えるブロック
 */
public class BlockAddBall extends Block {

	public BlockAddBall(int x, int y) {
		super(x, y);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(Color.BLUE);
		super.draw(canvas, paint);
	}

	@Override
	public boolean isClashPossible() {
		return true;
	}

}
