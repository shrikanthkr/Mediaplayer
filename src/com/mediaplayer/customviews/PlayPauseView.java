package com.mediaplayer.customviews;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
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
import android.view.animation.AnimationSet;
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

        int startAngle, endAngle;
        if (state == ROTATESTATE.PLAYING) {
            one.toX = one.iniX;
            two.toX = two.iniX;
            startAngle = 90;
            endAngle = 0;
        } else {
            two.toX = one.toX = centerX ;
            startAngle = 0;
            endAngle = 90;
        }
        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator lineOneAnimator = ValueAnimator.ofInt(one.x1,one.toX);
        ValueAnimator lineTwoAnimator = ValueAnimator.ofInt(two.x1,two.toX);
        ValueAnimator rotateAnimator = ValueAnimator.ofInt(startAngle,endAngle);

        lineOneAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                one.x1 = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        lineTwoAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                two.x1 = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                angle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animatorSet.setDuration(300);
        animatorSet.playTogether(lineOneAnimator,lineTwoAnimator,rotateAnimator);
        animatorSet.start();

    }

    class Line{
        int x1, y1, x2, y2, iniX, iniY, toX, toY;
    }

}
