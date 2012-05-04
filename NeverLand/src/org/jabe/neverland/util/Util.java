package org.jabe.neverland.util;

import android.content.Context;
import android.widget.Toast;

public class Util {
	public static final int TOAST_DEFAULT_DURATION = 1000;
	
	public static void showToast(Context ctx, String content) {
		Toast.makeText(ctx, content, TOAST_DEFAULT_DURATION).show();
	}
}
