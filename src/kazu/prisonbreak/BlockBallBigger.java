package kazu.prisonbreak;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * �{�[���̃T�C�Y��2�{�ɂ���u���b�N
 */
public class BlockBallBigger extends Block {

	public BlockBallBigger(int x, int y) {
		super(x, y);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(Color.YELLOW);
		super.draw(canvas, paint);
	}

	@Override
	public boolean isClashPossible() {
		return true;
	}

}
