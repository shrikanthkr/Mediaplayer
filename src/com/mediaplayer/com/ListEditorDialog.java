package com.mediaplayer.com;

import java.util.ArrayList;


import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.SimpleDragSortCursorAdapter;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ListEditorDialog extends Dialog {

	private SimpleDragSortCursorAdapter adapter;
	String[] artistNames;
	DragSortListView dslv;
	Context context;
	ArrayList<SongInfo> songs_array;
	MatrixCursor cursor;



	public SimpleDragSortCursorAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(SimpleDragSortCursorAdapter adapter) {
		this.adapter = adapter;
	}

	public MatrixCursor getCursor() {
		return cursor;
	}

	public void setCursor(MatrixCursor cursor) {
		this.cursor = cursor;
	}

	public ListEditorDialog(Context context, ArrayList<SongInfo> songs_array) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.songs_array=songs_array;
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.listeditor_dialog_xml);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();

		String[] cols = {"name"};
		int[] ids = {R.id.text};
		adapter = new MAdapter(context,
				R.layout.list_item_click_remove, null, cols, ids, 0);

		dslv = (DragSortListView) findViewById(android.R.id.list);
		dslv.setAdapter(adapter);

		// build a cursor from the String array
		cursor = new MatrixCursor(new String[] {"_id", "name"});
		for (int i = 0; i < songs_array.size(); i++) {
			cursor.newRow()
			.add(i)
			.add(songs_array.get(i).getTitle());
		}
		adapter.changeCursor(cursor);
	}
	private class MAdapter extends SimpleDragSortCursorAdapter {
		private Context mContext;

		public MAdapter(Context ctxt, int rmid, Cursor c, String[] cols, int[] ids, int something) {
			super(ctxt, rmid, c, cols, ids, something);
			mContext = ctxt;
		}



	}
}
