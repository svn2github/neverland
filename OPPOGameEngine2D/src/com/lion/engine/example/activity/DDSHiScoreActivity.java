package com.lion.engine.example.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lion.engine.R;
import com.lion.engine.example.storage.Score;
import com.lion.engine.example.storage.ScoreProvider;

public class DDSHiScoreActivity extends Activity {

	private String[] projection = {ScoreProvider.NAME,
								   ScoreProvider.SCORE,
								   ScoreProvider.TIME};
	
	private ArrayList<Score> scores;
	
	private TextView textScore;
	
	private ContentResolver cr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		setContentView(R.layout.da_di_shu_hi_score);
		
		cr = getContentResolver();
		
		scores = new ArrayList<Score>();
		
		getScoreFromDB();
		
		setViews();
		
	}
	
	private void setViews() {
		textScore = (TextView)findViewById(R.id.txt_score);
		if(!scores.isEmpty()) {
			textScore.setText("名次"+"\t"+"玩家"+"\t"+"分数"+"\t"+"时间\n");
			for(int i=0; i<scores.size(); i++) {
				Score s = scores.get(i);
				textScore.append(i+"\t"+s.getName()+"\t"+s.getScore()+"\t"+getStandardTime(s.getTime())+"\n");
			}
		}
	}
	
	private String getStandardTime(long ms) {
		StringBuffer result = new StringBuffer("");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(ms));
		result.append(calendar.get(Calendar.YEAR)+"-"+
					  calendar.get(Calendar.MONTH)+"-"+
					  calendar.get(Calendar.DAY_OF_MONTH)+" "+
					  calendar.get(Calendar.HOUR_OF_DAY)+":"+
					  calendar.get(Calendar.MINUTE));
		return result.toString();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Intent it = new Intent(DDSHiScoreActivity.this, DDSMenuActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(it);
		}
		return true;
	}

	private void getScoreFromDB() {
		Cursor scoreCursor = cr.query(ScoreProvider.CONTENT_URI, null, null, null, null);
		
		if (scoreCursor.moveToFirst()) { 
			 
	        String name;  
	        int score;
	        long time;
	        int nameColumn = scoreCursor.getColumnIndex(ScoreProvider.NAME);  
	        int scoreColumn = scoreCursor.getColumnIndex(ScoreProvider.SCORE); 
	        int timeColumn = scoreCursor.getColumnIndex(ScoreProvider.TIME);
	     
	        do { 
	            // Get the field values 
	            name = scoreCursor.getString(nameColumn); 
	            score = scoreCursor.getInt(scoreColumn); 
	            time = scoreCursor.getLong(timeColumn);
	            
	            // Do something with the values.  
	            Score s = new Score(name, score, time); 
	            scores.add(s);
	 
	        } while (scoreCursor.moveToNext()); 
	 
	    } 

		
	}
	
}
