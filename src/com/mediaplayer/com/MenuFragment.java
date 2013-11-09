package com.mediaplayer.com;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.mediaplayer.adapter.MenufragmentAdapter;
import com.mediaplayer.utility.StaticMusic;

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

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		((SplashActivity) getActivity()).getSlideoutHelper().close();

		switch (position) {
		case 1:
			Intent toNp = new Intent(getActivity(), Nowplaying.class);
			toNp.putExtra("activity", "nowplaying");
			toNp.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(toNp);
			getActivity().overridePendingTransition(0, 0);
			break;
		case 2:
			Intent x = new Intent(getActivity().getApplicationContext(),
					SongListFragment.class);
			x.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(x);
			getActivity().overridePendingTransition(0, 0);
			break;


		}
		
		
	}



}
