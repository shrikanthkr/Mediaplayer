package com.mediaplayer.com;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.korovyansk.android.slideout.SlideoutHelper;
public class SplashActivity extends FragmentActivity{
	private SlideoutHelper mSlideoutHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSlideoutHelper = new SlideoutHelper(this);
		mSlideoutHelper.activate();
		getSupportFragmentManager().beginTransaction().add(R.id.slideout_placeholder, new MenuFragment(), "menu").commit();
		mSlideoutHelper.open();
	}

	public SlideoutHelper getSlideoutHelper(){
		return mSlideoutHelper;
	}
}
