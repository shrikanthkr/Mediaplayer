package com.mediaplayer.com;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
	String contentTitle;
	int previousFragmentState = -1, currentFragmentState = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BroadcastManager.setApplicationContext(this);
		setContentView(R.layout.activity_container);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_frame);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, labels));
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.albums,  /* nav drawer icon to replace 'Up' caret */
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
				getActionBar().setTitle("Drawer Title");
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		loadNowPLayingFragment();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}
	private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			selectItem(position);
		}
		public void setTitle(CharSequence title) {
			contentTitle = title.toString();
			getActionBar().setTitle(contentTitle);
		}
	}
	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on position
		currentFragmentState = position;
		mDrawerLayout.closeDrawer(mDrawerList);
		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(labels[position]);
	}



	public void loadFragment(int state){
		previousFragmentState = currentFragmentState;
		Fragment fragment;
		switch (state){
			case 0:
				fragment =  new SongListFragment();
				break;
			case 1:
				fragment =  new PlaylistsFragment();
				break;
			case 2:
				fragment =  new SongListFragment();
				break;
			case 3:
				fragment =  new AlbumsFragment();
				break;
			case 4:
				fragment =  new ArtistsFragment();
				break;
			default:
				fragment =  new SongListFragment();
				previousFragmentState = currentFragmentState = 2;
				break;
		}
		setTitle(labels[currentFragmentState]);
		Bundle args = new Bundle();
		// args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);
		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment)
				.commit();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(previousFragmentState==-1){
			previousFragmentState = 0;
		}
		if(previousFragmentState!=currentFragmentState)
			loadFragment(currentFragmentState);

	}
	private void loadNowPLayingFragment(){
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.player, new NowPlayingFragment())
				.commit();
	}
}
