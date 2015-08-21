package com.wezebra.zebraking.http;

import android.util.Log;

import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by HQDev on 2015/6/16.
 */
public class HttpClient2 {

    private volatile static HttpClient2 uniqueInstance;

    private static CloseableHttpClient httpClient;

    public static HttpClient2 getInstance() {

        if (uniqueInstance == null) {
            synchronized (HttpClient2.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new HttpClient2();
                }
            }
        }

        return uniqueInstance;

    }

    private HttpClient2() {
        createHttpClient();
    }

    private static void createHttpClient() {

        X509TrustManager tm = new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[]  chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,  String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        try {

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{tm}, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.BEST_MATCH)
                    .setExpectContinueEnabled(true)
                    .setStaleConnectionCheckEnabled(true)
                    .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                    .setSocketTimeout(Constants.SOCKET_TIME_OUT)
                    .setConnectTimeout(Constants.CONNECTION_TIME_OUT)
                    .setConnectionRequestTimeout(Constants.CONNECTION_TIME_OUT)
                    .build();

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", socketFactory).build();

            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            httpClient = HttpClients.custom().setConnectionManager(connManager).setDefaultRequestConfig(defaultRequestConfig).build();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }

    private HttpPost setPostRequest(String url,String uid,List<NameValuePair> postParameters) {

        HttpPost post = new HttpPost(url);
        if (uid != null) {
            post.addHeader("uid",uid);
        }

        StringBuilder stringBuilder = new StringBuilder();
        if (postParameters != null) {
            Iterator<NameValuePair> iterator = postParameters.iterator();
            NameValuePair nameValuePair = null;
            while (iterator.hasNext()) {
                nameValuePair = iterator.next();
                try {
                    stringBuilder = stringBuilder.append(nameValuePair.getName()+"="+ URLEncoder.encode(nameValuePair.getValue(), "UTF-8")+"&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters, HTTP.UTF_8);
                Log.i("formEntity", formEntity.toString());
                post.setEntity(formEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            //Do Nothing
        }

        Log.i("requestStr",stringBuilder.toString());
        byte[] digestResult = null;
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String msg = stringBuilder.toString() + PreferenceUtils.getKey();
        Log.i("msg",msg);
        messageDigest.update(msg.getBytes());
        digestResult = messageDigest.digest();

        StringBuilder token =new StringBuilder(digestResult.length<<1);
        for(int i=0;i<digestResult.length; i++) {
            token.append(Character.forDigit((digestResult[i]>>4)&0xf,16));
            token.append(Character.forDigit(digestResult[i]&0xf,16));
        }

        Log.i("token", token.toString());
        post.addHeader("token",token.toString());
        Log.i("Post Content",post.toString());

        return post;

    }

    public String post(List<NameValuePair> postParameters) {

        HttpPost post = setPostRequest(Constants._URL,PreferenceUtils.getUserId()+"",postParameters);

        StringBuffer string = new StringBuffer("");
        try {

            HttpResponse httpResponse = httpClient.execute(post);
            BufferedReader in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            String lineStr = "";
            while ((lineStr = in.readLine()) != null) {
                string.append(lineStr + "\n");
            }
            in.close();
            Log.i("httpResult", string.toString());

        } catch (IOException e) {
            if (e instanceof ConnectTimeoutException)
                Log.i("Connection Time Out",".");
            e.printStackTrace();
        }

        return string.toString();

    }

}
