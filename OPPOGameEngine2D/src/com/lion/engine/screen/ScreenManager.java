package com.lion.engine.screen;


/**
 * 
 * @author 郭子亮
 * 屏幕管理者
 * 控制屏幕间的切换，保存当前屏幕的引用。
 *
 */
public class ScreenManager {
	
//	public static BaseScreen currentScreen;
//	private static BaseScreen nextScreen;// 下一个屏幕，在切换器的加载状态时赋值给currentScreen
//	
//	/**
//	 * 意图切换到的屏幕
//	 * @param nextScr，下个屏幕
//	 * 这个方法由具体的屏幕去调用，他们知道自己将要切换到哪一个屏幕。
//	 * 设置完下个屏幕索引后，就进入切屏特效
//	 */
//	public static void intentChangeScreen(BaseScreen nextScr) {
//		nextScreen = nextScr;
//		Switcher.begin();
//	}
//	
//	/**
//	 * 具体的切屏方法。在切换器的加载阶段(loading)调用。
//	 */
//	public static void changeScreen() {
//		if(currentScreen != null) {
//			currentScreen.releaseRes();
//			currentScreen = null;
//		}
//		currentScreen = nextScreen;
//		
//		new Thread(new Runnable() {
//			public void run() {
//				currentScreen.loadRes();
//				Switcher.end();
//			}
//		}).start();
//	}

}
