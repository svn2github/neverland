package com.lion.engine.objectRelation;

import com.lion.engine.object.GameObject;
import com.lion.engine.util.Util;

/**
 * 普通物体。
 * @author 郭子亮
 * 使用一种碰撞属性（矩形/圆形）
 * 
 */

public class NormalObject extends GameObject {
	
	private int range = 50;

	// 矩形碰撞属性。
	private RectCollider rectCollider;
	
	// 圆形碰撞属性。
	private CircleCollider circleCollider;
	
	/**
	 * 测试跟指定NormalObject是否接触
	 * @param other
	 * @return
	 */
	public boolean test(NormalObject other) {
		
		if(!inRange(other)) {
			return false;
		}
		
		if(rectCollider != null) {
			if(other.rectCollider != null) {
				return rectCollider.test(other.rectCollider);
			} else if(other.circleCollider != null) {
				return rectCollider.test(other.circleCollider);
			}
		} else if(circleCollider != null) {
			if(other.rectCollider != null) {
				return circleCollider.test(other.rectCollider);
			} else if(other.circleCollider != null) {
				return circleCollider.test(other.circleCollider);
			}
		}
		return false;
	}
	
	private boolean inRange(NormalObject other) {
		
		return Util.overlap(position.x + delta.x - range, 
				position.y + delta.y - range, 
				facies.getWidth() + (range>>1), 
				facies.getHeight() + (range>>1), 
				other.position.x + other.delta.x, 
				other.position.y + other.delta.y, 
				other.facies.getWidth(), 
				other.facies.getHeight());
		
	}

	public RectCollider getRectCollider() {
		return rectCollider;
	}

	public void setRectCollider(RectCollider rectCollider) {
		this.rectCollider = rectCollider;
	}

	public CircleCollider getCircleCollider() {
		return circleCollider;
	}

	public void setCircleCollider(CircleCollider circleCollider) {
		this.circleCollider = circleCollider;
	}

	@Override
	public boolean getTouchable() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
