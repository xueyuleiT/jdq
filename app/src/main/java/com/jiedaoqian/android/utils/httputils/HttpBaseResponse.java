package com.jiedaoqian.android.utils.httputils;

/**
 * Created by zenghui on 2017/7/6.
 */

public class HttpBaseResponse<T> {

    boolean login = false;
    String  code;
    T data;
    String message;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public  T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
