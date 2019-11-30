package com.mediaplayer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsManager;
import com.mediaplayer.manager.BroadcastManager;
import com.mediaplayer.utility.AlbumArtLoader;

import java.util.ArrayList;


public class NowPlayingHorizontalAdapter extends  RecyclerView.Adapter<NowPlayingHorizontalAdapter.ViewHolder>  {

	ArrayList<SongInfo> songArray;
	Activity context;
	RecyclerView rv;
	public NowPlayingHorizontalAdapter(ArrayList<SongInfo> songArray, RecyclerView rv, Activity activity) {
		this.songArray = songArray;
		context = activity;
		this.rv = rv;
	}


	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = rv.getChildPosition(v);
			SongInfo songInfo = songArray.get(position);
			Intent playSong = new Intent(BroadcastManager.PLAYSONG);
			Bundle b= new Bundle();
			b.putSerializable(BroadcastManager.SONG_KEY, songInfo);
			playSong.putExtras(b);
			LocalBroadcastManager.getInstance(context).sendBroadcast(playSong);
		}
	};


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.nowplaying_horizonal_songitem, parent, false);
		ViewHolder vh = new ViewHolder(v);
		v.setOnClickListener(clickListener);
		return vh;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.songName.setText(songArray.get(position).getDisplayName());
		new AlbumArtLoader(context,songArray.get(position).getAlbum_id(),holder.album, AlbumArtLoader.Mode.ALBUM).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		if(SongsManager.getInstance().getCurrentSongInfo()!=null
				&& songArray.get(position).getId()  == SongsManager.getInstance().getCurrentSongInfo().getId()){
			holder.songName.setTextColor(context.getResources().getColor(R.color.base_dark));
		}else{
			holder.songName.setTextColor(context.getResources().getColor(R.color.light_text_color));
		}
	}

	@Override
	public int getItemCount() {
		return songArray.size();
	}

	public void addAll(ArrayList<SongInfo> horizontal_songInfo_array) {
		if(songArray == null){
			songArray = new ArrayList<>();
		}
		songArray.clear();
		songArray.addAll(horizontal_songInfo_array);
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		TextView songName;
		ImageView album;
		public ViewHolder(View v) {
			super(v);
			songName = (TextView)v.findViewById(R.id.song_name_textView);
			album = (ImageView)v.findViewById(R.id.song_imageView);
		}
	}
}
