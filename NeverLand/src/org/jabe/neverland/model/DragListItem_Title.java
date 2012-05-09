package org.jabe.neverland.model;

import java.util.List;

import org.jabe.neverland.R;

import android.content.Context;

public class DragListItem_Title extends DragListItem {

	public DragListItem_Title(List<String> list, Context context) {
		super(list, context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.drag_list_item_tag;
	}


	@Override
	public boolean isClickable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick() {
		
	}

	@Override
	public void refreshHolder(ViewHolder holder, int position) {
		holder.textview.setText(mLists.get(position));
	}

	@Override
	public boolean userConvertView() {
		// TODO Auto-generated method stub
		return false;
	}

}
