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
    int width, height;
    Handler handler;

    public enum ROTATESTATE {
        PLAYING, PAUSED, UNKNOWN
    };
    ROTATESTATE  currentState = ROTATESTATE.UNKNOWN;
    public PlayPauseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaintAtttrubutes();
        initPositions(0, 0);
    }

    private void setPaintAtttrubutes(){
        paint.setColor(getResources().getColor(R.color.base));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
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
        width = w;
        height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void initPositions(int width, int height){
        one =new Line();
        one.iniX = one.x1 = 0 + adjustment;
        one.iniY = one.y1 = 0 + adjustment;
        one.x2 = 0 + adjustment;
        one.y2 = height - adjustment;

        two =new Line();
        two.iniX = two.x1 = width - adjustment ;
        two.iniY = two.y1 = 0 + adjustment;
        two.x2 = width - adjustment;
        two.y2 = height - adjustment;
        angle =0;
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
        this.currentState = currentState;
        switch (currentState){
            case PAUSED:
                moveLine(ROTATESTATE.PAUSED);
                invalidate();
                break;
            case PLAYING:
                moveLine(ROTATESTATE.PLAYING);
                invalidate();
                break;
            default: break;


        }

    }


    private  void moveLine(ROTATESTATE state) {

        float[] pos, tan;
        final float angleState;
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
            final int I = i;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    one.x1 = (int) OneMeasuredX;
                    one.y1 = (int) OneMeasuredY;
                    two.x1 = (int) twoMeasuredX;
                    two.y1 = (int) twoMeasuredY;

                    angle = angle + ratio;
                    if(angle < 0) angle = 0;

                    Log.d("PLAYPAUSE" , "i: " + I + " andle:" + angle +"");
                    invalidate();
                }
            }, 20 * i);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                one.x1 = one.toX;
                one.y1 = one.toY;
                two.x1 = two.toX;
                two.y1 = two.toY;
                if(angle > 90){
                    angle = 90;
                }else{
                    angle = 0;
                }
                invalidate();
            }
        }, 20 * (int)one.measure.getLength());

    }
    private int roundDown(int round){
        return round - round%10;
    }

    class Line{
        int x1, y1, x2, y2, iniX, iniY, toX, toY;
        Path path;
        PathMeasure measure;
    }

}
