package com.wezebra.zebraking.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.wezebra.zebraking.http.data.ActiveApplyData;

/**
 * Created by superalex on 2015/1/7.
 */
public class PreferenceUtils
{
    public static final String ZEBRA = "zebra";
    public static final String VERIFIED = "verified";
    public static final String VERIFY_TIME = "verify_time";
    public static final String PHONE_NUM = "phone_num";
    public static final String USER_ID = "user_id";
    public static final String PATTERN = "pattern";
    public static final String IS_FIRST_OPEN = "is_first_open";
    public static final String KEY = "key";
    public static final String ROLE = "role";
    public static final String IS_PUSH_OPEN = "is_push_open";
    public static final String IS_LOCK_RESUME = "is_lock_resume";
    public static final String DEVICE_UPDATE_TIME = "device_update_time";
    public static final String ACTIVE_ORDER_CODE = "active_order_code";
    public static final String ACTIVE_STATUS = "active_status";
    public static final String ACTIVE_STATE_MSG = "active_state_msg";
    public static final String ACTIVE_SERVER_ACCESSED = "active_server_accessed";
    public static final String ACTIVE_IS_ONLINE = "active_is_online";
    public static final String ACTIVE_STEP = "active_step";
    public static final String OUT_TRADE_NO = "out_trade_no";
    public static final String PAY_AMOUNT = "pay_amount";
    public static final String PAY_ORDER_CODE = "pay_order_code";
    public static final String PAY_Type = "pay_type";

    public static final String USER_NAME = "user_name";
    public static final String ID_CARD = "id_card";
    public static final String EDUCATIONAL = "educational";
    public static final String GRADUATION = "graduation"; //毕业
    public static final String JOB_STATUS = "job_status";
    public static final String MONTH_INCOME = "month_income";
    public static final String MONTH_RENT = "month_rent";
    public static final String MONTH_RENT_WAY = "month_rent_way";
    public static final String FOUR_STATUS = "four_status";


    public static void setFourStatus(int value)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putInt(FOUR_STATUS,value);
        editor.commit();
    }

    public static int getFourStatus() {
        return sInstance.getInt(FOUR_STATUS,0);
    }

    private static SharedPreferences sInstance;

//    private static SharedPreferences mPreferences;

    public static void create(final Context context)
    {
        if (sInstance == null)
        {
            sInstance = context.getSharedPreferences(ZEBRA, Context.MODE_PRIVATE);
        }
//        if (mPreferences == null)
//        {
//            mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        }
    }

    public static boolean deleteAll()
    {
        return sInstance.edit().clear().commit();
    }

    public static void setPhoneNum(String phone)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putString(PHONE_NUM, phone);
        editor.commit();
    }

    public static void setVerified(boolean verified)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putBoolean(VERIFIED, verified);
        editor.commit();
    }

    public static void setUserId(long userId)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putLong(USER_ID, userId);
        editor.commit();
    }

    public static void setPattern(String pattern)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putString(PATTERN, pattern);
        editor.commit();
    }

    public static void setIsFirstOpen(boolean isFirstOpen)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putBoolean(IS_FIRST_OPEN, isFirstOpen);
        editor.commit();
    }

    public static void setKey(String key)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putString(KEY, key);
        editor.commit();
    }

    public static void setRole(int role)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putInt(ROLE, role);
        editor.commit();
    }

    public static void setIsPushOpen(boolean isPushOpen)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putBoolean(IS_PUSH_OPEN, isPushOpen);
        editor.commit();
    }

    public static void setIsLockResume(boolean isLockResume)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putBoolean(IS_LOCK_RESUME, isLockResume);
        editor.commit();
    }

    public static void setDeviceUpdateTime(long time)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putLong(DEVICE_UPDATE_TIME, time);
        editor.commit();
    }

    public static void setOutTradeNo(String outTradeNo)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putString(OUT_TRADE_NO, outTradeNo);
        editor.commit();
    }

    public static void setActiveApplyData(ActiveApplyData activeApply)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putLong(ACTIVE_ORDER_CODE, activeApply.getOrderCode())
                .putInt(ACTIVE_STATUS, activeApply.getStatus())
                .putString(ACTIVE_STATE_MSG, activeApply.getStatemsg())
                .putInt(ACTIVE_STEP, activeApply.getStep())
                .putString(ACTIVE_IS_ONLINE, activeApply.getIsOnLine());
        editor.commit();
    }

    public static void setPayAmount(String amount)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putString(PAY_AMOUNT, amount);
        editor.commit();
    }

    public static void setPayOrderCode(long code)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putLong(PAY_ORDER_CODE, code);
        editor.commit();
    }

    public static void setPayType(int type)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putInt(PAY_Type, type);
        editor.commit();
    }

    public static String getPhoneNum()
    {
        return sInstance.getString(PHONE_NUM, "");
    }

    public static boolean getVerified()
    {
        return sInstance.getBoolean(VERIFIED, false);
    }

    public static long getUserId()
    {
        return sInstance.getLong(USER_ID, 0);
    }

    public static String getPattern()
    {
        return sInstance.getString(PATTERN, "");
    }

    public static boolean getIsFirstOpen()
    {
        return sInstance.getBoolean(IS_FIRST_OPEN, true);
    }

    public static String getKey()
    {
        return sInstance.getString(KEY, "");
    }

    public static int getRole()
    {
        return sInstance.getInt(ROLE, 0);
    }

    public static boolean getIsPushOpen()
    {
        return sInstance.getBoolean(IS_PUSH_OPEN, true);
    }

    public static long getDeviceUpdateTime()
    {
        return sInstance.getLong(DEVICE_UPDATE_TIME, 0);
    }

    public static long getActiveOrderCode()
    {
        return sInstance.getLong(ACTIVE_ORDER_CODE, -1);
    }

    public static int getActiveStatus()
    {
        return sInstance.getInt(ACTIVE_STATUS, 0);
    }

    public static String getActiveStateMsg()
    {
        return sInstance.getString(ACTIVE_STATE_MSG, "");
    }

    public static int getActiveStep()
    {
        return sInstance.getInt(ACTIVE_STEP, 0);
    }

    public static String getActiveIsOnline()
    {
        return sInstance.getString(ACTIVE_IS_ONLINE, "");
    }

    public static String getOutTradeNo()
    {
        return sInstance.getString(OUT_TRADE_NO, "");
    }

    public static ActiveApplyData getActiveAppayData()
    {
        long orderCode = getActiveOrderCode();
        if (orderCode < 0)
        {
            return null;
        }

        ActiveApplyData activeApplyData = new ActiveApplyData();
        activeApplyData.setOrderCode(orderCode);
        activeApplyData.setUserId(getUserId());
        activeApplyData.setStatus(getActiveStatus());
        activeApplyData.setStatemsg(getActiveStateMsg());
        activeApplyData.setStep(getActiveStep());
        activeApplyData.setIsOnLine(getActiveIsOnline());

        return activeApplyData;
    }

    public static String getPayAmount()
    {
        return sInstance.getString(PAY_AMOUNT, "");
    }

    public static long getPayOrderCode()
    {
        return sInstance.getLong(PAY_ORDER_CODE, 0);
    }

    public static int getPayType()
    {
        return sInstance.getInt(PAY_Type, 0);
    }
    public static void setUserName(String userName){
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName()
    {
        return sInstance.getString(USER_NAME, "");
    }

    public static void setIdCard(String idCard){
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putString(ID_CARD, idCard);
        editor.commit();
    }

    public static String getIdCard()
    {
        return sInstance.getString(ID_CARD, "");
    }

    public static void setEducational(int educational){
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putInt(EDUCATIONAL, educational);
        editor.commit();
    }

    public static int getEducational()
    {
        return sInstance.getInt(EDUCATIONAL, 1);
    }

    public static void setGraduation(int graduation){
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putInt(GRADUATION, graduation);
        editor.commit();
    }

    public static int getGraduation()
    {
        return sInstance.getInt(GRADUATION, 2);
    }

    public static void setJobStatus(int jobStatus){
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putInt(JOB_STATUS, jobStatus);
        editor.commit();
    }

    public static int getJobStatus()
    {
        return sInstance.getInt(JOB_STATUS, 1);
    }

    public static void setMonthIncome(int monthIncome){
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putInt(MONTH_INCOME, monthIncome);
        editor.commit();
    }

    public static int getMonthIncome()
    {
        return sInstance.getInt(MONTH_INCOME, 0);
    }

    public static void setMonthRent(String monthRent){
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putString(MONTH_RENT, monthRent);
        editor.commit();
    }

    public static String getMonthRent()
    {
        return sInstance.getString(MONTH_RENT, "");
    }

    public static int getMonthRentWay() {
        return sInstance.getInt(MONTH_RENT_WAY,3);
    }

    public static void setMonthRentWay(int rentWay)
    {
        SharedPreferences.Editor editor = sInstance.edit();
        editor.putInt(MONTH_RENT_WAY, rentWay);
        editor.commit();
    }
}
