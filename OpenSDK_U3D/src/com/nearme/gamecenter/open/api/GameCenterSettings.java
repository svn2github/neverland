package com.nearme.gamecenter.open.api;


public abstract class GameCenterSettings {

	public String app_key;
	public String app_secret;
	public static boolean isDebugModel = false;//是否是需要打印调试日志
	public static boolean isOritationPort = false;//是否是竖屏
	public static int request_time_out = 20000;//设置网络请求的超时(单位毫秒)
	public static volatile boolean isNeedShowLoading = false;
	public final static boolean isNeedFullscreen = true;

	public GameCenterSettings(String app_key, String app_secret) {
		super();
		this.app_key = app_key;
		this.app_secret = app_secret;
	}
	
	/**
	 * 由于token过期导致登录失效,需要cp重新登录.
	 */
	public abstract void onForceReLogin();
}
