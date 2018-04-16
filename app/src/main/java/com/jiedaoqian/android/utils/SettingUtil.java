package com.jiedaoqian.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by zenghui on 2017/8/3.
 */

public class SettingUtil {
    private static final Method APPLY_METHOD = findApplyMethod();

    public static boolean contains(Context context, int resId) {
        return contains(context, context.getString(resId));
    }

    public static boolean contains(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.contains(key);
    }

    public static void remove(Context context, int resId) {
        remove(context, context.getString(resId));
    }

    public static void remove(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.remove(key);
        commitOrApply(editor);
    }

    public static void set(Context context, int resId, boolean value) {
        set(context, context.getString(resId), value);
    }

    public static void set(Context context, String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        commitOrApply(editor);
    }

    public static void set(Context context, int resId, float value) {
        set(context, context.getString(resId), value);
    }

    public static void set(Context context, String key, float value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putFloat(key, value);
        commitOrApply(editor);
    }

    public static void set(Context context, int resId, int value) {
        set(context, context.getString(resId), value);
    }

    public static void set(Context context, String key, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putInt(key, value);
        commitOrApply(editor);
    }

    public static void set(Context context, int resId, long value) {
        set(context, context.getString(resId), value);
    }

    public static void set(Context context, String key, long value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.putLong(key, value);
        commitOrApply(editor);
    }

    public static void clear(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        SettingUtil.clear(context, Config.LOGIN_INFO);
    }

    public static void set(Context context, int resId, String value) {
        set(context, context.getString(resId), value);
    }

    public static void set(Context context, String key, String value) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Editor editor = prefs.edit();
            editor.putString(key, value);
            commitOrApply(editor);
        }catch (Exception e){
            Log.e("SettingsUtils","ERROR : " +e.getMessage());
        }
    }

    public static boolean get(Context context, int resId, boolean defValue) {
        return get(context, context.getString(resId), defValue);
    }

    public static boolean get(Context context, String key, boolean defValue) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getBoolean(key, defValue);
        }catch (Exception e){
            return false;
        }
    }

    public static float get(Context context, int resId, float defValue) {
        return get(context, context.getString(resId), defValue);
    }

    public static float get(Context context, String key, float defValue) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getFloat(key, defValue);
        }catch (Exception e){
            return 0;
        }
    }

    public static int get(Context context, int resId, int defValue) {
        return get(context, context.getString(resId), defValue);
    }

    public static int get(Context context, String key, int defValue) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getInt(key, defValue);
        }catch (Exception e){
            return 0;
        }
    }



    public static long get(Context context, int resId, long defValue) {
        return get(context, context.getString(resId), defValue);
    }

    public static long get(Context context, String key, long defValue) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getLong(key, defValue);
        }catch (Exception e){
            return 0;
        }
    }

    public static String get(Context context, int resId, String defValue) {
        return get(context, context.getString(resId), defValue);
    }

    public static String get(Context context, String key, String defValue) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            return prefs.getString(key, defValue);
        }catch (Exception e){
            return "";
        }
    }

    //////////////////////////////////////////////////////////////////////////
    // Apply method via reflection

    public static Editor getEditor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    private static Method findApplyMethod() {
        try {
            Class<Editor> cls = Editor.class;
            return cls.getMethod("apply");
        } catch (NoSuchMethodException unused) {
            return null;
        }
    }

    public static void commitOrApply(Editor editor) {
        if (APPLY_METHOD != null) {
            try {
                APPLY_METHOD.invoke(editor);
                return;
            } catch (InvocationTargetException e) {
            } catch (IllegalAccessException e) {
            }
        }

        editor.commit();
    }


    public static void clear( Context context,String spName){
        SharedPreferences preferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 把对象保存到以String形式保存到sp中
     * @param context 上下文
     * @param t 泛型参数
     * @param spName sp文件名
     * @param keyName 字段名
     * @param
     */
    public static void saveObj2SP(Context context, Serializable t, String spName, String keyName) {

        SharedPreferences preferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        ByteArrayOutputStream bos;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            byte[] bytes = bos.toByteArray();
            String ObjStr = Base64.encodeToString(bytes, Base64.DEFAULT);
            Editor editor = preferences.edit();
            editor.putString(keyName, ObjStr);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        //关闭流
            if (oos != null) {
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }



    /**
     *从sp中读取对象
     * @param context
     * @param spName sp文件名
     * @param keyNme 字段名
     * @param <T>    泛型参数
     * @return
     */
    public static <T extends Object> T getObjFromSp(Context context, String spName, String keyNme) {
        SharedPreferences preferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        byte[] bytes = Base64.decode(preferences.getString(keyNme, ""), Base64.DEFAULT);
        ByteArrayInputStream bis;
        ObjectInputStream ois = null;
        T obj = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
