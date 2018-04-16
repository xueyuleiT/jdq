package com.jiedaoqian.android.interfaces;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.widget.TextView;


import com.jiedaoqian.android.R;
import com.jiedaoqian.android.activitys.BaseActivity;
import com.jiedaoqian.android.activitys.WebviewActivity;
import com.jiedaoqian.android.login.LoginActivity;
import com.jiedaoqian.android.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zenghui on 2017/8/1.
 */

public class JavaScriptInterface {

    Context context;
    public JavaScriptInterface(Context context){
        this.context = context;
    }

    @JavascriptInterface
    public int getScreenHeight(){
        return Common.SCREEN_HEIGHT;
    }
    @JavascriptInterface
    public void sharePage(){
    }
    @JavascriptInterface
    public void setNavTitle(final String title){
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(title);
                    BaseActivity activity = ((BaseActivity)context);
                    ((TextView)activity.findViewById(R.id.title)).setText(jsonObject.optString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }
            });
//            activity.setToobar((Toolbar) (activity.findViewById(R.id.toolbar)),jsonObject.optString("title"));

    }

    @JavascriptInterface
    public void closePage(){
        ((BaseActivity)context).finish();
    }



    @JavascriptInterface
    public void needLogin(String s){
        WebviewActivity activity = (WebviewActivity)context;
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("link",activity.linkUrl);
        intent.putExtra("class",activity.getClass());
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_bottom_in, R.anim.slide_bottom_out);
        activity.finish();
    }

}
