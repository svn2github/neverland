package com.lion.engine.object;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;

import com.lion.engine.util.Util;

public class MovieFacies extends Facies {

	private Movie movies[];// 外形是gif动画图片的情况
	private long mMovieStart;
	
	@Override
	public void draw(Canvas c, float x, float y) {
		long now = android.os.SystemClock.uptimeMillis();
		if (mMovieStart == 0) { // first time
			mMovieStart = now;
		}
		if (movies[intCurrentIndex] != null) {
			int dur = movies[intCurrentIndex].duration();
			if (dur == 0) {
				dur = 1000;
			}
			int relTime = (int) ((now - mMovieStart) % dur);
			movies[intCurrentIndex].setTime(relTime);
			movies[intCurrentIndex].draw(c, x, y);
		}
		Util.drawMovie(c, movies[intCurrentIndex], x, y);
	}
	
	@Override
	public void setResource(Context ctx, int[] ids) {
		movies = new Movie[ids.length];
		for (int i = 0; i < ids.length; i++) {
			movies[i] = MoviePool.getMovie(ctx, ids[i]);
		}
	}

	@Override
	public int getWidth() {
		return movies[intCurrentIndex].width();
	}

	@Override
	public int getHeight() {
		return movies[intCurrentIndex].height();
	}

	@Override
	public boolean isActionComplete() {
		return false;
	}

}
