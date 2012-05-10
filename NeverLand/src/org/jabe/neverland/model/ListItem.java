package org.jabe.neverland.model;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ListItem<T> implements ListElement<T> {
	
	protected List<T> mLists;
	protected Context mContext;

	public ListItem(List<T> list, Context context) {
		this.mContext = context;
		this.mLists = list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		IViewHolder holder = null;
		if (useConvertView()) {
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
				holder = getViewHolder();
				holder.saveView(view, position, true);
			} else {
				holder =  (IViewHolder) view.getTag();
				if (holder == null) {
					view = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
					holder = getViewHolder();
					holder.saveView(view, position, true);
				} else {
					// nothing
				}
			}
		} else {
			view = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
			holder = getViewHolder();
			holder.saveView(view, position, false);
		}
		//do something for holder~~~
		refreshHolder(holder, position);
		return view;
	}
	
	public abstract void refreshHolder(IViewHolder holder, int position);
	public abstract IViewHolder getViewHolder();

	@Override
	public List<T> getDataList() {
		
		return mLists;
	}

	@Override
	public void remove(int postion) {
		mLists.remove(postion);
	}

	@Override
	public void add(T data) {
		mLists.add(data);
	}

	@Override
	public void insert(T data, int position) {
		mLists.add(position, data);
	}
}
