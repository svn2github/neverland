package org.jabe.neverland.model;

import java.util.List;

import org.jabe.neverland.R;
import org.jabe.neverland.model.ViewHolderFactory.DragListViewHolder;

import android.content.Context;
import android.view.View;

public class DragListItem_Title extends ListItem<String> {

	public DragListItem_Title(List<String> list, Context context) {
		super(list, context);
	}

	@Override
	public int getLayoutId() {
		return R.layout.drag_list_item_tag;
	}


	@Override
	public boolean isClickable() {
		return false;
	}

	@Override
	public void onClick() {
		
	}

	@Override
	public void refreshHolder(IViewHolder holder, int position) {
		ViewHolderFactory.DragListViewHolder h = (DragListViewHolder) holder;
		h.textview.setText(mLists.get(position));
	}

	@Override
	public boolean useConvertView() {
		return true;
	}

	@Override
	public IViewHolder getViewHolder() {
		return new ViewHolderFactory.DragListViewHolder();
	}

}
