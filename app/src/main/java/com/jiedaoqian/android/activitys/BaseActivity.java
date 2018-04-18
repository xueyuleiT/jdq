package com.jiedaoqian.android.activitys;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.utils.Common;
import com.jiedaoqian.android.utils.DialogUtil;

import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;

/**
 * Created by zenghui on 2018/3/15.
 */

public abstract class BaseActivity extends AppCompatActivity{

    protected Context mContext;
    public static Typeface typeface;
    public Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        bundle = getIntent().getExtras();
        if (Common.SCREEN_WIDTH == 0){
            Display display = getWindowManager().getDefaultDisplay();
            Common.SCREEN_WIDTH = display.getWidth();
            Common.SCREEN_HEIGHT = display.getHeight();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            View decorView = getWindow().getDecorView();
            //重点：SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        }


        initViews();
        initDatas();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    public void setToobar(Toolbar toolBar, String title){
        toolBar.setTitle("");

        TextView tvTitle = findViewById(R.id.title);
        tvTitle.setText(title);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


   public abstract void initViews();
    public abstract void initDatas();
    public void showWaringToast(String s){
        Toasty.warning(this, s, Toast.LENGTH_SHORT, true).show();
    }
    public void showToast(String s){
        Toasty.info(this, s, Toast.LENGTH_SHORT, true).show();
    }
    public void showSuccessToast(String s){
        Toasty.success(this, s, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void finish(){
        DialogUtil.dimissChooseDialog();
        DialogUtil.dismissLoading();
        super.finish();
    }


}
