package com.lion.engine.object;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.lion.engine.util.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Movie;

public class MoviePool {
	
	private static HashMap<Integer, Movie> mMovs = new HashMap<Integer, Movie>();
	
	private static Movie addMovie(Context ctx, int resId) {
		Movie result = null;
		InputStream is = ctx.getResources().openRawResource(resId);
		result = Movie.decodeStream(is);
		mMovs.put(resId, result);
		return result;
	}
	
	public static Movie getMovie(Context ctx, int resId) {
		Movie result = mMovs.get(resId);
		if(result == null) {
			result = addMovie(ctx, resId);
		}
		return result;
	}
	
	public static void clearMovies() {
		Iterator<Map.Entry<Integer, Movie>> iter = mMovs.entrySet().iterator();	
		while (iter.hasNext()) { 
		    Map.Entry<Integer, Movie> entry = iter.next(); 
		    Movie mov = entry.getValue(); 
		    mov = null;
		} 
		mMovs.clear();
	}
	
}
