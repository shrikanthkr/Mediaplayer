package com.mediaplayer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.utility.StaticMusic;

public class MenufragmentAdapter extends BaseAdapter {
	private Context activity;
	ArrayList<String> item_list;
	private LayoutInflater inflater = null;
	ListView lv, recent_lv;
	int layoutResourceId;
	ImageView im;
	ArrayList<SongInfo> song_array;

	public MenufragmentAdapter(Activity activity, ArrayList<String> item_list,
			ListView lv) {
		this.activity = activity;
		this.item_list = item_list;
		this.lv = lv;
		inflater = activity.getLayoutInflater();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item_list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public class ViewHolder {
		public TextView title;
	}


	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View vi;
		ViewHolder holder;
		vi = arg1;
		////Log.i("SLIDE", arg0+"::actual");
		switch (arg0) {
		
		case 0:
			////Log.i("SLIDE", "CASE 0");
			if(vi==null){
			vi = inflater.inflate(R.layout.slide_header_xml, null);
			}
			return vi;
	
		case 1:
			////Log.i("SLIDE", "CASE 1");
			TextView tv;
			if(vi==null){
			vi = inflater.inflate(R.layout.nowplaying_slidelayout, null);
			im = (ImageView) vi
					.findViewById(R.id.slideout_playbutton_imageView);
			tv = (TextView) vi
					.findViewById(R.id.slideout_nowplaying_textView);
			vi.setTag(tv);
			}else{
				tv=(TextView)vi.getTag();
			}
			if (StaticMusic.songInfo != null) {
				tv.setText(StaticMusic.songInfo.getTitle());
			} else {
				tv.setText("No Song is being played");
			}
			try {
				if (StaticMusic.music.isPlaying()) {
					setPauseButton();
				} else
					setPlayButton();
			} catch (Exception e) {
				setPlayButton();
			}
			im.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try {
						if (StaticMusic.music.isPlaying()) {
							StaticMusic.music.pause();
							setPlayButton();
						}

						else {
							try {
								if (StaticMusic.music != null) {
									StaticMusic.music.play();
									setPauseButton();
								}
							} catch (Exception e) {
								setPlayButton();
							}
						}
					} catch (Exception e) {
					}
					// ////Log.i("Clicked", "Play buton in slide layout");
				}
			});

			return vi;
		case 3:
			////Log.i("SLIDE", "CASE $");
			vi = inflater.inflate(R.layout.slide_logo, null);
			return vi;
		default:
			if(vi==null){
			vi = inflater.inflate(R.layout.itemlist_item, null);
			holder = new ViewHolder();
			vi.setTag(holder);
			}else{
				holder=(ViewHolder)vi.getTag();
			}

			try {
				holder.title = (TextView) vi.findViewById(R.id.items_textView);
				holder.title.setText(item_list.get(arg0));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return vi;

		}

	}

	public void setPlayButton() {
		im.setImageResource(R.drawable.play);
	}

	public void setPauseButton() {
		im.setImageResource(R.drawable.pause);
	}

}
