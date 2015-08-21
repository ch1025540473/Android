package com.wezebra.zebraking.behavior;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.HttpClientImp;
import com.wezebra.zebraking.http.HttpsClient;
import com.wezebra.zebraking.model.OnePhotoUploadComponent;
import com.wezebra.zebraking.model.PhotoUploadComponent;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.GenericUtils;
import com.wezebra.zebraking.util.LocationUtil;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.widget.LoadingBar;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by HQDev on 2015/5/21.
 */
public class UploadPictureTask {

    private BaseActivity activity;
    private String userId;
    private List<NameValuePair> postParameters;

    private HttpsClient httpClient;
    private HttpPost request;
    private HttpResponse httpResponse;
    private UrlEncodedFormEntity formEntity;

    private volatile static UploadPictureTask uniqueInstance;

    private UploadPictureTask() {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, Constants.PICTURE_UPLOAD_CONNECTION_TIME_OUT);
        HttpConnectionParams.setSoTimeout(httpParams, Constants.PICTURE_UPLOAD_SO_TIME_OUT);
        try {
            this.httpClient = new HttpsClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UploadPictureTask getUniqueInstance(@NonNull BaseActivity activity,@NonNull List<NameValuePair> postParameters) {

        if (uniqueInstance == null) {
            synchronized (UploadPictureTask.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new UploadPictureTask();
                }
            }
        } else {
        }
        uniqueInstance.activity = activity;
        uniqueInstance.userId = PreferenceUtils.getUserId()+"";
        uniqueInstance.postParameters = postParameters;

        return uniqueInstance;

    }

    public void setRequest(String url) {

        uniqueInstance.request = new HttpPost(url);
        Log.i("uniqueInstance.request",uniqueInstance.request.toString());
        Log.i("uniqueInstance.userId",uniqueInstance.userId);
        uniqueInstance.request.addHeader("uid", uniqueInstance.userId);

        StringBuilder stringBuilder = new StringBuilder();
        if (uniqueInstance.postParameters != null) {
            Iterator<NameValuePair> iterator = uniqueInstance.postParameters.iterator();
            NameValuePair nameValuePair = null;
            while (iterator.hasNext()) {
                nameValuePair = iterator.next();
                try {
                    stringBuilder = stringBuilder.append(nameValuePair.getName()+"="+ URLEncoder.encode(nameValuePair.getValue(),HTTP.UTF_8)+"&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length()-1);

            try {
                uniqueInstance.formEntity = new UrlEncodedFormEntity(uniqueInstance.postParameters, HTTP.UTF_8);
                Log.i("formEntity",uniqueInstance.formEntity.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            uniqueInstance.request.setEntity(uniqueInstance.formEntity);
        } else {
            //Do Nothing
        }

       // Log.i("requestStr",stringBuilder.toString());
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

        Log.i("token",token.toString());
        uniqueInstance.request.addHeader("token",token.toString());

    }
//提交图片
    public void submitIdentityAuthPictures(final OnePhotoUploadComponent[] pictures, final Intent intent) {
        //开启了一个异步任务
        AsyncTaskWithLoadingBar asyncTask  = new AsyncTaskWithLoadingBar() {

            //private String[] uri;

            private BufferedReader in;
            private int state;

            @Override
            protected String doInBackground(String... params) {

//                uri = new String[pictures.length];
//                for (int i=0;i<uri.length;i++) {
//                    uri[i] = "";
//                }
                for (int i=0;i<pictures.length;i++) {
                    if ( (pictures[i].photoPath != null) && (!pictures[i].photoPath.equals("")) ) {
                        try {
                            pictures[i].uri = HttpClientImp.INSTANCE.postFileForString(Constants.FILE_UPLOAD_URL,
                                    GenericUtils.getBytesFromBitmap(
                                            GenericUtils.compressBitmap(
                                                    pictures[i].photoPath, Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH)));
                            JSONObject jsonObject = new JSONObject(pictures[i].uri);
                            if ( jsonObject.optInt("code",0) != 2000 ) {
                                return "2999";
                            }
                        //    Log.i("JsonObject1",jsonObject.toString());
                            jsonObject = jsonObject.getJSONObject("data");
                        //    Log.i("JsonObject2",jsonObject.toString());
                            pictures[i].uri = jsonObject.getString("url");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return "2999";
                        }
                    }
                }

                uniqueInstance.postParameters.add(new BasicNameValuePair("url1", pictures[0].uri));
                uniqueInstance.postParameters.add(new BasicNameValuePair("url2", pictures[1].uri));
                uniqueInstance.postParameters.add(new BasicNameValuePair("url3", pictures[2].uri));
                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(uniqueInstance.activity);
               // Log.i("lng", location.get(0).getValue());
               // Log.i("lat", location.get(1).getValue());
                uniqueInstance.postParameters.add(new BasicNameValuePair("channel", "android"));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lng", location.get(0).getValue()));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lat",location.get(1).getValue()));
                uniqueInstance.setRequest(Constants._URL);

                try {
                    httpResponse = httpClient.execute(request);
                } catch (IOException e) {
                    if (e instanceof ConnectTimeoutException)
                      //  Log.i("Connection Time Out",".");
                    e.printStackTrace();
                }
                //得到执行的结果
                StringBuffer string = new StringBuffer("");
                try {
                    in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                    String lineStr = "";
                    while ((lineStr = in.readLine()) != null) {
                        string.append(lineStr + "\n");
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("httpResult",string.toString());

                try {
                    JSONObject jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (Integer.parseInt(s) == 2000) {
                    uniqueInstance.activity.startActivity(intent);
                } else {
                    Toast.makeText(uniqueInstance.activity,uniqueInstance.activity.getString(R.string.upload_file_errors),Toast.LENGTH_LONG).show();
                }
            }
        };

        asyncTask.execute();

    }

    public void submitPictureOfOneGroup(final PhotoUploadComponent photoUploadComponent, final Intent intent) {

        AsyncTaskWithLoadingBar asyncTask = new AsyncTaskWithLoadingBar() {

            //private String[] uri;

            private BufferedReader in;
            private int state;

            protected String doInBackground(String... params) {

                //uri = new String[photoUploadComponent.count];
                for (int i=0;i<photoUploadComponent.count;i++) {
                    if (photoUploadComponent.photoPath[i] != null) {

                        try {
                            photoUploadComponent.uri[i] = HttpClientImp.INSTANCE.postFileForString(Constants.FILE_UPLOAD_URL, GenericUtils.getBytesFromBitmap(GenericUtils.compressBitmap(photoUploadComponent.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH)));
                            Log.i("UploadPicSize", GenericUtils.compressBitmap(photoUploadComponent.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH).getByteCount() + "");
                            Log.i("UploadPicSize2",GenericUtils.getBytesFromBitmap(GenericUtils.compressBitmap(photoUploadComponent.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH)).length+"");
                            Log.i("BitMapInfo-Height",GenericUtils.compressBitmap(photoUploadComponent.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH).getHeight()+"");
                            JSONObject jsonObject = new JSONObject(photoUploadComponent.uri[i]);
                            if ( jsonObject.optInt("code",0) != 2000 ) {
                                return "2999";
                            }
                            Log.i("JsonObject1",jsonObject.toString());
                            jsonObject = jsonObject.getJSONObject("data");
                            Log.i("JsonObject2",jsonObject.toString());
                            photoUploadComponent.uri[i] = jsonObject.getString("url");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return "2999";
                        }
                        Log.i("PostFileResult",photoUploadComponent.uri[i]);

                    }
                }
                Log.i("Uri[]",photoUploadComponent.uri.toString());

                StringBuilder builder = new StringBuilder("");
                for (int i=0;i<photoUploadComponent.uri.length;i++) {
                    if (photoUploadComponent.uri[i] != null) {
                        builder.append(photoUploadComponent.uri[i]+",");
                    }
                }
                builder.deleteCharAt(builder.length() - 1);
                Log.i("UriString", builder.toString());

                uniqueInstance.postParameters.add(new BasicNameValuePair("url", builder.toString()));
                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(uniqueInstance.activity);
                Log.i("lng", location.get(0).getValue());
                Log.i("lat", location.get(1).getValue());
                uniqueInstance.postParameters.add(new BasicNameValuePair("channel", "android"));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lng", location.get(0).getValue()));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lat",location.get(1).getValue()));
                uniqueInstance.setRequest(Constants._URL);

                try {
                    httpResponse = httpClient.execute(request);
                } catch (IOException e) {
                    if (e instanceof ConnectTimeoutException)
                        Log.i("Connection Time Out",".");
                    e.printStackTrace();
                }

                StringBuffer string = new StringBuffer("");
                try {
                    in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                    String lineStr = "";
                    while ((lineStr = in.readLine()) != null) {
                        string.append(lineStr + "\n");
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("httpResult",string.toString());

                try {
                    JSONObject jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (Integer.parseInt(s) == 2000) {
                    uniqueInstance.activity.startActivity(intent);

                } else {
                    Toast.makeText(uniqueInstance.activity,uniqueInstance.activity.getString(R.string.upload_file_errors),Toast.LENGTH_LONG).show();
                }
            }
        };

        asyncTask.execute();

    }

    public void submitPictureOfTwoGroup(final ArrayList<String> pathsOne, final PhotoUploadComponent photoUploadComponent_second, final Intent intent) {

        AsyncTaskWithLoadingBar asyncTask = new AsyncTaskWithLoadingBar() {

            //private String[] uri1,uri2;

            private BufferedReader in;
            private int state;

            private StringBuilder builder1 = new StringBuilder("");
            private StringBuilder builder2 = new StringBuilder("");

            protected String doInBackground(String... params) {

                if (pathsOne.size() > 0) {
                    //uri1 = new String[photoUploadComponent_first.count];
                    String[] uri1 = new String[pathsOne.size()];
                    for (int i=0;i<pathsOne.size();i++) {
                        if ( (pathsOne.get(i) != null)&&(!pathsOne.get(i).equals("camera_default"))&&(!pathsOne.get(i).equals("null")) ) {

                            if (!pathsOne.get(i).contains("http")) {
                                try {
                                    uri1[i] = HttpClientImp.INSTANCE.postFileForString(Constants.FILE_UPLOAD_URL, GenericUtils.getBytesFromBitmap(GenericUtils.compressBitmap(pathsOne.get(i), Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH)));
                                    Log.i("UploadPicSize", GenericUtils.compressBitmap(pathsOne.get(i), Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH).getByteCount() + "");
                                    Log.i("UploadPicSize2",GenericUtils.getBytesFromBitmap(GenericUtils.compressBitmap(pathsOne.get(i), Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH)).length+"");
                                    Log.i("BitMapInfo-Height",GenericUtils.compressBitmap(pathsOne.get(i), Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH).getHeight()+"");
                                    JSONObject jsonObject = new JSONObject(uri1[i]);
                                    if ( jsonObject.optInt("code",0) != 2000 ) {
                                        return "2999";
                                    }
                                    Log.i("JsonObject1",jsonObject.toString());
                                    jsonObject = jsonObject.getJSONObject("data");
                                    Log.i("JsonObject2",jsonObject.toString());
                                    uri1[i] = jsonObject.getString("url");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return "2999";
                                }
                            } else {
                                uri1[i] = pathsOne.get(i);
                            }

                            Log.i("PostFileResult",uri1[i]);

                        } else {
                            uri1[i] = null;
                        }
                    }
                    Log.i("Uri1[]", uri1.toString());

                    builder1 = new StringBuilder("");
                    for (int i=0;i<uri1.length;i++) {
                        if (uri1[i] != null) {
                            builder1.append(uri1[i]+",");
                        }
                    }

                    if ( builder1.length() > 1 ) {
                        builder1.deleteCharAt(builder1.length() - 1);
                    }
                    Log.i("UriString", builder1.toString());

                }

                if (photoUploadComponent_second.count > 0) {
                    //uri2 = new String[photoUploadComponent_second.count];
                    for (int i=0;i<photoUploadComponent_second.count;i++) {
                        if (photoUploadComponent_second.photoPath[i] != null) {

                            try {
                                photoUploadComponent_second.uri[i] = HttpClientImp.INSTANCE.postFileForString(Constants.FILE_UPLOAD_URL, GenericUtils.getBytesFromBitmap(GenericUtils.compressBitmap(photoUploadComponent_second.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH)));
                                Log.i("UploadPicSize", GenericUtils.compressBitmap(photoUploadComponent_second.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH).getByteCount() + "");
                                Log.i("UploadPicSize2",GenericUtils.getBytesFromBitmap(GenericUtils.compressBitmap(photoUploadComponent_second.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH)).length+"");
                                Log.i("BitMapInfo-Height",GenericUtils.compressBitmap(photoUploadComponent_second.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH).getHeight()+"");
                                JSONObject jsonObject = new JSONObject(photoUploadComponent_second.uri[i]);
                                if ( jsonObject.optInt("code",0) != 2000 ) {
                                    return "2999";
                                }
                                Log.i("JsonObject1",jsonObject.toString());
                                jsonObject = jsonObject.getJSONObject("data");
                                Log.i("JsonObject2",jsonObject.toString());
                                photoUploadComponent_second.uri[i] = jsonObject.getString("url");
                            } catch (Exception e) {
                                e.printStackTrace();
                                return "2999";
                            }
                            Log.i("PostFileResult",photoUploadComponent_second.uri[i]);

                        }
                    }
                    Log.i("Uri2[]", photoUploadComponent_second.uri.toString());

                    builder2 = new StringBuilder("");
                    for (int i=0;i<photoUploadComponent_second.uri.length;i++) {
                        if (photoUploadComponent_second.uri[i] != null) {
                            builder2.append(photoUploadComponent_second.uri[i]+",");
                        }
                    }

                    if ( builder2.length() > 1 ) {
                        builder2.deleteCharAt(builder2.length() - 1);
                    }
                    Log.i("UriString", builder2.toString());

                }

                uniqueInstance.postParameters.add(new BasicNameValuePair("contract", builder1.toString()));
                uniqueInstance.postParameters.add(new BasicNameValuePair("others", builder2.toString()));
                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(uniqueInstance.activity);
                Log.i("lng", location.get(0).getValue());
                Log.i("lat", location.get(1).getValue());
                uniqueInstance.postParameters.add(new BasicNameValuePair("channel", "android"));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lng", location.get(0).getValue()));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lat",location.get(1).getValue()));
                uniqueInstance.setRequest(Constants._URL);

                try {
                    httpResponse = httpClient.execute(request);
                } catch (IOException e) {
                    if (e instanceof ConnectTimeoutException)
                        Log.i("Connection Time Out",".");
                    e.printStackTrace();
                }

                StringBuffer string = new StringBuffer("");
                try {
                    in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                    String lineStr = "";
                    while ((lineStr = in.readLine()) != null) {
                        string.append(lineStr + "\n");
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("httpResult",string.toString());

                try {
                    JSONObject jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (Integer.parseInt(s) == 2000) {

                    if (uniqueInstance.activity == null) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        App.getAppContext().startActivity(intent);
                    } else {
                        uniqueInstance.activity.startActivity(intent);
                    }

                } else {
                    Toast.makeText(uniqueInstance.activity,uniqueInstance.activity.getString(R.string.upload_file_errors),Toast.LENGTH_LONG).show();
                }
            }
        };

        asyncTask.execute();

    }

    public void submitJobInfo(final PhotoUploadComponent photoUploadComponent, final Intent intent) {

        AsyncTaskWithLoadingBar asyncTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;

            protected String doInBackground(String... params) {

                for (int i=0;i<photoUploadComponent.count;i++) {
                    if (photoUploadComponent.photoPath[i] != null) {

                        try {
                            photoUploadComponent.uri[i] = HttpClientImp.INSTANCE.postFileForString(Constants.FILE_UPLOAD_URL, GenericUtils.getBytesFromBitmap(GenericUtils.compressBitmap(photoUploadComponent.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH)));
                            Log.i("UploadPicSize", GenericUtils.compressBitmap(photoUploadComponent.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH).getByteCount() + "");
                            Log.i("UploadPicSize2",GenericUtils.getBytesFromBitmap(GenericUtils.compressBitmap(photoUploadComponent.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH)).length+"");
                            Log.i("BitMapInfo-Height",GenericUtils.compressBitmap(photoUploadComponent.photoPath[i], Constants.BITMAP_SCALE_MIN_HEIGHT, Constants.BITMAP_SCALE_MIN_WIDTH).getHeight()+"");
                            JSONObject jsonObject = new JSONObject(photoUploadComponent.uri[i]);
                            if ( jsonObject.optInt("code",0) != 2000 ) {
                                return "2999";
                            }
                            Log.i("JsonObject1",jsonObject.toString());
                            jsonObject = jsonObject.getJSONObject("data");
                            Log.i("JsonObject2",jsonObject.toString());
                            photoUploadComponent.uri[i] = jsonObject.getString("url");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return "2999";
                        }
                        Log.i("PostFileResult",photoUploadComponent.uri[i]);

                    }
                }
                Log.i("Uri[]",photoUploadComponent.uri.toString());

                StringBuilder builder = new StringBuilder("");
                for (int i=0;i<photoUploadComponent.uri.length;i++) {
                    if (photoUploadComponent.uri[i] != null) {
                        builder.append(photoUploadComponent.uri[i]+",");
                    }
                }
                builder.deleteCharAt(builder.length() - 1);
                Log.i("UriString", builder.toString());

                uniqueInstance.postParameters.add(new BasicNameValuePair("url", builder.toString()));
                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(uniqueInstance.activity);
                Log.i("lng", location.get(0).getValue());
                Log.i("lat", location.get(1).getValue());
                uniqueInstance.postParameters.add(new BasicNameValuePair("channel", "android"));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lng", location.get(0).getValue()));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lat",location.get(1).getValue()));
                uniqueInstance.setRequest(Constants._URL);

                try {
                    httpResponse = httpClient.execute(request);
                } catch (IOException e) {
                    if (e instanceof ConnectTimeoutException)
                        Log.i("Connection Time Out",".");
                    e.printStackTrace();
                }

                StringBuffer string = new StringBuffer("");
                try {
                    in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                    String lineStr = "";
                    while ((lineStr = in.readLine()) != null) {
                        string.append(lineStr + "\n");
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("httpResult",string.toString());

                try {
                    JSONObject jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (Integer.parseInt(s) == 2000) {
                    uniqueInstance.activity.startActivity(intent);
                    PreferenceUtils.setFourStatus(1);//用于四项认证做标记
                } else {
                    Toast.makeText(uniqueInstance.activity,uniqueInstance.activity.getString(R.string.upload_file_errors),Toast.LENGTH_LONG).show();
                }
            }
        };

        asyncTask.execute();

    }

    abstract class AsyncTaskWithLoadingBar extends AsyncTask<String,Integer,String> {

        private LoadingBar loadingBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (loadingBar == null)
            {
                loadingBar = new LoadingBar(uniqueInstance.activity);
                loadingBar.setMessage("图片上传中，请耐心等待...");
            }
            loadingBar.setCancelable(false);
            loadingBar.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (loadingBar != null && loadingBar.isShowing())
            {
                loadingBar.dismiss();
            }
        }

    }

}