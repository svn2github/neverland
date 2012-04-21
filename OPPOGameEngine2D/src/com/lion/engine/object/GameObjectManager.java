package com.lion.engine.object;

import java.util.ArrayList;

import android.graphics.Canvas;

import com.lion.engine.main.UserInput;
import com.lion.engine.objectRelation.NormalObject;

/**
 * 
 * @author 郭子亮
 * 对象管理者
 *
 */

public class GameObjectManager {
	
	public static ArrayList<GameObject> gObjs = new ArrayList<GameObject>();
	
	public static void addObj(GameObject obj) {
		gObjs.add(obj);
	}
	
	public static void removeObj(GameObject obj) {
		gObjs.remove(obj);
	}
	
	/**
	 * 画所有对象的外观。
	 * @param c
	 */
	public static void drawAllObjects(Canvas c) {
		for(int i=0; i<gObjs.size(); i++) {
			GameObject gameObj = gObjs.get(i);
//			if(gameObj.isTouchable()) {
//				gameObj.drawBound(c);
//			}
			gameObj.draw(c);
		}
	}

	/**
	 * 取得触摸到的物体。得到的总是最外层的
	 * @param x 指肚中心位置，需要加个手指范围，使得触摸显得更加灵敏
	 * @param y
	 * @return
	 */
	public static GameObject getTouchObject(float x, float y) {
		GameObject result = null;
//		UserInput uit = UserInput.getInstance();
		for(int i=gObjs.size()-1; i>=0; i--) {
			GameObject gObj = gObjs.get(i);
			if(x + UserInput.FINGER_SCOPE >= gObj.drawPosition.x && x - UserInput.FINGER_SCOPE < gObj.drawPosition.x+gObj.facies.getWidth() &&
					y + UserInput.FINGER_SCOPE > gObj.drawPosition.y && y - UserInput.FINGER_SCOPE < gObj.drawPosition.y+gObj.facies.getHeight()) {
				if(gObj.touchable) {
					result = gObj;
					break;
				}
			}
		}
		return result;
	}
	
//	public boolean isOnPo
		
//	/**
//	 * 全碰撞，默认NormalObject
//	 */
//	public void kynetics() {
//		for(int i=0; i<gObjs.size(); i++) {
//			NormalObject gObj = (NormalObject)gObjs.get(i);
//			if(gObj.getDeltaX() != 0 || gObj.getDeltaY() != 0) {
//				for(int j=0; j<gObjs.size(); j++) {
//					NormalObject other = (NormalObject)gObjs.get(j);
//					gObj.test(other);
//				}
//			}
//		}
//	}
	
	public static void removeAllObjects() {
		gObjs.clear();
	}
}
