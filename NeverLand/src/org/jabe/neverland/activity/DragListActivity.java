package org.jabe.neverland.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jabe.neverland.R;
import org.jabe.neverland.model.CommonAdapter;
import org.jabe.neverland.model.ListItem;
import org.jabe.neverland.model.DragListItem_Normal;
import org.jabe.neverland.model.DragListItem_Title;
import org.jabe.neverland.model.ListElement;
import org.jabe.neverland.view.DragListView;

import android.app.Activity;
import android.os.Bundle;

public class DragListActivity extends Activity {

	private List<ListElement<String>> allList = new ArrayList<ListElement<String>>();
	private CommonAdapter<String> adapter = null;

	private List<ListElement<String>> navList = new ArrayList<ListElement<String>>();
	private List<ListElement<String>> moreList = new ArrayList<ListElement<String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_list_activity);

		initData();

		DragListView dragListView = (DragListView) findViewById(R.id.drag_list);
		adapter = new CommonAdapter<String>(allList);
		dragListView.setAdapter(adapter);
	}

	public void initData() {
		 List<String> dataList = new ArrayList<String>();
		 dataList.add("A组");
		 navList.add(new DragListItem_Title(dataList, this));
		 for(int i=0; i<10; i++){
			 dataList.add("A组"+i);
			 navList.add(new DragListItem_Normal(dataList, this));
		 }
		 
		 dataList.add("B组");
		 navList.add(new DragListItem_Title(dataList, this));
		 for(int i=0; i<10; i++){
			 dataList.add("B组"+i);
			 navList.add(new DragListItem_Normal(dataList, this));
		 }
		 
		 dataList.add("C组");
		 moreList.add(new DragListItem_Title(dataList, this));
		 for(int i=0; i<10; i++){
			 dataList.add("C组"+i);
			 moreList.add(new DragListItem_Normal(dataList, this));
		 }
		 allList.addAll(navList);
		 allList.addAll(moreList);
	}
}