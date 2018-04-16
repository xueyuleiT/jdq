package com.jiedaoqian.android.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by zenghui on 2017/7/27.
 */

public class CountDownTime {
    TextView btTime;
    int ableColor,enableColor;
    CountDownTimer timer;
    public CountDownTime(TextView btTime , int ableColor, int enableColor){
        this.btTime = btTime;
        this.ableColor = ableColor;
        this.enableColor = enableColor;
    }

    //计时器相关
    public void countDownVoice(int count) {
        btTime.setEnabled(false);
        btTime.setTextColor(enableColor);
        timer = new CountDownTimer(count * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                btTime.setText("重新发送" + (millisUntilFinished/1000) + "秒");
            }

            @Override
            public void onFinish() {
                btTime.setEnabled(true);
                btTime.setTextColor(ableColor);
                btTime.setText("获取验证码");
            }
        };
        timer.start();
    }
}
