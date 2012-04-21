package com.lion.engine.example.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lion.engine.R;
import com.lion.engine.example.storage.ScoreProvider;
import com.nearme.gamecenter.api.OppoGC;

public class DDSWhetherCheckBoardActivity extends Activity {

	private Button btnOK;
	private Button btnCancel;
	private TextView tvScore;
	private int score;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setContentView(R.layout.da_di_shu_check_board);
		score = getIntent().getIntExtra("yourScore", -1);
		setViews();
	}
	
	private void setViews() {
		tvScore = (TextView) findViewById(R.id.score);
		tvScore.setText(""+score);
		btnOK = (Button)findViewById(R.id.yes);
		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(DDSWhetherCheckBoardActivity.this, DDSMenuActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);
				OppoGC.openDashboard();
			}
		});
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(DDSWhetherCheckBoardActivity.this, DDSMenuActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	
 
}
