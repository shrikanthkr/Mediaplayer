package com.mediaplayer.com;

import java.lang.annotation.Target;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SeekBar extends View {
	Paint paint = new Paint();
	Point point = new Point();
	int validate_flag = 0;
	RectF rect = new RectF();
	Shader gradient;
	Context context;
	float center_x, center_y, radius;
	float angle;
	float seek_x, seek_y;
	Bitmap seek_image;
	int seek_image_height,seek_image_width;
	RadialGradient linear_gradient;
	float seek_angle;
	boolean isSeeking = false;

	// int seek_measure_heigth,seek_measure_width;

	public float getRadius() {
		return radius;
	}

	public SeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		angle = -90;
		seek_image = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seek_image);
		setPaintAttributes();
		setWillNotDraw(false);
	}

	public void setPaintAttributes() {

		paint.setStrokeWidth(6);
		paint.setAntiAlias(true);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStyle(Paint.Style.STROKE);
		paint.setARGB(255, 50, 179, 230);
		paint.setAlpha(255);

	}

	public void setXY(float x, float y) {
		point.x = x;
		point.y = y;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		// canvas.drawCircle(point.x, point.y, 10, paint);
		// Example values
		setPaintAttributes();
		rect.set(center_x - radius, center_y - radius, center_x + radius,
				center_y + radius);
		canvas.drawArc(rect, -90, (float) (angle), false, paint);
		if (isSeeking) {

			paint.setAlpha(69);
			canvas.drawArc(rect, -90, (float) (seek_angle), false, paint);

		}
		canvas.drawBitmap(seek_image,seek_x + seek_image_width/2,seek_y+seek_image_height/2, paint);

		super.onDraw(canvas);
	}

	public boolean isSeeking() {
		return isSeeking;
	}

	public void setSeeking(boolean isSeeking) {
		this.isSeeking = isSeeking;
	}

	public float getSeek_x() {
		return seek_x;
	}

	public void setSeek_x(float seek_x) {
		this.seek_x = seek_x;
	}

	public float getSeek_y() {
		return seek_y;
	}

	public void setSeek_y(float seek_y) {
		this.seek_y = seek_y;
	}

	public class Point {
		float x, y;

		Point() {
			x = 0;
			y = 0;
		}
	}

	public void callfromTimerTask(int sec, int totalSeconds) {
/*		Log.i("SEC", sec + "");
		Log.i("total", totalSeconds + "");*/
		float ratiopersec = (float) ((2 * Math.PI * radius) / totalSeconds);
		float arc_length = sec * ratiopersec;
		float radian = arc_length / radius;
		angle = (float) Math.toDegrees(radian);
		radian = (float) Math.toRadians(angle - 90);
		if (!isSeeking) {
			seek_x = -seek_image_width + center_x + (float) Math.cos(radian) * radius;
			seek_y = -seek_image_height + center_y + (float) Math.sin(radian) * radius;
		}
		postInvalidate();//Called from thread
	}

	public void setRadius(float rad) {
		// TODO Auto-generated method stub
		radius = rad;
	}

	public float getCenter_x() {
		return center_x;
	}

	public void setCenter_x(float center_x) {
		this.center_x = center_x;
	}

	public float getCenter_y() {
		return center_y;
	}

	public void setCenter_y(float center_y) {
		this.center_y = center_y;
	}

	public void calculateTempSeek(float x1, float y1) {
		// TODO Auto-generated method stub
		float intan;
		float angle;
		intan = (center_y - y1) / (center_x - x1);
		angle = (float) Math.atan(intan);

		seek_x = (float) (radius * Math.cos(angle));
		seek_y = (float) (radius * Math.sin(angle));
		if (x1 > center_x) {
			seek_angle = (float) Math.toDegrees(angle) + 90;
			seek_x = center_x + seek_x - seek_image_width;
			seek_y = center_y + seek_y - seek_image_height;
		} else {
			seek_angle = 180 + (float) Math.toDegrees(angle) + 90;
			seek_x = center_x - seek_x - seek_image_width;
			seek_y = center_y - seek_y - seek_image_height;
		}
		setSeeking(true);
		postInvalidate();

	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getSeek_angle() {
		return seek_angle;
	}

	public void setSeek_angle(float seek_angle) {
		this.seek_angle = seek_angle;
	}

	public int getSeekedTime(int totalSeconds) {
		float ratiopersec = (float) ((2 * Math.PI * radius) / totalSeconds);
		float arc_length = (float) (((seek_angle) / 360) * 2 * Math.PI * radius);
		int sec = (int) (arc_length / ratiopersec);
		// //Log.i("seek angle in sec", sec+"");
		return sec;
	}

	public void setMeasuredHeigthWidth(int width,int height){
		seek_image_height=height;
		seek_image_width=width;
		seek_image = Bitmap.createScaledBitmap(seek_image, seek_image_width, seek_image_height, false);
	}

}
