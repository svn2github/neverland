package com.lion.engine.example.object;

import android.content.Context;

import com.lion.engine.R;
import com.lion.engine.object.GameObject;
import com.lion.engine.object.SpriteFacies;
import com.lion.engine.util.Vector2D;

/**
 * 爆炸对象
 * @author 80049146
 *
 */

public class Explosion extends GameObject {
	private int[] resId = {R.drawable.explosion};
	public static final int[] USE_BMP = {0};
	public static final int[] ROW = {1};
	public static final int[] COL = {9};
	public static final int[][] FRAME =       {{0, 1, 2, 3, 4, 5, 6, 7, 8}};
	public static final int[][] FRAME_DELAY = {{2, 2, 2, 2, 2, 2, 2, 2, 2}};
	
	
	public Explosion(Context ctx, float x, float y) {
		facies = new SpriteFacies();
		facies.setResource(ctx, resId);
		((SpriteFacies)facies).setSprites(USE_BMP, ROW, COL, FRAME, FRAME_DELAY);
		position = new Vector2D(x, y);
		drawPosition = new Vector2D(x-(facies.getWidth()>>1), y-(facies.getHeight()>>1));
	}

	@Override
	public boolean getTouchable() {
		return false;
	}
	
}
