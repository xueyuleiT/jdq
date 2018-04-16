package com.jiedaoqian.android.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zenghui on 2017/7/27.
 */

public class CheckUtil {
    public static String PHONE_PATTERN ="^[1][3,4,7,5,8][0-9]{9}$";
    /**
     * 校验电话号码
     *
     * @param phone
     * @return
     */
    public static boolean isPhoneValid(String phone) {

        if (TextUtils.isEmpty(phone)){
            return false;
        }

        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean checkName(String name){
        String regx = "^[\\*\\u4E00-\\u9FA5]{2,8}(?:[·•]{1}[\\u4E00-\\u9FA5]{2,10})*$";
        return name.matches(regx);
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){

        if (TextUtils.isEmpty(email)){
            return false;
        }

        boolean flag = false;
        try{
            String check = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}";//"^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }


    /**
     * 校验银行卡卡号
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        if (TextUtils.isEmpty(cardId)){
            return false;
        }

        if (cardId.length() < 16){
            return false;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if(bit == 'N'){
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }




    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId){
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }

    private final static String PWD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";

    public static int checkPwd(String pwd){

        if (TextUtils.isEmpty(pwd)){
            return 0;
        }

        if (pwd.length() > 18 || pwd.length() < 6){
            return 1;
        }

        Pattern pattern = Pattern.compile(PWD_PATTERN);
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches()?3:2;
    }

}
