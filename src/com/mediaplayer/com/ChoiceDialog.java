package com.mediaplayer.com;

import com.mediaplayer.com.IdentifyActivityThread.DialogChangeListener;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ChoiceDialog extends Dialog implements OnClickListener {

	DialogChangeListener listener;
	Context context;
	String msg;
	TextView choicedialog_textView;
	Button choicedialog_ok_button, choicedialog_cancel_button;

	public ChoiceDialog(Context context, DialogChangeListener listener,
			String msg) {
		super(context);
		// TODO Auto-generated constructor stub
		this.listener = listener;
		this.context = context;
		this.msg = msg;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		dismiss();
		switch (arg0.getId()) {
		case R.id.choice_ok_button:
			listener.receivedChange(true);
			break;
		case R.id.choice_cancel_button:
			listener.receivedChange(false);
			break;
		}
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();

		setContentView(R.layout.choicedialog_xml);

		choicedialog_textView = (TextView) findViewById(R.id.choicedialog_textView);
		choicedialog_ok_button = (Button) findViewById(R.id.choice_ok_button);
		choicedialog_cancel_button = (Button) findViewById(R.id.choice_cancel_button);
		choicedialog_textView.setText(msg);
		choicedialog_ok_button.setOnClickListener(this);
		choicedialog_cancel_button.setOnClickListener(this);

	}

}
