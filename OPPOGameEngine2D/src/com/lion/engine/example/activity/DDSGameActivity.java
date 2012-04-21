package com.lion.engine.example.activity;

import java.util.UUID;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lion.engine.R;
import com.lion.engine.activity.BaseGameActivity;
import com.lion.engine.example.screen.DDSGameScreen;
import com.lion.engine.screen.BaseScreen;
import com.lion.engine.util.JsonParser;
import com.lion.engine.util.PayResult;
import com.lion.engine.util.ResultChecker;
import com.lion.engine.util.Util;
import com.nearme.gamecenter.api.OppoGC;
import com.nearme.pay.AppInfo;
import com.nearme.pay.NearMePay;
import com.nearme.pay.Product;
import com.nearme.pay.RequestModel;

public class DDSGameActivity extends BaseGameActivity {

	private Context ctx;
	
	private Product product;
	private DDSGameScreen dds;

	public static final String GAME_NAME = "happy dds";
	public static final String GAME_ID = "441";
	public static final String GAME_KEY = "1H7jiseiR908QNpf6sJ6TC252";
	public static final String GAME_SECRET = "42311A59eFb54916966cB03b33000a62";
	
	public static final String HIGH_SCORE = "446";
	public static final String LOW_SCORE = "447";
	public static final String LEVEL_1 = "486";
	public static final String LEVEL_2 = "487";
	public static final String LEVEL_3 = "488";
	public static final String LEVEL_4 = "489";
	public static final String LEVEL_0 = "490";
	
	public static final int MENU_HIGH_BOARD_ID = 0;
	public static final int MENU_LOW_BOARD_ID = 1;
	public static final int MENU_ACH_ID = 2;
	public static final int MENU_BUY_TIME = 3;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		ctx = this;

		setAppInfo();
//
//		product = new Product("", 0.01f, Util.getString(ctx,
//				R.string.buy_time_content), Util.getString(ctx,
//				R.string.buy_time_title), 1);
//		product = new Product(getRequestId(), 2.0f, "小说《生命不能承受之轻》", "生命不能承受之轻", 1);
		product = new Product("", Util.getString(ctx,
				R.string.buy_time_title), Util.getString(ctx,
						R.string.buy_time_content), "0.01");
	}

	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}



	private String getRequestId() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

	private void setAppInfo() {
		AppInfo.PACKAGENAME  = "com.lion.engine";
		AppInfo.NOTIFY_URL = "";
		AppInfo.PARTNER_ID = "681";
//		AppInfo.APP_NAME = "PayDemo";
		AppInfo.RSA_PRIVATE_KEY = "MIICXAIBAAKBgQCxLUmVu0uZWeFkmg0ifwKoiB/Ac9JWl23GdoqlgAxxTvw7faDU" +
				"2/dybqMB5ZVbLDR5yt1B+l7wgshVTnqaMeI+Gy4kQjrtBsfZM8T99LiZYsueiRZb" +
				"4yi+5Tge1O8pRMyq+dGbYF4vNhiyEzT7RbcwSqyYi6hhKo9clDUAFrw0WQIDAQAB" +
				"AoGAVBaqVeYOezmtP5HdqgGdwV7Zsv1FVtOHxil8pA+h2CUkkReOvXeoeScJNl1A" +
				"BYV1YlhlqmqppHxtmA8nIQeL2CO60uVZ/IaVJSmvd3YUwkdmvWwaCyNLq5QLaTFq" +
				"MK0Bus4IyHU4iPSMJLpXeuC6T44T25v1SqWPUAwgWDG4gAECQQDd2GlsJfP7s6AQ" +
				"6oHYzaZR/XAS1lUuob80NMpkz+lJGVPzLqLnShazJoUT30wXxRhIy+ic4VoeJBYU" +
				"K750dcnhAkEAzHRbwbIxIyn8kdPkGKgdgCN5ocbyLxJ3AKBS4kReV/5WXpL7pl7o" +
				"u6lGwTWkLo+4mMNfeSxfUkNT+ENtNJLpeQJAcm5n6rYlBQdhoHgmZDun+BDfl4fh" +
				"Rn1+UCKO57NrJR6gdAbxVpab4OsUp+x9pqEu5pXvtcR0JKn+jYcG5bHAYQJBAJON" +
				"Mh8y+3VWOtn/YrFMuqWcdI6FZokEOW/xRH/ZOYElo61zRDwIaMvuWYrcrJWR0XYm" +
				"IcPwewaCYppcBvcXQ3kCQC0GH3AWsJMgfnH755+t1sabDBVLF+SEkZlWUAzCvlnC" +
				"h3wdxzt8Gx5mKToyyftux5HnCt8t2StMx5WFYdxBzFg=";
	}
//
	private void buyTime() {
		product.setOrderNo(getRequestId()); // 每次都生成唯一的交易号
		NearMePay nearMePay = new NearMePay(ctx);
		boolean payStatus = nearMePay.pay(new RequestModel(product), handler);
		if (payStatus) {
			// do something...
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String response = (String) msg.obj;		
			try {
				PayResult payResult = JsonParser.getPayResult(response);
				boolean isPayOK = ResultChecker.isPayOK(response);
				boolean isCancel = payResult.pay_status.equalsIgnoreCase("8002");
				if (isPayOK) {
					// 支付成功操作
					Toast.makeText(DDSGameActivity.this, "支付成功", Toast.LENGTH_LONG).show();
					dds.addTime(60);
					return;
				}
				if (isCancel) {
					Toast.makeText(DDSGameActivity.this, "用户取消支付操作", Toast.LENGTH_LONG).show();
					return;
				}
				// 支付失败操作
				Toast.makeText(DDSGameActivity.this, "支付失败", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	};


	@Override
	public BaseScreen getFirstScreen() {
		// TODO Auto-generated method stub
		dds = new DDSGameScreen(this);
		return dds;
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_MENU) {
//			// showDialog(1);
//			return true;
//		} else {
//			return super.onKeyDown(keyCode, event);
//		}
//
//	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub		
		super.onCreateOptionsMenu(menu);
			
		menu.add(0, MENU_HIGH_BOARD_ID, 1, Util.getString(this, R.string.check_high_board));
		menu.add(0, MENU_LOW_BOARD_ID, 2, Util.getString(this, R.string.check_low_board));
		menu.add(0, MENU_ACH_ID, 3,Util.getString(this, R.string.check_ach));
		menu.add(0, MENU_BUY_TIME, 4, "买60秒");

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		switch (item.getItemId()) {
		case MENU_HIGH_BOARD_ID:{
			OppoGC.openLeaderboard(HIGH_SCORE);
			return true;
		}
		case MENU_LOW_BOARD_ID:{
			OppoGC.openLeaderboard(LOW_SCORE);
			return true;
		}
		case MENU_ACH_ID:{
			OppoGC.openAchievementList();
			return true;
		}
		case MENU_BUY_TIME:{
			buyTime();
			return true;
		}
	}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 1) {
			return new AlertDialog.Builder(DDSGameActivity.this)
					.setIcon(R.drawable.icon)
					.setTitle(R.string.buy_time_title)
					.setMessage(R.string.buy_time_content)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
//									buyTime();
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked Cancel so do some stuff */
								}
							}).create();
		} else {
			return super.onCreateDialog(id);
		}
	}

}
