package com.lion.engine.object;

import android.content.Context;
import android.graphics.Canvas;

public class AniFacies extends Facies {
	
	private Ani anis[];// 复杂的拆分合并动画，以后有动画编辑工具后再使用。

	@Override
	public void draw(Canvas c, float x, float y) {
		anis[intCurrentIndex].draw();
	}

	@Override
	public void setResource(Context ctx, int[] ids) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isActionComplete() {
		// TODO Auto-generated method stub
		return false;
	}

}
