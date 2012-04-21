package com.lion.engine.object;

import com.lion.engine.util.Util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Sprite {
	
	private Bitmap bitmap;
	private int frameCount;
	private int frames[];
	private int frameDelays[];
	private int delay;
	private int frameW;
	private int frameH;
	
	private boolean actionComplete;
	
	public Sprite(Bitmap bmp, int row, int col, int[] f, int[] fd) {
		bitmap = bmp;
		frameCount = 0;
		frames = f;
		frameW = bmp.getWidth()/col;
		frameH = bmp.getHeight()/row;
		frameDelays = fd;
		delay = 1;
	}
	
	public void draw(Canvas c, float x, float y, Paint p) {
		int row = frames[frameCount]/10;
		int col = frames[frameCount]%10;
		Util.drawRegion(c, bitmap, frameW*col, frameH*row, frameW, frameH, x, y, p);
		delay++;
		if(delay > frameDelays[frameCount]) {
			frameCount++;
			delay = 1;
			actionComplete = false;
		}
		if(frameCount > frames.length-1) {
			frameCount = 0;
			actionComplete = true;
		}
	}
	
	public int getWidth() {
		return frameW;
	}
	
	public int getHeight() {
		return frameH;
	}

	public boolean isActionComplete() {
		return actionComplete;
	}

}
