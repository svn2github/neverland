package org.jabe.neverland.model;

import java.util.List;

import org.jabe.neverland.R;

import android.content.Context;
import android.view.View;

public class DragListItem_Title extends DragListItem {

	public DragListItem_Title(List<String> list, Context context) {
		super(list, context);
		// TODO Auto-generated constructor stub
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
		holder.box1.setVisibility(View.VISIBLE);
		holder.box2.setVisibility(View.GONE);
		holder.title.setText(mLists.get(position));
	}

	@Override
	public boolean useConvertView() {
		// TODO Auto-generated method stub
		return true;
	}

}
