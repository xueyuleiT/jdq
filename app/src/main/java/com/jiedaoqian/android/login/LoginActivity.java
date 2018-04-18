package com.jiedaoqian.android.login;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.activitys.BaseActivity;
import com.jiedaoqian.android.models.ReqLogin;
import com.jiedaoqian.android.models.ResLogin;
import com.jiedaoqian.android.models.ResStatistics;
import com.jiedaoqian.android.register.RegisterActivity;
import com.jiedaoqian.android.utils.CheckUtil;
import com.jiedaoqian.android.utils.Common;
import com.jiedaoqian.android.utils.Config;
import com.jiedaoqian.android.utils.CountDownTime;
import com.jiedaoqian.android.utils.DialogUtil;
import com.jiedaoqian.android.utils.SettingUtil;
import com.jiedaoqian.android.utils.TextViewUtil.TextUtil;
import com.jiedaoqian.android.utils.httputils.HttpBaseResponse;
import com.jiedaoqian.android.utils.httputils.HttpUtil;
import com.jiedaoqian.android.utils.httputils.RedirectObserver;
import com.jiedaoqian.android.utils.httputils.RedirectRTransformer;
import com.jiedaoqian.android.views.ScrollAdView;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Created by zenghui on 2018/3/16.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private View captchLayout,pwdLayout;
    private Button btnPwd,btnCaptcha;
    private EditText edtPwd,edtPwdPhone,edtSmsPhone,edtSms;
    private TextView tvCaptch;
    private CountDownTime countDownTime;
    private TextView tvRegister;
    private TextView loanTime,loanAmount,loanSucc;
    private ScrollAdView adView;

    @Override
    public void initViews() {
        setContentView(R.layout.activity_login);

        setToobar((Toolbar) findViewById(R.id.toolbar),"登录");

        captchLayout = findViewById(R.id.captchLayout);
        pwdLayout = findViewById(R.id.pwdLayout);

        btnPwd = findViewById(R.id.btnPwd);
        btnCaptcha = findViewById(R.id.btnCaptcha);

        adView = findViewById(R.id.adView);
        edtPwd = findViewById(R.id.edtPwd);
        edtPwdPhone = findViewById(R.id.edtPwdPhone);
        edtSmsPhone = findViewById(R.id.edtSmsPhone);
        edtSms = findViewById(R.id.edtSms);
        tvCaptch = findViewById(R.id.tvCaptch);
        tvRegister = findViewById(R.id.tvRegister);
        loanTime = findViewById(R.id.loanTime);
        loanAmount = findViewById(R.id.loanAmount);
        loanSucc = findViewById(R.id.loanSucc);

        tvRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvRegister.getPaint().setAntiAlias(true);//抗锯齿

//        TextUtil.changeColor(tvTip,getResources().getDimensionPixelSize(R.dimen.sp_12),12,17,Color.parseColor("#ff0000"));

        findViewById(R.id.rlLogin).setOnClickListener(this);
        tvCaptch.setOnClickListener(this);
        btnPwd.setOnClickListener(this);
        btnCaptcha.setOnClickListener(this);
        findViewById(R.id.tvRegister).setOnClickListener(this);
        statistics();
    }

    @Override
    public void initDatas() {
        if (System.currentTimeMillis() - SettingUtil.get(mContext,Config.SMS_SEND_TIME,0l) < 1000*60){
            if (countDownTime == null){
                countDownTime = new CountDownTime(tvCaptch,Color.parseColor("#4A61B8"),Color.parseColor("#999999"));
            }
            countDownTime.countDownVoice(60 - (int) (System.currentTimeMillis() - SettingUtil.get(mContext,Config.SMS_SEND_TIME,0l))/1000);
        }
    }


    ValueAnimator objectAnimator;
    void changeLoginType(boolean isLeft){
        if (objectAnimator != null && objectAnimator.isRunning()){
            objectAnimator.cancel();
        }
        if (isLeft) {
            pwdLayout.setTranslationX(pwdLayout.getWidth());
            pwdLayout.setVisibility(View.VISIBLE);
            objectAnimator = ValueAnimator.ofInt(0, -captchLayout.getWidth());
            objectAnimator.setDuration(500);
            objectAnimator.setInterpolator(new DecelerateInterpolator());
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    captchLayout.setTranslationX(value);
                    pwdLayout.setTranslationX(pwdLayout.getWidth()+value);

                }
            });
        }else {
            objectAnimator = ValueAnimator.ofInt( -captchLayout.getWidth(),0);
            objectAnimator.setDuration(500);
            objectAnimator.setInterpolator(new DecelerateInterpolator());
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    captchLayout.setTranslationX(value);
                    pwdLayout.setTranslationX(pwdLayout.getWidth()+value);


                }
            });
        }

        objectAnimator.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvCaptch:
                if (CheckUtil.isPhoneValid(edtSmsPhone.getText().toString().trim())) {
                    sendCode("86", edtSmsPhone.getText().toString().trim());
                }else {
                    showToast("请输入正确的手机号");
                }
                break;
            case R.id.btnCaptcha:
                if (objectAnimator != null && objectAnimator.isRunning()){
                    return;
                }
                btnCaptcha.setTextColor(Color.parseColor("#4A61B8"));
                btnPwd.setTextColor(Color.parseColor("#363636"));
                if (captchLayout.getX() != 0){
                    changeLoginType(false);
                }
                break;
            case R.id.btnPwd:
                if (objectAnimator != null && objectAnimator.isRunning()){
                    return;
                }
                btnCaptcha.setTextColor(Color.parseColor("#363636"));
                btnPwd.setTextColor(Color.parseColor("#4A61B8"));
                if (captchLayout.getX() == 0){
                    changeLoginType(true);
                }
                break;
            case R.id.rlLogin:

                if (!CheckUtil.isPhoneValid(edtSmsPhone.getText().toString().trim())){
                    showToast("请输入正确的手机号");
                    return;
                }

                if (edtSms.getText().length() < 4){
                    showToast("请输入正确的验证码");
                    return;
                }
                login();

                break;
            case R.id.tvRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;

        }
    }

    private void login() {

        DialogUtil.showLoading(mContext,"登录中...");
        final ReqLogin reqLogin = new ReqLogin();
        reqLogin.setPhone(edtSmsPhone.getText().toString().trim());
        reqLogin.setSmsCode(edtSms.getText().toString().trim());
        Observable<Response<HttpBaseResponse<ResLogin>>> observable = HttpUtil.getInstance().getHttpApi().login(reqLogin);
        observable.compose(new RedirectRTransformer<Response<HttpBaseResponse<ResLogin>>, HttpBaseResponse<ResLogin>>())
                .subscribe(new RedirectObserver<HttpBaseResponse<ResLogin>>(mContext){

                    @Override
                    public void onNext(HttpBaseResponse<ResLogin> value) {

                        if(HttpUtil.SUCCESS.equals(value.getCode())) {
                           ResLogin resLogin = value.getData();
                           if (!TextUtils.isEmpty(resLogin.getToken())) {
                               HttpUtil.ut = resLogin.getToken();
                               SettingUtil.set(mContext, "token", resLogin.getToken());
                               SettingUtil.set(mContext, "phone", edtSmsPhone.getText().toString().trim());
                               showSuccessToast("登录成功");

                               Common.scanHistory = SettingUtil.getObjFromSp(mContext, Common.scanSpName, Common.scanSpKey + SettingUtil.get(mContext, "phone", ""));
                               if (Common.scanHistory == null) {
                                   Common.scanHistory = new ArrayList<>(30);
                               }
                               finish();
                           }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        DialogUtil.dismissLoading();
                    }

                    @Override
                    public void onComplete() {
                        DialogUtil.dismissLoading();
                    }
                });

    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        DialogUtil.showLoading(mContext,"短信发送中...");
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                DialogUtil.dismissLoading();
                if (result == SMSSDK.RESULT_COMPLETE) {
                    SettingUtil.set(mContext, Config.SMS_SEND_TIME,System.currentTimeMillis());
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (countDownTime == null) {
                                countDownTime = new CountDownTime(tvCaptch,Color.parseColor("#4A61B8"),Color.parseColor("#999999"));
                            }
                            countDownTime.countDownVoice(60);
                            showSuccessToast("短信发送成功，请注意查收！");                        }
                    });

                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                } else{
                    try {
                        final JSONObject jsonObject = new JSONObject(((Throwable) data).getMessage());
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWaringToast(jsonObject.optString("error"));
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                } else{
                    // TODO 处理错误的结果
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_bottom_out);
    }
    
    void statistics(){
        Observable<Response<HttpBaseResponse<ResStatistics>>> observable = HttpUtil.getInstance().getHttpApi().statistics();
        observable.compose(new RedirectRTransformer<Response<HttpBaseResponse<ResStatistics>>, HttpBaseResponse<ResStatistics>>())
                .subscribe(new RedirectObserver<HttpBaseResponse<ResStatistics>>(mContext) {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpBaseResponse<ResStatistics> value) {
                        if (HttpUtil.SUCCESS.equals(value.getCode())){
                            ResStatistics resStatistics = value.getData();

                            List<String> adList = new ArrayList<>(resStatistics.getFunds().size());
                            for (int i = 0; i < resStatistics.getFunds().size(); i ++){
                                StringBuilder stringBuilder = new StringBuilder();
                                ResStatistics.FundsBean fundsBean = resStatistics.getFunds().get(i);
                                adList.add(stringBuilder.append(fundsBean.getFunder()).toString().replace("{amount}",fundsBean.getAmount()));

                            }
                            adView.initData(adList, (Activity) mContext);

                            loanTime.setText(resStatistics.getStatisticses().get(0).getSubTitle());
                            loanAmount.setText(resStatistics.getStatisticses().get(1).getSubTitle());
                            loanSucc.setText(resStatistics.getStatisticses().get(2).getSubTitle());

                            ((TextView)findViewById(R.id.tvLoanTime)).setText(resStatistics.getStatisticses().get(0).getTitle());
                            ((TextView)findViewById(R.id.tvLoanAmount)).setText(resStatistics.getStatisticses().get(1).getTitle());
                            ((TextView)findViewById(R.id.tvLoanSucc)).setText(resStatistics.getStatisticses().get(2).getTitle());
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
