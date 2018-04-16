package com.jiedaoqian.android.utils.httputils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jiedaoqian.android.BuildConfig;
import com.jiedaoqian.android.utils.Common;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zenghui on 2017/7/6.
 */

public class HttpUtil {

    public static final String SUCCESS = "0";

    public static String BASE_URL = "http://47.92.150.132:8000";//"http://stg.fushuninsurance.com/insurance-guide/";//"https://api.fushuninsurance.com/insurance-guide/";
    public static final String BASE_URL_RELEASE = "https://api.fushuninsurance.com/insurance-guide/";
    public static final String BASE_URL_TEST = "http://47.92.150.132:8000/rest/param/tags/";
    private static final long DEFAULT_TIMEOUT = 60;
    private HttpApi httpApi;
    public static String ut = "";
    private static HttpUtil INSTANCE = new HttpUtil();//懒汉模式

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
        INSTANCE = new HttpUtil();
    }

    private HttpUtil(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("User-Agent", Common.REQUEST_PARAM_USER_AGENT)
                                .addHeader("ver", BuildConfig.VERSION_NAME)
                                .addHeader("jdqtk", ut)
                                .addHeader("content-type", "application/json;charset=UTF-8")
                                .build();

                        Response originalResponse = chain.proceed(request);
                        return originalResponse;
                    }

                });

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        httpApi = retrofit.create(HttpApi.class);

    }

    //获取单例
    public static HttpUtil getInstance(){
        return HttpUtil.INSTANCE;
    }

    public HttpApi getHttpApi() {
        return httpApi;
    }
}
