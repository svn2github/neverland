package org.jabe.neverland;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
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

		public Bean(String packageName, String title, String description) {
			super();
			this.packageName = packageName;
			this.title = title;
			this.description = description;
		}

		public Bean(String packageName) {
			super();
			this.packageName = packageName;
		}

		public Bean() {

		}

		@SuppressWarnings("rawtypes")
		@Override
		public void run() {
			try {
				Class clazz = Class.forName(packageName);
				startIntentByClass(clazz);
			} catch (Exception e) {
				
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

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

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