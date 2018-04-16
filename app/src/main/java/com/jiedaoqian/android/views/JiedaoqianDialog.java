/*
 * UPDialog.java
 * classes : com.sunyard.chinaums.common.ui.UPDialog
 * @author zenghui
 * V 1.0.0
 * Create at 2015-8-7 下午5:18:07
 */
package com.jiedaoqian.android.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiedaoqian.android.R;


/**
 * 网络通讯中
 * @author zenghui <br/>
 *         create at 2015-8-7 下午5:18:07
 */
public class JiedaoqianDialog extends ProgressDialog {

    private String msg;
    private boolean cancelable = true;
    private Context context;
    private View mDialogContentView;
    TextView textView;

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public JiedaoqianDialog(Context context, String msg) {
        super(context);
        this.msg = msg;
        this.context = context;
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogContentView = View.inflate(context, R.layout.fushun_loading, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        setContentView(mDialogContentView,layoutParams);
    }

    public boolean isCancelable() {
        return cancelable;
    }

    @Override
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !cancelable) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
