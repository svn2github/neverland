package org.jabe.neverland.model;

import java.util.List;

import org.jabe.neverland.R;

import android.content.Context;
import android.view.View;

public class DragListItem_Normal extends DragListItem {

	public DragListItem_Normal(List<String> list, Context context) {
		super(list, context);
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
		holder.box1.setVisibility(View.GONE);
		holder.box2.setVisibility(View.VISIBLE);
		holder.content.setText(mLists.get(position));
	}


	@Override
	public boolean useConvertView() {
		// TODO Auto-generated method stub
		return true;
	}
}
