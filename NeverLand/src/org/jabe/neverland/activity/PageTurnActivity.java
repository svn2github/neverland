package org.jabe.neverland.activity;

import org.jabe.neverland.view.PageTurningView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class PageTurnActivity extends Activity {

    PageTurningView ptview;
	AlertDialog.Builder builder;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //设置横屏显示
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT){
        	   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        	}
        
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ptview=new PageTurningView(this);
        setContentView(ptview);
        builder=new AlertDialog.Builder(this);
    }

	@Override

	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键

	        //processExit();
	    	builder.setMessage("真要退出么");
	    	builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "sorry", Toast.LENGTH_LONG).show();
					finish();
				}
			});
	    	builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			});
	    	
	    	
	    	AlertDialog ad=builder.create();
	    	ad.show();
	        return true;

	    } else if(keyCode == KeyEvent.KEYCODE_MENU) {

	        //监控/拦截菜单键

	    } else if(keyCode == KeyEvent.KEYCODE_HOME) {

	        //由于Home键为系统键，此处不能捕获，需要重写onAttachedToWindow()

	   }
	    return super.onKeyDown(keyCode, event);
	}
}
