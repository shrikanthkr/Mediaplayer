package com.mediaplayer.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.devsmart.android.ui.HorizontalListView;
import com.mediaplayer.adapter.MenufragmentAdapter.ViewHolder;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PlayListDialogAdapter extends BaseAdapter{
	
	
	
	private Activity activity;
	ArrayList<String> playlists;
	private LayoutInflater inflater = null;
	BaseAdapter adapter;
	ListView lv;
	

	public PlayListDialogAdapter(Activity activity,ArrayList<String> playlists, ListView lv) {
		super();
		this.activity = activity;
		this.playlists = playlists;
		this.lv = lv;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return playlists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}


	public class ViewHolder{
		TextView  playlist_name;
	}
	
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View vi;
		ViewHolder holder;
		vi = arg1;
		if (vi == null) {
			vi = inflater.inflate(R.layout.playlist_dialog_listitem_xml, null);
			holder = new ViewHolder();
			holder.playlist_name = (TextView) vi.findViewById(R.id.playlistname_textView);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		try {
			
			holder.playlist_name.setText(playlists.get(arg0));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//Log.i("Dialog", "LISTVIEW");
		return vi;
	}

}
