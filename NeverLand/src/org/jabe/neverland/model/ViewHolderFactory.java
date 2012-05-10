package org.jabe.neverland.model;

import org.jabe.neverland.R;

import android.view.View;
import android.widget.TextView;

public class ViewHolderFactory {
	public static class DragListViewHolder implements IViewHolder {
		TextView textview;
		@Override
		public void saveView(View view, int position, boolean save) {
			textview = (TextView) view.findViewById(R.id.drag_list_item_text);
			if (save) {
				view.setTag(this);
			}
		}
		
	}
}
