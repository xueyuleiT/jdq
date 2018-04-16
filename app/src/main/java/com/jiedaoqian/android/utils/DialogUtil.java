package com.jiedaoqian.android.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.activitys.BaseActivity;
import com.jiedaoqian.android.interfaces.HandleDialogListener;
import com.jiedaoqian.android.views.JiedaoqianDialog;

import java.util.List;

/**
 * Created by zenghui on 2017/8/14.
 */

public class DialogUtil {
    public static JiedaoqianDialog jiedaoqianDialog;

    //获取中,定位中
    public static void showLoading(Context context, String content) {
        if (jiedaoqianDialog != null && jiedaoqianDialog.isShowing()) {
            return;
        }
        jiedaoqianDialog = new JiedaoqianDialog(context, content);
        jiedaoqianDialog.setCancelable(false);
        jiedaoqianDialog.show();
    }

    public static void dismissLoading() {
        if (jiedaoqianDialog != null && jiedaoqianDialog.isShowing()) {
            jiedaoqianDialog.dismiss();
        }

    }

    public static void showDialog(Context context, String title, String content, DialogInterface.OnClickListener negative,DialogInterface.OnClickListener positive) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context,android.R.style.Theme_Material_Light_Dialog_Alert);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setCancelable(false);
        builder.setMessage(content);
        if (negative != null) {
            builder.setNegativeButton("取消", negative);
        }
        builder.setPositiveButton("确定", positive);
        builder.show();
    }

    private static Dialog mDialog;

    public static void dimissChooseDialog(){
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }


//    public static void showInputDialog(final Context con, final WebView webView, final HandleDialogListener handleConfirm) {
//
//        mDialog = new Dialog(con, R.style.jiedaoqianDialog);
//        mDialog.setContentView(R.layout.dialog_input);
//        mDialog.setCanceledOnTouchOutside(false);
//
//
//        final EditText ip = mDialog.findViewById(R.id.ip);
//        final EditText port = mDialog.findViewById(R.id.port);
//        Button btnOk = mDialog.findViewById(R.id.btnOk);
//
//        btnOk.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//                WebviewProxy.setProxy(webView,ip.getText().toString().trim(),Integer.valueOf(port.getText().toString().trim()),"com.cashbus.android.fushun.activitys.FushunApplication");
//                if (handleConfirm != null) {
//                    handleConfirm.handle();
//                }
//
//            }
//        });
//
//        if(con != null  && !((Activity)con).isFinishing()) {
//            mDialog.show();
//        }
//
//    }

    public static void showChooseDialog(final Context con, String title, String content, String confirmBtnPrompt,
                                        String cancelBtnPrompt, int confirmColor, int cancelColor, final boolean isCancelable, final HandleDialogListener handleConfirm,
                                        final HandleDialogListener handleCancel) {

        mDialog = new Dialog(con, R.style.JiedaoqiaoDialog);
        mDialog.setContentView(R.layout.layout_dialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(isCancelable);
        TextView tvTitle = mDialog.findViewById(R.id.title);
        TextView tvContent = mDialog.findViewById(R.id.content);

        final Dialog dialog = mDialog;

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
            tvContent.setText(content);
        } else {
            tvTitle.setText(content);
            tvTitle.setMovementMethod(new ScrollingMovementMethod());
            tvContent.setVisibility(View.GONE);
        }

        Button mDialogCancel = (Button) mDialog.findViewById(R.id.btn_cancel);
        if (cancelColor != 0) {
            mDialogCancel.setTextColor(con.getResources().getColor(cancelColor));
        }
        if (!TextUtils.isEmpty(cancelBtnPrompt)) {
            mDialogCancel.setText(cancelBtnPrompt);
        } else {
            mDialog.findViewById(R.id.mutiLayout).setVisibility(View.GONE);
            Button btnOk = (Button) mDialog.findViewById(R.id.btnOk);
            btnOk.setVisibility(View.VISIBLE);
            if (confirmColor != 0) {
                btnOk.setTextColor(con.getResources().getColor(confirmColor));
            }
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (handleConfirm != null) {
                        handleConfirm.handle();
                    }
                }
            });
        }
        mDialogCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (handleCancel != null) {
                    handleCancel.handle();
                }
            }
        });
        Button mDialogOK = mDialog.findViewById(R.id.btn_ok);
        if (confirmColor != 0) {
            mDialogOK.setTextColor(con.getResources().getColor(confirmColor));
        }
        if (!TextUtils.isEmpty(confirmBtnPrompt)) {
            mDialogOK.setText(confirmBtnPrompt);
        }
        mDialogOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (handleConfirm != null) {
                    handleConfirm.handle();
                }

            }
        });

        if(con != null  && !((Activity)con).isFinishing()) {
            dialog.show();
        }

    }








    /**
     * 弹出选择框
     */
    public static void showLogoutDialog(final Activity context, final HandleDialogListener handleDialog) {


        View view = context.getLayoutInflater().inflate(R.layout.dialog_logout, null);
        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.dialog_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = Common.SCREEN_HEIGHT;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnLoginOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (handleDialog != null){
                    handleDialog.handle();
                }

                dialog.dismiss();
            }
        });
    
    }

}
