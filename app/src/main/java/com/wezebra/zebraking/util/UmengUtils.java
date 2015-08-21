package com.wezebra.zebraking.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 俊杰 on 2015/4/14.
 */
public class UmengUtils
{
    public static final String ASSESS_SUBMIT = "assess_submit";
    public static final String BASE_INFO_SUBMIT = "base_info_submit";
    public static final String LANDLORD_INFO_SUBMIT = "landlord_info_submit";

    public static void assessSubmit(Context context, String edu, String graduation, String job, String income, String rent, String howToPay, String budget)
    {
        Map<String, String> map = new HashMap<>();
        map.put("education", edu);
        map.put("graduation", graduation);
        map.put("jobStatus", job);
        map.put("income", income);
        map.put("rent", rent);
        map.put("howToPay", howToPay);
        map.put("budget", budget);
        MobclickAgent.onEvent(context, ASSESS_SUBMIT, map);
    }


    public static void baseInfoSubmit(Context context, String edu, String graduation, String job, String income, String marriage)
    {
        Map<String, String> map = new HashMap<>();
        map.put("education", edu);
        map.put("graduation", graduation);
        map.put("jobStatus", job);
        map.put("income", income);
        map.put("marriage", marriage);
        MobclickAgent.onEvent(context, BASE_INFO_SUBMIT, map);
    }

    public static void landlordInfoSubmit(Context context, String rent, String howToPay)
    {
        Map<String, String> map = new HashMap<>();
        map.put("rent", rent);
        map.put("howToPay", howToPay);
        MobclickAgent.onEvent(context, LANDLORD_INFO_SUBMIT, map);
    }
}
