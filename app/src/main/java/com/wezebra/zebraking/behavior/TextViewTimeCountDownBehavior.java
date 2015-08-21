package com.wezebra.zebraking.behavior;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by HQDev on 2015/4/8.
 */
public class TextViewTimeCountDownBehavior implements TimeCountDownBehavior {

    private TextView textView;
    private long millisInFuture,countDownInterval;
    private String countDownText, unCountDownText;

    public TextViewTimeCountDownBehavior(TextView textView, long millisInFuture, long countDownInterval,String countDownText,String unCountDownText) {
        this.textView = textView;
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
        this.countDownText = countDownText;
        this.unCountDownText = unCountDownText;
    }

    @Override
    public void countDownTime() {

        textView.setEnabled(false);
        textView.setBackgroundColor(0xffff0000);
        textView.setTextColor(0xffffffff);

        CountDownTimer countDownTimer = new CountDownTimer(millisInFuture,countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText(millisUntilFinished/1000 + "秒" + countDownText);
            }

            @Override
            public void onFinish() {
                textView.setText(unCountDownText);
                textView.setTextColor(0xff000000);
                textView.setBackgroundColor(0xffaaaaaa);
                textView.setEnabled(true);
            }
        };
        countDownTimer.start();

    }

}
