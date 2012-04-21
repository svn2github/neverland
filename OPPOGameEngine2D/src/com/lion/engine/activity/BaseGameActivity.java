package com.lion.engine.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.lion.engine.R;
import com.lion.engine.main.AutoFresh;
import com.lion.engine.main.GameSurfaceView;
import com.lion.engine.media.MediaPool;
import com.lion.engine.screen.BaseScreen;
import com.lion.engine.util.Util;
import com.oppo.nearme.trafficstatistics.TrafficStatistics;

public abstract class BaseGameActivity extends Activity {

	private GameSurfaceView gsv;// 绘制屏幕
	private AutoFresh autoFresh;// 刷新数据
	private AudioManager am;
	
	public static final String INTENT_PAUSE_GAME = "pause_game";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		// 取得手机屏幕的尺寸
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Util.scrWidth = metrics.widthPixels;
		Util.scrHeight = metrics.heightPixels;
		
		am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		// 全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 绘图类初始化，数据刷新线程跑起来。
		setContentView(R.layout.base_game);
		gsv = (GameSurfaceView)findViewById(R.id.gsv); 
		
		// 取得上下文引用，初始化第一个屏幕
		gsv.intentChangeScreen(getFirstScreen());
        
		autoFresh = new AutoFresh(gsv);
		autoFresh.setDaemon(true);
		autoFresh.start();
		
		// 注册暂停receiver
		registerReceiver(pauseReceiver, new IntentFilter(INTENT_PAUSE_GAME));
		
		Util.DEBUG("--------------------------onCreate-----------------------");
		
	}
	
	BroadcastReceiver pauseReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			onPause();
		}
	};
	
	/**
	 * 通用的GameActivity弹出Dialog的行为：
	 * 结束当前Activity，在onDestroy()中释放资源。
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 0) {
			return new AlertDialog.Builder(BaseGameActivity.this)
					.setIcon(R.drawable.icon)
					.setTitle(R.string.quit_game)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									finish();
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked Cancel so do some stuff */
								}
							}).create();
		}
		return null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
			case (KeyEvent.KEYCODE_BACK): {
				showDialog(0);
				return true;
			}
			case (KeyEvent.KEYCODE_VOLUME_UP): {
				am.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
		                AudioManager.ADJUST_RAISE, 
		                AudioManager.FLAG_SHOW_UI);    //调高声音
				return true;
			}
			case (KeyEvent.KEYCODE_VOLUME_DOWN): {
				am.adjustStreamVolume(AudioManager.STREAM_MUSIC, 
		                AudioManager.ADJUST_LOWER, 
		                AudioManager.FLAG_SHOW_UI);//调低声音
				return true;
			}
			default:
				return super.onKeyDown(keyCode, event);
		}
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(pauseReceiver);
		quitGame();
		Util.DEBUG("destroy game");
	}

	private void quitGame() {
		autoFresh.gameStatus = AutoFresh.STOP;// 结束绘图循环
		gsv.currentScreen.releaseRes();
		autoFresh = null;
		gsv = null;
	}

	/** 设定第一个Screen，由具体的GameActivity设定 */
	public abstract BaseScreen getFirstScreen();

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		autoFresh.gameStatus = AutoFresh.PAUSE;
		MediaPool.pauseAll();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		autoFresh.gameStatus = AutoFresh.RUNNING;
	}
	
}
