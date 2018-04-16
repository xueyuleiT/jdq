package com.jiedaoqian.android.activitys;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jiedaoqian.android.R;
import com.jiedaoqian.android.interfaces.JavaScriptInterface;
import com.jiedaoqian.android.utils.Common;

/**
 * Created by zenghui on 2018/3/18.
 */

public class WebviewActivity extends BaseActivity{
    private WebView webView;
    private ProgressBar progressBar;
    public String linkUrl;
    @Override
    public void initViews() {
        setContentView(R.layout.activity_webview);
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webView);

        ImageView imgClose = findViewById(R.id.imgClose);
        imgClose.setVisibility(View.VISIBLE);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void initDatas() {
        String title = getIntent().getStringExtra(Common.TITLE);
        String link = getIntent().getStringExtra(Common.LINK);


        setToobar((Toolbar) findViewById(R.id.toolbar),title);

        webSetting(webView);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")){
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse(url);
                        intent.setData(data);
                        startActivity(intent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });

        linkUrl = link;

        webView.loadUrl(linkUrl);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (webView != null && webView.canGoBack()){
                    webView.goBack();
                }else {
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    JavaScriptInterface javaScriptInterface;
    public void webSetting(final WebView webView){
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        javaScriptInterface = new JavaScriptInterface(webView.getContext());
        webView.addJavascriptInterface(javaScriptInterface,"android");
        webView.getSettings().setUserAgentString(Common.REQUEST_PARAM_USER_AGENT+"-webview");

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }


        webView.getSettings().setLightTouchEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        webView.getSettings().setBlockNetworkImage(false);

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setFocusable(true);
        webView.setClickable(true);
        webView.setHapticFeedbackEnabled(true);
        webView.setFocusableInTouchMode(true);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        webView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        webView.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webView.getSettings().setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题

    }
}
