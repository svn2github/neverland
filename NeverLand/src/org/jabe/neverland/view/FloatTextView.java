package org.jabe.neverland.view;

import org.jabe.neverland.R;
import org.jabe.neverland.util.ViewUtil;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class FloatTextView extends TextView {
	private final String TAG = TextView.class.getSimpleName();
	public static WindowManager.LayoutParams params;
	public static int TOOL_BAR_HIGH = 0;
	private float startX;
	private float startY;
	private WindowManager wm = (WindowManager) getContext()
			.getApplicationContext().getSystemService(ViewUtil.WINDOW_SERVICE);
	private float x;
	private float y;

	public FloatTextView(Context context) {
		super(context);
		changeBG2Left();
	}

	private void changeBG2Left() {
		this.setBackgroundResource(R.drawable.act_float_left_selector);
	}
	
	private void changeBG2Right() {
		this.setBackgroundResource(R.drawable.act_float_right_selector);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 触摸点相对于屏幕左上角坐标
		x = event.getRawX();
		y = event.getRawY() - TOOL_BAR_HIGH;
		Log.d(TAG, "------getX: " + event.getX() + "------getY:" + event.getY());
		Log.d(TAG, "------getRawX: " + event.getRawX() + "------getRawY:"
				+ event.getRawY());
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			startY = event.getY() + this.getHeight()/2;
			changeBG2Mid();
			break;
		case MotionEvent.ACTION_MOVE:
			updatePosition();
			break;
		case MotionEvent.ACTION_UP:
			endUp();
			break;
		}
		return true;
	}

	private void changeBG2Mid() {
		this.setBackgroundResource(R.drawable.assist_anzai_middle_green);
	}
	private void endUp() {
		DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
		if ((int) (x - startX) > width/2) {
			params.x = width;
			changeBG2Right();
		} else {
			params.x = 0;
			changeBG2Left();
		}
		params.y = (int) (y - startY);
		wm.updateViewLayout(this, params);
		startX = startY = 0;
	}
	
	/**
	 * 更新浮动窗口位置参数
	 */
	private void updatePosition() {
		params.x = (int) (x - startX);
		params.y = (int) (y - startY);
		wm.updateViewLayout(this, params);
	}
}
