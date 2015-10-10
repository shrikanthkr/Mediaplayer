package com.mediaplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.listener.PlaylistChangedListener;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

	private Activity activity;
	ArrayList<ArrayList<SongInfo>> song_all_array;
	private LayoutInflater inflater = null;
	GridView gv;
	ViewHolder holder;
	PlaylistChangedListener playlistChangedListener;

	public GridAdapter(Activity activity,
								  ArrayList<ArrayList<SongInfo>> song_array, GridView gv) {
		super();
		this.activity = activity;
		this.song_all_array = song_array;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.gv = gv;
		this.playlistChangedListener = playlistChangedListener;
	}

	public void addAll(ArrayList<ArrayList<SongInfo>> song_array){
		this.song_all_array = song_array;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return song_all_array.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder {
		TextView name;
		ImageView album;
	}

	@Override
	public View getView(int position, View vi, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (vi == null) {
			holder = new ViewHolder();
			vi = inflater.inflate(R.layout.grid_listitem_xml, null);
			holder.name = (TextView) vi
					.findViewById(R.id.song_name);
			holder.album = (ImageView) vi
					.findViewById(R.id.album_imageView);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		holder.name.setText(song_all_array.get(position).get(0).getPlaylist());

		return vi;
	}
	
	

}
