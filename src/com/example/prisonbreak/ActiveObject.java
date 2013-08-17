package com.example.prisonbreak;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 動的オブジェクトのインターフェース
 */
public interface ActiveObject {
	 void update();
     void draw(Canvas canvas, Paint paint);
     Rect getRect();
}
