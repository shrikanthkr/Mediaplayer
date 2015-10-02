package com.mediaplayer.fragments;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mediaplayer.adapter.CommonListAdapter;
import com.mediaplayer.adapter.PlayListDialogAdapter;
import com.mediaplayer.adapter.ReadLisstAdapter;
import com.mediaplayer.adapter.ReadLisstAdapter.ViewHolder;
import com.mediaplayer.com.Music;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.listener.OnDatabaseChangeListener;
import com.mediaplayer.listener.PlaylistChangedListener;
import com.mediaplayer.utility.DatabaseUpdateThread;
import com.mediaplayer.utility.StaticMusic;
import com.mediaplayer.utility.Util;

public class SongListFragment extends Fragment implements
OnGestureListener, OnDatabaseChangeListener, PlaylistChangedListener {
	ListView lv;
	ReadLisstAdapter adapter;
	ArrayList<SongInfo> songList;
	String path;
	GestureDetector detector;
	HashMap<String, Bitmap> art_work;
	Context context;
	SharedPreferences app_start;
	SharedPreferences.Editor app_start_editor;
	boolean checkFirst;
	// SongInfo songInfo;
	long duration;
	SongInfoDatabase database;
	Activity activity;
	// ContentBody cbFile;
	Util util;
	//ProgressBar pb;
	ArrayList<ArrayList<SongInfo>> all_playlists;
	float downX = 0;
	float upX = 0;
	CommonListAdapter common_list_adapter;
	//EditText search_edittext;
	DatabaseThread search_database_thread;
	final int SONG_VIEW = 0;
	final int ARTIST_VIEW = 1;
	final int PLAYLIST_VIEW = 2;
	final int ALBUMS_VIEW = 3;
	static int SWITCH_VIEW = 0;
	Thread x;
	// UriObserver observer;
	//TextView songlist_header_textview;
	ImageView swipe_left, swipe_right, point_tut_imageview;
	TextView swipe_right_textview, swipe_left_textview, point_textview;
	DatabaseUpdateThread databaseUpdateThread;
	Cursor cursor;
	long previousTime = 0;
	PlaylistChangedListener playlistChangedListener;
	Button tut_button;
	AlphaAnimation alphaDown;
	AlphaAnimation alphaUp;
	boolean slidebutton_clicked=false;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		slidebutton_clicked=false;

	}

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		//setContentView(R.layout.songlistfragment_xml);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.songlistfragment_xml,container,false);
		context = getActivity();
		activity = getActivity();
		alphaDown = new AlphaAnimation(1.0f, 0.3f);
		alphaUp = new AlphaAnimation(0.3f, 1.0f);
		alphaDown.setDuration(1000);
		alphaUp.setDuration(1000);
		alphaDown.setFillAfter(true);
		alphaUp.setFillAfter(true);
		slidebutton_clicked=false;
		lv = (ListView) v.findViewById(R.id.listView);
		//slide_songlist_button = (Button) v.findViewById(R.id.slide_songlist_button);
		//songlist_header_textview = (TextView) v.findViewById(R.id.songslist_header_textview);
		//pb = (ProgressBar) v.findViewById(R.id.songload_progressBar);
		app_start = activity.getSharedPreferences("checkFirst", Activity.MODE_PRIVATE);
		checkFirst = app_start.getBoolean("checkFirst", true);
		database = new SongInfoDatabase(context);
		util = new Util();
		songList = new ArrayList<SongInfo>();
		//song_search_button = (ImageButton) v.findViewById(R.id.song_search_button);
		/*song_search_button.setOnClickListener(this);*/
/*		search_edittext = (EditText) v.findViewById(R.id.search_edittext);
		search_edittext.setVisibility(View.INVISIBLE);*/
		search_database_thread = new DatabaseThread();
		playlistChangedListener = this;
		swipe_left = (ImageView) v.findViewById(R.id.swipe_left_imageView);
		swipe_right = (ImageView) v.findViewById(R.id.swipe_right_imageView);
		swipe_right_textview = (TextView) v.findViewById(R.id.swipe_right_textView);
		swipe_left_textview = (TextView) v.findViewById(R.id.swipe_left_textView);
		point_tut_imageview = (ImageView) v.findViewById(R.id.point_tut_imageView);
		tut_button = (Button) v.findViewById(R.id.tut_button);
		point_textview = (TextView) v.findViewById(R.id.tut_button_textView);

		tut_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				disableTut();
			}
		});
		/*search_edittext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				search_database_thread.setSearch(s.toString());
				if (search_database_thread.isAlive()) {
					search_database_thread.interrupt();
				}
				try {
					search_database_thread.run();
				} catch (Exception r) {
					r.printStackTrace();
					search_database_thread.start();
				}

			}
		});*/
		lv.setVisibility(View.INVISIBLE);
		makeAllAlphaDown();
		if (checkFirst) {

		} else {
			disableTut();
			populateSonglist();
		}
		return v;
	}

	public void populateSonglist() {
		SWITCH_VIEW = SONG_VIEW;
		//pb.setVisibility(View.VISIBLE);
		//Log.i("LIST FRAG", "POPULATING SONG LIST");
		//songlist_header_textview.setText("Songs");
		database.open();
		songList = database.getFullList();
		// database.close();

		detector = new GestureDetector(getActivity(), this);

		adapter = new ReadLisstAdapter(getActivity(), songList, lv);
		lv.setTextFilterEnabled(true);

		lv.setAdapter(adapter);
		lv.setSelection(StaticMusic.smoothScrollTo);
		lv.setFastScrollEnabled(true);
		lv.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent e) {
				detector.onTouchEvent(e);
				return false;
			}
		});
		//adapter.imageLoader.loadImage(StaticMusic.smoothScrollTo,StaticMusic.smoothScrollTo + 8);
		//pb.setVisibility(View.INVISIBLE);
		//Log.i("INSTANT", "registered content observer");
	}

	/*public void populatePlaylist() {
		SWITCH_VIEW = PLAYLIST_VIEW;
		songlist_header_textview.setText("Playlists");
		x = new Thread() {
			public void run() {
				database.open();
				all_playlists = new ArrayList<ArrayList<SongInfo>>();
				all_playlists = database.getSongs_allPlayList();
				common_list_adapter = new CommonListAdapter(activity,
						all_playlists, lv, SWITCH_VIEW, playlistChangedListener);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						lv.setAdapter(common_list_adapter);
						pb.setVisibility(View.INVISIBLE);
						try {
							finalize();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				common_list_adapter.notifyDataSetChanged();
			}
		};
		x.start();
		try {
			x.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pb.setVisibility(View.VISIBLE);

	}

	public void populateAlbums() {
		songlist_header_textview.setText("Albums");
		SWITCH_VIEW = ALBUMS_VIEW;
		x = new Thread() {
			public void run() {
				database.open();
				all_playlists = new ArrayList<ArrayList<SongInfo>>();
				all_playlists = database.getSongs_albums();
				// database.close();
				common_list_adapter = new CommonListAdapter(activity,
						all_playlists, lv, SWITCH_VIEW, playlistChangedListener);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						lv.setAdapter(common_list_adapter);
						pb.setVisibility(View.INVISIBLE);
						try {
							finalize();
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				common_list_adapter.notifyDataSetChanged();
			}
		};
		x.start();
		try {
			x.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pb.setVisibility(View.VISIBLE);

	}

	public void populateArtists() {
		songlist_header_textview.setText("Artists");
		SWITCH_VIEW = ARTIST_VIEW;
		x = new Thread() {
			public void run() {
				database.open();
				all_playlists = new ArrayList<ArrayList<SongInfo>>();
				all_playlists = database.getSongs_artists();
				// database.close();
				common_list_adapter = new CommonListAdapter(activity,
						all_playlists, lv, SWITCH_VIEW, playlistChangedListener);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						lv.setAdapter(common_list_adapter);
						pb.setVisibility(View.INVISIBLE);
					}
				});

				common_list_adapter.notifyDataSetChanged();
			}
		};
		x.start();
		try {
			x.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pb.setVisibility(View.VISIBLE);
	}

	private void updateSongList() {
		// TODO Auto-generated method stub
		Thread t = new Thread() {
			public void run() {
				database.open();
				songList = database.getFullList();
				// database.close();
				adapter.setUrlList(songList);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter.notifyDataSetChanged();
					}
				});

				try {
					finalize();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
	}*/

	/*@Override
	public void onClick(View arg0) {
		// //Log.i("As Activity", "Songlist fragment acivity click happened");
		try {
			restoreSearchButton();

		} catch (Exception e) {
		}

		switch (arg0.getId()) {

		case R.id.playlists_imageButton:
			clearCache();
			populatePlaylist();
			break;
		case R.id.albums_imageButton:
			clearCache();
			populateAlbums();
			break;
		case R.id.artists_imageButton:
			clearCache();
			populateArtists();
			break;
		case R.id.songs_imageButton:
			clearCache();
			populateSonglist();
			break;
		case R.id.song_search_button:

			song_search_button.setVisibility(View.INVISIBLE);
			search_edittext.setVisibility(View.VISIBLE);
			search_edittext.requestFocus();
			search_edittext.dispatchTouchEvent(MotionEvent.obtain(
					SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
					MotionEvent.ACTION_DOWN, 0, 0, 0));
			search_edittext.dispatchTouchEvent(MotionEvent.obtain(
					SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
					MotionEvent.ACTION_UP, 0, 0, 0));
			break;
		}

	}*/

	private void clearCache() {
		// TODO Auto-generated method stub
		try {
			common_list_adapter.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void removeGestureListener() {
		// TODO Auto-generated method stub
		lv.setOnTouchListener(null);

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StaticMusic.smoothScrollTo = lv.getFirstVisiblePosition();
		try {
			database.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		if (SWITCH_VIEW != SONG_VIEW) {
			return false;
		}
		try {
			final int id = lv.pointToPosition((int) e1.getX(), (int) e1.getY());
			final String songId = songList.get(id).getId();
			// TODO Auto-generated method stub
			if ((e1.getX() - e2.getX()) > 100 && arg2 < -30
					&& Math.abs(e1.getY() - e2.getY()) < 150) {

				final Dialog dialog = new Dialog(context,
						android.R.style.Theme_Panel);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.playlist_dialog_xml);
				dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
				Button create_playlist = (Button) dialog
						.findViewById(R.id.create_newplaylist_button);
				Button cancel_playlist = (Button) dialog
						.findViewById(R.id.cancel_playlist_button);
				final EditText playlist_edittext = (EditText) dialog
						.findViewById(R.id.newplaylist_editText);
				ListView dialog_playlist_listview = (ListView) dialog
						.findViewById(R.id.dialog_playlists_listView);
				dialog.show();
				database.open();
				final ArrayList<String> dialog_playlists;
				dialog_playlists = database.getAllPlaylists();
				PlayListDialogAdapter playlist_dialog_adapter = new PlayListDialogAdapter(
						activity, dialog_playlists, dialog_playlist_listview);
				dialog_playlist_listview.setAdapter(playlist_dialog_adapter);
				dialog_playlist_listview
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						database.open();
						database.addToPlaylist(songId,
								dialog_playlists.get(arg2));
						dialog.dismiss();
						// //Log.i("Song ID", songId+"; Playlist :" +
						// dialog_playlists.get(arg2));
					}

				});
				create_playlist.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						database.open();
						if (playlist_edittext.getText().toString().length() > 0) {
							database.addToPlaylist(songId, playlist_edittext
									.getText().toString());
							dialog.dismiss();
							// //Log.i("Song ID", songId);
						} else
							Toast.makeText(context, "Empty Name",
									Toast.LENGTH_LONG).show();

					}
				});
				cancel_playlist.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				// database.close();
			} else if ((e2.getX() - e1.getX() > 100) && arg2 > 30
					&& Math.abs(e1.getY() - e2.getY()) < 150) {
				try {
					StaticMusic.songQueue.addLast(songList.get(id));
				} catch (NullPointerException e) {
					StaticMusic.songQueue = new LinkedList<SongInfo>();
					StaticMusic.songQueue.addLast(songList.get(id));
					try {
						FileInputStream fis = new FileInputStream(new File(
								songList.get(id).getData()));
						FileDescriptor fileDescriptor = fis.getFD();
						Music music = new Music(fileDescriptor);
						fis.close();
						StaticMusic.setMusic(music);
						StaticMusic.songInfo = songList.get(id);
						music.play();
					} catch (Exception er) {
						er.printStackTrace();
					}

				}
				Toast.makeText(context,
						"Enqueued As : " + StaticMusic.songQueue.size(),
						Toast.LENGTH_SHORT).show();

			}

			return true;
		} catch (Exception e) {
			return false;

		}
	}

	@Override
	public void onLongPress(MotionEvent e1) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		restoreSearchButton();
		return true;
	}

	private void restoreSearchButton() {
		// TODO Auto-generated method stub
		/*try {
			song_search_button.setVisibility(View.VISIBLE);
			search_edittext.setVisibility(View.INVISIBLE);
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(search_edittext.getWindowToken(), 0);
		} catch (Exception e) {
		}*/

	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e1) {
		// TODO Auto-generated method stub

		return false;
	}

	public class DatabaseThread extends Thread {

		String search;
		ViewHolder holder;
		SongInfo songInfo;

		public String getSearch() {
			return search;
		}

		public void setHolder(ViewHolder holder) {
			// TODO Auto-generated method stub
			this.holder = holder;
		}

		public void setSearch(String search) {
			this.search = search;
		}

		public DatabaseThread() {
			songInfo = new SongInfo();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			//Log.i("DATABASE THREAD", search);

			switch (SWITCH_VIEW) {
			case SONG_VIEW:
				database.open();
				songList = database.searchSong_byName(search);
				// database.close();
				//Log.i("DATABASE THREAD", songList.size() + "");
				// TODO Auto-generated method stub
				if (songList.size() == 0) {
					songInfo.setTitle("No Songs Found");
					songList.add(songInfo);
					removeGestureListener();

				} else {
					addGestureListener();
				}
				adapter = new ReadLisstAdapter(activity, songList, lv);
				lv.setAdapter(adapter);
				break;
			case ALBUMS_VIEW:
				database.open();
				all_playlists = new ArrayList<ArrayList<SongInfo>>();
				all_playlists = database.searchSongs_albums(search);
				// database.close();
				if (all_playlists.size() == 0) {
					songInfo.setTitle("No Songs Found");
					songList.clear();
					songList.add(songInfo);
					all_playlists.add(songList);
					removeGestureListener();
				} else {
					addGestureListener();
				}
				common_list_adapter = new CommonListAdapter(activity,
						all_playlists, lv, SWITCH_VIEW, playlistChangedListener);
				lv.setAdapter(common_list_adapter);
				common_list_adapter.notifyDataSetChanged();

				break;
			case ARTIST_VIEW:
				database.open();
				all_playlists = new ArrayList<ArrayList<SongInfo>>();
				all_playlists = database.searchSongs_artists(search);
				// database.close();
				if (all_playlists.size() == 0) {
					songInfo.setTitle("No Songs Found");
					songList.clear();
					songList.add(songInfo);
					all_playlists.add(songList);
					removeGestureListener();
				} else {
					addGestureListener();
				}
				common_list_adapter = new CommonListAdapter(activity,
						all_playlists, lv, SWITCH_VIEW, playlistChangedListener);
				lv.setAdapter(common_list_adapter);
				common_list_adapter.notifyDataSetChanged();

				break;
			case PLAYLIST_VIEW:
				database.open();
				all_playlists = new ArrayList<ArrayList<SongInfo>>();
				all_playlists = database.searchSongs_playlists(search);
				// database.close();
				if (all_playlists.size() == 0) {
					songInfo.setTitle("No Songs Found");
					songList.clear();
					songList.add(songInfo);
					all_playlists.add(songList);
					removeGestureListener();
				} else {
					addGestureListener();
				}
				common_list_adapter = new CommonListAdapter(activity,
						all_playlists, lv, SWITCH_VIEW, playlistChangedListener);
				lv.setAdapter(common_list_adapter);
				common_list_adapter.notifyDataSetChanged();
				break;
			}

		}

		private void addGestureListener() {
			// TODO Auto-generated method stub
			lv.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View view, MotionEvent e) {
					detector.onTouchEvent(e);
					return false;
				}
			});

		}

	}

	@Override
	public void onChange() {


	}

	@Override
	public void onPlaylistChanged() {
		// TODO Auto-generated method stub

	}

	private void disableTut() {
		// TODO Auto-generated method stub
		swipe_left.setVisibility(View.GONE);
		swipe_left_textview.setVisibility(View.GONE);
		swipe_right.setVisibility(View.GONE);
		swipe_right_textview.setVisibility(View.GONE);
		tut_button.setVisibility(View.GONE);
		point_tut_imageview.setVisibility(View.GONE);
		point_textview.setVisibility(View.GONE);

		lv.startAnimation(alphaUp);
		//song_search_button.startAnimation(alphaUp);

	}

	private void makeAllAlphaDown() {
		// TODO Auto-generated method stub
		lv.startAnimation(alphaDown);
//		song_search_button.startAnimation(alphaDown);


	}

}
