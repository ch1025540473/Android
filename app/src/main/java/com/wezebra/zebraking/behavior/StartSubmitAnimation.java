package com.wezebra.zebraking.behavior;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.wezebra.zebraking.R;

/**
 * Created by HQDev on 2015/4/24.
 */
public class StartSubmitAnimation {

    public static void run(final Activity activity, final ImageView img) {
        img.setVisibility(View.VISIBLE);
        Animation animation1 = AnimationUtils.loadAnimation(activity, R.anim.animation_submit_one);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation animation2 = AnimationUtils.loadAnimation(activity,R.anim.animation_submit_two);
                animation2.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation animation3 = AnimationUtils.loadAnimation(activity,R.anim.animation_submit_three);
                        img.startAnimation(animation3);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                img.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        img.startAnimation(animation1);
    }

}
