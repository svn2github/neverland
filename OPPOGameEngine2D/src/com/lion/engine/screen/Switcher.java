package com.lion.engine.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.lion.engine.util.Util;

/**
 * 
 * @author 郭子亮 
 * 屏幕切换器，包括切换特效(淡入淡出)
 * 
 */

public class Switcher {

	public static final int IDLE = 0;
	public static final int OUT = 1;
	public static final int LOADING = 2;
	public static final int IN = 3;
	public int status = IDLE;
	
	private int count;
	public final int COUNT_MAX = 20;

	private int color = Color.BLACK;
	private int alpha = 0;
	private int step = 255 / COUNT_MAX;
	private Paint p = new Paint();
	
	OnLoadingListener onLoadingListener;
	
	public void setOnLoadingListener(OnLoadingListener listener) {
		onLoadingListener = listener;
	}
	
	/**
	 * 开始切换屏幕
	 */
	public void begin() {
		status = OUT;
		count = 0;
	}
	
	public void end() {
		status = IN;
		count = 0;
	}

	public void refresh() {

		if (status == IDLE) {
			return;
		} else if (status == LOADING) {
		} else {// in 和 out
			count++;
			if (count == COUNT_MAX) {
				status++;
				count = 0;
				if (status == LOADING) {
					onLoadingListener.OnLoading();
				}
				if (status == 4) {
					count = 0;
					status = IDLE;
				}
			}
		}

		switch (status) {
		case OUT:
			alpha += step;
			if(alpha > 255) {
				alpha = 255;
			}
			break;
		case LOADING:
			break;
		case IN:
			alpha -= step;
			if(alpha < 0) {
				alpha = 0;
			}
			break;
		}
		
	}

	public void draw(Canvas canvas) {
		switch (status) {
		case IDLE:
			// canvas.drawText("IDEL:"+count, 100, 200, p);
			break;
		case OUT:
			p.setColor(color);
			p.setAlpha(alpha);
			canvas.drawRect(0, 0, Util.scrWidth, Util.scrHeight, p);
			break;
		case LOADING:
			canvas.drawColor(color);
			
			p.setTextSize(20);
			p.setAlpha(255);
			p.setColor(Color.WHITE);
			canvas.drawText("LOADING:" + count, 100, 200, p);
			break;
		case IN:
			p.setColor(color);
			p.setAlpha(alpha);
			canvas.drawRect(0, 0, Util.scrWidth, Util.scrHeight, p);
			break;
		}
	}
	
	public interface OnLoadingListener {
		public void OnLoading();
	}

}
