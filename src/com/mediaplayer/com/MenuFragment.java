package com.mediaplayer.com;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.mediaplayer.adapter.MenufragmentAdapter;

public class MenuFragment extends ListFragment {
	ArrayList<String> item_list;
	MenufragmentAdapter adapter;
	ListView lv;
	int size;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		item_list = new ArrayList<String>();
		item_list.add("Header");
		item_list.add(" Now Playing");
		item_list.add("Songs");
		item_list.add(" Logo");
		getListView().setBackgroundDrawable(getResources().getDrawable(R.drawable.slidelist_bg_repeat));
		getListView().setScrollingCacheEnabled(false);
		getListView().setDivider(null);
		adapter = new MenufragmentAdapter(getActivity(), item_list,
				getListView());
		setListAdapter(adapter);

		adapter.notifyDataSetChanged();
	}





}
