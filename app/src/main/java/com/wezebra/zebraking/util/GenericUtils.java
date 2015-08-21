package com.wezebra.zebraking.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * User: superalex
 * Date: 2015/1/15
 * Time: 10:51
 */
public class GenericUtils extends Constants
{
    private static final double EARTH_RADIUS = 6378137;// 赤道半径(单位m)

    public static DatePickerDialog createDialogWithoutDateField(Context context, DatePickerDialog.OnDateSetListener listener, int year, int month)
    {

        DatePickerDialog dpd = new DatePickerDialog(context, listener, year, month, 1);
        try
        {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields)
            {
                if (datePickerDialogField.getName().equals("mDatePicker"))
                {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields)
                    {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName()))
                        {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }

            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return dpd;
    }

    /**
     * * Method for Setting the Height of the ListView dynamically.
     * *** Hack to fix the issue of not showing all the items of the ListView
     * *** when placed inside a ScrollView  ***
     */
    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static int getListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return 0;

        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            view = listAdapter.getView(i, null, listView);
            view.setLayoutParams(new AbsListView.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

    /*public static String getBillStatusString(Context context, int billStatus, int orderStatus)
    {
        String str = "";

        switch (orderStatus)
        {
            case _AUDIT_WAIT_SUBMIT:
                str = "等待上传";
                break;
            case _AUDIT_AUDITING:
                str = "处理中";
                break;
            case _AUDIT_PASSED:
                switch (billStatus)
                {
                    case _BILL_WAIT_PAY:
                        str = context.getString(R.string.wait_pay);
                        break;
                    case _BILL_PAYED:
                        str = context.getString(R.string.payed);
                        break;
                }
                break;
            case _AUDIT_UN_PASS:
                str = "关闭";
                break;
            case _ORDER_DONE:
                str = "已完成";
                break;
        }


//        switch (billStatus)
//        {
//            case _BILL_WAIT_PAY:
//                str = context.getString(R.string.wait_pay);
//                break;
//            case _BILL_PAYED:
//                str = context.getString(R.string.payed);
//                break;
//        }

        return str;
    }

    public static String getAuditStatusString(Context context, int status)
    {
        String str = "";
        switch (status)
        {
            case _AUDIT_WAIT_SUBMIT:
                str = context.getString(R.string.audit_wait_submit);
                break;
            case _AUDIT_AUDITING:
                str = context.getString(R.string.audit_auditing);
                break;
            case _AUDIT_PASSED:
                str = context.getString(R.string.audit_passed);
                break;
            case _AUDIT_UN_PASS:
                str = context.getString(R.string.audit_un_pass);
                break;
        }

        return str;
    }

    public static String getOrderStatusString(Context context, int status)
    {
        String str = "";
        switch (status)
        {
            case _AUDIT_WAIT_SUBMIT:
                str = "等待上传材料";
                break;
            case _AUDIT_AUDITING:
                str = "处理中";
                break;
            case _AUDIT_PASSED:
                str = "还款中";
                break;
            case _AUDIT_UN_PASS:
                str = "关闭";
                break;
            case _ORDER_DONE:
                str = "已完成";
                break;
        }

        return str;
    }

    public static String getOrderTypeString(Context context, int type)
    {
        String str = "";
        switch (type)
        {
            case _HOUSE_STAGE:
                str = context.getString(R.string.house_stage);
                break;
            case _TEL_FEE:
                str = context.getString(R.string.tel_pee);
                break;
        }

        return str;
    }

    public static double formatDouble(double d)
    {
        BigDecimal bd = new BigDecimal(d);

        return bd.setScale(2, RoundingMode.UP).doubleValue();
    }

    public static String getStringFromStream(InputStream inputStream)
    {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try
        {
            while ((line = bufferedReader.readLine()) != null)
            {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }*/

    /**
     * @param value   字体的变量大小 sp为单位
     * @param context 上下文环境
     * @return sp为单位的px的大小
     */
    public static int sp2px(float value, Context context)
    {
        Resources r;
        if (context == null)
        {
            r = Resources.getSystem();
        } else
        {
            r = context.getResources();
        }
        float spvalue = value * r.getDisplayMetrics().scaledDensity;
        return (int) (spvalue + 0.5f);
    }

    /**
     * @param value   像素dp转化为px
     * @param context 上下文环境
     * @return px的像素值大小
     */
    public static float dp2px(float value, Context context)
    {
        Resources r;
        if (context == null)
        {
            r = Resources.getSystem();
        } else
        {
            r = context.getResources();
        }
        return value * r.getDisplayMetrics().density;
    }

    /**
     * 将 Bitmap 转成字节数组
     *
     * @param bm
     *            Bitmap
     * @return 字节数据
     */
    public static byte[] getBytesFromBitmap(Bitmap bm)
    {
        if (bm == null)
        {
            return null;
        }

//        Bitmap newbm;
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        int max = Math.max(width, height);
//        float scale = (float) 500 / max;

//        /**
//         * 设置最大边尺寸不超过500
//         */
//        if (scale < 1)
//        {
//            Matrix matrix = new Matrix();
//            matrix.setScale(scale, scale);
//            newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
//        }
//        else
//        {
//            newbm = bm;
//        }

        /**
         * 获取字节数组
         */
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, Constants.PICTURE_COMPRESS, output);
        byte[] bytes = output.toByteArray();



        try
        {
            output.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return bytes;
    }

    public static boolean isOpenNetwork(Context context)
    {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null)
        {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }

    public static void hideSoftKeyboard(Activity activity)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void hideSoftKeyboard(Activity activity, View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Activity activity)
    {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles)
    {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
        {
            return false;
        } else
        {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email)
    {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard)
    {
        if (TextUtils.isEmpty(idCard))
        {
            return false;
        } else
        {
            String regex = "(\\d{14}[0-9xX])|\\d{17}[0-9xX]";
            return Pattern.matches(regex, idCard);
        }
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile)
    {
        String regex = "(\\+\\d+)?1[3-9]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *              数字之后是空格分隔的国家（地区）代码。</p>
     *              <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     *              <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone)
    {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit)
    {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals)
    {
        String regex = "\\-?\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBlankSpace(String blankSpace)
    {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese)
    {
        String regex = "^[\u4E00-\u9FFF]+$";
        return Pattern.matches(regex, chinese);
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBirthday(String birthday)
    {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkURL(String url)
    {
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        return Pattern.matches(regex, url);
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPostcode(String postcode)
    {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIpAddress(String ipAddress)
    {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }

    public static ProgressDialog showProgressDialog(Context context, ProgressDialog dialog, String msg)
    {
        return showProgressDialog(context, dialog, msg, false);
    }

    public static ProgressDialog showProgressDialog(Context context, ProgressDialog dialog, String msg, boolean cancelable)
    {
        if (dialog == null)
        {
            dialog = new ProgressDialog(context);
        }
        dialog.setCancelable(cancelable);
        dialog.setMessage(msg);
        dialog.show();
        return dialog;
    }

    public static void hideProgressDialog(ProgressDialog dialog)
    {
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }

    /**
     * Gets the corresponding path to a file from the given content:// URI
     *
     * @param selectedVideoUri The content:// URI to find the file path from
     * @param contentResolver  The content resolver to use to perform the query.
     * @return the file path as a string
     */
    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver)
    {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//	    也可用下面的方法拿到cursor
//	    Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    /**
     * 显示文件的大小 单位 KB MB GB
     *
     * @param fileLen 文件的长度
     * @return 有单位 KB MB GB的文件大小字符串
     */
    public static String showFileSize(int fileLen)
    {
        long fileSizeLong = 0;
        if (fileLen < 1024)
        {
            fileSizeLong = fileLen;
            return "" + fileSizeLong;
        } else if (fileLen < 1048576)
        {
            fileSizeLong = Long.valueOf((long) fileLen / 1024);
            return "" + fileSizeLong + " KB";
        } else if (fileLen < 1073741824)
        {
            fileSizeLong = Long.valueOf((long) fileLen / 1048576);
            return "" + fileSizeLong + " MB";
        } else
        {
            fileSizeLong = Long.valueOf((long) fileLen / 1073741824);
            return "" + fileSizeLong + " GB";
        }
    }

    public static Bitmap decodeSampleImage(File f, int width, int height)
    {
        try
        {
            System.gc(); // First of all free some memory

            // Decode image size

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to

            final int requiredWidth = width;
            final int requiredHeight = height;

            // Find the scale value (as a power of 2)

            int sampleScaleSize = 10;

            while (o.outWidth / sampleScaleSize / 2 >= requiredWidth && o.outHeight / sampleScaleSize / 2 >= requiredHeight)
                sampleScaleSize *= 2;

            // Decode with inSampleSize

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = sampleScaleSize;

            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (Exception e)
        {
            Log.d("GenericUtils", e.getMessage()); // We don't want the application to just throw an exception
        }

        return null;
    }

    /**
     * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下
     *
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位m
     */
    public static double GetDistance(double lon1, double lat1, double lon2,
                                     double lat2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        // s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 转化为弧度(rad)
     */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static String getStringFromStream(InputStream inputStream)
    {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try
        {
            while ((line = bufferedReader.readLine()) != null)
            {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 验证密码
     *
     * @param password 密码，格式：6位及以上数字和字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPassword(String password)
    {
        String regex = "^[A-Za-z\\d]{6,}$";
        return Pattern.matches(regex, password);
    }

    public static Bitmap compressBitmap(String srcPath,float height,float width) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath,newOpts);

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //设置高和宽
        float hh = height;
        float ww = width;
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        return bitmap;//压缩好比例大小后再进行质量压缩

    }

}
