package com.lion.engine.example.screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;

import com.lion.engine.R;
import com.lion.engine.main.UserInput;
import com.lion.engine.media.MediaPool;
import com.lion.engine.object.BitmapPool;
import com.lion.engine.screen.BaseScreen;
import com.lion.engine.util.Util;

public class GameScreen extends BaseScreen {

//	private UserInput uit;
	
//	private MyMediaPool sound;
	
	private MediaPlayer mpGame;
	
	private Context context;
	
	private Paint p;
	
	private Bitmap bmpIcon;
	
	private float degree;
	
	private float zoomSize;
	
	private int zoomStatus;
	private static final int ZOOM_IN = 0;// 放大
	private static final int ZOOM_OUT = 1;// 缩小
	
	public GameScreen(Context c) {
		super(c);
		context = c;
//		uit = UserInput.getInstance();
//		sound = MyMediaPool.getInstance();
		p = new Paint();
	}
	
	@Override
	public void handleInput() {
		
		if(UserInput.keyMenuShort == 1) {
//			ScreenManager.intentChangeScreen(new MenuScreen(context));
			onScreenChangeListener.changeScreen(new MenuScreen(context));
		}

	}

	@Override
	public void refreshData() {
		// TODO Auto-generated method stub
		MediaPool.start(mpGame);
		changeDegree();
		changeZoomSize();
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawColor(Color.WHITE);
		
		p.setColor(0);
		p.setAlpha(200);// 初始的alpha值似乎是全透明的。
		p.setStrokeWidth(8);
		p.setTextSize(60);
		canvas.drawText(""+Util.runCount, 50, 50, p);
		
		
		Util.drawRotateBitmap(canvas, bmpIcon, 100, 100, degree, p);
		Util.drawScaleBitmap(canvas, bmpIcon, 100, 150, zoomSize, p);
		Util.drawAlphaBitmap(canvas, bmpIcon, 100, 200, 100, p);
	}

	@Override
	public void releaseRes() {
		// TODO Auto-generated method stub
		MediaPool.releaseMP(mpGame);
	}

	@Override
	public void loadRes() {
		// TODO Auto-generated method stub
		mpGame = MediaPool.createMP(getContext(), R.raw.game_back, true);
		bmpIcon = BitmapPool.getBitmap(getContext(), R.drawable.icon);
	}
	
	private void changeDegree() {
		degree++;
		if(degree>360) {
			degree = 0;
		}
	}
	
	private void changeZoomSize() {
		if(zoomStatus == ZOOM_IN) {
			zoomSize += 0.2;
			if(zoomSize >= 3) {
				zoomStatus = ZOOM_OUT;
			}
		} else if(zoomStatus == ZOOM_OUT) {
			zoomSize -= 0.2;
			if(zoomSize <= 0.2) {
				zoomStatus = ZOOM_IN;
			}
		}
	}

}
