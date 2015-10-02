package com.mediaplayer.com;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class InfoDialog extends Dialog implements android.view.View.OnClickListener{

	Context context;
	String showMessage;
	Button info_ok_button;
	TextView info_textview;

	public InfoDialog(Context context, String msg) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		showMessage = msg;
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		
		setContentView(R.layout.infodialog_xml);
		info_ok_button=(Button)findViewById(R.id.info_ok_button);
		info_textview=(TextView)findViewById(R.id.info_textView);
		info_ok_button.setOnClickListener(this);
		info_textview.setText(showMessage);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dismiss();
	}

}
