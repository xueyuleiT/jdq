package com.jiedaoqian.android.utils;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.jiedaoqian.android.R;


/**
 * Created by zenghui on 2017/8/3.
 */

public class AnimateUtil {

    public static final void appearAnimation(final View view){
        view.setVisibility(View.VISIBLE);
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.translate_out));
        view.getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static final void disappearAnimation(final View view){

        TranslateAnimation disappearAnimation = new TranslateAnimation(0,0,0,-view.getHeight());
        disappearAnimation.setDuration(500);
        disappearAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(disappearAnimation);
    }

    public static final void moveAnimationUp(final View view, final int height){

        TranslateAnimation disappearAnimation = new TranslateAnimation(0,0,0,height);
        disappearAnimation.setDuration(500);
        disappearAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(disappearAnimation);
    }

    public static final void moveAnimationDown(final View view, final int height){

        TranslateAnimation disappearAnimation = new TranslateAnimation(0,0,height,0);
        disappearAnimation.setDuration(500);
        disappearAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(disappearAnimation);
    }

}
