package com.mediaplayer.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.mediaplayer.com.R;


/**
 * Created by shrikanth on 10/6/15.
 */
public class PlayPauseView extends View {
    Paint paint = new Paint();
    int  centerX, centerY;
    float angle;
    Line one,two;
    int adjustment = 10;

    public enum ROTATESTATE {
        PLAYING, PAUSED, UNKNOWN
    };
    ROTATESTATE  currentState = ROTATESTATE.UNKNOWN;
    public PlayPauseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaintAtttrubutes();
        initPositions(0,0);
    }

    private void setPaintAtttrubutes(){
        paint.setColor(getResources().getColor(R.color.base));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerX = w/2;
        centerY = h/2;
        initPositions(w,h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void initPositions(int width, int height){
        one =new Line();
        one.iniX = one.x1 = 0 + adjustment;
        one.iniY = one.y1 = 0 + adjustment;
        one.x2 = 0 + adjustment;
        one.y2 = height - adjustment;

        two =new Line();
        two.iniX = two.x1 =width - adjustment;
        two.iniY = two.y1 = 0 + adjustment;
        two.x2 = width - adjustment;
        two.y2 =height - adjustment;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float[] line1 = new float[] {one.x1, one.y1, one.x2, one.y2};
        float[] line2 = new float[] {two.x1, two.y1, two.x2, two.y2};
        canvas.rotate(angle, centerX, centerY);
        canvas.drawLines(line1, paint);
        canvas.drawLines(line2,paint);

    }



    public void togglePlayPauseButton(ROTATESTATE  currentState) {
        if(currentState == this.currentState) return;

        this.currentState = currentState;
        switch (currentState){
            case PAUSED:
                moveLine(ROTATESTATE.PAUSED);
                invalidate();
                currentState =ROTATESTATE.PAUSED;
                break;
            case PLAYING:
                moveLine(ROTATESTATE.PLAYING);
                invalidate();
                currentState = ROTATESTATE.PLAYING;
                break;
            default: break;


        }

    }


    private  void moveLine(ROTATESTATE state) {

        float[] pos, tan;
        float angleState;
        Handler handler;
        handler = new Handler();
        if (state == ROTATESTATE.PLAYING) {
            one.toX = one.iniX;
            two.toX = two.iniX;
            angleState = -1;
        } else {
            two.toX = one.toX = centerX ;
            angleState = 1;
        }
        one.path = new Path();
        one.path.moveTo(one.x1, one.y1);
        one.path.cubicTo(one.x1, one.y1,  one.toX, one.y1,  one.toX, one.y1);
        one.measure = new PathMeasure(one.path, false);

        two.path = new Path();
        two.path.moveTo(two.x1, two.y1);
        two.path.cubicTo(two.x1, two.y1, two.toX, two.y1, two.toX, two.y1);
        two.measure = new PathMeasure(two.path, false);


        pos = new float[2];
        tan = new float[2];
        for (int i = 0; i < one.measure.getLength(); i++) {

            one.measure.getPosTan(i, pos, tan);
            final float OneMeasuredX = pos[0];
            final float OneMeasuredY = pos[1];
            final float  ratio = (90/( one.measure.getLength() - 1)) * angleState;

            two.measure.getPosTan(i, pos, tan);
            final float twoMeasuredX = pos[0];
            final float twoMeasuredY = pos[1];

            Log.d("ScallableView", i + "");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    one.x1 = (int) OneMeasuredX;
                    one.y1 = (int) OneMeasuredY;
                    two.x1 = (int) twoMeasuredX;
                    two.y1 = (int) twoMeasuredY;

                    angle = angle + ratio;
                    invalidate();
                }
            }, 60);
        }
        one.measure.getPosTan(one.measure.getLength()-1, pos, tan);
        final float OneMeasuredX = pos[0];
        final float OneMeasuredY = pos[1];

        two.measure.getPosTan(two.measure.getLength() - 1, pos, tan);
        final float twoMeasuredX = pos[0];
        final float twoMeasuredY = pos[1];
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                one.x1 = (int) OneMeasuredX;
                one.y1 = (int) OneMeasuredY;
                two.x1 = (int) twoMeasuredX;
                two.y1 = (int) twoMeasuredY;

                invalidate();
            }
        }, 60);
    }

    private void rotateView(){
        Animation an = new RotateAnimation(0.0f, 360.0f, 100, 100);

        // Set the animation's parameters
        an.setDuration(1000);               // duration in ms
        an.setRepeatCount(0);                // -1 = infinite repeated
        an.setRepeatMode(Animation.REVERSE); // reverses each repeat
        an.setFillAfter(true);               // keep rotation after animation

        // Aply animation to image view
        this.setAnimation(an);
    }


    class Line{
        int x1, y1, x2, y2, iniX, iniY, toX, toY;
        Path path;
        PathMeasure measure;
    }

}
