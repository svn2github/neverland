package com.lion.engine.object;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.lion.engine.util.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Bitmap池
 * @author 郭子亮
 * 统一管理所有的Bitmap对象，避免重复加载资源。
 */

public class BitmapPool {
	
	private static HashMap<Integer, Bitmap> mBmps = new HashMap<Integer, Bitmap>();
	
	// 直接添加指定的资源到池中
	private static Bitmap addBitmap(Context context, int id) {
		Bitmap result = null;
		InputStream is = context.getResources().openRawResource(id);
		result = BitmapFactory.decodeStream(is);
		mBmps.put(new Integer(id), result);
		return result;
	}
	
	/**
	 * 从池中获得指定id的bitmap，如果不存在，立刻添加一张并返回新建的bitmap
	 * @param id
	 * @return
	 */
	public static Bitmap getBitmap(Context ctx, int id) {
		Bitmap result = mBmps.get(id);
		if(result == null) {
			result = addBitmap(ctx, id);
		}
		return result;
	}
	
	/**
	 * 回收所有bitmap资源
	 */
	public static void removeAllBmp() {	
		Iterator<Map.Entry<Integer, Bitmap>> iter = mBmps.entrySet().iterator();	
		while (iter.hasNext()) { 
		    Map.Entry<Integer, Bitmap> entry = iter.next(); 
		    Bitmap bmp = entry.getValue(); 
		    bmp.recycle();
		    bmp = null;
		} 
		mBmps.clear();
	}
	
}
