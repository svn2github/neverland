package com.lion.engine.media;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

import com.lion.engine.util.Util;

public class MediaPool {
	
	private static HashMap<Integer, MediaPlayer> mMedias = new HashMap<Integer, MediaPlayer>();
	private static final float VOL = 10.0f;
	
	/**
	 * 创建/获取MediaPlayer实例。 
	 * @param c
	 * @param resId
	 * @param loop
	 * @return 
	 */
	public static MediaPlayer createMP(Context c, int resId, boolean loop) {
		MediaPlayer result = mMedias.get(resId);
		if(result == null) {
			MediaPlayer mp = MediaPlayer.create(c, resId);
			mp.setVolume(VOL, VOL);
			mMedias.put(resId, mp);
			result = mp;
		} 
		// 监听声音的状态
		if(result != null) {
			result.setLooping(loop);
			result.setOnPreparedListener(prepareListener);
			result.setOnCompletionListener(completionListener);
			result.setOnErrorListener(errorListener);
		}
		return result;
	}
	
	private static OnCompletionListener completionListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			Util.DEBUG(mp.toString()+" is completed");
		}
	};
	
	private static OnPreparedListener prepareListener = new OnPreparedListener() {
		@Override
		public void onPrepared(MediaPlayer mp) {
			Util.DEBUG(mp.toString()+" is prepared");
		}
	};
	
	private static OnErrorListener errorListener = new OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			Util.DEBUG(mp.toString()+" is errored");
			return false;
		}
	};
	
	/**
	 * 播放指定的声音
	 * @param mp
	 */
	public static void start(MediaPlayer mp) {
		if(mp != null && !mp.isPlaying()) {
			mp.start();
		}
	}
	
	/**
	 * 暂停指定的声音
	 * @param mp
	 */
	public static void pause(MediaPlayer mp) {
		if(mp != null && mp.isPlaying()) {
			mp.pause();
		}
	}
	
	/**
	 * 暂停播放所有声音
	 */
	public static void pauseAll() {
		Iterator<Map.Entry<Integer, MediaPlayer>> iter = mMedias.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry<Integer, MediaPlayer> entry = iter.next(); 
		    MediaPlayer mp = entry.getValue(); 
		    pause(mp);
		} 
	}
	
	/**
	 * 将指定的声音停止掉
	 * @param mp
	 */
	public static void stop(MediaPlayer mp) {
		if(mp != null) {
			mp.reset();
		}
	}
	
	public static void stopAll() {
		Iterator<Map.Entry<Integer, MediaPlayer>> iter = mMedias.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry<Integer, MediaPlayer> entry = iter.next(); 
		    MediaPlayer mp = entry.getValue(); 
		    stop(mp);
		} 
	}
	
	/**
	 * 设置音量
	 * @param mp
	 * @param lv
	 * @param rv
	 */
	public static void setVolume(MediaPlayer mp, float lv, float rv) {
		if(mp != null) {
			mp.setVolume(lv, rv);
		}
	}
	
	/**
	 * 释放声音资源
	 */
	public static void releaseMP(MediaPlayer mp) {
		if(mp != null) {
			mp.reset();
			mp.release();
			removeMP(mp);
		}
	}
	
	// 从池中清除该声音
	private static void removeMP(MediaPlayer mp) {
		Iterator<Map.Entry<Integer, MediaPlayer>> iter = mMedias.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry<Integer, MediaPlayer> entry = iter.next(); 
		    MediaPlayer amp = entry.getValue(); 
		    if(amp == mp) {
		    	mMedias.remove(entry.getKey());
		    	break;
		    }
		} 
	}
	
	// 清除池中所有声音
	public static void removeAllMP() {
		Iterator<Map.Entry<Integer, MediaPlayer>> iter = mMedias.entrySet().iterator(); 
		while (iter.hasNext()) { 
		    Map.Entry<Integer, MediaPlayer> entry = iter.next(); 
		    MediaPlayer mp = entry.getValue(); 
		    mp.reset();
		    mp.release();
		    mp = null;
		} 
		mMedias.clear();
	}

}
