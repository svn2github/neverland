package com.lion.engine.example.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lion.engine.R;
import com.nearme.gamecenter.api.IOppoGCDelegate;
import com.nearme.gamecenter.api.OppoGC;
import com.nearme.gamecenter.api.OppoGC.SubmitScoreCB;
import com.nearme.gamecenter.api.OppoGC.UnlockAchCB;
import com.nearme.gamecenter.api.OppoGCSettings;
import com.nearme.gamecenter.api.resource.User;
import com.nearme.gamecenter.api.resource.Achievement.ResultBean;
import com.nearme.gamecenter.core.OppoGCInternal;

public class DDSMenuActivity extends Activity {

	private Button btnBegin;
	private Button btnOption;
	private Button btnScore;
	private Button btnCheckBoard;
	private Button btnCheckAch;
	private Button btnOpenGC;
	private Button btnLog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.da_di_shu_menu);
		
		setViews();
		
		initGameCenter();
		
	}
	
	private void initGameCenter() {
		OppoGCSettings settings = new OppoGCSettings(DDSGameActivity.GAME_NAME, 
				DDSGameActivity.GAME_ID, 
				DDSGameActivity.GAME_KEY,
				DDSGameActivity.GAME_SECRET);
		IOppoGCDelegate delegate = new IOppoGCDelegate() {
			@Override
			public void userLoggedOut(User user) {
				// TODO Auto-generated method stub
			}
			@Override
			public void userLoggedIn(User user) {
				// TODO Auto-generated method stub	
			}
			@Override
			public void onDashBoardDisappear() {
				// TODO Auto-generated method stub
			}
			@Override
			public void onDashBoardAppear() {
				// TODO Auto-generated method stub
			}
		};
		try {
			OppoGCInternal.DEVELOPMENT_LOGGING_ENABLED = true;
			OppoGC.initialize(this, settings, delegate);
			
			//测试api的方法！！
			testApi();
			
			
		} catch (Exception e) {
		}


	}

	private void testApi() {
		OppoGC.submitScore(new SubmitScoreCB() {
			
			@Override
			public void onFailure(int arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0);
			}
			
			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0);
			}
		}, DDSGameActivity.HIGH_SCORE, 100d);
		
		
		OppoGC.unlockAchievement(new UnlockAchCB() {
			
			@Override
			public void onFailure(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(List<ResultBean> arg0) {
				// TODO Auto-generated method stub
				System.out.println("unlocked success");
			}
		}, "490;489");
	}
	
	private void setViews() {
		btnBegin = (Button)findViewById(R.id.begin);
		btnBegin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(DDSMenuActivity.this, DDSGameActivity.class);
				startActivity(it);
			}
		});
		
		btnOption = (Button)findViewById(R.id.option);
		btnOption.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DDSMenuActivity.this, DDSOptionActivity.class));
			}
		});
		
//		btnScore = (Button)findViewById(R.id.score);
//		btnScore.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(DDSMenuActivity.this, DDSHiScoreActivity.class));
//			}
//		});
		
		btnCheckBoard = (Button) findViewById(R.id.check_board);
		btnCheckBoard.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OppoGC.openLeaderboardList();
			}
		});
		
		btnCheckAch = (Button) findViewById(R.id.check_ach);
		btnCheckAch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OppoGC.openAchievementList();
			}
		});
		
		btnOpenGC = (Button) findViewById(R.id.btn_open_gc);
		btnOpenGC.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OppoGC.openDashboard();
			}
		});
		
		btnLog = (Button) findViewById(R.id.btn_log);
		btnLog.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				OppoGC.logoutUser();
			}
		});
	}

}
