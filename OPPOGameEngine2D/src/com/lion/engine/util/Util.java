package com.lion.engine.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Toast;

public class Util {

	public static int scrWidth;
	public static int scrHeight;
	public static int runCount;// 游戏运行计数器，需要用到计数器的可以从这里统一取得
	
//	public static Context CTX_FOR_RESOURCE;// 资源上下文
	
	public static boolean ifShowFPS = true;
	public static boolean ifDEBUG = true;
	
	/**
	 * 统一的输出打印接口
	 * @param s
	 */
	public static void DEBUG(String s) {
		if(ifDEBUG) {
			Log.d("lion_engine", s);
		}
	}
	
	/**
	 * 绘制旋转图片，以图片中心来旋转
	 * @param c
	 * @param bmp
	 * @param x
	 * @param y
	 * @param degree
	 * @param p
	 */
	public static void drawRotateBitmap(Canvas c, Bitmap bmp, float x, float y, float degree, Paint p) {
		if(bmp == null) {
			return;
		} else {
			// matirx版效果更好些，但是处理更慢些
//			Matrix matrix = new Matrix(); 
//			matrix.setRotate(degree); 
//			Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true); 
//			drawBitmap(c, resizeBmp, x, y, p);
			c.save();
			c.rotate(degree, x+(bmp.getWidth()>>1), y+(bmp.getHeight()>>1));
			drawBitmap(c, bmp, x, y, p);
			c.restore();
		}
	}
	
	/**
	 * 以图片中心做缩放
	 * @param c
	 * @param bmp
	 * @param x
	 * @param y
	 * @param zoomSize
	 * @param p
	 */
	public static void drawScaleBitmap(Canvas c, Bitmap bmp, float x, float y, float zoomSize, Paint p) {
		if(bmp == null) {
			return;
		} else {
			if(zoomSize == 1) {
				drawBitmap(c, bmp, x, y, p);
				return;
			}
//			Matrix matrix = new Matrix(); 
//			matrix.postScale(zoomSize, zoomSize);
//			Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true); 
//			int coordw = (int)(bmp.getWidth()*zoomSize - bmp.getWidth())>>1;
//			int coordh = (int)(bmp.getHeight()*zoomSize - bmp.getHeight())>>1;
//			drawBitmap(c, resizeBmp, x-coordw, y-coordh, p);
			
			c.save();
			c.scale(zoomSize, zoomSize, x+(bmp.getWidth()>>1), y+(bmp.getHeight()>>1));
			drawBitmap(c, bmp, x, y, p);
			c.restore();
		}
	}
	
	/**
	 * 绘制半透明图。
	 * @param c
	 * @param bmp
	 * @param x
	 * @param y
	 * @param alpha 0-255
	 * @param p
	 */
	public static void drawAlphaBitmap(Canvas c, Bitmap bmp, float x, float y, int alpha, Paint p) {
		if(bmp == null) {
			return;
		}
		p.setAlpha(alpha);
		drawBitmap(c, bmp, x, y, p);
	}
	
	/**
	 * 统一的绘制Bitmap的接口
	 * @param c
	 * @param bmp
	 * @param x 9999 居中，8888底部
	 * @param y 9999 居中，8888底部
	 * @param p
	 */
	public static void drawBitmap(Canvas c, Bitmap bmp, float x, float y, Paint p) {
		if(bmp == null) {
			DEBUG("no BMP1:"+bmp);
			return;
		} else {
			if(x == 9999) {
				x = (scrWidth - bmp.getWidth())>>1;
			} else if(x == 8888) {
				x = scrWidth - bmp.getWidth();
			}
			if(y == 9999) {
				y = (scrHeight - bmp.getHeight())>>1;
			} else if(y == 8888) {
				y = scrHeight - bmp.getHeight();
			}
			c.drawBitmap(bmp, x, y, p);
		}
	}
	
	/**
	 * 绘制图片剪辑。
	 * @param c
	 * @param bmp
	 * @param clipx 相对于图片左上角坐标(0,0)
	 * @param clipy
	 * @param clipw
	 * @param cliph
	 * @param x 图片要绘制在屏幕上的位置
	 * @param y
	 * @param p
	 */
	public static void drawRegion(Canvas c, Bitmap bmp, float clipx, float clipy, int clipw, int cliph, float x, float y, Paint p) {
		if(bmp == null) {
			DEBUG("no BMP2:"+bmp);
			return;
		} else {
			c.save();
			c.clipRect(x, y, x+clipw, y+cliph);
			c.drawBitmap(bmp, x-clipx, y-clipy, p);
			c.restore();
		}
	}
	
	/**
	 * 统一的绘制Movie的接口
	 * @param c
	 * @param mov
	 * @param x
	 * @param y
	 */
	public static void drawMovie(Canvas c, Movie mov, float x, float y) {
		if(mov == null) {
			DEBUG("no MOV:"+mov);
			return;
		} else {
			mov.draw(c, x, y);
		}
	}
	
	/**
	 * 绘制图片数字
	 * @param c
	 * @param bmp 数字图片：0123456789-（的图片，每个数字等宽）
	 * @param num 要画的数字
	 * @param x 9999，x轴居中；8888底部
	 * @param y 9999，y轴居中；8888底部
	 */
	public static void drawImageNum(Canvas c, Bitmap bmp, int num, float x, float y, Paint p) {
		int numWidth = bmp.getWidth()/11;
		
		boolean minus = false;
		if(num < 0) {
			minus = true;
			num = Math.abs(num);
		}
		
		int bit = bitsOfNum(num);
		
		if(x == 9999) {
			if(minus) {
				x = (scrWidth - numWidth*(bit+1))>>1;
			} else {
				x = (scrWidth - numWidth*bit)>>1;	
			}
		} else if(x == 8888) {
			if(minus) {
				x = scrWidth - numWidth*(bit+1);
			} else {
				x = scrWidth - numWidth*bit;
			}
		}
		if(y == 9999) {
			y = (scrHeight - bmp.getHeight())>>1;
		} else if(y == 8888) {
			y = scrHeight - bmp.getHeight();
		}
		
		// 画负号
		if(minus) {
			drawRegion(c, bmp, 10*numWidth, 0, numWidth, bmp.getHeight(), x, y, p);
		}
		
		for(int i=bit; i>0; i--) {
			int pow = (int)Math.pow(10, i-1);
			int n = num/(pow);
			num %= pow;
			if(minus) {
				drawRegion(c, bmp, n*numWidth, 0, numWidth, bmp.getHeight(), x+(bit-i+1)*numWidth, y, p);
			} else {
				drawRegion(c, bmp, n*numWidth, 0, numWidth, bmp.getHeight(), x+(bit-i)*numWidth, y, p);
			}
		}
	}
	
	// 算一个数字有多少位
	private static int bitsOfNum(int num) {
		int result = 1;
		while(num/10 != 0) {
			result++;
			num /= 10;
		}
		return result;
	}
	
	/**
	 * 判断矩形重叠
	 * @param ax
	 * @param ay
	 * @param aw
	 * @param ah
	 * @param bx
	 * @param by
	 * @param bw
	 * @param bh
	 * @return
	 */
	public static boolean overlap(float ax, float ay, int aw, int ah, float bx, float by, int bw, int bh) {
		boolean result = false;
		
		if(ax <= bx+bw && ax+aw >= bx && ay <= by+bh && ay+ah >= by) {
			result = true;
		} else {
			result = false;
		}
		
		return result;
	}
	
	public static String getString(Context ctx, int i) {	
		String rs = "";
		rs = ctx.getResources().getString(i);
		return rs;
	}
	
	public static void Toast(Context ctx, String str) {
		// str = R(str);
		if (str == null)
			str = "null point";
		Toast.makeText(ctx, str, Toast.LENGTH_LONG).show();
	}
	
}
