package com.example.prisonbreak;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * ���I�I�u�W�F�N�g�̃C���^�[�t�F�[�X
 */
public interface ActiveObject {
	 void update();
     void draw(Canvas canvas, Paint paint);
     Rect getRect();
}
