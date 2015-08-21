package com.wezebra.zebraking.http;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.PreferenceUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 俊杰 on 2015/5/12.
 */
public class ZebraRequest extends Request<TaskResponse>
{
    public static final String TAG = "ZebraRequest";
//    public static final String SCHEMA_HTTP = "http://";
//    public static final String SCHEMA_HTTPS = "https://";
//    public static final String HOST = SCHEMA_HTTPS + "121.40.78.61:9070"; // "192.168.1.159:8080";"121.40.78.61:9070";"m.wezebra.com";
//    public static final String URL_QUESTION = HOST +"/zf/app/help.do"; // "/king/app/help.do"; "/zf/app/help.do"
//
//    public static final String URL = HOST + "/zf/api/data"; // "/king/api/data"; "/zf/api/data";
    private final Response.Listener<TaskResponse> mListener;
    private final Class[] classes;
    private final Map<String, String> params;
    private final String api;
//    private long startTime;

    public ZebraRequest(int method, Response.Listener<TaskResponse> listener,
                        Response.ErrorListener errorListener, Map<String, String> params, Class... clazz)
    {
        super(method, Constants._URL, errorListener);
        mListener = listener;
        classes = clazz;
        this.params = params;

        if (!params.containsKey("api") || TextUtils.isEmpty(params.get("api")))
        {
            throw new RuntimeException("api cannot be empty");
        }
        api = params.get("api");
//        startTime = System.currentTimeMillis();
    }

    public ZebraRequest(Response.Listener<TaskResponse> listener, Response.ErrorListener errorListener,
                        Map<String, String> params, Class... clazz)
    {
        this(Method.POST, listener, errorListener, params, clazz);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        if (api.equals("login") || api.equals("getSmsCode") || api.equals("signin") || api.equals("updatepwd"))
        {
            return super.getHeaders();
        } else
        {
            Map<String, String> headers = new HashMap<>();
            headers.put("uid", PreferenceUtils.getUserId() + "");
            L.d(TAG, "uid: " + PreferenceUtils.getUserId());
            headers.put("token", generateToken());
            return headers;
        }
    }

    private String generateToken()
    {
        StringBuilder reqStr = new StringBuilder();
        String key = PreferenceUtils.getKey();
        if (key.length() == 0)
        {
            throw new RuntimeException("don't have secure key");
        }

        if (params != null && params.size() > 0)
        {
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                try
                {
                    reqStr.append(entry.getKey());
                    reqStr.append("=");
                    reqStr.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    reqStr.append("&");
                } catch (UnsupportedEncodingException e)
                {
                    throw new RuntimeException("Encoding not supported: ", e);
                }
            }
        }

        reqStr.deleteCharAt(reqStr.length() - 1);

        return SHA1(reqStr.toString() + key);
    }

    public static String SHA1(String inStr)
    {
        MessageDigest md;
        String outStr = null;
        try
        {
            md = MessageDigest.getInstance("SHA-1");     //选择SHA-1，也可以选择MD5
            byte[] digest = md.digest(inStr.getBytes());       //返回的是byet[]，要转化为String存储比较方便
            outStr = bytetoString(digest);
        } catch (NoSuchAlgorithmException nsae)
        {
            nsae.printStackTrace();
        }
        return outStr;
    }


    public static String bytetoString(byte[] digest)
    {
        StringBuilder token = new StringBuilder(digest.length << 1);
        for (int i = 0; i < digest.length; i++)
        {
            token.append(Character.forDigit((digest[i] >> 4) & 0xf, 16));
            token.append(Character.forDigit(digest[i] & 0xf, 16));
        }

        return token.toString();
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError
    {
        return params;
    }

    @Override
    protected Response<TaskResponse> parseNetworkResponse(NetworkResponse response)
    {
        String parsed;
        TaskResponse taskResponse = null;

        try
        {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e)
        {
            parsed = new String(response.data);
        }
          L.d("ZebraRequest parsed --> " + api, parsed);
        try
        {
            if (classes == null || classes.length == 0)
            {
                taskResponse = JacksonUtils.getInstance().readValue(parsed, TaskResponse.class);
            } else if (classes.length == 1)
            {
                taskResponse = JacksonUtils.readJson(parsed, TaskResponse.class, classes[0]);
            } else if (classes.length == 2)
            {
                taskResponse = JacksonUtils.readListJson(parsed, TaskResponse.class, classes[0], classes[1]);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

//        long elapsedTime = System.currentTimeMillis() - startTime;
//        L.d("ZebraRequest parsed --> " + api, (elapsedTime / 1000f) + "秒");
        return Response.success(taskResponse, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(TaskResponse response)
    {
        mListener.onResponse(response);
    }
}
