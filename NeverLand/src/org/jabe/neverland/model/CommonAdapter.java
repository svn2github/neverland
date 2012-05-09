package org.jabe.neverland.model;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommonAdapter<T> extends BaseAdapter{
	
	public CommonAdapter(List<ListElement<T>> mList) {
		super();
		this.mList = mList;
	}

	private List<ListElement<T>> mList;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public ListElement<T> getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return mList.get(position).getView(position, convertView, parent);
	}

	public void remove(ListElement<T> dragItem, int position) {
		dragItem.remove(position);
		mList.remove(position);
	}

	public void insert(ListElement<T> dragItem, int position, T data) {
		dragItem.insert(data, position);
		mList.add(position, dragItem);
	}
}
