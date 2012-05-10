package org.jabe.neverland.model;

import java.util.List;

import android.content.Context;

public class SimpleListItem extends ListItem<String> {
	public SimpleListItem(List<String> list, Context context) {
		super(list, context);
	}

	@Override
	public int getLayoutId() {
		return android.R.layout.simple_list_item_1;
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public void onClick() {
		
	}

	@Override
	public boolean useConvertView() {
		return true;
	}

	@Override
	public void refreshHolder(IViewHolder holder, int position) {
		ViewHolderFactory.SimpleListItem h = (org.jabe.neverland.model.ViewHolderFactory.SimpleListItem) holder;
		h.text1.setText(mLists.get(position));
	}

	@Override
	public IViewHolder getViewHolder() {
		return new ViewHolderFactory.SimpleListItem();
	}

	@Override
	public ListElement<String> clone() {
		return new SimpleListItem(mLists, mContext);
	}
}
