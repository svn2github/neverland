package com.lion.engine.screen;

import com.lion.engine.activity.BaseGameActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;

/**
 * 
 * @author 郭子亮
 * 屏幕基类
 * 声明了屏幕的基本行为
 *
 */

public abstract class BaseScreen {
	
	public Context context;
	
	public OnScreenChangeListener onScreenChangeListener;
	
	public BaseScreen(Context c) {
		context = c;
	}
	
	/**
	 * 处理用户输入
	 */
	public abstract void handleInput();
	
	/**
	 * 刷新屏幕数据
	 */
	public abstract void refreshData();
	
	/**
	 * 绘制屏幕
	 * @param canvas
	 */
	public abstract void draw(Canvas canvas);
	
	/**
	 * 加载资源
	 */
	public abstract void loadRes();
	
	/**
	 * 释放资源
	 */
	public abstract void releaseRes();
	
	public interface OnScreenChangeListener {
		public void changeScreen(BaseScreen screen);
	}
	
	public void setOnScreenChangeListener(OnScreenChangeListener listener) {
		onScreenChangeListener = listener;
	}

	public Context getContext() {
		return context;
	}
	
	public void pauseGame() {
		Intent it = new Intent(BaseGameActivity.INTENT_PAUSE_GAME);
		context.sendBroadcast(it);
	}

}
