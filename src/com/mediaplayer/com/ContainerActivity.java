package com.mediaplayer.com;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ContainerActivity extends Activity {
	private String[] labels = {"Now Playing","Playlist","Songs","Albums"};
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	String contentTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_container);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_frame);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, labels));
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_launcher,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.drawer_close  /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle("TItle");
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
		Activity activity;

		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			selectItem(position);
		}

		/** Swaps fragments in the main content view */
		private void selectItem(int position) {
			// Create a new fragment and specify the planet to show based on position
			Fragment fragment = new SongListFragment();
			Bundle args = new Bundle();
			// args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
			fragment.setArguments(args);

			// Insert the fragment by replacing any existing fragment
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment)
					.commit();

			// Highlight the selected item, update the title, and close the drawer
			mDrawerList.setItemChecked(position, true);
			setTitle(labels[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}


		public void setTitle(CharSequence title) {
			contentTitle = title.toString();
			getActionBar().setTitle(contentTitle);
		}
	}
}
