package com.mediaplayer.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.devsmart.android.ui.HorizontalListView;
import com.mediaplayer.adapter.HorizontalAdapter.ViewHolder;
import com.mediaplayer.com.ListEditorDialog;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.listener.PlaylistChangedListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommonListAdapter extends BaseAdapter {

	private Activity activity;
	ArrayList<ArrayList<SongInfo>> song_all_array;
	private LayoutInflater inflater = null;

	Thread t;
	ListView lv;
	HorizontalAdapter horizontal_adapter;
	ViewHolder holder;
	final int PLAYLIST_VIEW = 2;
	int SWITCH_VIEW;
	PlaylistChangedListener playlistChangedListener;

	public CommonListAdapter(Activity activity,
			ArrayList<ArrayList<SongInfo>> song_array, ListView lv,
			int viewType, PlaylistChangedListener playlistChangedListener) {
		super();
		this.activity = activity;
		this.song_all_array = song_array;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.lv = lv;
		SWITCH_VIEW = viewType;
		this.playlistChangedListener = playlistChangedListener;
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
		HorizontalListView listview;
		TextView name;
		Button edit;
	}

	@Override
	public View getView(int position, View vi, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int pos = position;
		if (vi == null) {
			holder = new ViewHolder();
			vi = inflater.inflate(R.layout.horizontal_listitem_xml, null);
			holder.name = (TextView) vi
					.findViewById(R.id.playlist_name_textView);
			holder.edit = (Button) vi
					.findViewById(R.id.playlist_adapter_edit_button);
			holder.listview = (HorizontalListView) vi
					.findViewById(R.id.horizontal_listview);
			if (SWITCH_VIEW == PLAYLIST_VIEW) {
				holder.edit.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						final ListEditorDialog listeditor_dialog = new ListEditorDialog(
								activity, song_all_array.get(pos));
						listeditor_dialog.show();
						ImageButton reset_playaueue = (ImageButton) listeditor_dialog
								.findViewById(R.id.reset_playaueue);
						reset_playaueue
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										SongInfoDatabase database = new SongInfoDatabase(
												activity);
										database.open();
										//Log.i("PLAYLIST LIST ADAPTER","OPENED DB");
										ArrayList<SongInfo> temp_array = new ArrayList<SongInfo>();
										ArrayList<Integer> cursor_positions = listeditor_dialog
												.getAdapter()
												.getCursorPositions();
										database.deletePlaylist(song_all_array
												.get(pos).get(0).getPlaylist());
										
										for (int x = 0; x < cursor_positions
												.size(); x++) {
											
											database.addToPlaylist(
													song_all_array
															.get(pos)
															.get(cursor_positions
																	.get(x))
															.getId(),
													song_all_array
															.get(pos)
															.get(cursor_positions
																	.get(x))
															.getPlaylist());
										}
										listeditor_dialog.dismiss();
										if(playlistChangedListener!=null) playlistChangedListener.onPlaylistChanged();
									}
								});

					}
				});
			} else {
				holder.edit.setVisibility(View.INVISIBLE);
			}
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		holder.name.setText(song_all_array.get(position).get(0).getPlaylist());
		holder.name.setBackgroundResource(R.drawable.blue_strip);
		horizontal_adapter = new HorizontalAdapter(
				song_all_array.get(position), holder.listview, activity);
		holder.listview.setAdapter(horizontal_adapter);
		return vi;
	}
	
	

}
