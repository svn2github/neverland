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

public class DDSResultActivity extends Activity {

	private int score;
	private CharSequence name;
	
	private TextView viewScore;
	private EditText editName;
	private Button btnOK;
	
	private ContentResolver cr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		score = getIntent().getIntExtra("yourScore", -1);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setContentView(R.layout.da_di_shu_result);
		setViews();
		
		cr = getContentResolver();
	}
	
	private void setViews() {
		viewScore = (TextView)findViewById(R.id.score);
		viewScore.setText(""+score);
		
		editName = (EditText)findViewById(R.id.name);
		
		btnOK = (Button)findViewById(R.id.yes);
		btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editable e = editName.getText();
				name = e.subSequence(0, e.length());
				
				insertAScore();
				
				Intent it = new Intent();
				it.setClass(DDSResultActivity.this, DDSHiScoreActivity.class);
				startActivity(it);
				
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			
		}
		return true;
	}
	
	private void insertAScore() {
		ContentValues values = new ContentValues();
		values.put(ScoreProvider.NAME, name.toString());
		values.put(ScoreProvider.SCORE, score);
		cr.insert(ScoreProvider.CONTENT_URI, values);
	}
 
}
