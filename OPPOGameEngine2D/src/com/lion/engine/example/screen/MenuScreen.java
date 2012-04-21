package com.lion.engine.example.screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;

import com.lion.engine.R;
import com.lion.engine.example.object.Monster;
import com.lion.engine.main.UserInput;
import com.lion.engine.media.MediaPool;
import com.lion.engine.object.GameObject;
import com.lion.engine.object.GameObjectManager;
import com.lion.engine.screen.BaseScreen;
import com.lion.engine.util.Util;

public class MenuScreen extends BaseScreen {
	
//	private UserInput uit;
	
//	private GameObjectManager gameObjMgr;
	
//	private MyMediaPool sound;
	
	private GameObject touchObject;// example，选中的gameObject
	private float oldX;
	private float oldY;
	
	private MediaPlayer mpBack;
	private MediaPlayer mpPull;
	private MediaPlayer mpPull1;
	private MediaPlayer mpPull3;
	private Context context;
	
	private Paint p;

	public MenuScreen(Context c) {
		super(c);
		context = c;
//		uit = UserInput.getInstance();
//		gameObjMgr = GameObjectManager.getInstance();
//		sound = MyMediaPool.getInstance();
		p = new Paint(); 
		MediaPool.start(mpBack);
	}

	@Override
	public void handleInput() {
		
		// 按键响应
		if(UserInput.keyMenuShort == 1) {
//			ScreenManager.changeScreen(new GameScreen(context));
			onScreenChangeListener.changeScreen(new MenuScreen(context));
		} else if(UserInput.keySearchShort == 1) {
			MediaPool.start(mpPull);
		}
		
		// 触摸响应
		if(UserInput.touchAction == UserInput.PRESS) {
			touchObject = GameObjectManager.getTouchObject(UserInput.touchX, UserInput.touchY);
			if(touchObject != null) {
				oldX = UserInput.touchX;
				oldY = UserInput.touchY;
			}
			if(touchObject instanceof Monster) {
				touchObject.facies.intCurrentIndex = Monster.FAINT;
				MediaPool.start(mpPull1);
			}
		} else if(UserInput.touchAction == UserInput.DRAG) {
			if(touchObject != null) {
				float mx = UserInput.touchX-oldX;
				float my = UserInput.touchY-oldY; 
				touchObject.position.x += mx;
				touchObject.position.y += my;
				touchObject.drawPosition.x += mx;
				touchObject.drawPosition.y += my;
				oldX = UserInput.touchX;
				oldY = UserInput.touchY;
				MediaPool.start(mpPull3);
			}
		} else if(UserInput.touchAction == UserInput.CLICK) {
			if(touchObject instanceof Monster) {
				touchObject.facies.intCurrentIndex = Monster.HATE;
			}
			touchObject = null;
		}
	}

	@Override
	public void refreshData() {
		MediaPool.start(mpBack);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.GRAY);
		
		p.setColor(0);
		p.setAlpha(200);// 初始的alpha值似乎是全透明的。
		p.setStrokeWidth(8);
		p.setTextSize(60);
		canvas.drawText("PRESS", 50, 50, p);
		canvas.drawText("MENU", 50, 110, p);
		canvas.drawText(""+Util.runCount, 50, 170, p);
		canvas.drawText(""+UserInput.touchAction, 50, 230, p);
		
		GameObjectManager.drawAllObjects(canvas);
	}

	@Override
	public void releaseRes() {
		GameObjectManager.removeAllObjects();
		MediaPool.removeAllMP();
	}

	@Override
	public void loadRes() {
		
		Monster monster = new Monster(getContext());
		monster.position.x = 80;
		monster.position.y = 80;
		monster.drawPosition.x = 80;
		monster.drawPosition.y = 80;

		GameObjectManager.addObj(monster);
        
        // 加载声音
        mpBack = MediaPool.createMP(getContext(), R.raw.menu_back, true);
        mpPull = MediaPool.createMP(getContext(), R.raw.pull, false);
        mpPull1 = MediaPool.createMP(getContext(), R.raw.pull1, false);
        mpPull3 = MediaPool.createMP(getContext(), R.raw.pull3, false);
	}
	
}
