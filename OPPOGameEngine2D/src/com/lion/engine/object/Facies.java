package com.lion.engine.object;

import android.content.Context;
import android.graphics.Canvas;

/**
 * 
 * @author 郭子亮 外形。 每个可视化的对象都要有一个完整的外形，来描述这个对象的行为
 *         每个外形根据类型，保存了1个至多个的Sprite，或者1个至多个Bitmap，或者1个至多个Movie。
 * 
 */

public abstract class Facies {
	
	public int intCurrentIndex;// 当前外观索引

	/**
	 * 
	 * @param c
	 * @param x
	 * @param y
	 * 不同类型的外形有不同的绘制方法。
	 * 
	 */
	public abstract void draw(Canvas c, float x, float y);

	public abstract void setResource(Context ctx, int[] ids);

	public abstract int getWidth(); 

	public abstract int getHeight(); 

	public abstract boolean isActionComplete();

}
