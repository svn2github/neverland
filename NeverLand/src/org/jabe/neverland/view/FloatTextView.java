package org.jabe.neverland.view;

import org.jabe.neverland.R;
import org.jabe.neverland.util.ViewUtil;

import android.content.Context;
import android.graphics.PixelFormat;
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
		this.setBackgroundResource(R.drawable.assist_anzai_left_green);
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
			break;
		case MotionEvent.ACTION_MOVE:
//			onDrag(x, y);
			updatePosition();
			break;
		case MotionEvent.ACTION_UP:
			updatePosition();
			startX = startY = 0;
			break;
		}
		return true;
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
