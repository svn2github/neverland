package com.lion.engine.main;

import com.lion.engine.screen.ScreenManager;
import com.lion.engine.screen.Switcher;
import com.lion.engine.util.Util;

public class AutoFresh extends Thread {
	
	private GameSurfaceView gsv;
	
	public int gameStatus;
	public static final int RUNNING = 0;// running和pause可以互为转换，到stop意味着游戏终结。
	public static final int PAUSE = 1;
	public static final int STOP = 2;
	
	private static final int DELAY = 30;// 秒间60帧
	private int lostTime;// 刷新数据，消逝的时间
	private int delayTime;// 根据实际的消逝时间，调整每帧的延时，使得游戏匀速。
	
	public AutoFresh(GameSurfaceView view) {
		Util.runCount = 0;
		delayTime = DELAY;
		gsv = view;
		gameStatus = RUNNING;
	}
	
	public void run() {
		while(gameStatus != STOP) {
			// 线程休眠，使得游戏帧数可控。
			try{
				delayTime = DELAY - lostTime;
				if(delayTime < 0) {
					delayTime = 0;
				}
				Thread.sleep(delayTime);
				if(Util.ifShowFPS) {
					if(Util.runCount%10 == 0) {
						gsv.fps = 1000/(lostTime + delayTime);
					}
				}
			} catch(Exception e) {
			}
			
			if(gameStatus == PAUSE || gsv.created == false) {
				continue;
			}
			
			// 由于异步线程，gameStatus有机会在PAUSE时进入循环，在循环中被外界改状态为STOP，
			// 此时，应该要退出该while循环。
			if(gameStatus == STOP) {
				break;
			}
			
			// 帧开始
			long startTime = System.currentTimeMillis(); 
			
			// 用户的输入延迟到帧开始时赋值，使得中途接收的按键输入能响应到全帧。
//			UserInput.setUserInput();
			
			// 在切屏的时候暂停用户输入
			if(gsv.switcher.status == Switcher.IDLE) {
				// 处理输入
//				gsv.currentScreen.handleInput();
				// 刷新数据
				gsv.currentScreen.refreshData();
			}	
			
			// 如果切屏器不空闲的话，切屏器刷新
			gsv.switcher.refresh();
			
			// 游戏总计数器计数
			runningCount();
			
			// 数据刷新完，绘制屏幕
			gsv.doDraw();
			
//			// 帧结束前要清除用户输入的各种状态。
//			UserInput.clearUserInput();
			
			lostTime = (int)(System.currentTimeMillis()-startTime);
			
		}
	}
	
	private void runningCount() {
		Util.runCount++;
		if(Util.runCount > 999999) {
			Util.runCount = 0;
		}
	}

}
