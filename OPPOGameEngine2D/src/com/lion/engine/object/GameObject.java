package com.lion.engine.object;

import com.lion.engine.util.Vector2D;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * 
 * @author 郭子亮
 * 游戏对象基类
 * 定义了游戏中各种对象的基本属性和行为。
 *
 */

public abstract class GameObject {

	// obj的位置（底部中央）
	public Vector2D position;
	
	// obj绘制的位置（左上角坐标）
	public Vector2D drawPosition;
	
	// obj的位移
	public Vector2D delta;
	
	// obj外形
	// 允许某些obj没有具体外形。
	public Facies facies;
	
	// 是否对触碰有反应
	public boolean touchable;
	
	private Paint p = new Paint();
	
	public GameObject() {
		touchable = getTouchable();
	}
	
	/**
	 * 交给Facies画具体的外观
	 * @param c
	 */
	public void draw(Canvas c) {
		facies.draw(c, drawPosition.x, drawPosition.y);
	}
	
	/**
	 * 画轮廓, 一般做测试用
	 * @param c
	 */
	public void drawBound(Canvas c) {
		p.setColor(Color.BLUE);
		p.setAlpha(255);
		c.drawRect(position.x, position.y, 
				position.x+facies.getWidth(), position.y+facies.getHeight(), p);
	}

	/**
	 * 直接返回该对象是否可响应触摸
	 * @return
	 */
	public abstract boolean getTouchable();

}
