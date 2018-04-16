package com.jiedaoqian.android.utils.TextViewUtil;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by zenghui on 2018/3/21.
 */

public class TextUtil {

   public static void changeColor(TextView textView ,int spSource, int startIndex , int endIndex, int color){

        Spannable span = new SpannableString(textView.getText());
        span.setSpan(new AbsoluteSizeSpan(spSource), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(span);
    }

}
