package com.lion.engine.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lion.engine.R;
import com.lion.engine.util.Util;
import com.oppo.nearme.trafficstatistics.TrafficStatistics;

public class MainMenuActivity extends Activity {

	private Button btnSample;
	private Button btnDadishu;
	private Button btnDafeiji;
	
	private Button btnShowTraffic;
	private Button btnBegin;
	private Button btnEnd;
	
	private TrafficStatistics ts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_menu);
		
		setViews();
		
		ts = new TrafficStatistics(this);
		ts.beginAutoTrafficStatistics();
		
		Util.DEBUG("begin traffic------------");
		TrafficStatistics.beginCountTraffic();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ts.stopAutoTrafficStatistics();
		
		long data = TrafficStatistics.endCountAndGetTraffic();
		Util.DEBUG("end traffic------------:"+data);
	}

	private void setViews() {
		
		btnSample = (Button)findViewById(R.id.sample);
		btnSample.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainMenuActivity.this, SampleGameActivity.class));
			}
		});
		
		btnDadishu = (Button)findViewById(R.id.dadishu);
		btnDadishu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent();
				it.setClass(MainMenuActivity.this, DDSMenuActivity.class);
				startActivity(it);
			}
		});
		
		btnDafeiji = (Button)findViewById(R.id.dafeiji);
		btnDafeiji.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainMenuActivity.this, DFJMenuActivity.class));
				
			}
		});
		
		btnShowTraffic = (Button) findViewById(R.id.btn_traffic);
		btnShowTraffic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.DEBUG("today all :"+TrafficStatistics.getTodayTraffic(MainMenuActivity.this));
				Util.DEBUG("today wifi:"+TrafficStatistics.getTodayTrafficByWifi(MainMenuActivity.this));
				Util.DEBUG("today mobi:"+TrafficStatistics.getTodayTrafficByMobile(MainMenuActivity.this));
			}
		});
		
		btnBegin = (Button) findViewById(R.id.btn_beginTraffic);
		btnBegin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.DEBUG("begin traffic------------");
				TrafficStatistics.beginCountTraffic();
			}
		});
		
		btnEnd = (Button) findViewById(R.id.btn_endTraffic);
		btnEnd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				long data = TrafficStatistics.endCountAndGetTraffic();
				Util.DEBUG("end traffic------------:"+data);
			}
		});
	}

}
