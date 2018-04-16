package com.jiedaoqian.android.utils.httputils;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jiedaoqian.android.BuildConfig;
import com.jiedaoqian.android.activitys.MainActivity;
import com.jiedaoqian.android.activitys.BaseActivity;
import com.jiedaoqian.android.login.LoginActivity;
import com.jiedaoqian.android.utils.DialogUtil;
import com.jiedaoqian.android.utils.SettingUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by zenghui on 2017/7/6.
 */

public class RedirectRTransformer<T,R> implements ObservableTransformer<T,R> {

    @Override
    public ObservableSource apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .lift(new ObservableOperator<Object, T>() {
                    @Override
                    public Observer<? super T> apply(final Observer<? super Object> observer) throws Exception {
                        return new Observer<T>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                observer.onSubscribe(d);
                            }

                            @Override
                            public void onNext(T value) {
                                if (value instanceof Response) {
                                    HttpBaseResponse httpBaseResponse;
                                    Response response = (Response) value;
                                    if (response.code() != 200){
                                        if (response.code() == 401){
                                            if (observer instanceof RedirectObserver) {
                                                onLogin(((RedirectObserver) observer).getmContext());
                                                return;
                                            }
                                        }

                                        httpBaseResponse = convertStreamToResponseInfo(response.errorBody().byteStream());
                                        String REQID = null;
                                        List<String> headers = response.raw().headers().values("REQID");
                                        for (String string:headers){
                                            String split = string.split(";")[0];
                                            REQID = split;
                                        }

                                        if (httpBaseResponse != null){
                                            if (observer instanceof RedirectObserver) {
                                                Context context = ((RedirectObserver) observer).getmContext();
                                                if (!TextUtils.isEmpty(httpBaseResponse.getMessage())) {
                                                    if (!TextUtils.isEmpty(REQID)){
                                                        DialogUtil.showChooseDialog(context, "安卓V_"+ BuildConfig.VERSION_NAME+httpBaseResponse.getMessage(),"错误ID:"+REQID,"确定","",0,0,false,null,null );
                                                    }else {
                                                        DialogUtil.showChooseDialog(context,"安卓V_"+BuildConfig.VERSION_NAME,httpBaseResponse.getMessage(),"确定","",0,0,false,null,null );
                                                    }
                                                }
                                            }
                                        }

                                        onComplete();
                                        return;
                                    }

                                    if (response.body() == null){//判空 顶层类判空 其它地方可以不用管 放心用
                                        httpBaseResponse = convertStreamToResponseInfo(response.errorBody().byteStream());
                                        if (httpBaseResponse != null) {//判空
                                            observer.onNext(httpBaseResponse);
                                            //统一处理错误弹框
                                            if (observer instanceof RedirectObserver) {
                                                if (!TextUtils.isEmpty(httpBaseResponse.getMessage())) {
                                                    DialogUtil.showChooseDialog(((RedirectObserver) observer).getmContext(), "安卓V_"+BuildConfig.VERSION_NAME, httpBaseResponse.getMessage(), "确定", "", 0, 0, false, null, null);
                                                }
                                            }
                                        }else {
                                            onError(new Throwable());
                                        }
                                    }else {
                                        if (response.body() instanceof HttpBaseResponse ) {
                                            httpBaseResponse = (HttpBaseResponse) response.body();
                                            observer.onNext(httpBaseResponse);
                                            if (observer instanceof RedirectObserver) {
                                                //统一处理错误弹框
                                                if (!HttpUtil.SUCCESS.equals(httpBaseResponse.getCode())) {
                                                    if (!TextUtils.isEmpty(httpBaseResponse.getMessage())) {
                                                        DialogUtil.showChooseDialog(((RedirectObserver) observer).getmContext(), "安卓V_"+BuildConfig.VERSION_NAME, httpBaseResponse.getMessage(), "确定", "", 0, 0, false, null, null);
                                                    }
                                                }
                                            }
                                        }else {
                                            observer.onNext(response.body());
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                observer.onError(e);
                                onComplete();
                            }

                            @Override
                            public void onComplete() {
                                observer.onComplete();
                            }



                        };
                    }
                });
    }

    public HttpBaseResponse convertStreamToResponseInfo(InputStream is){
        if (is == null){
            return null;
        }
        try {
            HttpBaseResponse basicResponse = new Gson().fromJson(convertStreamToString(is),HttpBaseResponse.class);
            return basicResponse;
        }catch (Exception e){
        }
        return null;
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


    public void onLogin(Context mContext){
        DialogUtil.dismissLoading();
        //清除cookie
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();

        HttpUtil.ut = "";
        SettingUtil.clear(mContext);

        Toasty.warning(mContext, "登陆失效", Toast.LENGTH_SHORT, true).show();
        BaseActivity activity = ((BaseActivity)mContext);
        Intent intent = new Intent(mContext, LoginActivity.class);
        if (activity.bundle != null){
            activity.bundle.putSerializable("class",activity.getClass());
            intent.putExtras(activity.bundle);
        }else {
            intent.putExtra("class", activity.getClass());
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(com.jiedaoqian.android.R.anim.slide_bottom_in, com.jiedaoqian.android.R.anim.slide_bottom_out);
        if (!(activity instanceof MainActivity)){
            activity.finish();
        }
    }
}
