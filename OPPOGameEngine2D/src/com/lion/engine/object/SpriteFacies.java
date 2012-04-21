package com.lion.engine.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class SpriteFacies extends Facies {
	
	private Bitmap bmps[];
	
	private Sprite sprites[];// 精灵，简单动画。
	
	private Paint p = new Paint();

	@Override
	public void draw(Canvas c, float x, float y) {
		sprites[intCurrentIndex].draw(c, x, y, p);
	}

	@Override
	public void setResource(Context ctx,int[] ids) {
		// 取得相应的图片资源
		bmps = new Bitmap[ids.length];
		for (int i = 0; i < ids.length; i++) {
			bmps[i] = BitmapPool.getBitmap(ctx, ids[i]);
		}
		
	}

	/**
	 * 设置Sprites数组
	 * 
	 * @param ids
	 * @param useBmps
	 *            使用到的图片索引，通常由具体的GameObject类指定。
	 * @param frame
	 *            帧序列
	 * @param frameDelay
	 *            帧序列中每帧的延时
	 */
	public void setSprites(int[] useBmps, int[] row, int[] col,
			int[][] frame, int[][] frameDelay) {
		// 生成sprite对象
		sprites = new Sprite[frame.length];
		for (int i = 0; i < frame.length; i++) {
			sprites[i] = new Sprite(bmps[useBmps[i]], row[i], col[i], frame[i],
					frameDelay[i]);
		}
		
		// 清除临时的bmps
		for(int i=0; i<bmps.length; i++) {
			bmps[i] = null;
		}
		bmps = null;
	}

	@Override
	public int getWidth() {
		return sprites[intCurrentIndex].getWidth();
	}

	@Override
	public int getHeight() {
		return sprites[intCurrentIndex].getHeight();
	}

	@Override
	public boolean isActionComplete() {
		return sprites[intCurrentIndex].isActionComplete();
	}

}
