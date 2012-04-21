package com.lion.engine.example.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.lion.engine.R;
import com.lion.engine.object.BitmapPool;
import com.lion.engine.object.GameObject;
import com.lion.engine.object.SpriteFacies;
import com.lion.engine.util.Util;
import com.lion.engine.util.Vector2D;

public class Hole extends GameObject {
	
	private int idHoles[] = {R.drawable.male,
	 		 R.drawable.female};

	// 洞的状态
	private int status;
	public static final int MALE = 0;
	public static final int MALE_HURT = 1;
	public static final int FEMALE = 2;
	public static final int FEMALE_HURT = 3;
	public static final int IDLE = 4;
	
	public static final int USE_BMP[] = {0,
										 0,
										 1,
										 1,
										 0};
	public static final int ROW[] = {1,
									 1,
									 1,
									 1,
									 1};
	public static final int COL[] = {6,
									 6,
									 5,
									 5,
									 6};
	public static final int FRAME[][] = {{0,1,2,3,2,1}, 
										 {4,5},
										 {0,1,2,3,2,1},
										 {4},
										 {-1}};// -1相当于当前外观是透明
	public static final int FRAME_DELAY[][] = {{3,4,5,4,3,3}, 
											   {4,5},
											   {3,4,3,4,4,5},
											   {4},
											   {1}};
	
	private Vector2D start;
	private Vector2D end;
	private int action;
	public static final int MOVE_UP = 0;
	public static final int MOVE_DOWN = 1;
	public static final int MOVE_STOP = 2;
	private int step;
	public static final int STEP_SLOW = 1;
	public static final int STEP_NORMAL = 2;
	public static final int STEP_FAST = 3;
	private int stopTime = 20;
	
	private int score;
	private float scorePos;
	
	private Bitmap bmpNum1;
	
	private Paint p;
	
	public Hole(Context ctx, float x, float y) {
		facies = new SpriteFacies();
		facies.setResource(ctx, idHoles);
		((SpriteFacies)facies).setSprites(USE_BMP, ROW, COL, FRAME, FRAME_DELAY);
		position = new Vector2D(x, y);
		drawPosition = new Vector2D(x-(facies.getWidth()>>1), y-facies.getHeight());
		
		start = new Vector2D(position);
		end = new Vector2D(position.x, position.y-40);
		
		randomInit();
		
		bmpNum1 = BitmapPool.getBitmap(ctx, R.drawable.num1);
		
		p = new Paint();
	}
	
	public void action() {
		if(action == MOVE_UP) {
			moveUp();
			if(position.y < end.y) {
				position.y = end.y;
				action = MOVE_STOP;
				stopTime = 20;
			}
		} else if(action == MOVE_DOWN) {
			moveDown();
			if(position.y > start.y) {
				position.y = start.y;
				randomInit();
				action = MOVE_UP;
			}
		} else if(action == MOVE_STOP) {
			moveStop();
			if(stopTime <= 0) {
				action = MOVE_DOWN;
			}
		}
	}
	
	private void randomInit() {
		randomStep();
		randomStatus();
	}
	
	private void randomStep() {
		double r = Math.random();
		step = STEP_SLOW;
		if(r >= 0.0 && r < 0.6) {
			step = STEP_SLOW;
		} else if(r >= 0.6 && r < 0.95) {
			step = STEP_NORMAL;
		} else if(r >= 0.95 && r <= 1.0) {
			step = STEP_FAST;
		}
	}
	
	private void randomStatus() {
		double r = Math.random();
		status = IDLE;
		if(r >= 0.0 && r < 0.4) {
			status = MALE;
		} else if(r >= 4.0 && r < 9.0) {
			status = IDLE;
		} else if(r >= 0.9 && r <= 1.0) {
			status = FEMALE;
		}
		facies.intCurrentIndex = status;
	}
	
	private void moveUp() {
		position.y -= step;
		drawPosition.y = position.y-facies.getHeight();
	}
	
	private void moveDown() {
		position.y += step;
		drawPosition.y = position.y-facies.getHeight();
	}
	
	private void moveStop() {
		stopTime--;
	}
	
	public void setScore() {
		if(status == MALE_HURT){
			if(step == STEP_SLOW) {
				score = 10;
			} else if(step == STEP_NORMAL) {
				score = 20;
			} else if(step == STEP_FAST) {
				score = 30;
			}
		} else if(status == FEMALE_HURT) {
			score = -20;
		}
		scorePos = end.y;
	}
	
	/**
	 * 根据不同的点击状态取得分数
	 * @return
	 */
	public int getScore() {
		return score;
	}
	
	public void drawScore(Canvas c) {
		if(getScore() != 0) {
			Util.drawImageNum(c, bmpNum1, score, end.x-10, scorePos, p);
			scorePos -= 2;
			if(end.y - scorePos > 40) {
				score = 0;
			}
		}
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	@Override
	public boolean getTouchable() {
		return true;
	}
	
}
