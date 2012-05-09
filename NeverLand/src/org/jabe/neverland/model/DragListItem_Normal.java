package org.jabe.neverland.model;

import java.util.List;

import org.jabe.neverland.R;

import android.content.Context;

public class DragListItem_Normal extends DragListItem {

	public DragListItem_Normal(List<String> list, Context context) {
		super(list, context);
	}


	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.drag_list_item;
	}


	@Override
	public void onClick() {
		
	}


	@Override
	public boolean isClickable() {
		return true;
	}


	@Override
	public void refreshHolder(ViewHolder holder, int position) {
		holder.textview.setText(mLists.get(position));
	}


	@Override
	public boolean userConvertView() {
		// TODO Auto-generated method stub
		return true;
	}
}
