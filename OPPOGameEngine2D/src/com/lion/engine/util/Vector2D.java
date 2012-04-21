package com.lion.engine.util;

/**
 * 2d 向量
 * @author lion
 *
 */

public class Vector2D {

	public float x;
	public float y;

	public Vector2D() {
		x = 0.0f;
		y = 0.0f;
	}

	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2D(Vector2D src) {
		x = src.x;
		y = src.y;
	}

}
