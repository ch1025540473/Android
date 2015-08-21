package com.wezebra.zebraking.behavior;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.wezebra.zebraking.ui.installment.AssessmentPassedActivity;

/**
 * Created by HQDev on 2015/4/13.
 */
public class ActivityTimeCountDownBehavior implements TimeCountDownBehavior {

    private Activity activity;
    private long millisInFuture,countDownInterval;
    private TextView textView;

    public ActivityTimeCountDownBehavior(Activity activity,long millisInFuture,long countDownInterval) {
        this.activity = activity;
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
    }

    public ActivityTimeCountDownBehavior(Activity activity,long millisInFuture,long countDownInterval,TextView textView) {
        this(activity, millisInFuture, countDownInterval);
        this.textView = textView;
    }

    @Override
    public void countDownTime() {

        CountDownTimer countDownTimer = new CountDownTimer(millisInFuture,countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText(millisUntilFinished/1000 + "秒");
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent();
                intent.setClass(activity, AssessmentPassedActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        };
        countDownTimer.start();

    }

}
