package com.jiedaoqian.android.register;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.activitys.BaseActivity;
import com.jiedaoqian.android.login.LoginActivity;
import com.jiedaoqian.android.utils.CheckUtil;
import com.jiedaoqian.android.utils.Config;
import com.jiedaoqian.android.utils.CountDownTime;
import com.jiedaoqian.android.utils.DialogUtil;
import com.jiedaoqian.android.utils.SettingUtil;
import com.jiedaoqian.android.utils.TextViewUtil.LinkTouchMovementMethod;
import com.jiedaoqian.android.utils.TextViewUtil.TextUtil;
import com.jiedaoqian.android.utils.TextViewUtil.TouchableSpan;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{


    TextView tvRegisterAgreement,tvLogin;
    private TextView tvTip,tvCaptch;
    private RelativeLayout register;
    private EditText edtPwd,edtSms,edtPhone;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_register);
        setToobar((Toolbar) findViewById(R.id.toolbar),"注册");

        tvRegisterAgreement = findViewById(R.id.tvRegisterAgreement);
        tvLogin = findViewById(R.id.tvLogin);
        tvTip = findViewById(R.id.tvTip);
        tvCaptch = findViewById(R.id.tvCaptch);
        edtPhone = findViewById(R.id.edtPhone);
        edtSms = findViewById(R.id.edtSms);
        edtPwd = findViewById(R.id.edtPwd);


        TextUtil.changeColor(tvTip,getResources().getDimensionPixelSize(R.dimen.sp_12),12,17,Color.parseColor("#ff0000"));

        tvRegisterAgreement.setText("点击“立即注册”即默认同意");
        tvLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvLogin.getPaint().setAntiAlias(true);//抗锯齿

        setUnderLine();

        tvCaptch.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvRegisterAgreement.setOnClickListener(this);

    }

    void setUnderLine(){
        Paint paint = new Paint();
        paint.setTextSize(tvRegisterAgreement.getTextSize());

        String appendStr = "《用户注册协议》";


        SpannableString spStr = new SpannableString(appendStr.trim());
        int touchColor = Color.parseColor("#4A61B8");
        TouchableSpan touchableSpan = new TouchableSpan(touchColor, touchColor, touchColor) {
            @Override
            public void onClick(View widget) {
                super.onClick(widget);
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        };

        spStr.setSpan(touchableSpan, 0, appendStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegisterAgreement.append(spStr);
        tvRegisterAgreement.setMovementMethod(new LinkTouchMovementMethod());//开始响应点击事件
        tvRegisterAgreement.setLongClickable(false);
    }

    private CountDownTime countDownTime;

    @Override
    public void initDatas() {
        if (System.currentTimeMillis() - SettingUtil.get(mContext, Config.SMS_SEND_TIME,0l) < 1000*60){
            if (countDownTime == null){
                countDownTime = new CountDownTime(tvCaptch,Color.parseColor("#4A61B8"),Color.parseColor("#999999"));
            }
            countDownTime.countDownVoice(60 - (int) (System.currentTimeMillis() - SettingUtil.get(mContext,Config.SMS_SEND_TIME,0l))/1000);
        }
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
                    RegisterActivity.this.runOnUiThread(new Runnable() {
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
                        RegisterActivity.this.runOnUiThread(new Runnable() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvLogin:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.tvCaptch:
                if (CheckUtil.isPhoneValid(edtPhone.getText().toString().trim())) {
                    sendCode("86", edtPhone.getText().toString().trim());
                }else {
                    showToast("请输入正确的手机号");
                }
                break;
            case R.id.tvRegisterAgreement:
                break;

        }
    }
}
