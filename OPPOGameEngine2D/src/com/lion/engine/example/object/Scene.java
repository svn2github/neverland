package com.lion.engine.example.object;

import android.content.Context;

import com.lion.engine.object.BitmapFacies;
import com.lion.engine.object.GameObject;
import com.lion.engine.util.Vector2D;

public class Scene extends GameObject {

	public Scene(Context ctx, int[] ids, float x, float y) {
		facies = new BitmapFacies();
		facies.setResource(ctx, ids);
		position = new Vector2D(x, y);
		drawPosition = new Vector2D(x, y);
	}

	@Override
	public boolean getTouchable() {
		return false;
	}
	
}
