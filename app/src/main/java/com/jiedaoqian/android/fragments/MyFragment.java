package com.jiedaoqian.android.fragments;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.activitys.ScanHistoryActivity;
import com.jiedaoqian.android.interfaces.HandleDialogListener;
import com.jiedaoqian.android.login.LoginActivity;
import com.jiedaoqian.android.utils.Common;
import com.jiedaoqian.android.utils.DialogUtil;
import com.jiedaoqian.android.utils.SettingUtil;
import com.jiedaoqian.android.utils.TextViewUtil.TextUtil;
import com.jiedaoqian.android.utils.httputils.HttpBaseResponse;
import com.jiedaoqian.android.utils.httputils.HttpUtil;
import com.jiedaoqian.android.utils.httputils.RedirectObserver;
import com.jiedaoqian.android.utils.httputils.RedirectRTransformer;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Created by zenghui on 2018/3/21.
 */

public class MyFragment extends BaseFragment implements View.OnClickListener{
    private View rootView;
    private boolean init = false;
    private RelativeLayout rlHead;

    private LinearLayout aboutusLayout,inviteLayout,scanLayout,logoutLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my, null);
            aboutusLayout = rootView.findViewById(R.id.aboutusLayout);
            inviteLayout = rootView.findViewById(R.id.inviteLayout);
            scanLayout = rootView.findViewById(R.id.scanLayout);
            logoutLayout = rootView.findViewById(R.id.logoutLayout);

            aboutusLayout.setOnClickListener(this);
            inviteLayout.setOnClickListener(this);
            scanLayout.setOnClickListener(this);
            logoutLayout.setOnClickListener(this);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(SettingUtil.get(getActivity(),"token",""))){
            ((TextView)rootView.findViewById(R.id.tvHead)).setText(SettingUtil.get(getActivity(),"phone",""));
            logoutLayout.setVisibility(View.VISIBLE);
        }else {
            ((TextView)rootView.findViewById(R.id.tvHead)).setText("登录/注册");
            logoutLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!init) {
            init = true;
            rlHead = rootView.findViewById(R.id.rlHead);
            rlHead.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlHead:
                if (TextUtils.isEmpty(SettingUtil.get(getActivity(),"token",""))) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(com.jiedaoqian.android.R.anim.slide_bottom_in, com.jiedaoqian.android.R.anim.slide_bottom_out);
                }
                break;
            case R.id.scanLayout:
                if (TextUtils.isEmpty(SettingUtil.get(getActivity(),"token",""))) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(com.jiedaoqian.android.R.anim.slide_bottom_in, com.jiedaoqian.android.R.anim.slide_bottom_out);
                }else {
                    startActivity(new Intent(getActivity(), ScanHistoryActivity.class));
                }
                break;
            case R.id.aboutusLayout:
                if (TextUtils.isEmpty(SettingUtil.get(getActivity(),"token",""))) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(com.jiedaoqian.android.R.anim.slide_bottom_in, com.jiedaoqian.android.R.anim.slide_bottom_out);
                }else {
                    DialogUtil.showChooseDialog(getActivity(), "联系我们", "xiaolongnv6660", "", "", 0, 0, false, new HandleDialogListener() {
                        @Override
                        public void handle() {
                            ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            // 将文本内容放到系统剪贴板里。
                            cm.setText("xiaolongnv6660");
                            showSuccessToast("复制成功!");
                        }
                    }, null);
                }
                break;
            case R.id.inviteLayout:
                if (TextUtils.isEmpty(SettingUtil.get(getActivity(),"token",""))) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(com.jiedaoqian.android.R.anim.slide_bottom_in, com.jiedaoqian.android.R.anim.slide_bottom_out);
                }else {

                }
                break;
            case R.id.logoutLayout:
                DialogUtil.showLogoutDialog(getActivity(), new HandleDialogListener() {
                    @Override
                    public void handle() {
                        logout();
                    }
                });
                break;
        }
    }

    void logout(){
        Observable<Response<HttpBaseResponse<Object>>> observable = HttpUtil.getInstance().getHttpApi().logout();
        observable.compose(new RedirectRTransformer<Response<HttpBaseResponse<Object>>, HttpBaseResponse<Object>>())
                .subscribe(new RedirectObserver<HttpBaseResponse<Object>>(getActivity()) {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpBaseResponse<Object> value) {

                        if (HttpUtil.SUCCESS.equals(value.getCode())){

                            ((TextView)rootView.findViewById(R.id.tvHead)).setText("登录/注册");
                            logoutLayout.setVisibility(View.GONE);


                            CookieSyncManager.createInstance(getActivity());
                            CookieManager cookieManager = CookieManager.getInstance();
                            cookieManager.setAcceptCookie(true);
                            cookieManager.removeAllCookie();
                            CookieSyncManager.getInstance().sync();
                            HttpUtil.ut = "";
                            Common.scanHistory = null;
                            SettingUtil.clear(getActivity());

                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
