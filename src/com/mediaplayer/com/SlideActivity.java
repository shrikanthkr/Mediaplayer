package com.mediaplayer.com;

import com.korovyansk.android.slideout.SlideoutActivity;
import com.mediaplayer.receiver.SDCardMountReceiver;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SlideActivity extends Activity implements OnClickListener {
	Button slide_button;
	SharedPreferences app_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slideactivity_xml);
		slide_button = (Button) findViewById(R.id.slide_button);
		slide_button.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int width = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
						.getDisplayMetrics());
		SlideoutActivity.prepare(SlideActivity.this, R.id.slide_inner_content, width);
		startActivity(new Intent(SlideActivity.this, SplashActivity.class));
		overridePendingTransition(0, 0);
	}
}
