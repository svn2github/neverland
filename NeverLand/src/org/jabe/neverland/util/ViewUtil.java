package org.jabe.neverland.util;

import org.jabe.neverland.R;
import org.jabe.neverland.activity.MoveViewDemo;
import org.jabe.neverland.view.FloatTextView;
import org.jabe.neverland.view.SearchDialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class ViewUtil {
	public static final String WINDOW_SERVICE = "window";

	public static WindowManager.LayoutParams addTopView(Context context, View view) {
		WindowManager wm = (WindowManager) context.getApplicationContext()
				.getSystemService(WINDOW_SERVICE);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
				| WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.alpha = 100;
		params.gravity = Gravity.LEFT|Gravity.TOP;
		params.format = PixelFormat.RGBA_8888;
		// 以屏幕左上角为原点，设置x、y初始值
		params.x = 0;
		params.y = 0;
		wm.addView(view, params);
		return params;
	}
	
	public static void showSearchUI(final MoveViewDemo context) {
		final SearchDialog dialog = new SearchDialog(context,
				R.style.main_dialog, R.layout.act_moveview_search_dialog);
		dialog.show();
	}

	public static void addTopView(Context context, View view,
			WindowManager.LayoutParams params) {
		WindowManager wm = (WindowManager) context.getApplicationContext()
				.getSystemService(WINDOW_SERVICE);
		View tv = new FloatTextView(context);
		wm.addView(tv, params);
	}

	public static Rect getWindowVisibleDisplayFrame(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame;
	}
}
