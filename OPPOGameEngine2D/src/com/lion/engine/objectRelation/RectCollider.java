package com.lion.engine.objectRelation;

import com.lion.engine.object.GameObject;
import com.lion.engine.util.Util;

/**
 * 矩形碰撞
 * @author 郭子亮
 *
 */

public class RectCollider {

	private GameObject gObj;
	
	public RectCollider(GameObject obj) {
		gObj = obj;
	}
	
	/**
	 * 判断与指定的矩形属性是否发生接触。
	 * @param other
	 * @return
	 */
	public boolean test(RectCollider other) {
		// 默认取的GameObject的位置和大小信息。可按实际情况修改。
		return Util.overlap(gObj.position.x + gObj.delta.x, 
				gObj.position.y + gObj.delta.y,
				gObj.facies.getWidth(), 
				gObj.facies.getHeight(),
				other.getgObj().position.x + other.getgObj().delta.x, 
				other.getgObj().position.y + other.getgObj().delta.y,
				other.getgObj().facies.getWidth(), 
				other.getgObj().facies.getHeight());
	}
	
	/**
	 * 判断与指定的圆形属性是否发生接触。
	 * @param other
	 * @return
	 */
	public boolean test(CircleCollider other) {
		boolean result = false;
		
		return result;
	}

	public GameObject getgObj() {
		return gObj;
	}
	
}
