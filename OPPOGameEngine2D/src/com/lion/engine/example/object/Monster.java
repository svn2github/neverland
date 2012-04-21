package com.lion.engine.example.object;

import android.content.Context;

import com.lion.engine.R;
import com.lion.engine.object.GameObject;
import com.lion.engine.object.MovieFacies;
import com.lion.engine.util.Vector2D;

/**
 * 
 * @author 郭子亮
 * example
 *
 */

public class Monster extends GameObject {
	
	public static final int IDEL = 0;
	public static final int FAINT = 3;
	public static final int HATE = 2;
	
	private int[] resId = {R.drawable.egg_1,
						   R.drawable.egg_2,
						   R.drawable.egg_3,
						   R.drawable.egg_4,
						   R.drawable.egg_5
	};
	
	public Monster(Context ctx) {
		facies = new MovieFacies();
		facies.setResource(ctx, resId);
		position = new Vector2D();
		drawPosition = new Vector2D();
		delta = new Vector2D();
	}

	@Override
	public boolean getTouchable() {
		return true;
	}

}
