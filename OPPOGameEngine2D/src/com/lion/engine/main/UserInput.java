package com.lion.engine.main;

import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * 
 * @author 郭子亮 用户输入处理
 * 
 */

public class UserInput {


	public static boolean acceptTouch;// 触摸接收，延时处理，确保帧中途获得的用户输入在全帧都有效。
	public static boolean acceptKey;// 按键接收。效果同上

	// 触摸屏相关
	public static int action;// 从View的OnTouchEvent中获得的原始Action，对应MotionEvent的DOWN，MOVE，UP
	
	public static float touchX;
	public static float touchY;
	public static boolean motionDownShouldExistOneFrame;// 由于触摸很容易在一帧中多次响应，会由down，直接切到move，而少了down的操作。
	public static final int RELEASE = 0;
	public static final int PRESS = 1;
	public static final int CLICK = 2;
	public static final int DRAG = 3;
	public static int touchAction = RELEASE;// 将原始的Action解释成具体的屏幕操作：RELEASE，PRESS，CLICK。
	
	// 指肚范围，以触摸点为中心，扩展响应范围。
	public static final float FINGER_SCOPE = 5;

	// 按键相关
	public static int keyCode;
	// 长短按键变量，1-按下，0-没按。
	public static int keyHomeLong, keyMenuLong, keyBackLong, keySearchLong,
			keyCallLong, keyEndCallLong, keyPlusLong, keyMinusLong;// 参考HTC
																	// G2的按键
	public static int keyHomeShort, keyMenuShort, keyBackShort, keySearchShort,
			keyCallShort, keyEndCallShort, keyPlusShort, keyMinusShort;// 参考HTC
																		// G2的按键


	/**
	 * 清理用户输入，在帧的结尾要调用
	 */
	public static void clearUserInput() {
		if (touchAction == CLICK) {
			
			touchAction = RELEASE;
			
		}
		motionDownShouldExistOneFrame = false;
		clearShortKeys();
	}

	/**
	 * 设置用户输入，在每帧开始时调用
	 */
	public static void setUserInput() {
		setTouchAction();
		setShortKeys();
	}

	/**
	 * 将原始的MotionEvent解释成实际的触摸操作。
	 */
	private static void setTouchAction() {
		if (acceptTouch) {
			if (action == MotionEvent.ACTION_UP) {
				touchAction = CLICK;
			} else if (action == MotionEvent.ACTION_DOWN) {
				touchAction = PRESS;
			} else if (action == MotionEvent.ACTION_MOVE) {
				touchAction = DRAG;
			}
			acceptTouch = false;
		}
	}

	/**
	 * 设置短按键
	 */
	private static void setShortKeys() {
		if (acceptKey) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_HOME:
				keyHomeShort = 1;
				break;
			case KeyEvent.KEYCODE_MENU:
				keyMenuShort = 1;
				break;
			case KeyEvent.KEYCODE_BACK:
				keyBackShort = 1;
				break;
			case KeyEvent.KEYCODE_SEARCH:
				keySearchShort = 1;
				break;
			case KeyEvent.KEYCODE_CALL:
				keyCallShort = 1;
				break;
			case KeyEvent.KEYCODE_ENDCALL:
				keyEndCallShort = 1;
				break;
			case KeyEvent.KEYCODE_PLUS:
				keyPlusShort = 1;
				break;
			case KeyEvent.KEYCODE_MINUS:
				keyMinusShort = 1;
				break;
			}
			acceptKey = false;
		}
	}

	/**
	 * 清除短按键
	 */
	public static void clearShortKeys() {
		keyHomeShort = 0;
		keyMenuShort = 0;
		keyBackShort = 0;
		keySearchShort = 0;
		keyCallShort = 0;
		keyEndCallShort = 0;
		keyPlusShort = 0;
		keyMinusShort = 0;
	}

	/**
	 * 设置长按键
	 */
	public static void setLongKeys() {
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			keyHomeLong = 1;
			break;
		case KeyEvent.KEYCODE_MENU:
			keyMenuLong = 1;
			break;
		case KeyEvent.KEYCODE_BACK:
			keyBackLong = 1;
			break;
		case KeyEvent.KEYCODE_SEARCH:
			keySearchLong = 1;
			break;
		case KeyEvent.KEYCODE_CALL:
			keyCallLong = 1;
			break;
		case KeyEvent.KEYCODE_ENDCALL:
			keyEndCallLong = 1;
			break;
		case KeyEvent.KEYCODE_PLUS:
			keyPlusLong = 1;
			break;
		case KeyEvent.KEYCODE_MINUS:
			keyMinusLong = 1;
			break;
		}
	}

	/**
	 * 清除长按键
	 */
	public static void clearLongKeys() {
		keyHomeLong = 0;
		keyMenuLong = 0;
		keyBackLong = 0;
		keySearchLong = 0;
		keyCallLong = 0;
		keyEndCallLong = 0;
		keyPlusLong = 0;
		keyMinusLong = 0;
	}

	public static void setAction(int motionAction) {
		if(motionAction == MotionEvent.ACTION_DOWN){
			action = motionAction;
			motionDownShouldExistOneFrame = true;
		}
		if(motionAction == MotionEvent.ACTION_UP) {
			action = motionAction;
			motionDownShouldExistOneFrame = false;
		}
		if(motionAction == MotionEvent.ACTION_MOVE) {
			if(motionDownShouldExistOneFrame == false) {
				action = motionAction;
			}
		}
	}

}
