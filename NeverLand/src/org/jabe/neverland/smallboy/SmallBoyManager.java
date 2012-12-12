package org.jabe.neverland.smallboy;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.WindowManager;

public class SmallBoyManager extends IBoyManager {
	
	@Override
	public void showBoy(Activity activity, IBoy boy) {
		if (boy.getWindowLayoutParams() == null) {
			WindowManager.LayoutParams params = ViewUtil.getLeftTopWindowLayout();
			Rect r = ViewUtil.getWindowVisibleDisplayFrame(activity);
			switch (boy.getPositionStatus()) {
			case IBoy.POSITION_STATUS_LEFT:
				params.x = 0;
				params.y = 0;
				break;
			case IBoy.POSITION_STATUS_RIGHT:
				params.x = r.right;
				params.y = 0;
				break;
			case IBoy.POSITION_STATUS_TOP:
				break;
			case IBoy.POSITION_STATUS_BOTTOM:
				break;
			default:
				break;
			}
			showBoyWithLayout(activity, boy, params);
		} else {
			showBoyWithLayout(activity, boy, boy.getWindowLayoutParams());
		}
	}

	private void showBoyWithLayout(Context activity, IBoy boy,
			WindowManager.LayoutParams layoutParams) {
		if (hasBoy(boy)) {
			return;
		}
		addBoy(boy);
		boy.setWindowLayoutParams(layoutParams);
		ViewUtil.addViewToTop(activity, boy.getRootView(), layoutParams);
		boy.init();
	}

	private IBoy mDefaultBoy = null;

	public void showDefaultBoy(Activity activity, IBoyAction callback) {
		if (mDefaultBoy == null) {
			mDefaultBoy = new FloatTextView(activity);
			mDefaultBoy.setToolBarHight(ViewUtil
					.getWindowVisibleDisplayFrame(activity).top);
			final String position = "";
			final WindowManager.LayoutParams params = ViewUtil.getLeftTopWindowLayout();
			if (!TextUtils.isEmpty(position)) {
				try {
					params.x = Integer.parseInt(position.split(";")[0]);
					params.y = Integer.parseInt(position.split(";")[1]);
				} catch (Exception e) {
				}
			}
			mDefaultBoy.setWindowLayoutParams(params);
			mDefaultBoy.setCallback(callback);
			showBoy(activity, mDefaultBoy);
		} else {
			mDefaultBoy.setCallback(callback);
			showBoyWithLayout(activity, mDefaultBoy, mDefaultBoy.getWindowLayoutParams());
		}
	}
	
	public IBoy getDefaultBoy() {
		return mDefaultBoy;
	}

	public void dimissDefaultBoy(Activity activity) {
		if (mDefaultBoy == null) {
			return;
		}
		dismisBoy(activity, mDefaultBoy);
	}

	@Override
	public void dismisBoy(Activity activity, IBoy boy) {
		if (boy != null && hasBoy(boy)) {
			boy.destroy();
			ViewUtil.removeViewInTop(activity, boy.getRootView());
			removeBoy(boy);
		}
	}

	@Override
	public void showBoy(Context context, IBoy boy) {
		if (boy.getWindowLayoutParams() == null) {
			WindowManager.LayoutParams params = ViewUtil.getLeftTopWindowLayout();
			showBoyWithLayout(context, boy, params);
		} else {
			showBoyWithLayout(context, boy, boy.getWindowLayoutParams());
		}
	}

	@Override
	public void dismisBoy(Context context, IBoy boy) {
		if (boy != null && hasBoy(boy)) {
			ViewUtil.removeViewInTop(context, boy.getRootView());
			removeBoy(boy);
		}
	}
}
