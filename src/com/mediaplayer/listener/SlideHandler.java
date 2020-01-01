package com.mediaplayer.listener;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.mediaplayer.ui.NowPlayingFragment;

public class SlideHandler implements View.OnTouchListener{
	Context context;
	float prevTouchY=0f;
	float currentTouchY, totalTranslation = 0f, maxBottom, maxTop;



	enum ActionType  {UP,DOWN};
	ActionType currentAction;
	DisplayMetrics dm;
	float density;
	NowPlayingFragment parent;
	public SlideHandler(Context context) {
		this.context = context;
		dm =context.getResources().getDisplayMetrics();
		density = dm.density;
		maxBottom =  dm.heightPixels -80 * density;
		maxTop = 0;
		totalTranslation = maxBottom;
	}

	public void setParent(NowPlayingFragment f){
		parent = f;
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
				if(point[1]< maxTop) {
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
				if(currentAction==null) break;
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
		totalTranslation = maxTop;
		ObjectAnimator translationY = ObjectAnimator.ofFloat(v, "translationY", totalTranslation);
		translationY.setInterpolator(new AccelerateInterpolator());

		AnimatorSet as = new AnimatorSet();
		as.playTogether(translationY);
		as.setDuration(200);
		as.start();
		parent.setIsUp(true);
	}
	private void translateDown(View v){
		totalTranslation = maxBottom;
		ObjectAnimator translationY = ObjectAnimator.ofFloat(v, "translationY", totalTranslation);
		translationY.setInterpolator(new AccelerateInterpolator());

		AnimatorSet as = new AnimatorSet();
		as.playTogether(translationY);
		as.setDuration(200);
		as.start();
		parent.setIsUp(false);
	}

	public void slideDown(View v){
		translateDown(v);
	}

	public void slideUp(View v) {
		translateUp(v);
	}

}