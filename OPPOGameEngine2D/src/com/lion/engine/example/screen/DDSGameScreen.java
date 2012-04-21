package com.lion.engine.example.screen;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;

import com.lion.engine.R;
import com.lion.engine.example.activity.DDSGameActivity;
import com.lion.engine.example.activity.DDSResultActivity;
import com.lion.engine.example.activity.DDSWhetherCheckBoardActivity;
import com.lion.engine.example.object.Explosion;
import com.lion.engine.example.object.Hole;
import com.lion.engine.example.object.Scene;
import com.lion.engine.main.AutoFresh;
import com.lion.engine.main.UserInput;
import com.lion.engine.media.MediaPool;
import com.lion.engine.object.BitmapPool;
import com.lion.engine.object.GameObject;
import com.lion.engine.object.GameObjectManager;
import com.lion.engine.screen.BaseScreen;
import com.lion.engine.util.Util;
import com.nearme.gamecenter.api.OppoGC;
import com.nearme.gamecenter.api.OppoGC.SubmitScoreCB;
import com.nearme.gamecenter.api.OppoGC.UnlockAchCB;
import com.nearme.gamecenter.api.resource.Achievement;
import com.nearme.gamecenter.api.resource.Achievement.ResultBean;

public class DDSGameScreen extends BaseScreen {

	// private UserInput uit;

	// private GameObjectManager gameObjMgr;

	private Bitmap bmpNum1;
	private Bitmap bmpNum2;
	private Bitmap bmpScore;

	private static final int TIME_MAX = 60;
	private int time;
	private long oldTime;

	private Paint p;

	private int score;

//	 private MyMediaPool sound;
	private MediaPlayer mpBack;
	private MediaPlayer mpBeat;

	private String currentAch;

	public DDSGameScreen(Context c) {
		super(c);
		// uit = UserInput.getInstance();
		// gameObjMgr = GameObjectManager.getInstance();
//		 sound = MyMediaPool.getInstance();
		time = TIME_MAX;
		oldTime = System.currentTimeMillis();
		p = new Paint();
		score = 0;
	}

	public void addTime(int t) {
		time += t;
	}

	@Override
	public void handleInput() {
		if (UserInput.touchAction == UserInput.PRESS) {
			GameObject gObj = GameObjectManager.getTouchObject(
					UserInput.touchX, UserInput.touchY);
			if (gObj instanceof Hole) {
				Hole hole = (Hole) gObj;
				int status = hole.getStatus();

				if (status == Hole.MALE) {
					hole.setStatus(Hole.MALE_HURT);
					hole.setScore();
					score += hole.getScore();
					hole.setAction(Hole.MOVE_DOWN);
					hole.setStep(1);
				} else if (status == Hole.FEMALE) {
					hole.setStatus(Hole.FEMALE_HURT);
					hole.setScore();
					score += hole.getScore();
					hole.setAction(Hole.MOVE_DOWN);
					hole.setStep(1);
				}

				submitAch();

				hole.facies.intCurrentIndex = hole.getStatus();
				addExplosion(UserInput.touchX, UserInput.touchY);
				 MediaPool.start(mpBeat);
			}

		} else if (UserInput.touchAction == UserInput.CLICK) {

		}
	}

	private void submitAch() {
		if (score >= 100 && score <= 200) {
			currentAch = DDSGameActivity.LEVEL_1;
			OppoGC.unlockAchievement(unlockAchCB, DDSGameActivity.LEVEL_1);
		} else if (score >= 300 && score <= 400) {
			currentAch = DDSGameActivity.LEVEL_2;
			OppoGC.unlockAchievement(unlockAchCB, DDSGameActivity.LEVEL_2);
		} else if (score >= 500 && score <= 600) {
			currentAch = DDSGameActivity.LEVEL_3;
			OppoGC.unlockAchievement(unlockAchCB, DDSGameActivity.LEVEL_3);
		} else if (score >= 1000 && score <= 1100) {
			currentAch = DDSGameActivity.LEVEL_4;
			OppoGC.unlockAchievement(unlockAchCB, DDSGameActivity.LEVEL_4);
		} else if (score <= -100 && score >= -200) {
			currentAch = DDSGameActivity.LEVEL_0;
			OppoGC.unlockAchievement(unlockAchCB, DDSGameActivity.LEVEL_0);
		}
	}

	private void addExplosion(float x, float y) {
		Explosion e = new Explosion(getContext(), x, y);
		GameObjectManager.addObj(e);
	}

	@Override
	public void refreshData() {

		// sound.start(mpBack);

		gameObjectAction();
		countDown();
	}

	@Override
	public void draw(Canvas canvas) {
		// gameObjMgr.drawAllObjects(canvas);
		drawAllObjects(canvas);
		drawCountDown(canvas);
		drawScore(canvas);
	}

	// @Override
	// public void loadRes() {// hvga版本
	//
	// int ids[] = {R.drawable.sence00};
	// GameObjectManager.addObj(new Scene(getContext(), ids, 0, 172));
	//
	// GameObjectManager.addObj(new Hole(getContext(), 52, 290));
	// GameObjectManager.addObj(new Hole(getContext(), 122, 290));
	// GameObjectManager.addObj(new Hole(getContext(), 198, 290));
	// GameObjectManager.addObj(new Hole(getContext(), 263, 290));
	//
	// ids[0] = R.drawable.sence01;
	// GameObjectManager.addObj(new Scene(getContext(), ids, 0, 227));
	//
	// GameObjectManager.addObj(new Hole(getContext(), 52, 290+57));
	// GameObjectManager.addObj(new Hole(getContext(), 122, 290+57));
	// GameObjectManager.addObj(new Hole(getContext(), 198, 290+57));
	// GameObjectManager.addObj(new Hole(getContext(), 263, 290+57));
	//
	// ids[0] = R.drawable.sence02;
	// GameObjectManager.addObj(new Scene(getContext(), ids, 0, 282));
	//
	// GameObjectManager.addObj(new Hole(getContext(), 52, 290+57*2));
	// GameObjectManager.addObj(new Hole(getContext(), 122, 290+57*2));
	// GameObjectManager.addObj(new Hole(getContext(), 198, 290+57*2));
	// GameObjectManager.addObj(new Hole(getContext(), 263, 290+57*2));
	//
	// ids[0] = R.drawable.sence03;
	// GameObjectManager.addObj(new Scene(getContext(), ids, 0, 337));
	//
	// GameObjectManager.addObj(new Hole(getContext(), 52, 290+57*3));
	// GameObjectManager.addObj(new Hole(getContext(), 122, 290+57*3));
	// GameObjectManager.addObj(new Hole(getContext(), 198, 290+57*3));
	// GameObjectManager.addObj(new Hole(getContext(), 263, 290+57*3));
	//
	// ids[0] = R.drawable.sence04;
	// GameObjectManager.addObj(new Scene(getContext(), ids, 0, 392));
	//
	// ids[0] = R.drawable.tree;
	// GameObjectManager.addObj(new Scene(getContext(), ids, 0, 0));
	//
	// bmpNum1 = BitmapPool.getBitmap(getContext(), R.drawable.num1);
	// bmpNum2 = BitmapPool.getBitmap(getContext(), R.drawable.num2);
	// bmpScore = BitmapPool.getBitmap(getContext(), R.drawable.score);
	//
	// // add sound
	// mpBack = MediaPool.createMP(getContext(), R.raw.game_back, true);
//	 mpBeat = MediaPool.createMP(getContext(), R.raw.beat, false);
	// }

	@Override
	public void loadRes() {// wvga版本

		int ycord = 80;
		int hcord = 80;
		int xcord = 100;

		int ids[] = { R.drawable.sence00 };
		GameObjectManager.addObj(new Scene(getContext(), ids, 0, 172 + ycord));

		GameObjectManager.addObj(new Hole(getContext(), 79, 410));
		GameObjectManager.addObj(new Hole(getContext(), 180, 410));
		GameObjectManager.addObj(new Hole(getContext(), 293, 410));
		GameObjectManager.addObj(new Hole(getContext(), 389, 410));

		ids[0] = R.drawable.sence01;
		GameObjectManager.addObj(new Scene(getContext(), ids, 0,
				172 + ycord * 2));

		GameObjectManager.addObj(new Hole(getContext(), 79, 410 + hcord));
		GameObjectManager.addObj(new Hole(getContext(), 180, 410 + hcord));
		GameObjectManager.addObj(new Hole(getContext(), 293, 410 + hcord));
		GameObjectManager.addObj(new Hole(getContext(), 389, 410 + hcord));

		ids[0] = R.drawable.sence02;
		GameObjectManager.addObj(new Scene(getContext(), ids, 0,
				172 + ycord * 3));

		GameObjectManager.addObj(new Hole(getContext(), 79, 410 + ycord * 2));
		GameObjectManager.addObj(new Hole(getContext(), 180, 410 + ycord * 2));
		GameObjectManager.addObj(new Hole(getContext(), 293, 410 + ycord * 2));
		GameObjectManager.addObj(new Hole(getContext(), 389, 410 + ycord * 2));

		ids[0] = R.drawable.sence03;
		GameObjectManager.addObj(new Scene(getContext(), ids, 0,
				172 + ycord * 4));

		GameObjectManager.addObj(new Hole(getContext(), 79, 410 + ycord * 3));
		GameObjectManager.addObj(new Hole(getContext(), 180, 410 + ycord * 3));
		GameObjectManager.addObj(new Hole(getContext(), 293, 410 + ycord * 3));
		GameObjectManager.addObj(new Hole(getContext(), 389, 410 + ycord * 3));

		ids[0] = R.drawable.sence04;
		GameObjectManager.addObj(new Scene(getContext(), ids, 0,
				172 + ycord * 5));

		ids[0] = R.drawable.tree;
		GameObjectManager.addObj(new Scene(getContext(), ids, 0, 0));

		bmpNum1 = BitmapPool.getBitmap(getContext(), R.drawable.num1);
		bmpNum2 = BitmapPool.getBitmap(getContext(), R.drawable.num2);
		bmpScore = BitmapPool.getBitmap(getContext(), R.drawable.score);

		// add sound
		mpBack = MediaPool.createMP(getContext(), R.raw.game_back, true);
		mpBeat = MediaPool.createMP(getContext(), R.raw.beat, false);
	}

	@Override
	public void releaseRes() {
		BitmapPool.removeAllBmp();
		GameObjectManager.removeAllObjects();
		MediaPool.releaseMP(mpBeat);
	}

	private void gameObjectAction() {
		for (int i = 0; i < GameObjectManager.gObjs.size(); i++) {
			GameObject gObj = (GameObject) GameObjectManager.gObjs.get(i);
			if (gObj instanceof Hole) {
				Hole hole = (Hole) gObj;
				hole.action();
			} else if (gObj instanceof Explosion) {
				Explosion e = (Explosion) gObj;
				if (e.facies.isActionComplete()) {
					GameObjectManager.removeObj(e);
					i--;
				}
			}
		}
	}

	private void countDown() {
		if (time > 0) {
			long nowTime = System.currentTimeMillis();
			if (nowTime - oldTime >= 1000) {
				time--;
				if (time <= 0) {

					// 在线提交分数
					OppoGC.submitScore(submitCallback,
							DDSGameActivity.HIGH_SCORE, score);
					
					OppoGC.submitScore(submitCallback,
							DDSGameActivity.LOW_SCORE, score);
					
					// 暂停游戏
					pauseGame();

					// 本地提交分数
//					Intent it = new Intent(getContext(),
//							DDSResultActivity.class);
//					it.putExtra("yourScore", score);
//					getContext().startActivity(it);

					// 询问用户是否查看排行
					Intent it = new Intent(getContext(),
							DDSWhetherCheckBoardActivity.class);
					it.putExtra("yourScore", score);
					getContext().startActivity(it);
				}
				oldTime = nowTime;
			}
		}
	}

	SubmitScoreCB submitCallback = new SubmitScoreCB() {

		@Override
		public void onSuccess(String successMessage) {
			System.out.println(successMessage);
			Util.Toast(getContext(),
					Util.getString(getContext(), R.string.score_submit_success));
			// successMessage一般为：a 正常 b 分数无变化
		}

		@Override
		public void onFailure(int code) {
			System.out.println(""+code);
			try{
			Util.Toast(getContext(),
					Util.getString(getContext(), R.string.score_submit_fail));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	UnlockAchCB unlockAchCB = new UnlockAchCB() {

		@Override
		public void onSuccess(List<ResultBean> results) {
			for(int i=0; i<results.size(); i++) {
				ResultBean rb = results.get(i);
				if(rb.code == Achievement.RESULT_CODE_SUCCESS) {
					Util.Toast(getContext(), 
							Util.getString(getContext(), 
									R.string.ach_unlock_success) + " " + currentAch);
				}
			}
		}

		@Override
		public void onFailure(int code) {
			System.out.println(code);
//			Util.Toast(getContext(),
//					Util.getString(getContext(), R.string.ach_unlock_fail));
			
		}
	};

	private void drawAllObjects(Canvas c) {
		for (int i = 0; i < GameObjectManager.gObjs.size(); i++) {
			GameObject gameObj = GameObjectManager.gObjs.get(i);
			gameObj.draw(c);
			if (gameObj instanceof Hole) {
				Hole hole = (Hole) gameObj;
				hole.drawScore(c);
			}
		}
	}

	private void drawCountDown(Canvas c) {
		Util.drawImageNum(c, bmpNum2, time, 9999, 30, p);
	}

	private void drawScore(Canvas c) {
		Util.drawBitmap(c, bmpScore, 9999, 8888, p);
		int x = (Util.scrWidth >> 1) + (bmpScore.getWidth() >> 1);
		Util.drawImageNum(c, bmpNum1, score, x, 8888, p);
	}

}
