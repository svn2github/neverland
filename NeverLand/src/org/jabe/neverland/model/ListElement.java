package org.jabe.neverland.model;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;

public interface ListElement<T> {
	public int getLayoutId();
	public View getView(int position, View view, ViewGroup parent);
	public boolean isClickable();
	public void onClick();
	public List<T> getDataList();
	public void remove(int position);
	public void add(T data);
	public void insert(T data, int position);
	public boolean useConvertView();
	public ListElement<T> clone();
}
