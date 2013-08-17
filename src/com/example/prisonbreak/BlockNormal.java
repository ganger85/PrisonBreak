package com.example.prisonbreak;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 1度叩くと壊れるブロック
 */
public class BlockNormal extends Block {

	public BlockNormal(int x, int y) {
		super(x, y);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		paint.setColor(Color.RED);
		super.draw(canvas, paint);
	}

	@Override
	public boolean isClashPossible() {
		return true;
	}

}
