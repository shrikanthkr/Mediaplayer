package com.mediaplayer.com;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mediaplayer.adapter.MySpinnerAdapter;
import com.mediaplayer.utility.StaticMusic;

public class AudioFxDemo extends Activity implements OnGlobalLayoutListener{
	private static final String TAG = "AudioFxDemo";

	private static final float VISUALIZER_HEIGHT_DIP = 50f;

	private Visualizer mVisualizer;
	public static Equalizer mEqualizer;

	private LinearLayout mLinearLayout;
	private LinearLayout mLinearLayout_equalizer;
	private VisualizerView mVisualizerView;
	private TextView mStatusTextView;
	Spinner eqpreset_spinner;
	short numPreset;
	String[] presets;
	MySpinnerAdapter adapter;
	Drawable thumb;
	int thumb_height, thumb_width;
	//public static short band_equi=0;
	Display display; 
	int width;
	ViewTreeObserver vto;
	ImageView eqmeasure_imageView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.equalizer_xml);
		mStatusTextView = new TextView(this);

		mLinearLayout = (LinearLayout) findViewById(R.id.equalizer_linearlayout);
		mLinearLayout_equalizer = (LinearLayout) findViewById(R.id.equalizer_seekbar_linearlayout);
		eqpreset_spinner = (Spinner) findViewById(R.id.eqpreset_spinner);
		eqmeasure_imageView=(ImageView)findViewById(R.id.eqmeasure_imageView);

		mLinearLayout.addView(mStatusTextView);
		thumb = getResources().getDrawable(R.drawable.equ);
		display = getWindowManager().getDefaultDisplay(); 
		mLinearLayout_equalizer.getViewTreeObserver().addOnGlobalLayoutListener(this);
		vto = mLinearLayout_equalizer.getViewTreeObserver();
		// Create the MediaPlayer
		Log.d(TAG, "MediaPlayer audio session ID: "
				+ StaticMusic.music.mediaPlayer.getAudioSessionId());
		getEqualizer();
		
		mEqualizer.usePreset((short) StaticMusic.band_equi);
		
		mEqualizer.setEnabled(true);
		setupVisualizerFxAndUI();
		
		numPreset = mEqualizer.getNumberOfPresets();
		
		presets = new String[numPreset + 1];
		for (int x = 0; x < numPreset; x++) {
			presets[x] = mEqualizer.getPresetName((short) x);
			Log.i("Preset Name", presets[x]);
		}
		presets[numPreset] = "Custom";
		adapter = new MySpinnerAdapter(this, R.layout.spinner_listitem, presets);
		eqpreset_spinner
		.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (arg2 != numPreset) {
					StaticMusic.band_equi=(short)arg2;
					mEqualizer.usePreset((short) arg2);
					
					mLinearLayout_equalizer.removeAllViews();
					setupEqualizerFxAndUI();
				} else {

					eqpreset_spinner.setSelection(numPreset);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		eqpreset_spinner.setAdapter(adapter);
		try {
			eqpreset_spinner.setSelection(mEqualizer.getCurrentPreset());
		} catch (Exception e) {
			eqpreset_spinner.setSelection(numPreset);
		}
		// Make sure the visualizer is enabled only when you actually want to
		// receive data, and
		// when it makes sense to receive data.
		mVisualizer.setEnabled(true);

		// When the stream ends, we don't need to collect any more data. We
		// don't do this in
		// setupVisualizerFxAndUI because we likely want to have more,
		// non-Visualizer related code
		// in this callback.

		mStatusTextView.setText("Playing audio...");
	}

	public static void getEqualizer() {
		// TODO Auto-generated method stub
		try {
			mEqualizer.hasControl();
			mEqualizer.release();
			mEqualizer = new Equalizer(Thread.MAX_PRIORITY,
					StaticMusic.music.mediaPlayer.getAudioSessionId());
		} catch (Exception e) {
			mEqualizer = new Equalizer(Thread.MAX_PRIORITY,
					StaticMusic.music.mediaPlayer.getAudioSessionId());
		}
		
	}

	private void setupEqualizerFxAndUI() {
		// Create the Equalizer object (an AudioEffect subclass) and attach it
		// to our media player,
		// with a default priority (0).

		TextView eqTextView = new TextView(this);
		eqTextView.setText("Equalizer:");

		mLinearLayout_equalizer.addView(eqTextView);

		short bands = mEqualizer.getNumberOfBands();
		final short minEQLevel = mEqualizer.getBandLevelRange()[0];
		final short maxEQLevel = mEqualizer.getBandLevelRange()[1];
		short center;
		for (short i = 0; i < bands; i++) {
			final short band = i;

			TextView freqTextView = new TextView(this);
			freqTextView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			freqTextView.setGravity(Gravity.CENTER_HORIZONTAL);
			freqTextView.setText((mEqualizer.getCenterFreq(band) / 1000)
					+ " Hz");
			mLinearLayout_equalizer.addView(freqTextView);

			LinearLayout row = new LinearLayout(this);
			row.setOrientation(LinearLayout.HORIZONTAL);

			TextView minDbTextView = new TextView(this);
			minDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			minDbTextView.setText((minEQLevel / 100) + " dB");

			TextView maxDbTextView = new TextView(this);
			maxDbTextView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			maxDbTextView.setText((maxEQLevel / 100) + " dB");

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);

			layoutParams.weight = 1;
			SeekBar bar = new SeekBar(this);
			bar.setPadding(20, 20, 20, 20);
			bar.setLayoutParams(layoutParams);
			bar.setMax(maxEQLevel - minEQLevel);
			center = mEqualizer.getBandLevel(band);
			bar.setProgress(mEqualizer.getBandLevel(band) + center);
			thumb_width = thumb_height = bar.getHeight();
			Log.i(TAG, "BEFORE CREATING"+thumb_width);
			Bitmap bmpOrg = ((BitmapDrawable) thumb).getBitmap();
			Log.i(TAG, "AFTER CREATING");
			Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, width,
					width, true);
			Drawable newThumb = new BitmapDrawable(getResources(), bmpScaled);
			newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(),
					newThumb.getIntrinsicHeight());
			bar.setThumb(newThumb);
			bar.setPadding(newThumb.getIntrinsicHeight(), 0, newThumb.getIntrinsicHeight(), 0);
			bar.setProgressDrawable(getResources().getDrawable(R.drawable.progress));
			//bar.setThumbOffset(newThumb.getIntrinsicWidth());
			bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					mEqualizer.setBandLevel(band,
							(short) (progress + minEQLevel));
					eqpreset_spinner.setSelection(numPreset);
				}

				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				public void onStopTrackingTouch(SeekBar seekBar) {
				}
			});

			row.addView(minDbTextView);
			row.addView(bar);
			row.addView(maxDbTextView);

			mLinearLayout_equalizer.addView(row);
		}
	}

	private void setupVisualizerFxAndUI() {
		// Create a VisualizerView (defined below), which will render the
		// simplified audio
		// wave form to a Canvas.
		mVisualizerView = new VisualizerView(this);
		mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				(int) (VISUALIZER_HEIGHT_DIP * getResources()
						.getDisplayMetrics().density)));
		mLinearLayout.addView(mVisualizerView);

		// Create the Visualizer object and attach it to our media player.
		mVisualizer = new Visualizer(
				StaticMusic.music.mediaPlayer.getAudioSessionId());
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		mVisualizer.setDataCaptureListener(
				new Visualizer.OnDataCaptureListener() {
					public void onWaveFormDataCapture(Visualizer visualizer,
							byte[] bytes, int samplingRate) {
						mVisualizerView.updateVisualizer(bytes);
					}

					public void onFftDataCapture(Visualizer visualizer,
							byte[] bytes, int samplingRate) {
					}
				}, Visualizer.getMaxCaptureRate(), true, false);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (isFinishing() && StaticMusic.music.mediaPlayer != null) {
			mVisualizer.setEnabled(false);
			mVisualizer.release();
			//mEqualizer.release();
		}
		finish();
	}

	@Override
	public void onGlobalLayout() {
		// TODO Auto-generated method stub
		Log.i("FX GLOBAL LAYOUT", "GLOBAL LAYOUT" );
		width=eqmeasure_imageView.getWidth();
		setupEqualizerFxAndUI();
		if (vto.isAlive()) {
			vto.removeGlobalOnLayoutListener(this);
			// Log.i("Layout listener", "Removed");
		} else {
			vto = mLinearLayout_equalizer.getViewTreeObserver();
			vto.removeGlobalOnLayoutListener(this);
			// Log.i("Layout listener", "Removed");
		}
	}

}

/**
 * A simple class that draws waveform data received from a
 * {@link Visualizer.OnDataCaptureListener#onWaveFormDataCapture }
 */
class VisualizerView extends View {
	private byte[] mBytes;
	private float[] mPoints;
	private Rect mRect = new Rect();

	private Paint mForePaint = new Paint();

	public VisualizerView(Context context) {
		super(context);
		init();
	}

	private void init() {
		mBytes = null;

		mForePaint.setStrokeWidth(1f);
		mForePaint.setAntiAlias(true);
		mForePaint.setColor(Color.rgb(0, 128, 255));
	}

	public void updateVisualizer(byte[] bytes) {
		mBytes = bytes;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mBytes == null) {
			return;
		}

		if (mPoints == null || mPoints.length < mBytes.length * 4) {
			mPoints = new float[mBytes.length * 4];
		}

		mRect.set(0, 0, getWidth(), getHeight());

		for (int i = 0; i < mBytes.length - 1; i++) {
			mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
			mPoints[i * 4 + 1] = mRect.height() / 2
					+ ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
			mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
			mPoints[i * 4 + 3] = mRect.height() / 2
					+ ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2)
					/ 128;
		}

		canvas.drawLines(mPoints, mForePaint);
	}


}