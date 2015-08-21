package com.wezebra.zebraking.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wezebra.zebraking.R;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by 俊杰 on 2015/4/8.
 */
public class Utils
{
    public static String formatFloat(float f)
    {
        BigDecimal bd = new BigDecimal(f);
        return String.format("%.2f", bd.setScale(2, BigDecimal.ROUND_UP).floatValue());
    }

    public static Dialog getDialog(Context context, String title, String msg)
    {
        final Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_asset_tip);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView mTitle = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView mContent = (TextView) dialog.findViewById(R.id.dialog_content);
        Button ok = (Button) dialog.findViewById(R.id.dialog_ok);

        mTitle.setText(title);
        mContent.setText(msg);
        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    public static String formatName(String name)
    {
        String result = "";
        if (!TextUtils.isEmpty(name) && name.length() >= 2)
        {
            result = formatStars(name, 1, name.length());
        }

        return result;
    }

    public static String formatID(String ID)
    {
        String result = "";
        if (!TextUtils.isEmpty(ID) && ID.length() == 18)
        {
            result = formatStars(ID, 3, 14);
        } else if (!TextUtils.isEmpty(ID) && ID.length() == 15)
        {
            result = formatStars(ID, 3, 11);
        }

        return result;
    }

    public static String formatMobile(String mobile)
    {
        String result = "";
        if (!TextUtils.isEmpty(mobile) && mobile.length() >= 11)
        {
            int start = mobile.length() - 8;
            int end = start + 4;
            result = formatStars(mobile, start, end);
        }

        return result;
    }

    public static String formatStars(String source, int start, int end)
    {
//        String repalce = source.substring(start, end);
//        String stars = "";
//        for (int i = 0; i < end - start; i++)
//        {
//            stars += "*";
//        }
//
//        return source.replace(repalce, stars);

        String result = "";
        for (int i = 0; i < source.length(); i++)
        {
            if (i >= start && i < end)
            {
                result += "*";
            } else
            {
                result += source.charAt(i);
            }

        }

        return result;
    }

    /**
     * 获取图片缓存路径
     * @param context
     * @return
     */
    public static File getDiskCacheDir(Context context){
        String cachepath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()){
            cachepath = context.getExternalCacheDir().getPath();
        }else {
            cachepath = context.getCacheDir().getPath();
        }
        return new File(cachepath+File.separator);
    }
}
