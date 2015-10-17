package com.mediaplayer.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;

public class SongsListAdapter extends BaseAdapter {
	private Activity activity;
	ArrayList<SongInfo> song_array;
	private LayoutInflater inflater = null;
	BaseAdapter adapter;
	ListView lv;
	Thread t;
	// SongInfoDatabase database;
	String searchString = "";
	DecimalFormat format;
	int min, sec, total;
	final static int SECTION = 2;
	final static int NORMAL = 1;
	int[] rowStates;

	public SongsListAdapter(Activity activity2, ArrayList<SongInfo> song_array,
							ListView lv) {
		activity = activity2;
		this.song_array = song_array;
		this.lv = lv;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.lv = lv;
		format = new DecimalFormat("#.00");
		rowStates = new int[getCount()];
	}

	public ArrayList<SongInfo> getUrlList() {
		return song_array;
	}

	public void addAll(ArrayList<SongInfo> songs){
		this.song_array = songs;
		notifyDataSetChanged();
	}

	public void setUrlList(ArrayList<SongInfo> song_array) {
		this.song_array = song_array;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return song_array.size();
	}

	public ListView getListView() {
		return lv;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return super.getViewTypeCount();
	}

	public class ViewHolder {
		public TextView title;
		public TextView duration;
		public TextView album;
		public TextView section_headerview;
	}

	public View getView(final int arg0, View vi, ViewGroup arg2) {
		ViewHolder holder;
		boolean showSeparator = false;

		if (vi == null) {

			holder = new ViewHolder();
			vi = inflater.inflate(R.layout.songlist_item, null);
			holder.title = (TextView) vi.findViewById(R.id.song_textView);
			holder.album = (TextView) vi.findViewById(R.id.song_album_textView);
			holder.duration = (TextView) vi.findViewById(R.id.song_duration_textView);
			holder.section_headerview = (TextView) vi.findViewById(R.id.section_headerview);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
			holder.title.setText(song_array.get(arg0).getTitle());
			total = Integer.parseInt(song_array.get(arg0).getDuration()) / 1000;
			min = total / 60;
			sec = total % 60;
			holder.duration.setText(min + ":" + sec);
			holder.album.setText(song_array.get(arg0).getAlbum());
			switch (rowStates[arg0]){
				case SECTION:
					showSeparator = true;
					break;
				case NORMAL:
					showSeparator = false;
					break;
				default:
					if (arg0 == 0) {
						showSeparator = true;
						rowStates[arg0] = SECTION;
					}else {
						char previousName = Character.toLowerCase(song_array.get(arg0 - 1).getTitle().toCharArray()[0] );
						char currentName = Character.toLowerCase(song_array.get(arg0).getTitle().toCharArray()[0] );
						if (previousName != currentName) {
							showSeparator = true;
							rowStates[arg0] = SECTION;
						} else {
							showSeparator = false;
							rowStates[arg0] = NORMAL;
						}
					}
					break;
			}
		if (showSeparator) {
			holder.section_headerview.setText( Character.toUpperCase(song_array.get(arg0).getTitle().toCharArray()[0]) +"" );
			holder.section_headerview.setVisibility(View.VISIBLE);
		}
		else {
			holder.section_headerview.setVisibility(View.GONE);
		}
		return vi;
	}

	private void setSection(TextView text, String label) {
		// text.setBackgroundColor(0xffe47168);
		text.setBackgroundResource(R.drawable.blue_strip);
		text.setText((label.substring(0, 1) + "").toUpperCase());
		text.setTextSize(15);
		text.setPadding(5, 0, 0, 0);
		text.setGravity(Gravity.CENTER_VERTICAL);

	}


}