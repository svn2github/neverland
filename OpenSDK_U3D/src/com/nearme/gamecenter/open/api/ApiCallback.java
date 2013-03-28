package com.nearme.gamecenter.open.api;

public interface ApiCallback {
	public void onSuccess(String content, int code);
	public void onFailure(String content, int code);
}
