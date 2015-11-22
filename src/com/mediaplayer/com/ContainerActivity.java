package com.mediaplayer.com;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mediaplayer.fragments.AlbumsFragment;
import com.mediaplayer.fragments.ArtistsFragment;
import com.mediaplayer.fragments.NowPlayingFragment;
import com.mediaplayer.fragments.PlaylistsFragment;
import com.mediaplayer.fragments.SongListFragment;
import com.mediaplayer.manager.BroadcastManager;


public class ContainerActivity extends Activity {
	private String[] labels = {"Now Playing","Playlist","Songs","Albums", "Artists"};
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	int previousFragmentState = -1, currentFragmentState = -1;
	NowPlayingFragment nowPlayingFragment;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BroadcastManager.setApplicationContext(this);
		startService(new Intent(MyApplication.getContext(), NotificationService.class));
		setContentView(R.layout.activity_container);
		hackActionBar();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_frame);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, labels));
		mDrawerList.addHeaderView(this.getLayoutInflater().inflate(R.layout.sidebar_header, null));
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				android.R.drawable.ic_menu_gallery,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.drawer_close  /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				if(previousFragmentState!=currentFragmentState)
					loadFragment(currentFragmentState);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		loadNowPLayingFragment();
	}

	private void hackActionBar(){

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.decor, null); // "null" is important.

		// HACK: "steal" the first child of decor view
		ViewGroup decor = (ViewGroup) getWindow().getDecorView();
		View child = decor.getChildAt(0);
		decor.removeView(child);
		LinearLayout container = (LinearLayout) drawer.findViewById(R.id.container); // This is the container we defined just now.
		LinearLayout dummy = (LinearLayout)container.findViewById(R.id.dummy);
		dummy.addView(child);
		container.bringChildToFront(child);

		// Make the drawer replace the first child
		decor.addView(drawer);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			selectItem(position);
		}
	}
	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on position
		mDrawerLayout.closeDrawer(mDrawerList);
		// Highlight the selected item, update the title, and close the drawer
		position = position - mDrawerList.getHeaderViewsCount();
		currentFragmentState = position;
		mDrawerList.setItemChecked(position, true);

	}



	public void loadFragment(int state){
		if(state == 0){
			nowPlayingFragment.slideUpPlayer();
			return;
		}
		previousFragmentState = currentFragmentState;
		Fragment fragment;
		fragment = getFragmentManager().findFragmentByTag(""+currentFragmentState);
		if(fragment==null) {
			switch (state) {
				case 0:
					fragment = new SongListFragment();
					break;
				case 1:
					fragment = new PlaylistsFragment();
					break;
				case 2:
					fragment = new SongListFragment();
					break;
				case 3:
					fragment = new AlbumsFragment();
					break;
				case 4:
					fragment = new ArtistsFragment();
					break;
				default:
					fragment = new SongListFragment();
					previousFragmentState = currentFragmentState = 2;
					break;
			}
			Bundle args = new Bundle();
			fragment.setArguments(args);
		}
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment,""+currentFragmentState)
				.addToBackStack(null)
				.commit();
	}
	@Override
	protected void onPause() {
		super.onPause();
		MyApplication.activityPaused();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyApplication.activityResumed();
		if(previousFragmentState==-1){
			previousFragmentState = 0;
		}
		if(previousFragmentState!=currentFragmentState)
			loadFragment(currentFragmentState);
	}
	private void loadNowPLayingFragment(){
		FragmentManager fragmentManager = getFragmentManager();
		nowPlayingFragment = new NowPlayingFragment();
		fragmentManager.beginTransaction()
				.replace(R.id.player, nowPlayingFragment)
				.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);

		return true;
	}

	@Override
	public void onBackPressed() {
		FragmentManager manager = getFragmentManager();
		int count = manager.getBackStackEntryCount();
		if(nowPlayingFragment.getIsUp() ){
			nowPlayingFragment.slideDownPlayer();
		}else if( count >1){
			manager.popBackStack();
		}
		else if( count == 1){
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(SongsManager.getInstance().getCurrentSongInfo()==null){
			sendBroadcast(new Intent(BroadcastManager.NOTIFICATION_CLOSE));
		}
	}
}
