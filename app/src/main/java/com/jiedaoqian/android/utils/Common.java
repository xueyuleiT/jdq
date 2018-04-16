package com.jiedaoqian.android.utils;


import android.support.v4.widget.SwipeRefreshLayout;

import com.jiedaoqian.android.BuildConfig;
import com.jiedaoqian.android.R;
import com.jiedaoqian.android.models.ProductHotInfo;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.http.PUT;

/**
 * Created by zenghui on 2017/6/27.
 */

public class Common {


    public static final String REQUEST_PARAM_USER_AGENT = "com.jedaoqian.android.v"+ BuildConfig.VERSION_NAME;
    public static final String TITLE = "title";
    public static final String LINK = "linkUrl";


    public static int SCREEN_HEIGHT = 0;
    public static int SCREEN_WIDTH = 0;
    public static long downTime = 0;
    public static ArrayList<ProductHotInfo> scanHistory = new ArrayList<>(30);

    public static String scanSpName = "scanHistory";
    public static String scanSpKey = "scanHistory";

    public static final void setSwipeLayout(SwipeRefreshLayout swipeLayout){
        swipeLayout.setProgressBackgroundColorSchemeColor(swipeLayout.getResources().getColor(R.color.w_1));
        swipeLayout.setColorSchemeColors(swipeLayout.getResources().getColor(R.color.n_2), swipeLayout.getResources().getColor(R.color.n_3),
                swipeLayout.getResources().getColor(R.color.i_2), swipeLayout.getResources().getColor(R.color.w_3));
    }
}
