package com.lion.engine.objectRelation;

import com.lion.engine.object.GameObject;

public class CircleCollider {
	
	private GameObject gObj;
	
	public CircleCollider(GameObject obj) {
		gObj = obj;
	}
	
	/**
	 * 判断与指定的矩形属性是否发生接触。
	 * @param other
	 * @return
	 */
	public boolean test(RectCollider other) {
		boolean result = false;
		
		return result;
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
