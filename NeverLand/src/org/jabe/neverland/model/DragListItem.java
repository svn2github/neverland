package org.jabe.neverland.model;

import java.util.List;

import org.jabe.neverland.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class DragListItem implements ListElement<String> {
	
	protected List<String> mLists;
	protected Context mContext;

	public DragListItem(List<String> list, Context context) {
		this.mContext = context;
		this.mLists = list;
	}
	
	public class ViewHolder implements IViewHolder {
		TextView textview;

		@Override
		public void saveView(View view, int position, boolean save) {
			textview = (TextView) view.findViewById(R.id.drag_list_item_text);
			if (save) {
				view.setTag(this);
			}
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder = null;
		if (userConvertView()) {
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
				holder = new ViewHolder();
				holder.saveView(view, position, true);
			} else {
				holder =  (ViewHolder) view.getTag();
				if (holder == null) {
					view = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
					holder = new ViewHolder();
					holder.saveView(view, position, true);
				} else {
					// nothing
				}
			}
		} else {
			view = LayoutInflater.from(mContext).inflate(getLayoutId(), null);
			holder = new ViewHolder();
			holder.saveView(view, position, false);
		}
		//do something for holder~~~
		refreshHolder(holder, position);
		return view;
	}
	
	public abstract void refreshHolder(ViewHolder holder, int position);

	@Override
	public List<String> getDataList() {
		
		return mLists;
	}

	@Override
	public void remove(int postion) {
		mLists.remove(postion);
	}

	@Override
	public void add(String data) {
		mLists.add(data);
	}

	@Override
	public void insert(String data, int position) {
		mLists.add(position, data);
	}
}
