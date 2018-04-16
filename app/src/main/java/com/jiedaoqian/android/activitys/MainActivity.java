package com.jiedaoqian.android.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.activitys.BaseActivity;
import com.jiedaoqian.android.fragments.LoanSelectFragment;
import com.jiedaoqian.android.fragments.MainFragment;
import com.jiedaoqian.android.fragments.MyFragment;
import com.jiedaoqian.android.login.LoginActivity;
import com.jiedaoqian.android.utils.Common;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    RadioGroup radioGroup;
    RadioButton rbOne,rbTwo,rbThree;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private List<Fragment> fragmentList;
    @Override
    public void initViews() {

        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.rg);

        rbOne = findViewById(R.id.rbOne);
        rbTwo = findViewById(R.id.rbTwo);
        rbThree = findViewById(R.id.rbThree);

        fragmentList = new ArrayList<>();
        fragmentList.add(new MainFragment());
        fragmentList.add(new LoanSelectFragment());
        fragmentList.add(new MyFragment());


        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content,fragmentList.get(0));
        transaction.commitAllowingStateLoss();

        (rbOne).setChecked(true);
        rbOne.setTextColor(getResources().getColor(R.color.colorTitle));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                radioButton.setTextColor(Color.parseColor("#ff5f5f"));
                switch (checkedId){
                    case R.id.rbOne:
                        rbOne.setTextColor(Color.parseColor("#4A61B8"));
                        rbThree.setTextColor(Color.parseColor("#888888"));
                        rbTwo.setTextColor(Color.parseColor("#888888"));

                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content,fragmentList.get(0));
                        transaction.commitAllowingStateLoss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            View decorView = getWindow().getDecorView();
                            //重点：SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                            decorView.setSystemUiVisibility(option);
                            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                        }
                        break;
                    case R.id.rbTwo:
                        rbOne.setTextColor(Color.parseColor("#888888"));
                        rbTwo.setTextColor(Color.parseColor("#4A61B8"));
                        rbThree.setTextColor(Color.parseColor("#888888"));
                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content,fragmentList.get(1));
                        transaction.commitAllowingStateLoss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            View decorView = getWindow().getDecorView();
                            //重点：SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                            decorView.setSystemUiVisibility(option);
                            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                        }

                        break;
                    case R.id.rbThree:
                        rbOne.setTextColor(Color.parseColor("#888888"));
                        rbTwo.setTextColor(Color.parseColor("#888888"));
                        rbThree.setTextColor(Color.parseColor("#4A61B8"));
                        transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.content,fragmentList.get(2));
                        transaction.commitAllowingStateLoss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            View decorView = getWindow().getDecorView();
                            //重点：SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                            decorView.setSystemUiVisibility(option);
                            getWindow().setStatusBarColor(getResources().getColor(R.color.colorTitle));
                        }
                        break;
                }
            }
        });

    }

    @Override
    public void initDatas() {

    }

    //双击退出
    public void doubleClickExitApp() {
        if (Common.downTime == 0) {
            Common.downTime = System.currentTimeMillis();
            showToast("再点一次 退出应用");
            return;
        }
        long lastDownTime = System.currentTimeMillis();
        if ((lastDownTime - Common.downTime) > 1000) {
            Common.downTime = lastDownTime;
            showToast("再点一次 退出应用");
        } else {
            System.exit(0);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        doubleClickExitApp();
    }
}
