package com.lion.engine.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.lion.engine.util.Util;

public class BitmapFacies extends Facies {

	public Bitmap bitmaps[];// 一个外形就一个图片，用于最简单的外形。

	private Paint p = new Paint();

	@Override
	public void draw(Canvas c, float x, float y) {
		Util.drawBitmap(c, bitmaps[intCurrentIndex], x, y, p);
	}

	@Override
	public void setResource(Context ctx, int[] ids) {
		bitmaps = new Bitmap[ids.length];
		for (int i = 0; i < ids.length; i++) {
			bitmaps[i] = BitmapPool.getBitmap(ctx, ids[i]);
		}
	}

	@Override
	public int getWidth() {
		return bitmaps[intCurrentIndex].getWidth();
	}

	@Override
	public int getHeight() {
		return bitmaps[intCurrentIndex].getHeight();
	}

	@Override
	public boolean isActionComplete() {
		return false;
	}
}
