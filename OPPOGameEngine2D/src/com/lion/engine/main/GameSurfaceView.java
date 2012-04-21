package com.lion.engine.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lion.engine.screen.BaseScreen;
import com.lion.engine.screen.ScreenManager;
import com.lion.engine.screen.Switcher;
import com.lion.engine.util.Util;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	
	public boolean created;
	public Switcher switcher;
	public BaseScreen currentScreen;
	private BaseScreen nextScreen;// 下一个屏幕，在切换器的加载状态时赋值给currentScreen
	
	// 每秒的游戏帧数，大于12帧/秒算流畅
	public int fps;
	
	private SurfaceHolder surfaceHolder;
	
	private Paint p;
	
	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		created = false;
		switcher = new Switcher();
		switcher.setOnLoadingListener(onLoadingListener);
		
		// 注册监听Surface变化的holder，该holder还用来取得canvas
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		
		setFocusable(true); // make sure we get key events
		setFocusableInTouchMode(true);
		
		p = new Paint();
		
		
	}
	
	/**
	 * 意图切换到的屏幕
	 * @param nextScr，下个屏幕
	 * 这个方法由具体的屏幕去调用，他们知道自己将要切换到哪一个屏幕。
	 * 设置完下个屏幕索引后，就进入切屏特效
	 */
	public void intentChangeScreen(BaseScreen nextScr) {
		nextScreen = nextScr;
		switcher.begin();
	}
	
	private BaseScreen.OnScreenChangeListener onScreenChangeListener = new BaseScreen.OnScreenChangeListener() {
		
		@Override
		public void changeScreen(BaseScreen screen) {
			intentChangeScreen(screen);
		}
	};
	
	private Switcher.OnLoadingListener onLoadingListener = new Switcher.OnLoadingListener() {
		
		@Override
		public void OnLoading() {
			if(currentScreen != null) {
				currentScreen.releaseRes();
				currentScreen = null;
			}
			currentScreen = nextScreen;
			currentScreen.setOnScreenChangeListener(onScreenChangeListener);
			
			new Thread(new Runnable() {
				public void run() {
					currentScreen.loadRes();
					switcher.end();
				}
			}).start();
		}
	};
	
	public void doDraw() {
		Canvas canvas = null;
		try {
			canvas = surfaceHolder.lockCanvas();
			if(switcher.status != Switcher.LOADING && currentScreen != null) {
//				ScreenManager.currentScreen.draw(canvas);
				currentScreen.draw(canvas);
			}
			
			switcher.draw(canvas);
			drawFPS(canvas);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(canvas != null) {
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
			// 帧结束
		}
	}
	
	private void drawFPS(Canvas c) {
		if(Util.ifShowFPS) {
			p.setColor(Color.BLUE);
			p.setTextSize(20);
			c.drawText("FPS:"+fps, 10, 30, p);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		System.out.println("key down:"+keyCode);
		UserInput.keyCode = keyCode;
		UserInput.setLongKeys();
		UserInput.acceptKey = true;
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		UserInput.keyCode = keyCode;
		UserInput.clearLongKeys();
		return super.onKeyUp(keyCode, event);
	}

	@Override	
	public boolean onTouchEvent(MotionEvent event) {
//		System.out.println("touch:"+event.getAction()+" "+event.getX()+" "+event.getY());
		UserInput.setAction(event.getAction());
		UserInput.touchX = event.getX();
		UserInput.touchY = event.getY();
		UserInput.acceptTouch = true;
		
		if(switcher.status == Switcher.IDLE) {
			// 用户的输入延迟到帧开始时赋值，使得中途接收的按键输入能响应到全帧。
			UserInput.setUserInput();
			// 处理输入
			currentScreen.handleInput();
			// 帧结束前要清除用户输入的各种状态。
			UserInput.clearUserInput();
		}
		
		return true;
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
//		System.out.println("ball");
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		created = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}

}
