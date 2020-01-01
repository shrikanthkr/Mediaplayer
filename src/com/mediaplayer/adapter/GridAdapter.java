package com.mediaplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediaplayer.com.MetaInfo;
import com.mediaplayer.com.R;
import com.mediaplayer.ui.MultiviewFragment;
import com.mediaplayer.utility.AlbumArtLoader;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter{

	private Activity activity;
	ArrayList<MetaInfo> infos;
	private LayoutInflater inflater = null;
	GridView gv;
	ViewHolder holder;
	AlbumArtLoader.Mode current;
	MultiviewFragment fragment;

	public GridAdapter(Activity activity, ArrayList<MetaInfo> infos, GridView gv, AlbumArtLoader.Mode mode, MultiviewFragment fragment) {
		super();
		this.activity = activity;
		this.infos = infos;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.gv = gv;
		current = mode;
		this.fragment = fragment;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
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
		LinearLayout more_layout;
	}

	public void addAll(ArrayList<MetaInfo> infos){
		this.infos.clear();
		this.infos = infos;
		notifyDataSetChanged();
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
			holder.more_layout = (LinearLayout)vi.findViewById(R.id.more_layout);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		holder.more_layout.setOnClickListener(moreListener);
		holder.name.setText(infos.get(position).getName());
		new AlbumArtLoader(activity,infos.get(position).getId(),holder.album, current).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		return vi;
	}

	View.OnClickListener moreListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = gv.getPositionForView(v);
			fragment.showMore(v, position);
		}
	};
	

}
