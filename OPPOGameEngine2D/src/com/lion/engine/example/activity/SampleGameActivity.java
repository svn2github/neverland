package com.lion.engine.example.activity;

import android.os.Bundle;

import com.lion.engine.activity.BaseGameActivity;
import com.lion.engine.example.screen.MenuScreen;
import com.lion.engine.screen.BaseScreen;

public class SampleGameActivity extends BaseGameActivity {


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
	}


	/** 设定第一个Screen */
	@Override
	public BaseScreen getFirstScreen() {
		return new MenuScreen(this);
	}

}