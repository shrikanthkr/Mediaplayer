package com.mediaplayer.listener;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class SlideHandler implements View.OnTouchListener{
	Context context;
	float prevTouchY=0f;
	float currentTouchY, totalTranslation = 0f, maxBottom;
	enum ActionType  {UP,DOWN};
	ActionType currentAction;
	DisplayMetrics dm;
	SlideListener listener;
	public SlideHandler(Context context, SlideListener slideListener) {
		this.context = context;
		dm =context.getResources().getDisplayMetrics();
		maxBottom =  dm.heightPixels - 70;
		totalTranslation = dm.heightPixels - 260;
		this.listener = slideListener;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		int action = motionEvent.getAction();
		switch (action){
			case MotionEvent.ACTION_DOWN:
				prevTouchY =  motionEvent.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				currentTouchY = motionEvent.getRawY();
				totalTranslation +=currentTouchY - prevTouchY;
				int[] point = new int[2];
				view.getLocationOnScreen(point);
				if(point[1]<= 120) {
					currentAction = ActionType.UP;
					break;
				}else if(point[1] > maxBottom){
					currentAction = ActionType.DOWN;
					break;
				}

				view.setTranslationY(totalTranslation);

				if(currentTouchY<prevTouchY){
					currentAction = ActionType.UP;
				}
				if(currentTouchY>prevTouchY){
					currentAction = ActionType.DOWN;
				}
				prevTouchY = currentTouchY;
				break;
			case MotionEvent.ACTION_UP:
				switch (currentAction){
					case UP:
						translateUp(view);
						break;
					case DOWN:
						translateDown(view);
						break;
					default:
						translateDown(view);
						break;
				}
				break;
		}
		return true;
	}
	private void translateUp(View v){
		totalTranslation = 0;
		ObjectAnimator translationY = ObjectAnimator.ofFloat(v, "translationY", totalTranslation);
		translationY.setInterpolator(new AccelerateInterpolator());

		AnimatorSet as = new AnimatorSet();
		as.playTogether(translationY);
		as.setDuration(200);
		as.start();
	}
	private void translateDown(View v){
		totalTranslation = dm.heightPixels - 260;
		ObjectAnimator translationY = ObjectAnimator.ofFloat(v, "translationY", totalTranslation);
		translationY.setInterpolator(new AccelerateInterpolator());

		AnimatorSet as = new AnimatorSet();
		as.playTogether(translationY);
		as.setDuration(200);
		as.start();
	}

	public interface SlideListener{
		void aboveBar();
		void onBottom();
		void onTop();
	}

}