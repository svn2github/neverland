package org.jabe.neverland.activity;

import java.io.File;
import java.io.IOException;

import org.jabe.neverland.R;
import org.jabe.neverland.download.Task;
import org.jabe.neverland.download.TaskAssign;
import org.jabe.neverland.download.TaskAssign.TaskListener;

import android.R.attr;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DownloadActivity extends Activity {
	
	private Button mButton;
	private ProgressBar mProgressBar;
	private ProgressDialog mProgressDialog;
	private long contentLength = -1;
	private Task task;
	private TaskAssign taskAssign;
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(android.os.Message msg) {
			Bundle b = msg.getData();
			double d = b.getDouble("downloaded");
			if (d > 0) {
				mProgressBar.setProgress((int) d);
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_download_manager);
		setUpView();
		beginToDown();
	}

	private void beginToDown() {
		final String url = "http://down.sandai.net/Thunder5.7.9.472.exe";
		task = new Task();
		task.setDownURL(url);
		task.setSaveFile(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"Thunder.exe");
		task.setSectionCount(5);
		task.setWorkerCount(5);
		task.setBufferSize(8 * 1024);
		taskAssign = new TaskAssign();
		taskAssign.setTaskListener(new TaskListener() {
			@Override
			public void resumeTask() {
				makeToast("Resume to download");
			}
			
			@Override
			public void onUpdateProgress(double added, double downloaded, double total) {
				Message message = mHandler.obtainMessage();
				Bundle b = new Bundle();
				b.putDouble("downloaded", downloaded);
				b.putDouble("total", total);
				message.setData(b);
				message.sendToTarget();
			}
			
			@Override
			public void onSuccess() {
				makeToast("Finish download");
			}
			
			@Override
			public void onPreTask() {
				makeToast("Begin to download");
			}
			
			@Override
			public void onFailure() {
				makeToast("Download failure");
			}
			
			@Override
			public void onBeforeExecute() {
				
			}

			@Override
			public void onException(Exception e) {
				makeToast("An exception occured while downloading : " + e.getMessage());				
			}
		});
		
		AsyncTask<Void, Void, Long> aTask = new AsyncTask<Void, Void, Long>() {
			@Override
			protected void onPreExecute() {
				mProgressDialog.show();
			}
			
			@Override
			protected Long doInBackground(Void... params) {
				try {
					return Task.getContentLength(url);
				} catch (IOException e) {
					makeToast("Net Error");
					return new Long(0);
				}
			}
			
			@Override
			protected void onPostExecute(Long result) {
				mProgressDialog.dismiss();
				contentLength = result;
				if (contentLength < 0 ) {
					return;
				}
				mProgressBar.setMax((int) contentLength);
				mProgressBar.setProgress(0);
				task.setContentLength(contentLength);
				taskAssign.work(task);
					
			}
		};
		aTask.execute();
	}
	
	private void setUpView() {
		mButton = (Button) findViewById(R.id.button1);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setTitle("Download Initial...");
	}
	
	public void makeToast(final String content) {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(DownloadActivity.this, content, 1000).show();
			}
		});
	}
}
