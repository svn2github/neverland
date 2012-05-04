package org.jabe.neverland.activity;

import java.util.ArrayList;

import org.jabe.neverland.R;
import org.jabe.neverland.view.PullRefreshContainerView;
import org.jabe.neverland.view.PullRefreshContainerView.OnChangeStateListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PullRefreshActivity extends Activity {

	private ArrayAdapter<String> mAdapter;
	private PullRefreshContainerView mContainerView;
	private ListView mList;
	
	private TextView mRefreshHeader;
	private ArrayList<String> mStrings = new ArrayList<String>();
	
    private void addStrings(int count) {
    	int curSize = mStrings.size();
    	for(int i = 0; i < count; ++i) {
    		mStrings.add("String " + (curSize + i));
    	}
    	mAdapter.notifyDataSetChanged();
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_pullrefresh);
		
		mRefreshHeader = new TextView(this);
        mRefreshHeader.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mRefreshHeader.setGravity(Gravity.CENTER);
        mRefreshHeader.setText("Pull to refresh...");

        mContainerView = (PullRefreshContainerView) findViewById(R.id.container);
        mContainerView.setRefreshHeader(mRefreshHeader);
        
        mContainerView.setOnChangeStateListener(new OnChangeStateListener() {
    		@Override
    		public void onChangeState(PullRefreshContainerView container, int nowState, int oldState) {
    			switch(nowState) {
    			case PullRefreshContainerView.STATE_IDLE:
    			case PullRefreshContainerView.STATE_PULL:
    				mRefreshHeader.setText("Pull to refresh...");
    				break;
    			case PullRefreshContainerView.STATE_RELEASE:
    				mRefreshHeader.setText("Release to refresh...");
    				break;
    			case PullRefreshContainerView.STATE_LOADING:
    				mRefreshHeader.setText("Loading...");
    				new Thread(new Runnable() {
						@Override
						public void run() {
							PullRefreshActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									addStrings(1);
									mContainerView.completeRefresh();
									mAdapter.notifyDataSetChanged();
								}
		    				});
						}
    					
    				}).start();
    				break;
    			}
    		}
        });
        
        mList = mContainerView.getList();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings);
        mList.setAdapter(mAdapter);
        addStrings(3);
	}
}
