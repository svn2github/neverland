package org.jabe.neverland;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jabe.neverland.util.ViewUtil;
import org.jabe.neverland.view.FloatTextView;
import org.xmlpull.v1.XmlPullParser;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NeverLandActivity extends ListActivity {
	private List<Entry> mBeansList;

	private class Bean implements Entry {
		String packageName;
		String title;
		String description;

		public Bean(String packageName) {
			super();
			this.packageName = packageName;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void run() {
			try {
				Class clazz = Class.forName(packageName);
				startIntentByClass(clazz);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}

		@Override
		public String toString() {
			return title;
		}

		@Override
		public String getDescription() {
			return description;
		}

		@SuppressWarnings("unused")
		public String getPackageName() {
			return packageName;
		}

		@SuppressWarnings("unused")
		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		@SuppressWarnings("unused")
		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

	private String mDateTimeXmlContent;
	private static final String TAG = "yanwc";
	private static final String oppoServerURL = "http://newds.oppo.com/autotime/dateandtime.xml";

	private boolean getNetType() {
		ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conn == null) {
			// Log.d(TAG, "conn = " + conn );
			return false;
		}

		NetworkInfo info = conn.getActiveNetworkInfo();
		if (info == null) {
			// Log.d(TAG, "info = " + info );
			return false;
		}

		String type = info.getTypeName(); // MOBILE(GPRS)WIFI
		// Log.d(TAG, "type = " + type );
		if (type.equalsIgnoreCase("WIFI")) {
			return true;
		} else if (type.equalsIgnoreCase("MOBILE")
				|| type.equalsIgnoreCase("GPRS")) {
			String apn = info.getExtraInfo();
			// Log.d(TAG, "apn = " + apn );
			if (apn != null && apn.equalsIgnoreCase("cmwap")) {
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	private boolean oppoServer() {
		boolean returnFlag = false;
		Log.d(TAG, "oppoServer run");
		try {
			// modify by maoJianGuo 2011-07-08
			URL url = new URL(oppoServerURL);
			HttpURLConnection httpconn = null;
			String proxyHost = android.net.Proxy.getDefaultHost();
			// Log.d(TAG, "proxyHost: " + proxyHost);
			int proxyPort = android.net.Proxy.getDefaultPort();
			// Log.d(TAG, "proxyPort: " + proxyPort);

			// establish the connection
			if (true == getNetType()) {
				// Log.d(TAG, "true == getNetType()");
				Log.d(TAG, "1");
				httpconn = (HttpURLConnection) url.openConnection();
				Log.d(TAG, "2");
			} else if (false == getNetType()) {
				// Log.d(TAG, "false == getNetType()");
				// InetSocketAddress("10.0.0.172", 80)
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
						proxyHost, proxyPort));
				httpconn = (HttpURLConnection) url.openConnection(proxy);
			}

			httpconn.setDoInput(true);
			httpconn.setConnectTimeout(5000);
			Log.d(TAG, "3");
			httpconn.connect();
			Log.d(TAG, "4");
			InputStreamReader mInputStreamReader = null;
			BufferedReader mBufferedReader = null;
			String mDateTimeXmlString = "";
			long mBeginParseTime = 0;
			if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Log.d(TAG, "5");
				// get the time when begin parse the xml file
				mBeginParseTime = System.currentTimeMillis();
				// read data from net stream
				mInputStreamReader = new InputStreamReader(
						httpconn.getInputStream(), "utf-8");// GB2312
				mBufferedReader = new BufferedReader(mInputStreamReader);
				String lineString = "";
				while ((lineString = mBufferedReader.readLine()) != null) {
					mDateTimeXmlString = lineString;
					// Log.e(TAG, "mDateTimeXmlString" + mDateTimeXmlString);
					mDateTimeXmlContent = mDateTimeXmlString;
				}
				Log.d(TAG, "6");
			}
			// disconnect
			mBufferedReader.close();
			mInputStreamReader.close();
			httpconn.disconnect();
			Log.d(TAG, "7");

		} catch (Exception e) {
			Log.e(TAG, "oppoServer exception: " + e);
			returnFlag = false;
		} finally {
			return returnFlag;
		}
	}

	private interface Entry {
		public String toString();

		public String getDescription();

		public void run();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		setListAdapter(new ArrayAdapter<Entry>(this,
				android.R.layout.simple_list_item_1, mBeansList));
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mBeansList.get(position).run();
			}
		});
		FloatTextView floatTextView = new FloatTextView(this);
		FloatTextView.TOOL_BAR_HIGH = ViewUtil.getWindowVisibleDisplayFrame(this).top;
		FloatTextView.params = ViewUtil.addTopView(this, floatTextView);
	}

	private void initData() {
		mBeansList = parseXml(getResources().getXml(R.xml.config));
	}

	@SuppressWarnings("rawtypes")
	private void startIntentByClass(Class clazz) {
		Intent it = new Intent(this, clazz);
		startActivity(it);
	}

	private List<Entry> parseXml(XmlResourceParser xml) {
		List<Entry> list = new ArrayList<Entry>();
		Bean curEntry = null;
		if (xml != null) {
			try {
				int eventType = xml.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
						break;
					case XmlPullParser.START_TAG:// 开始元素事件
						final String name = xml.getName();
						if (name.equalsIgnoreCase("item")) {
							curEntry = new Bean(xml.getAttributeValue(0));
						} else if (curEntry != null) {
							if (name.equalsIgnoreCase("title")) {
								curEntry.setTitle(xml.nextText());
							} else if (name.equalsIgnoreCase("description")) {
								curEntry.setDescription(xml.nextText());
							}
						}
						break;
					case XmlPullParser.END_TAG:// 结束元素事件
						if (xml.getName().equalsIgnoreCase("item")
								&& curEntry != null) {
							list.add(curEntry);
							curEntry = null;
						}
						break;
					}
					eventType = xml.next();
				}
			} catch (Exception e) {

			}
			xml.close();
		}
		return list;
	}
}