package com.mediaplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mediaplayer.app.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.interfaces.RecyclerClickHelper;
import com.mediaplayer.viewholders.SongListViewHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SongsListAdapter extends BaseRecyclerAdapter<SongListViewHolder>{
	private Activity activity;
	ArrayList<SongInfo> song_array;
	private LayoutInflater inflater = null;
	RecyclerView lv;
	DecimalFormat format;
	int min, sec, total;
	final static int SECTION = 2;
	final static int NORMAL = 1;
	int[] rowStates;

	public SongsListAdapter(Activity activity2, ArrayList<SongInfo> song_array,
							RecyclerView lv, RecyclerClickHelper clickHelper) {
		super(clickHelper);
		activity = activity2;
		this.song_array = song_array;
		this.lv = lv;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.lv = lv;
		format = new DecimalFormat("#.00");
		rowStates = new int[getItemCount()];
	}

	public ArrayList<SongInfo> getUrlList() {
		return song_array;
	}

	public void addAll(ArrayList<SongInfo> songs){
		this.song_array = songs;
		notifyDataSetChanged();
	}


	@Override
	public int getItemCount() {
		return song_array.size();
	}

	@Override
	public SongListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		super.onCreateViewHolder(parent, viewType);
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.songlist_item, parent, false);
		return new SongListViewHolder(v);
	}

	@Override
	public void onBindViewHolder(SongListViewHolder holder, int position) {
		super.onBindViewHolder(holder, position);
		boolean showSeparator = false;
		holder.title.setText(song_array.get(position).getTitle());
		total = Integer.parseInt(song_array.get(position).getDuration()) / 1000;
		min = total / 60;
		sec = total % 60;
		holder.duration.setText(min + ":" + sec);
		holder.album.setText(song_array.get(position).getAlbum());
		switch (rowStates[position]){
			case SECTION:
				showSeparator = true;
				break;
			case NORMAL:
				showSeparator = false;
				break;
			default:
				if (position == 0) {
					showSeparator = true;
					rowStates[position] = SECTION;
				}else {
					char previousName = Character.toLowerCase(song_array.get(position - 1).getTitle().toCharArray()[0] );
					char currentName = Character.toLowerCase(song_array.get(position).getTitle().toCharArray()[0] );
					if (previousName != currentName) {
						showSeparator = true;
						rowStates[position] = SECTION;
					} else {
						showSeparator = false;
						rowStates[position] = NORMAL;
					}
				}
				break;
		}
		if (showSeparator) {
			holder.section_headerview.setText( Character.toUpperCase(song_array.get(position).getTitle().toCharArray()[0]) +"" );
			holder.section_headerview.setVisibility(View.VISIBLE);
		}
		else {
			holder.section_headerview.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

}