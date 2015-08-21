package com.wezebra.zebraking.behavior;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.HttpsClient;
import com.wezebra.zebraking.model.City;
import com.wezebra.zebraking.model.Payee;
import com.wezebra.zebraking.model.User;
import com.wezebra.zebraking.model.UserLogin;
import com.wezebra.zebraking.orm.DBManager;
import com.wezebra.zebraking.ui.installment.AssessActivity;
import com.wezebra.zebraking.ui.installment.ContactsAuthActivity;
import com.wezebra.zebraking.ui.installment.EducationAuthActivity;
import com.wezebra.zebraking.ui.installment.HouseDataAuthActivity;
import com.wezebra.zebraking.ui.installment.IdentificationActivity;
import com.wezebra.zebraking.ui.installment.IncomeAuthActivity;
import com.wezebra.zebraking.ui.installment.JobInfoActivity;
import com.wezebra.zebraking.ui.installment.LandlordAuthActivity;
import com.wezebra.zebraking.ui.installment.PersonalProfileActivity;
import com.wezebra.zebraking.ui.installment.PhoneAuthStepFourActivity;
import com.wezebra.zebraking.ui.installment.PhoneAuthStepThreeActivity;
import com.wezebra.zebraking.ui.installment.PhoneAuthStepTwoActivity;
import com.wezebra.zebraking.ui.installment.PreCalculationActivity;
import com.wezebra.zebraking.ui.installment.PublicAccFundsAuthActivity;
import com.wezebra.zebraking.ui.installment.SIAStepOneResultActivity;
import com.wezebra.zebraking.ui.installment.SocialSecurityAuthActivity;
import com.wezebra.zebraking.ui.installment.SubmitInstallmentApplicationStepOneActivity;
import com.wezebra.zebraking.ui.installment.SubmitInstallmentApplicationStepThreeActivity;
import com.wezebra.zebraking.ui.installment.SubmitInstallmentApplicationStepTwoActivity;
import com.wezebra.zebraking.ui.login.PhoneNumberActivity;
import com.wezebra.zebraking.util.Base64Digest;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by HQDev on 2015/5/13.
 */
public class HttpAsyncTask {

    private ActionBarActivity activity;
    private String userId;
    private List<NameValuePair> postParameters;

    private HttpsClient httpClient;
    private HttpPost request;
    private HttpResponse httpResponse;
    private UrlEncodedFormEntity formEntity;

    private volatile static HttpAsyncTask uniqueInstance;

    private HttpAsyncTask(ActionBarActivity activity, String userId, List<NameValuePair> postParameters) {
        this.activity = activity;
        this.userId = userId;
        this.postParameters = postParameters;

        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, Constants.CONNECTION_TIME_OUT);
        HttpConnectionParams.setSoTimeout(httpParams, Constants.SOCKET_TIME_OUT);
        try {
            this.httpClient = new HttpsClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpAsyncTask getInstance(@NonNull ActionBarActivity activity,String userId,@NonNull List<NameValuePair> postParameters) {

        if (uniqueInstance == null) {
            synchronized (HttpAsyncTask.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new HttpAsyncTask(activity,userId,postParameters);
                }
            }
        } else {
            uniqueInstance.activity = activity;
            uniqueInstance.userId = userId;
            uniqueInstance.postParameters = postParameters;
        }
        uniqueInstance.setRequest(Constants._URL);
        return uniqueInstance;

    }

    public void setHttpParams(BasicHttpParams httpParams) {
        try {
            uniqueInstance.httpClient = new HttpsClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    stringBuilder = stringBuilder.append(nameValuePair.getName()+"="+URLEncoder.encode(nameValuePair.getValue(),"UTF-8")+"&");
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

        Log.i("token",token.toString());
        uniqueInstance.request.addHeader("token",token.toString());

    }

    public void post(final Intent intent) {

        AsyncTaskWithLoadingBar postAsyncTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;

            @Override
            protected String doInBackground(String... params) {

                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(uniqueInstance.activity);
                Log.i("lng",location.get(0).getValue());
                Log.i("lat", location.get(1).getValue());
                uniqueInstance.postParameters.add(new BasicNameValuePair("channel", "android"));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lng",location.get(0).getValue()));
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

                }catch(JSONException e) {
                    e.printStackTrace();
                }

                return state+"";

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    if (intent != null) {
                        Boolean doFinish = intent.getExtras().getBoolean("doFinish",false);
                        uniqueInstance.activity.startActivity(intent);
                        if (doFinish == true) {
                            uniqueInstance.activity.finish();
                        }
                    } else {
                        Toast.makeText(uniqueInstance.activity, uniqueInstance.activity.getString(R.string.http_post_errors), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(uniqueInstance.activity, uniqueInstance.activity.getString(R.string.http_request_failure), Toast.LENGTH_LONG).show();
                }

            }
        };

        postAsyncTask.execute();

    }

    public void postWithMultiEntrance(final Intent intent) {

        AsyncTaskWithLoadingBar postAsyncTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;

            @Override
            protected String doInBackground(String... params) {

                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(uniqueInstance.activity);
                Log.i("lng",location.get(0).getValue());
                Log.i("lat", location.get(1).getValue());
                uniqueInstance.postParameters.add(new BasicNameValuePair("channel", "android"));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lng",location.get(0).getValue()));
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

                }catch(JSONException e) {
                    e.printStackTrace();
                }

                return state + "";

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    if (intent != null) {
                        Boolean doFinish = intent.getExtras().getBoolean("doFinish", false);

                        SharedPreferences entrance_flag = uniqueInstance.activity.getSharedPreferences(Constants.ENTRANCE_FLAG, Activity.MODE_PRIVATE);
                        int entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG,Constants.DEFAULT_ENTRANCE);
                        switch (entrance) {
                            case Constants.IS_FROM_APPLICATION:
                                intent.setClass(uniqueInstance.activity,SubmitInstallmentApplicationStepOneActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                uniqueInstance.activity.startActivity(intent);
                                break;
                            case Constants.IS_FROM_PROFILE:
                                intent.setClass(uniqueInstance.activity,PersonalProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                uniqueInstance.activity.startActivity(intent);
                                break;
                            case Constants.IS_FROM_STEP_TWO:
                                intent.setClass(uniqueInstance.activity,SubmitInstallmentApplicationStepTwoActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                uniqueInstance.activity.startActivity(intent);
                                break;
                            case Constants.IS_FROM_SIA_ONE:
                                intent.setClass(uniqueInstance.activity, SIAStepOneResultActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                uniqueInstance.activity.startActivity(intent);
                                break;
                        }

                        if (doFinish == true) {
                            uniqueInstance.activity.finish();
                        }
                    } else {
                        //Toast.makeText(uniqueInstance.activity, uniqueInstance.activity.getString(R.string.http_post_errors), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(uniqueInstance.activity, uniqueInstance.activity.getString(R.string.http_request_failure), Toast.LENGTH_LONG).show();
                }

            }
        };

        postAsyncTask.execute();

    }

    public void assessPost(final Intent intent) {

        AsyncTaskWithLoadingBar assessPostTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;

            @Override
            protected String doInBackground(String... params) {

                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(uniqueInstance.activity);
                Log.i("lng",location.get(0).getValue());
                Log.i("lat", location.get(1).getValue());
                uniqueInstance.postParameters.add(new BasicNameValuePair("channel", "android"));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lng",location.get(0).getValue()));
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

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("JsonObject",jsonObject.toString());
                        Log.i("JsonObject_length",jsonObject.length()+"");
                        ((AssessActivity)uniqueInstance.activity).qualificationStatus = jsonObject.optInt("qualificationStatus",99);
                    }

                }catch(JSONException e) {
                    e.printStackTrace();
                }

                return state+"";

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {
                    switch ( ((AssessActivity)uniqueInstance.activity).qualificationStatus ) {
                        case 99:
                            intent.putExtra("qualificationStatus",99);
                            uniqueInstance.activity.startActivity(intent);
                            uniqueInstance.activity.finish();
                            break;
                        case 100:
                            intent.putExtra("qualificationStatus",100);
                            uniqueInstance.activity.startActivity(intent);
                            uniqueInstance.activity.finish();
                            break;
                    }
                }

            }
        };

        assessPostTask.execute();

    }

    public void mobilePhoneAuthPostStepOne(final Intent intent) {

        AsyncTaskWithLoadingBar postAsyncTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;

            @Override
            protected String doInBackground(String... params) {

                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(uniqueInstance.activity);
                Log.i("lng",location.get(0).getValue());
                Log.i("lat", location.get(1).getValue());
                uniqueInstance.postParameters.add(new BasicNameValuePair("channel", "android"));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lng",location.get(0).getValue()));
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

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("JsonObject",jsonObject.toString());
                        Log.i("JsonObject_length",jsonObject.length()+"");
                        if (jsonObject.length() > 0) {

                            ((App)uniqueInstance.activity.getApplication()).mobilePhoneAuth.setIsNeed(jsonObject.optBoolean("isNeed",false));
                            if ( ((App)uniqueInstance.activity.getApplication()).mobilePhoneAuth.getIsNeed() == true ) {
                                ((App)uniqueInstance.activity.getApplication()).mobilePhoneAuth.setUrl(jsonObject.optString("url",""));
                            }
                            ((App)uniqueInstance.activity.getApplication()).mobilePhoneAuth.setSource(jsonObject.optString("source",""));

                            return state+"";
                        } else {
                            return state+"";
                        }
                    }

                }catch(JSONException e) {
                    e.printStackTrace();
                }

                return state+"";

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    if (intent != null) {
                        Boolean doFinish = intent.getExtras().getBoolean("doFinish",false);
                        uniqueInstance.activity.startActivity(intent);
                        if (doFinish == true) {
                            uniqueInstance.activity.finish();
                        }
                    } else {
                        Toast.makeText(uniqueInstance.activity, uniqueInstance.activity.getString(R.string.http_post_errors), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(uniqueInstance.activity, uniqueInstance.activity.getString(R.string.http_request_failure), Toast.LENGTH_LONG).show();
                }

            }
        };

        postAsyncTask.execute();

    }

    public void mobilePhoneAuthPostStepTwo(final Intent intent) {

        AsyncTaskWithLoadingBar postAsyncTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in,in3,in4;
            private boolean isNext;
            private boolean isAgain;
            private int state,state3,state4;

            @Override
            protected String doInBackground(String... params) {

                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(uniqueInstance.activity);
                Log.i("lng",location.get(0).getValue());
                Log.i("lat", location.get(1).getValue());
                uniqueInstance.postParameters.add(new BasicNameValuePair("channel", "android"));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lng",location.get(0).getValue()));
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

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("JsonObject",jsonObject.toString());
                        Log.i("JsonObject_length",jsonObject.length()+"");

                        switch (state) {
                            case 2000:
                                if (jsonObject.length() > 0) {

                                    isNext = jsonObject.optBoolean("isNextStep",false);

                                    if (isNext == true) {

                                        ((App)uniqueInstance.activity.getApplication()).mobilePhoneAuth.setNextStep(jsonObject.optInt("nextStep", 0));
                                        switch ( ((App)uniqueInstance.activity.getApplication()).mobilePhoneAuth.getNextStep() ) {

                                            case 0:
                                                return "finish";
                                            case 3:
                                                return "Step3";
                                            case 4:
                                                return "Step4";

                                        }

                                    } else {
                                        return "finish";
                                    }

                                    return "OK";
                                } else {
                                    return "Err";
                                }
                            case 2999:
                                if (jsonObject.length() > 0) {

                                    isAgain = jsonObject.optBoolean("isAgain",false);
                                    ((App)uniqueInstance.activity.getApplication()).mobilePhoneAuth.setUrl(jsonObject.optString("url",""));
                                    Log.i("state","2999");

                                    return "OK";
                                } else {
                                    return "Err";
                                }
                        }
                    }

                }catch(JSONException e) {
                    e.printStackTrace();
                }

                return "Err";

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.i("onPost state=", state + "");
                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    if (s.equals("finish")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(uniqueInstance.activity);
                        builder.setMessage("提交成功");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                SharedPreferences entrance_flag = uniqueInstance.activity.getSharedPreferences(Constants.ENTRANCE_FLAG, Activity.MODE_PRIVATE);
                                int entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG, Constants.DEFAULT_ENTRANCE);
                                Intent i = new Intent();

                                switch (entrance) {
                                    case Constants.IS_FROM_SIA_ONE:
                                        i.setClass(uniqueInstance.activity, SIAStepOneResultActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        break;
                                    case Constants.IS_FROM_PROFILE:
                                        i.setClass(uniqueInstance.activity, PersonalProfileActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        break;
                                }

                                uniqueInstance.activity.startActivity(i);
                            }
                        });

                        builder.create().show();
                        return;

                    } else if (s.equals("Step3")) {
                        Intent intent = new Intent();
                        intent.setClass(uniqueInstance.activity, PhoneAuthStepThreeActivity.class);
                        uniqueInstance.activity.startActivity(intent);
                    } else if (s.equals("Step4")) {
                        Intent intent = new Intent();
                        intent.setClass(uniqueInstance.activity,PhoneAuthStepFourActivity.class);
                        uniqueInstance.activity.startActivity(intent);
                    }

                    if (intent != null) {

                        Boolean doFinish = intent.getExtras().getBoolean("doFinish",false);
                        //uniqueInstance.activity.startActivity(intent);
                        if (doFinish == true) {
                            uniqueInstance.activity.finish();
                        }
                    } else {
                        Toast.makeText(uniqueInstance.activity, uniqueInstance.activity.getString(R.string.http_post_errors), Toast.LENGTH_LONG).show();
                    }

                } else if (state == 2999) {

                    Intent i = new Intent();
                    if (isAgain == false) {

//                        i.setClass(uniqueInstance.activity, PhoneAuthStepOneActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        uniqueInstance.activity.startActivity(i);

                        Toast.makeText(uniqueInstance.activity,"服务密码错误",Toast.LENGTH_LONG).show();
                        List<NameValuePair> postParameters = new ArrayList<>();
                        postParameters.add(new BasicNameValuePair("mobile", App.mobilePhoneAuth.getMobilePhoneNumber()));
                        App.mobilePhoneAuth.setMobilePhoneNumber(App.mobilePhoneAuth.getMobilePhoneNumber());
                        App.mobilePhoneAuth.setIndex(App.mobilePhoneAuth.getIndex() + 1);
                        postParameters.add(new BasicNameValuePair("index", App.mobilePhoneAuth.getIndex() + ""));
                        postParameters.add(new BasicNameValuePair("api", "authPhoneStep1"));
                        HttpAsyncTask httpAsyncTask = HttpAsyncTask.getInstance(uniqueInstance.activity, PreferenceUtils.getUserId()+"", postParameters);

                        Intent intent = new Intent();
                        intent.setClass(uniqueInstance.activity, PhoneAuthStepTwoActivity.class);
                        intent.putExtra("doFinish", false);
                        httpAsyncTask.mobilePhoneAuthPostStepOne(intent);

                    } else {

                        ((PhoneAuthStepTwoActivity)uniqueInstance.activity).verifyCodeLayout.setVisibility(View.VISIBLE);
                        ImageLoader imageloader = ImageLoader.getInstance();
                        imageloader.displayImage( ((App)uniqueInstance.activity.getApplication()).mobilePhoneAuth.getUrl(),((PhoneAuthStepTwoActivity)uniqueInstance.activity).verifyCodeImg );
                        ((PhoneAuthStepTwoActivity) uniqueInstance.activity).verifyCode.requestFocus();
                        ((PhoneAuthStepTwoActivity) uniqueInstance.activity).verifyCode.setError((uniqueInstance.activity).getString(R.string.verity_code_re_input_errors));

                    }

                } else {
                    Toast.makeText(uniqueInstance.activity, uniqueInstance.activity.getString(R.string.http_request_failure), Toast.LENGTH_LONG).show();
                }

            }
        };

        postAsyncTask.execute();

    }

    public void pullBaseInfo(final Intent intent) {

        AsyncTaskWithLoadingBar pullBaseInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;
            private User user;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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
                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.optInt("code", 0);
                    if (state == 0) {
                        return "Network Error";
                    }

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");

                        if ( (jsonObject.get("user") == JSONObject.NULL) || (jsonObject.get("user") == null) ) {
                            user = new User();
                            ((App)uniqueInstance.activity.getApplication()).user = user;
                            return "New User";
                        } else {
                            jsonObject = jsonObject.getJSONObject("user");
                        }

                        Log.i("userObject",jsonObject.toString());
                        Log.i("userObject_length",jsonObject.length()+"");
                        if (jsonObject.length() > 0) {

                            user = new User();
                            user.setUserName(jsonObject.optString("userName", ""));
                            user.setQQ(jsonObject.optString("qQ", ""));
                            user.setIdentity(jsonObject.optString("identity", ""));
                            user.setCityId(jsonObject.optInt("cityId", 0));
                            user.setCityName(jsonObject.optString("cityName", ""));
                            user.setEducation(jsonObject.optInt("education", 0));
                            user.setGraduateSchool(jsonObject.optString("graduateSchool", ""));
                            user.setGraduateTime(jsonObject.optString("graduateTime", ""));
                            user.setJobStatus(jsonObject.optInt("jobStatus", 0));
                            user.setAcademicStatus(jsonObject.optInt("academicStatus", 0));
                            user.setCompany(jsonObject.optString("company", ""));
                            user.setPosition(jsonObject.optString("position", ""));
                            user.setWorkTime(jsonObject.optString("workTime", ""));
                            user.setCompanyScale(jsonObject.optInt("companyScale", 0));
                            user.setHasIncome(jsonObject.optInt("hasIncome", 0));
                            user.setSalary(jsonObject.optInt("salary", 0));
                            user.setSalaryType(jsonObject.optInt("salaryType", 0));
                            user.setHasSecurity(jsonObject.optInt("hasSecurity", 0));
                            user.setHasReserve(jsonObject.optInt("hasReserve", 0));
                            user.setHasCard(jsonObject.optInt("hasCard", 0));
                            user.setCardQuota(jsonObject.optInt("cardQuota", 0));
                            user.setHasOverdue(jsonObject.optInt("hasOverdue", 0));
                            user.setOnEducation(jsonObject.optInt("onEducation", 0));
                            user.setSchoolInTime(jsonObject.optString("schoolInTime", ""));
                            user.setStatusInt(jsonObject.optInt("statusInt", -1));
                            user.setMemo(jsonObject.optString("memo", ""));
                            user.setResidenceAddr(jsonObject.optString("residenceAddr", ""));
                            user.setLifeType(jsonObject.optInt("lifeType", 0));
                            user.setMonthlyPayType(jsonObject.optInt("monthlyPayType", 0));
                            user.setMarriageType(jsonObject.optInt("marriageType",0));

                            ((App)uniqueInstance.activity.getApplication()).user = user;

                            return state+"";

                        } else {
                            user = new User();
                            ((App)uniqueInstance.activity.getApplication()).user = user;
                            return "New User";
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                uniqueInstance.activity.startActivity(intent);
            }
        };

        pullBaseInfoTask.execute();

    }

    public void pullEduInfo() {

        AsyncTaskWithLoadingBar pullEduInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;
            private String[] echoUrl;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");

                        JSONObject education;
                        if (jsonObject.optJSONObject("education") != null) {
                            education = jsonObject.getJSONObject("education");
                            ((EducationAuthActivity)uniqueInstance.activity).account_s = education.optString("chsiName", "");
                            ((EducationAuthActivity)uniqueInstance.activity).password_s = Base64Digest.decode(education.optString("pwdStr", ""));
                            ((EducationAuthActivity)uniqueInstance.activity).status = education.optInt("status", Constants.WAIT_SUBMIT);
                            ((EducationAuthActivity)uniqueInstance.activity).tips_s = education.optString("memo", "");
                        } else {

                        }

                        JSONArray material = jsonObject.getJSONArray("material");
                        Log.i("material.length",material.length()+"");
                        echoUrl = new String[material.length()];
                        Log.i("echoUrl.length",echoUrl.length+"");
                        for (int j=0;j<material.length();j++) {
                            echoUrl[j] = material.getJSONObject(j).getString("materialUrl");
                        }
                        ((EducationAuthActivity)uniqueInstance.activity).echoUrl = echoUrl;
                        if (echoUrl != null) {
                            for (int i=0;i<5;i++) {
                                if (i < echoUrl.length) {
                                    ((EducationAuthActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = echoUrl[i];
                                } else {
                                    ((EducationAuthActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = null;
                                }
                            }
                        }
                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    ((EducationAuthActivity)uniqueInstance.activity).account.setText(((EducationAuthActivity) uniqueInstance.activity).account_s);
                    ((EducationAuthActivity)uniqueInstance.activity).password.setText(((EducationAuthActivity) uniqueInstance.activity).password_s);

                    if ( (echoUrl != null)&&(echoUrl.length > 0) ) {

                        ((EducationAuthActivity)uniqueInstance.activity).choice.check(R.id.choice_two);

                        ((EducationAuthActivity)uniqueInstance.activity).photoUploadComponent.showMore(echoUrl.length - 1);
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        for (int i=0;i<echoUrl.length;i++) {
                            imageLoader.displayImage(echoUrl[i],((EducationAuthActivity)uniqueInstance.activity).photoUploadComponent.photo_img[i]);
                        }

                    }

                    if ( ((EducationAuthActivity)uniqueInstance.activity).status == Constants.UNPASS ) {
                        ((EducationAuthActivity)uniqueInstance.activity).tips.setTextColor(uniqueInstance.activity.getResources().getColor(R.color.text_red));
                        ((EducationAuthActivity)uniqueInstance.activity).tips.setGravity(Gravity.LEFT);
                        ((EducationAuthActivity)uniqueInstance.activity).tips.setText(((EducationAuthActivity) uniqueInstance.activity).tips_s);
                        ((EducationAuthActivity)uniqueInstance.activity).tips.setVisibility(View.VISIBLE);
                    } else {
                        ((EducationAuthActivity)uniqueInstance.activity).tips.setVisibility(View.GONE);
                    }

                }

            }
        };

        pullEduInfoTask.execute();

    }

    public void pullIncomeInfo() {

        AsyncTaskWithLoadingBar pullIncomeInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;
            private String[] echoUrl;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");
                        JSONObject income;
                        if (jsonObject.optJSONObject("income") != null) {
                            income = jsonObject.getJSONObject("income");
                            ((IncomeAuthActivity)uniqueInstance.activity).payDay_s = income.optString("payday", "");
                            if ( ((IncomeAuthActivity)uniqueInstance.activity).payDay_s.equals("null") ) {
                                ((IncomeAuthActivity)uniqueInstance.activity).payDay_s = "";
                            }
                            Log.i("paydays",((IncomeAuthActivity)uniqueInstance.activity).payDay_s);
                            ((IncomeAuthActivity)uniqueInstance.activity).cardNo_s = income.optString("cardStr", "");
                            ((IncomeAuthActivity)uniqueInstance.activity).password_s = Base64Digest.decode(income.optString("queryPassword", ""));

                            ((IncomeAuthActivity)uniqueInstance.activity).status = income.optInt("status", Constants.WAIT_SUBMIT);
                            ((IncomeAuthActivity)uniqueInstance.activity).tips_s = income.optString("memo", "");
                        } else {

                        }
                        JSONArray material = jsonObject.getJSONArray("material");
                        echoUrl = new String[material.length()];
                        Log.i("echoUrl.length",echoUrl.length+"");
                        for (int j=0;j<material.length();j++) {
                            echoUrl[j] = material.getJSONObject(j).getString("materialUrl");
                        }
                        ((IncomeAuthActivity)uniqueInstance.activity).echoUrl = echoUrl;
                        if (echoUrl != null) {
                            for (int i=0;i<5;i++) {
                                if (i < echoUrl.length) {
                                    ((IncomeAuthActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = echoUrl[i];
                                } else {
                                    ((IncomeAuthActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = null;
                                }
                            }
                        }
                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    ((IncomeAuthActivity)uniqueInstance.activity).payDay.setText(((IncomeAuthActivity) uniqueInstance.activity).payDay_s);
                    ((IncomeAuthActivity)uniqueInstance.activity).cardNo.setText(((IncomeAuthActivity) uniqueInstance.activity).cardNo_s);
                    ((IncomeAuthActivity)uniqueInstance.activity).password.setText(((IncomeAuthActivity) uniqueInstance.activity).password_s);

                    if ( (echoUrl != null)&&(echoUrl.length > 0) ) {

                        ((IncomeAuthActivity)uniqueInstance.activity).choice.check(R.id.choice_two);

                        ((IncomeAuthActivity)uniqueInstance.activity).photoUploadComponent.showMore(echoUrl.length - 1);
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        for (int i=0;i<echoUrl.length;i++) {
                            imageLoader.displayImage(echoUrl[i],((IncomeAuthActivity)uniqueInstance.activity).photoUploadComponent.photo_img[i]);
                        }

                    }

                    if ( ((IncomeAuthActivity)uniqueInstance.activity).status == Constants.UNPASS ) {
                        ((IncomeAuthActivity)uniqueInstance.activity).tips.setTextColor(uniqueInstance.activity.getResources().getColor(R.color.text_red));
                        ((IncomeAuthActivity)uniqueInstance.activity).tips.setGravity(Gravity.LEFT);
                        ((IncomeAuthActivity)uniqueInstance.activity).tips.setText(((IncomeAuthActivity) uniqueInstance.activity).tips_s);
                        ((IncomeAuthActivity)uniqueInstance.activity).tips.setVisibility(View.VISIBLE);
                    } else {
                        ((IncomeAuthActivity)uniqueInstance.activity).tips.setVisibility(View.GONE);
                    }

                }

            }
        };

        pullIncomeInfoTask.execute();

    }

    public void pullSocialSecurityInfo() {

        AsyncTaskWithLoadingBar pullSocialSecurityInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;
            private String[] echoUrl;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");
                        JSONObject ass;
                        if (jsonObject.optJSONObject("ass") != null) {
                            ass = jsonObject.getJSONObject("ass");
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).cityId = ass.optInt("cityId", 0);
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).selfPayAmount_i = ass.optInt("paymentMoney", 0);
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).account_s = ass.optString("numStr", "");
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).password_s = Base64Digest.decode(ass.optString("queryPassword", ""));

                            ((SocialSecurityAuthActivity)uniqueInstance.activity).status = ass.optInt("status", Constants.WAIT_SUBMIT);
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).tips_s = ass.optString("memo", "");
                        } else {
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).cityId = 0;
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).selfPayAmount_i = 0;
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).account_s = "";
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).password_s = "";
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).status = Constants.WAIT_SUBMIT;
                        }
                        JSONArray material = jsonObject.getJSONArray("material");
                        echoUrl = new String[material.length()];
                        Log.i("echoUrl.length",echoUrl.length+"");
                        for (int j=0;j<material.length();j++) {
                            echoUrl[j] = material.getJSONObject(j).getString("materialUrl");
                        }
                        ((SocialSecurityAuthActivity)uniqueInstance.activity).echoUrl = echoUrl;
                        if (echoUrl != null) {
                            for (int i=0;i<5;i++) {
                                if (i < echoUrl.length) {
                                    ((SocialSecurityAuthActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = echoUrl[i];
                                } else {
                                    ((SocialSecurityAuthActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = null;
                                }
                            }
                        }
                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    if ( ((SocialSecurityAuthActivity)uniqueInstance.activity).cityId != 0 ) {

                        City city;
                        try {
                            city = DBManager.getInstance().queryCityById(((SocialSecurityAuthActivity)uniqueInstance.activity).cityId);
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).attribution.setText(city.getName());
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).attribution.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                            ((SocialSecurityAuthActivity)uniqueInstance.activity).attribution.setTextColor(uniqueInstance.activity.getResources().getColor(R.color.text_dark));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    ((SocialSecurityAuthActivity) uniqueInstance.activity).selfPayAmount.setSelection(((SocialSecurityAuthActivity) uniqueInstance.activity).selfPayAmount_i);
                    ((SocialSecurityAuthActivity)uniqueInstance.activity).account.setText(((SocialSecurityAuthActivity) uniqueInstance.activity).account_s);
                    ((SocialSecurityAuthActivity)uniqueInstance.activity).password.setText(((SocialSecurityAuthActivity) uniqueInstance.activity).password_s);

                    if ( (echoUrl != null)&&(echoUrl.length > 0) ) {

                        ((SocialSecurityAuthActivity)uniqueInstance.activity).choice.check(R.id.choice_two);

                        ((SocialSecurityAuthActivity)uniqueInstance.activity).photoUploadComponent.showMore(echoUrl.length - 1);
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        for (int i=0;i<echoUrl.length;i++) {
                            imageLoader.displayImage(echoUrl[i],((SocialSecurityAuthActivity)uniqueInstance.activity).photoUploadComponent.photo_img[i]);
                        }

                    }

                    if ( ((SocialSecurityAuthActivity)uniqueInstance.activity).status == Constants.UNPASS ) {
                        ((SocialSecurityAuthActivity)uniqueInstance.activity).tips.setTextColor(uniqueInstance.activity.getResources().getColor(R.color.text_red));
                        ((SocialSecurityAuthActivity)uniqueInstance.activity).tips.setGravity(Gravity.LEFT);
                        ((SocialSecurityAuthActivity)uniqueInstance.activity).tips.setText(((SocialSecurityAuthActivity) uniqueInstance.activity).tips_s);
                        ((SocialSecurityAuthActivity)uniqueInstance.activity).tips.setVisibility(View.VISIBLE);
                    } else {
                        ((SocialSecurityAuthActivity)uniqueInstance.activity).tips.setVisibility(View.GONE);
                    }

                }

            }
        };

        pullSocialSecurityInfoTask.execute();

    }

    public void pullPAFInfo() {

        AsyncTaskWithLoadingBar pullPAFInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;
            private String[] echoUrl;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");
                        JSONObject reserve;
                        if (jsonObject.optJSONObject("reserve") != null) {
                            reserve = jsonObject.getJSONObject("reserve");
                            Log.i("reserve not null","");
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).cityId = reserve.optInt("cityId", 0);
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).selfPayAmount_i = reserve.optInt("paymentMoney", 0);
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).account_s = reserve.optString("numStr", "");
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).password_s = Base64Digest.decode(reserve.optString("queryPassword", ""));

                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).status = reserve.optInt("status", Constants.WAIT_SUBMIT);
                            Log.i("status",((PublicAccFundsAuthActivity)uniqueInstance.activity).status+"");
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).tips_s = reserve.optString("memo", "");
                        } else {
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).cityId = 0;
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).selfPayAmount_i = 0;
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).account_s = "";
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).password_s = "";

                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).status = Constants.WAIT_SUBMIT;
                        }
                        JSONArray material = jsonObject.getJSONArray("material");
                        echoUrl = new String[material.length()];
                        Log.i("echoUrl.length",echoUrl.length+"");
                        for (int j=0;j<material.length();j++) {
                            echoUrl[j] = material.getJSONObject(j).getString("materialUrl");
                        }
                        ((PublicAccFundsAuthActivity)uniqueInstance.activity).echoUrl = echoUrl;
                        if (echoUrl != null) {
                            for (int i=0;i<5;i++) {
                                if (i < echoUrl.length) {
                                    ((PublicAccFundsAuthActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = echoUrl[i];
                                } else {
                                    ((PublicAccFundsAuthActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = null;
                                }
                            }
                        }
                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    if ( ((PublicAccFundsAuthActivity)uniqueInstance.activity).cityId != 0 ) {

                        City city;
                        try {
                            city = DBManager.getInstance().queryCityById(((PublicAccFundsAuthActivity)uniqueInstance.activity).cityId);
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).attribution.setText(city.getName());
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).attribution.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                            ((PublicAccFundsAuthActivity)uniqueInstance.activity).attribution.setTextColor(uniqueInstance.activity.getResources().getColor(R.color.text_dark));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }
                    ((PublicAccFundsAuthActivity) uniqueInstance.activity).selfPayAmount.setSelection( ((PublicAccFundsAuthActivity)uniqueInstance.activity).selfPayAmount_i );
                    ((PublicAccFundsAuthActivity)uniqueInstance.activity).account.setText(((PublicAccFundsAuthActivity) uniqueInstance.activity).account_s);
                    ((PublicAccFundsAuthActivity)uniqueInstance.activity).password.setText(((PublicAccFundsAuthActivity) uniqueInstance.activity).password_s);

                    if ( (echoUrl != null)&&(echoUrl.length > 0) ) {

                        ((PublicAccFundsAuthActivity)uniqueInstance.activity).choice.check(R.id.choice_two);

                        ((PublicAccFundsAuthActivity)uniqueInstance.activity).photoUploadComponent.showMore(echoUrl.length - 1);
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        for (int i=0;i<echoUrl.length;i++) {
                            imageLoader.displayImage(echoUrl[i],((PublicAccFundsAuthActivity)uniqueInstance.activity).photoUploadComponent.photo_img[i]);
                        }
                    }

                    Log.i("status",((PublicAccFundsAuthActivity)uniqueInstance.activity).status+"");
                    if ( ((PublicAccFundsAuthActivity)uniqueInstance.activity).status == Constants.UNPASS ) {
                        ((PublicAccFundsAuthActivity)uniqueInstance.activity).tips.setTextColor(uniqueInstance.activity.getResources().getColor(R.color.text_red));
                        ((PublicAccFundsAuthActivity)uniqueInstance.activity).tips.setGravity(Gravity.LEFT);
                        ((PublicAccFundsAuthActivity)uniqueInstance.activity).tips.setText(((PublicAccFundsAuthActivity) uniqueInstance.activity).tips_s);
                        ((PublicAccFundsAuthActivity)uniqueInstance.activity).tips.setVisibility(View.VISIBLE);
                        Log.i("Tips Showing","");
                    } else {
                        ((PublicAccFundsAuthActivity)uniqueInstance.activity).tips.setVisibility(View.GONE);
                    }

                }

            }
        };

        pullPAFInfoTask.execute();

    }

    public void pullIdentityInfo() {

        AsyncTaskWithLoadingBar pullIdentityInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;
            private String[] echoUrl;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {

                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("dataObject",jsonObject.toString());

                        ((IdentificationActivity)uniqueInstance.activity).status = jsonObject.optInt("status", -1);
                        if ( ((IdentificationActivity)uniqueInstance.activity).status == Constants.UNPASS ) {
                            ((IdentificationActivity)uniqueInstance.activity).tips_s = jsonObject.optString("memo","");
                        }
                        echoUrl = new String[3];
                        for (int j=0;j<3;j++) {
                            echoUrl[j] = jsonObject.optString("url" + (j+1),"");
                        }
                        ((IdentificationActivity)uniqueInstance.activity).echoUrl = echoUrl;

                        if ( !echoUrl[0].equals("") ) {
                            ((IdentificationActivity)uniqueInstance.activity).id.uri = echoUrl[0];
                        } else {
                            ((IdentificationActivity)uniqueInstance.activity).id.uri = null;
                        }

                        if ( !echoUrl[1].equals("") ) {
                            ((IdentificationActivity)uniqueInstance.activity).id_front.uri = echoUrl[1];
                        } else {
                            ((IdentificationActivity)uniqueInstance.activity).id_front.uri = null;
                        }

                        if ( !echoUrl[2].equals("") ) {
                            ((IdentificationActivity)uniqueInstance.activity).id_back.uri = echoUrl[2];
                        } else {
                            ((IdentificationActivity)uniqueInstance.activity).id_back.uri = null;
                        }

                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {
                    if ( ((IdentificationActivity)uniqueInstance.activity).status == Constants.UNPASS ) {
                        ((IdentificationActivity)uniqueInstance.activity).tips.setText( ((IdentificationActivity)uniqueInstance.activity).tips_s );
                        ((IdentificationActivity)uniqueInstance.activity).tips.setVisibility(View.VISIBLE);
                    }

                    ImageLoader imageLoader = ImageLoader.getInstance();
                    if ( !((IdentificationActivity)uniqueInstance.activity).echoUrl[0].equals("") ) {
                        //((IdentificationActivity)uniqueInstance.activity).upload_id.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageLoader.displayImage( ((IdentificationActivity)uniqueInstance.activity).echoUrl[0],((IdentificationActivity)uniqueInstance.activity).upload_id );
                    }
                    if ( !((IdentificationActivity)uniqueInstance.activity).echoUrl[1].equals("") ) {
                        //((IdentificationActivity)uniqueInstance.activity).upload_id_front.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageLoader.displayImage( ((IdentificationActivity)uniqueInstance.activity).echoUrl[1],((IdentificationActivity)uniqueInstance.activity).upload_id_front );
                    }
                    if ( !((IdentificationActivity)uniqueInstance.activity).echoUrl[2].equals("") ) {
                        //((IdentificationActivity)uniqueInstance.activity).upload_id_back.setScaleType(ImageView.ScaleType.FIT_XY);
                        imageLoader.displayImage( ((IdentificationActivity)uniqueInstance.activity).echoUrl[2],((IdentificationActivity)uniqueInstance.activity).upload_id_back );
                    }
                }

            }
        };

        pullIdentityInfoTask.execute();

    }

    public void pullContactsInfo() {

        AsyncTaskWithLoadingBar pullContactsInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Log.i("dataArray", jsonArray.toString());

                        if (jsonArray.length() < 2) {
                            return "data null";
                        }

                        jsonObject = jsonArray.getJSONObject(0);
                        ((ContactsAuthActivity)uniqueInstance.activity).status = jsonObject.optInt("status",-1);
                        if ( ((ContactsAuthActivity)uniqueInstance.activity).status == Constants.UNPASS ) {
                            ((ContactsAuthActivity)uniqueInstance.activity).tips_s = jsonObject.optString("memo","");
                        }
                        ((ContactsAuthActivity)uniqueInstance.activity).id1 = jsonObject.optInt("id", 0);
                        ((ContactsAuthActivity)uniqueInstance.activity).familyName_s = jsonObject.optString("contactName", "");
                        ((ContactsAuthActivity)uniqueInstance.activity).familyPhone_s = jsonObject.optString("contactMobile", "");
                        ((ContactsAuthActivity)uniqueInstance.activity).familyRelation_i = jsonObject.optInt("relationshipInt", 0);
                        jsonObject = jsonArray.getJSONObject(1);
                        ((ContactsAuthActivity)uniqueInstance.activity).id2 = jsonObject.optInt("id", 0);
                        ((ContactsAuthActivity)uniqueInstance.activity).friendName_s = jsonObject.optString("contactName", "");
                        ((ContactsAuthActivity)uniqueInstance.activity).friendPhone_s = jsonObject.optString("contactMobile", "");
                        ((ContactsAuthActivity)uniqueInstance.activity).friendRelation_i = jsonObject.optInt("relationshipInt", 0);

                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s.equals("data null"))
                {
                    return;
                }

                if (((ContactsAuthActivity) uniqueInstance.activity).status == Constants.UNPASS)
                {
                    ((ContactsAuthActivity) uniqueInstance.activity).tips.setText(((ContactsAuthActivity) uniqueInstance.activity).tips_s);
                    ((ContactsAuthActivity) uniqueInstance.activity).tips.setVisibility(View.VISIBLE);
                }
                switch (((ContactsAuthActivity) uniqueInstance.activity).familyRelation_i)
                {
                    case 8:
                        ((ContactsAuthActivity) uniqueInstance.activity).familyRelation.setSelection(2);
                        break;
                    case 0:
                    case 1:
                        ((ContactsAuthActivity) uniqueInstance.activity).familyRelation.setSelection(((ContactsAuthActivity) uniqueInstance.activity).familyRelation_i);
                        break;
                    default:
                        ((ContactsAuthActivity) uniqueInstance.activity).familyRelation.setSelection(0);
                        break;
                }
                ((ContactsAuthActivity) uniqueInstance.activity).familyName.setText(((ContactsAuthActivity) uniqueInstance.activity).familyName_s);
                ((ContactsAuthActivity) uniqueInstance.activity).familyPhone.setText(((ContactsAuthActivity) uniqueInstance.activity).familyPhone_s);
                switch (((ContactsAuthActivity) uniqueInstance.activity).friendRelation_i)
                {
                    case 4:
                    case 5:
                    case 6:
                        ((ContactsAuthActivity) uniqueInstance.activity).friendRelation.setSelection(((ContactsAuthActivity) uniqueInstance.activity).friendRelation_i - 3);

                        break;
                    default:
                        ((ContactsAuthActivity) uniqueInstance.activity).friendRelation.setSelection(0);
                        break;
                }
                ((ContactsAuthActivity) uniqueInstance.activity).friendName.setText(((ContactsAuthActivity) uniqueInstance.activity).friendName_s);
                ((ContactsAuthActivity) uniqueInstance.activity).friendPhone.setText(((ContactsAuthActivity) uniqueInstance.activity).friendPhone_s);


            }
        };

        pullContactsInfoTask.execute();

    }

    public void pullLandlordInfo() {

        AsyncTaskWithLoadingBar pullLandlordInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;
            private Payee payee;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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
                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");


                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");
                        ((LandlordAuthActivity)uniqueInstance.activity).rentFeeMonthly_s = jsonObject.optString("monthlyFee", "");
                        Log.i("monthlyFee",((LandlordAuthActivity)uniqueInstance.activity).rentFeeMonthly_s);
                        ((LandlordAuthActivity) uniqueInstance.activity).howToPay_i = jsonObject.optInt("stagingDay", 0);
                        if ( (jsonObject.get("payee") == JSONObject.NULL) || (jsonObject.get("payee") == null) ) {
                            payee = null;
                        } else {
                            jsonObject = jsonObject.getJSONObject("payee");
                        }

                        Log.i("userObject",jsonObject.toString());
                        Log.i("userObject_length",jsonObject.length()+"");
                        if (jsonObject.length() > 0) {

                            payee = new Payee();
                            payee.setAccountNo(jsonObject.optString("accountNo", ""));
                            payee.setCityId(jsonObject.optInt("cityId", 0));
                            payee.setName(jsonObject.optString("name", ""));
//                            payee.setPayIdentity(jsonObject.optString("payIdentity", ""));
                            payee.setAccountPhone(jsonObject.optString("accountPhone", ""));
                            payee.setBank(jsonObject.optString("bank", ""));

                            ((App)uniqueInstance.activity.getApplication()).payee = payee;

                            ((LandlordAuthActivity)uniqueInstance.activity).status = jsonObject.optInt("stateAudit",Constants.WAIT_SUBMIT);
                            ((LandlordAuthActivity)uniqueInstance.activity).memo = jsonObject.optString("memo", "");

                            return state+"";

                        } else {
                            return state+"";
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == 2000) {

                    if ( ((LandlordAuthActivity)uniqueInstance.activity).rentFeeMonthly_s.equals("null") ) {
//                        ((LandlordAuthActivity)uniqueInstance.activity).rentFeeMonthly_s = "";
                        ((LandlordAuthActivity)uniqueInstance.activity).rentFeeMonthly_s = PreferenceUtils.getMonthRent();
                        Log.i("monthlyFee",((LandlordAuthActivity)uniqueInstance.activity).rentFeeMonthly_s);
                    }
                    ((LandlordAuthActivity)uniqueInstance.activity).rentFeeMonthly.setText(((LandlordAuthActivity)uniqueInstance.activity).rentFeeMonthly_s);
                    if (((LandlordAuthActivity)uniqueInstance.activity).howToPay_i==0){
                        ((LandlordAuthActivity)uniqueInstance.activity).howToPay_i = PreferenceUtils.getMonthRentWay();
                    }
                    switch ( ((LandlordAuthActivity)uniqueInstance.activity).howToPay_i ) {

                        case 3:
                            ((LandlordAuthActivity)uniqueInstance.activity).howToPay.setSelection(1);
                            break;
                        case 6:
                            ((LandlordAuthActivity)uniqueInstance.activity).howToPay.setSelection(2);
                            break;

                    }

                    if (payee == null) {
                        return;
                    }

                    ((LandlordAuthActivity)uniqueInstance.activity).cardNum.setText(((App)uniqueInstance.activity.getApplication()).payee.getAccountNo());
                    ((LandlordAuthActivity)uniqueInstance.activity).name.setText(((App)uniqueInstance.activity.getApplication()).payee.getName());
//                    ((LandlordAuthActivity)uniqueInstance.activity).idCard.setText(((App)uniqueInstance.activity.getApplication()).payee.getPayIdentity());
                    ((LandlordAuthActivity)uniqueInstance.activity).phone.setText(((App)uniqueInstance.activity.getApplication()).payee.getAccountPhone());
                    ((LandlordAuthActivity)uniqueInstance.activity).branch_bank_name.setText(((App)uniqueInstance.activity.getApplication()).payee.getBank());
                    if ( ((App)uniqueInstance.activity.getApplication()).payee.getCityId() != 0 ) {

                        City city;
                        try {
                            city = DBManager.getInstance().queryCityById(((App)uniqueInstance.activity.getApplication()).payee.getCityId());
                            ((LandlordAuthActivity)uniqueInstance.activity).city_i = ((App)uniqueInstance.activity.getApplication()).payee.getCityId();
                            ((LandlordAuthActivity)uniqueInstance.activity).attribution.setText(city.getName());
                            ((LandlordAuthActivity)uniqueInstance.activity).attribution.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                            ((LandlordAuthActivity)uniqueInstance.activity).attribution.setTextColor(uniqueInstance.activity.getResources().getColor(R.color.text_dark));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }

                    if ( ((LandlordAuthActivity)uniqueInstance.activity).status == Constants.UNPASS ) {
                        ((LandlordAuthActivity)uniqueInstance.activity).tips.setVisibility(View.VISIBLE);
                        ((LandlordAuthActivity)uniqueInstance.activity).tips.setTextColor(uniqueInstance.activity.getResources().getColor(R.color.text_red));
                        ((LandlordAuthActivity)uniqueInstance.activity).tips.setGravity(Gravity.LEFT);
                        ((LandlordAuthActivity)uniqueInstance.activity).tips.setText(((LandlordAuthActivity)uniqueInstance.activity).memo);
                    } else {
                        ((LandlordAuthActivity)uniqueInstance.activity).tips.setVisibility(View.GONE);
                    }

                }

            }
        };

        pullLandlordInfoTask.execute();

    }

    public void pullHouseDataInfo() {

        AsyncTaskWithLoadingBar pullHouseDataInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;
            private String[] echoUrl1,echoUrl2;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {

                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("dataObject",jsonObject.toString());

                        String contract = jsonObject.optString("contract","");
                        String others = jsonObject.optString("others","");
                        if (!contract.equals("")) {
                            echoUrl1 = contract.split(",");
                            Log.i("echoUrl1",echoUrl1.toString());
                        } else {
                            echoUrl1 = null;
                        }
                        if (!others.equals("")) {
                            echoUrl2 = others.split(",");
                            Log.i("echoUrl2",echoUrl2.toString());
                        } else {
                            echoUrl2 = null;
                        }
                        ((HouseDataAuthActivity)uniqueInstance.activity).status = jsonObject.optInt("materialStatus",Constants.WAIT_SUBMIT);
                        ((HouseDataAuthActivity)uniqueInstance.activity).memo = jsonObject.optString("materialMemo", "");
                        if (echoUrl1 != null) {
                            ((HouseDataAuthActivity)uniqueInstance.activity).contractPath.clear();
                            for (int i=0;i<echoUrl1.length;i++) {
                                ((HouseDataAuthActivity)uniqueInstance.activity).contractPath.add(echoUrl1[i]);
                            }
                            if (echoUrl1.length < 10) {
                                ((HouseDataAuthActivity)uniqueInstance.activity).contractPath.add("camera_default");
                            }
                        }
                        if (echoUrl2 != null) {
                            for (int i=0;i<5;i++) {
                                if (i < echoUrl2.length) {
                                    ((HouseDataAuthActivity)uniqueInstance.activity).photoUploadComponent_second.uri[i] = echoUrl2[i];
                                } else {
                                    ((HouseDataAuthActivity)uniqueInstance.activity).photoUploadComponent_second.uri[i] = null;
                                }
                            }
                        }

                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == 2000) {

                    if ( (echoUrl1 != null)&&(echoUrl2 != null) ) {

//                        ((HouseDataAuthActivity)uniqueInstance.activity).photoUploadComponent_first.showMore(echoUrl1.length - 1);
                        ImageLoader imageLoader = ImageLoader.getInstance();
//                        for (int i=0;i<echoUrl1.length;i++) {
//                            imageLoader.displayImage(echoUrl1[i],((HouseDataAuthActivity)uniqueInstance.activity).photoUploadComponent_first.photo_img[i]);
//                        }
                        ((HouseDataAuthActivity)uniqueInstance.activity).contractAdapter.notifyDataSetChanged();
                        ((HouseDataAuthActivity)uniqueInstance.activity).photoUploadComponent_second.showMore(echoUrl2.length - 1);
                        for (int i=0;i<echoUrl2.length;i++) {
                            imageLoader.displayImage(echoUrl2[i],((HouseDataAuthActivity)uniqueInstance.activity).photoUploadComponent_second.photo_img[i]);
                        }

                        if ( ((HouseDataAuthActivity)uniqueInstance.activity).status == Constants.UNPASS ) {

                            ((HouseDataAuthActivity)uniqueInstance.activity).tips.setVisibility(View.VISIBLE);
                            ((HouseDataAuthActivity)uniqueInstance.activity).tips.setTextColor(uniqueInstance.activity.getResources().getColor(R.color.text_red));
                            ((HouseDataAuthActivity)uniqueInstance.activity).tips.setGravity(Gravity.LEFT);
                            ((HouseDataAuthActivity)uniqueInstance.activity).tips.setText(((HouseDataAuthActivity)uniqueInstance.activity).memo);

                        } else {
                            ((HouseDataAuthActivity)uniqueInstance.activity).tips.setVisibility(View.GONE);
                        }

                    }

                }

            }
        };

        pullHouseDataInfoTask.execute();

    }

    public void pullSubmitOneInfo() {

        AsyncTaskWithLoadingBar pullSubmitOneInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {

                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("dataObject",jsonObject.toString());
                        ((SubmitInstallmentApplicationStepOneActivity)uniqueInstance.activity).academicStatus = jsonObject.optInt("academicStatus",0);
                        ((SubmitInstallmentApplicationStepOneActivity)uniqueInstance.activity).formatMemo = jsonObject.optString("formatMemo", "");
                        ((SubmitInstallmentApplicationStepOneActivity)uniqueInstance.activity).orderStatus = jsonObject.optInt("orderStatus", 0);
                        ((SubmitInstallmentApplicationStepOneActivity)uniqueInstance.activity).step = jsonObject.optInt("step",0);

                        ((SubmitInstallmentApplicationStepOneActivity)uniqueInstance.activity).basicStatus = jsonObject.optInt("basicStatus",0);
                        ((SubmitInstallmentApplicationStepOneActivity)uniqueInstance.activity).identityStatus = jsonObject.optInt("identityStatus",0);
                        ((SubmitInstallmentApplicationStepOneActivity)uniqueInstance.activity).workStatus = jsonObject.optInt("workStatus",0);
                        ((SubmitInstallmentApplicationStepOneActivity)uniqueInstance.activity).educationStatus = jsonObject.optInt("educationStatus",0);
                        ((SubmitInstallmentApplicationStepOneActivity)uniqueInstance.activity).contactStatus = jsonObject.optInt("contactStatus", 0);

//                        jsonObject = jsonObject.getJSONObject("userLogin");
//                        setUserLogin(jsonObject);

                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    ((SubmitInstallmentApplicationStepOneActivity)uniqueInstance.activity).refreshView();
                    ((SubmitInstallmentApplicationStepOneActivity)uniqueInstance.activity).submitAnim();

                }

            }
        };

        pullSubmitOneInfoTask.execute();

    }

    public void pullSubmitTwoInfo() {

        AsyncTaskWithLoadingBar pullSubmitTwoInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {

                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("dataObject",jsonObject.toString());
                        ((SubmitInstallmentApplicationStepTwoActivity)uniqueInstance.activity).payeeStatus = jsonObject.optInt("payeeStatus", -1);
                        ((SubmitInstallmentApplicationStepTwoActivity)uniqueInstance.activity).materrialStatus = jsonObject.optInt("materialStatus", -1);
                        ((SubmitInstallmentApplicationStepTwoActivity)uniqueInstance.activity).formatMemo = jsonObject.optString("formatMemo", "");
                        ((SubmitInstallmentApplicationStepTwoActivity)uniqueInstance.activity).orderStatus = jsonObject.optInt("orderStatus", 0);
                        ((SubmitInstallmentApplicationStepTwoActivity)uniqueInstance.activity).step = jsonObject.optInt("step",0);

                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    ((SubmitInstallmentApplicationStepTwoActivity)uniqueInstance.activity).refreshView();
                    ((SubmitInstallmentApplicationStepTwoActivity)uniqueInstance.activity).iconAnim();

                }

            }
        };

        pullSubmitTwoInfoTask.execute();

    }

    private void setUserLogin(JSONObject jsonObject) {

        UserLogin userLogin = new UserLogin();
        userLogin.setBasicStatusInt(jsonObject.optInt("basicStatusInt", 0));
        userLogin.setIdentityStatusInt(jsonObject.optInt("identityStatusInt", 0));
        userLogin.setOnJobStatusInt(jsonObject.optInt("onJobStatusInt", 0));
        userLogin.setContactStatusInt(jsonObject.optInt("contactStatusInt", 0));
        userLogin.setEducationStatusInt(jsonObject.optInt("educationStatusInt", 0));
        userLogin.setIncomeStatusInt(jsonObject.optInt("incomeStatusInt", 0));
        userLogin.setSocialSecurityStatusInt(jsonObject.optInt("socialSecurityStatusInt", 0));
        userLogin.setReserveStatusInt(jsonObject.optInt("reserveStatusInt", 0));
        userLogin.setMobileStatusInt(jsonObject.optInt("mobileStatusInt", 0));
        userLogin.setCompanyEamilInt(jsonObject.optInt("companyEamilInt",0));
//        userLogin.setWorkStatusInt(jsonObject.optInt("workStatusInt", 0));
//        userLogin.setInitStatusInt(jsonObject.optInt("initStatusInt", 0));
//        userLogin.setTaobaoStatusInt(jsonObject.optInt("taobaoStatusInt", 0));
//        userLogin.setJdStatusInt(jsonObject.optInt("jdStatusInt", 0));
        ((App)uniqueInstance.activity.getApplication()).userLogin = userLogin;

    }

    public void pullUserLogin() {

        AsyncTaskWithLoadingBar pullUserLoginTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {

                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("dataObject",jsonObject.toString());
                        setUserLogin(jsonObject);

                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (App.userLogin != null) {

                    switch ( ((App)uniqueInstance.activity.getApplication()).userLogin.getBasicStatusInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((PersonalProfileActivity)uniqueInstance.activity).baseAuthImg.setImageResource(R.drawable.base_info_grey);
                            break;
                        case Constants.AUDITING:
                            ((PersonalProfileActivity)uniqueInstance.activity).baseAuthImg.setImageResource(R.drawable.base_info_green);
                            break;
                        case Constants.PASSED:
                            ((PersonalProfileActivity)uniqueInstance.activity).baseAuthImg.setImageResource(R.drawable.base_info_green);
                            break;
                    }
                    switch ( ((App)uniqueInstance.activity.getApplication()).userLogin.getIdentityStatusInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((PersonalProfileActivity)uniqueInstance.activity).idAuthImg.setImageResource(R.drawable.id_info_grey);
                            break;
                        case Constants.AUDITING:
                            ((PersonalProfileActivity)uniqueInstance.activity).idAuthImg.setImageResource(R.drawable.id_info_green);
                            break;
                        case Constants.PASSED:
                            ((PersonalProfileActivity)uniqueInstance.activity).idAuthImg.setImageResource(R.drawable.id_info_green);
                            break;
                    }
                    switch ( ((App)uniqueInstance.activity.getApplication()).userLogin.getOnJobStatusInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((PersonalProfileActivity)uniqueInstance.activity).jobAuthImg.setImageResource(R.drawable.injobs_grey);
                            break;
                        case Constants.AUDITING:
                            ((PersonalProfileActivity)uniqueInstance.activity).jobAuthImg.setImageResource(R.drawable.injobs_green);
                            break;
                        case Constants.PASSED:
                            ((PersonalProfileActivity)uniqueInstance.activity).jobAuthImg.setImageResource(R.drawable.injobs_green);
                            break;
                    }
                    switch ( ((App)uniqueInstance.activity.getApplication()).userLogin.getContactStatusInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((PersonalProfileActivity)uniqueInstance.activity).contactsAuthImg.setImageResource(R.drawable.contacts_info_grey);
                            break;
                        case Constants.AUDITING:
                            ((PersonalProfileActivity)uniqueInstance.activity).contactsAuthImg.setImageResource(R.drawable.contacts_info_green);
                            break;
                        case Constants.PASSED:
                            ((PersonalProfileActivity)uniqueInstance.activity).contactsAuthImg.setImageResource(R.drawable.contacts_info_green);
                            break;
                    }
                    switch ( ((App)uniqueInstance.activity.getApplication()).userLogin.getEducationStatusInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((PersonalProfileActivity)uniqueInstance.activity).eduAuthImg.setImageResource(R.drawable.edu_info_grey);
                            break;
                        case Constants.AUDITING:
                            ((PersonalProfileActivity)uniqueInstance.activity).eduAuthImg.setImageResource(R.drawable.edu_info_green);
                            break;
                        case Constants.PASSED:
                            ((PersonalProfileActivity)uniqueInstance.activity).eduAuthImg.setImageResource(R.drawable.edu_info_green);
                            break;
                    }
                    switch ( ((App)uniqueInstance.activity.getApplication()).userLogin.getIncomeStatusInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((PersonalProfileActivity)uniqueInstance.activity).incomeAuthImg.setImageResource(R.drawable.income_grey);
                            break;
                        case Constants.AUDITING:
                            ((PersonalProfileActivity)uniqueInstance.activity).incomeAuthImg.setImageResource(R.drawable.income_green);
                            break;
                        case Constants.PASSED:
                            ((PersonalProfileActivity)uniqueInstance.activity).incomeAuthImg.setImageResource(R.drawable.income_green);
                            break;
                    }
                    switch ( ((App)uniqueInstance.activity.getApplication()).userLogin.getSocialSecurityStatusInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((PersonalProfileActivity)uniqueInstance.activity).ssAuthImg.setImageResource(R.drawable.social_security_grey);
                            break;
                        case Constants.AUDITING:
                            ((PersonalProfileActivity)uniqueInstance.activity).ssAuthImg.setImageResource(R.drawable.social_security_green);
                            break;
                        case Constants.PASSED:
                            ((PersonalProfileActivity)uniqueInstance.activity).ssAuthImg.setImageResource(R.drawable.social_security_green);
                            break;
                    }
                    switch ( ((App)uniqueInstance.activity.getApplication()).userLogin.getReserveStatusInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((PersonalProfileActivity)uniqueInstance.activity).pafAuthImg.setImageResource(R.drawable.paf_grey);
                            break;
                        case Constants.AUDITING:
                            ((PersonalProfileActivity)uniqueInstance.activity).pafAuthImg.setImageResource(R.drawable.paf_green);
                            break;
                        case Constants.PASSED:
                            ((PersonalProfileActivity)uniqueInstance.activity).pafAuthImg.setImageResource(R.drawable.paf_green);
                            break;
                    }
                    switch ( ((App)uniqueInstance.activity.getApplication()).userLogin.getMobileStatusInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((PersonalProfileActivity)uniqueInstance.activity).phoneAuthImg.setImageResource(R.drawable.mobile_phone_grey);
                            break;
                        case Constants.AUDITING:
                            ((PersonalProfileActivity)uniqueInstance.activity).phoneAuthImg.setImageResource(R.drawable.mobile_phone_green);
                            break;
                        case Constants.PASSED:
                            ((PersonalProfileActivity)uniqueInstance.activity).phoneAuthImg.setImageResource(R.drawable.mobile_phone_green);
                            break;
                    }

                    switch ( ((App)uniqueInstance.activity.getApplication()).userLogin.getCompanyEamilInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((PersonalProfileActivity)uniqueInstance.activity).email_info_img.setImageResource(R.drawable.email_grey);
                            break;
                        case Constants.AUDITING:
                            ((PersonalProfileActivity)uniqueInstance.activity).email_info_img.setImageResource(R.drawable.email_green);
                            break;
                        case Constants.PASSED:
                            ((PersonalProfileActivity)uniqueInstance.activity).email_info_img.setImageResource(R.drawable.email_green);
                            break;
                    }

                } else {
                    Toast.makeText(uniqueInstance.activity,"获取数据失败，请检查你的网络连接，稍后再试",Toast.LENGTH_LONG).show();
                    uniqueInstance.activity.finish();
                }

            }
        };

        pullUserLoginTask.execute();

    }

    public void pullOrderFee() {

        AsyncTaskWithLoadingBar pullOrderFeeTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {

                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("dataObject", jsonObject.toString());

                        ((SubmitInstallmentApplicationStepThreeActivity)uniqueInstance.activity).advanceFunding = jsonObject.optString("loanFee","");
                        ((SubmitInstallmentApplicationStepThreeActivity)uniqueInstance.activity).serviceFee = jsonObject.optString("servFee","");

                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s.equals("data null")) {
                    ((SubmitInstallmentApplicationStepThreeActivity)uniqueInstance.activity).advanceFunding = "";
                    ((SubmitInstallmentApplicationStepThreeActivity)uniqueInstance.activity).serviceFee = "";
                    return;
                }
                ((SubmitInstallmentApplicationStepThreeActivity)uniqueInstance.activity).refreshView();

            }
        };

        pullOrderFeeTask.execute();

    }

    public void preCal() {

        AsyncTaskWithLoadingBar preCalTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;

            @Override
            protected String doInBackground(String... params) {

                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(uniqueInstance.activity);
                Log.i("lng",location.get(0).getValue());
                Log.i("lat", location.get(1).getValue());
                uniqueInstance.postParameters.add(new BasicNameValuePair("channel", "android"));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lng",location.get(0).getValue()));
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

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("JsonObject",jsonObject.toString());
                        Log.i("JsonObject_length",jsonObject.length()+"");
                        ((PreCalculationActivity)uniqueInstance.activity).amount_int = jsonObject.optInt("moneyCount",0);
                        ((PreCalculationActivity)uniqueInstance.activity).payment_per_month = jsonObject.optInt("monthlyBill",0);
                        ((PreCalculationActivity)uniqueInstance.activity).service_fee = jsonObject.optInt("servFee",0);
                        ((PreCalculationActivity)uniqueInstance.activity).terms_int = jsonObject.optInt("stagingDay",0);
                        return state+"";
                    }

                }catch(JSONException e) {
                    e.printStackTrace();
                }

                return state+"";

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                ((PreCalculationActivity)uniqueInstance.activity).set_result();
                ((PreCalculationActivity)uniqueInstance.activity).start_animation();

            }
        };

        preCalTask.execute();

    }

    public void pullPhoneAuthStepThree() {

        AsyncTaskWithLoadingBar pullPhoneAuthStepThreeTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;
            private String url;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {

                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("dataObject", jsonObject.toString());

                        url = jsonObject.optString("url","");

                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s.equals("data null")) {
                    Toast.makeText(uniqueInstance.activity,"获取图片验证码出错",Toast.LENGTH_LONG).show();
                    return;
                } else if (url.equals("")) {
                    Toast.makeText(uniqueInstance.activity,"获取图片验证码出错",Toast.LENGTH_LONG).show();
                    return;
                } else {
                    ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(url,((PhoneAuthStepThreeActivity)uniqueInstance.activity).verifyCodeImg);
                }

            }
        };

        pullPhoneAuthStepThreeTask.execute();

    }

    public void pullPhoneAuthStepFour() {

        AsyncTaskWithLoadingBar pullPhoneAuthStepFourTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {

                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("dataObject", jsonObject.toString());

                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    if (s.equals("data null")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(uniqueInstance.activity);
                        builder.setMessage("发送短信验证码失败");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                Intent intent = new Intent();
                                intent.setClass(uniqueInstance.activity,PersonalProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                uniqueInstance.activity.startActivity(intent);
                            }
                        });
                        builder.create().show();

                    } else {

                        ((PhoneAuthStepFourActivity)uniqueInstance.activity).smsTipsLayout.setVisibility(View.VISIBLE);

                    }

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(uniqueInstance.activity);
                    builder.setMessage("认证失败");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent();
                            intent.setClass(uniqueInstance.activity,PersonalProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            uniqueInstance.activity.startActivity(intent);
                        }
                    });

                    builder.create().show();

                }

            }
        };

        pullPhoneAuthStepFourTask.execute();

    }

    public void postPhoneAuthStepFour() {

        AsyncTaskWithLoadingBar postAsyncTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;

            @Override
            protected String doInBackground(String... params) {

                ArrayList<BasicNameValuePair> location = LocationUtil.getLocation(uniqueInstance.activity);
                Log.i("lng",location.get(0).getValue());
                Log.i("lat", location.get(1).getValue());
                uniqueInstance.postParameters.add(new BasicNameValuePair("channel", "android"));
                uniqueInstance.postParameters.add(new BasicNameValuePair("lng",location.get(0).getValue()));
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
                    return state+"";
                }catch(JSONException e) {
                    e.printStackTrace();
                }

                return state+"";

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if ( s.equals("data null") || (s.equals("Err")) ) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(uniqueInstance.activity);
                    builder.setMessage("认证失败");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent intent = new Intent();
                            intent.setClass(uniqueInstance.activity,PersonalProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            uniqueInstance.activity.startActivity(intent);
                        }
                    });
                    builder.create().show();

                } else if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(uniqueInstance.activity);
                    builder.setMessage("提交成功");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            SharedPreferences entrance_flag = uniqueInstance.activity.getSharedPreferences(Constants.ENTRANCE_FLAG, Activity.MODE_PRIVATE);
                            int entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG, Constants.DEFAULT_ENTRANCE);
                            Intent i = new Intent();

                            switch (entrance) {
                                case Constants.IS_FROM_SIA_ONE:
                                    i.setClass(uniqueInstance.activity, SIAStepOneResultActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    break;
                                case Constants.IS_FROM_PROFILE:
                                    i.setClass(uniqueInstance.activity, PersonalProfileActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    break;
                            }

                            uniqueInstance.activity.startActivity(i);
                        }
                    });
                    builder.create().show();

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(uniqueInstance.activity);
                    builder.setMessage("认证失败");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            SharedPreferences entrance_flag = uniqueInstance.activity.getSharedPreferences(Constants.ENTRANCE_FLAG, Activity.MODE_PRIVATE);
                            int entrance = entrance_flag.getInt(Constants.ENTRANCE_FLAG, Constants.DEFAULT_ENTRANCE);
                            Intent i = new Intent();

                            switch (entrance) {
                                case Constants.IS_FROM_SIA_ONE:
                                    i.setClass(uniqueInstance.activity, SIAStepOneResultActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    break;
                                case Constants.IS_FROM_PROFILE:
                                    i.setClass(uniqueInstance.activity, PersonalProfileActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    break;
                            }

                            uniqueInstance.activity.startActivity(i);
                        }
                    });
                    builder.create().show();

                }

            }
        };

        postAsyncTask.execute();

    }

//    public void pullWorkInfo() {
//
//        AsyncTaskWithLoadingBar pullWorkInfoTask = new AsyncTaskWithLoadingBar() {
//
//            private BufferedReader in;
//            private int state;
//            private JSONObject jsonObject;
//            private String[] echoUrl;
//
//            @Override
//            protected String doInBackground(String... params) {
//
//                try {
//                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
//                } catch (IOException e) {
//                    if (e instanceof ConnectTimeoutException)
//                        Log.i("Connection Time Out",".");
//                    e.printStackTrace();
//                }
//
//                StringBuffer string = new StringBuffer("");
//                try {
//                    in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
//                    String lineStr = "";
//                    while ((lineStr = in.readLine()) != null) {
//                        string.append(lineStr + "\n");
//                    }
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Log.i("httpResult",string.toString());
//
//                try {
//
//                    jsonObject = new JSONObject(string.toString());
//                    state = jsonObject.getInt("code");
//
//                    if (jsonObject.get("data") == JSONObject.NULL) {
//                        return "data null";
//                    } else if (jsonObject.get("data") == null) {
//                        return "data null";
//                    } else {
//                        jsonObject = jsonObject.getJSONObject("data");
//
//                        JSONObject work;
//                        if (jsonObject.optJSONObject("authwork") != null) {
//                            work = jsonObject.getJSONObject("authwork");
//                            ((JobInfoActivity)uniqueInstance.activity).status = work.optInt("status", Constants.WAIT_SUBMIT);
//                            ((JobInfoActivity)uniqueInstance.activity).tips_s = work.optString("memo", "");
//
//                            ((JobInfoActivity)uniqueInstance.activity).companyType_i = work.optInt("companyTypeInt", 0);
//                            ((JobInfoActivity)uniqueInstance.activity).department_s = work.optString("position", "");
//                            ((JobInfoActivity)uniqueInstance.activity).phone_s = work.optString("companyPhone", "");
//                            ((JobInfoActivity)uniqueInstance.activity).address_s = work.optString("companyAddr","");
//
//                        } else {
//
//                        }
//
//                        JSONArray material;
//                        if (jsonObject.optJSONArray("material") != null) {
//                            material = jsonObject.getJSONArray("material");
//                            echoUrl = new String[material.length()];
//                            for (int j=0;j<material.length();j++) {
//                                echoUrl[j] = material.getJSONObject(j).getString("materialUrl");
//                            }
//                            ((JobInfoActivity)uniqueInstance.activity).echoUrl = echoUrl;
//                            if (echoUrl != null) {
//                                for (int i=0;i<5;i++) {
//                                    if (i < echoUrl.length) {
//                                        ((JobInfoActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = echoUrl[i];
//                                    } else {
//                                        ((JobInfoActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = null;
//                                    }
//                                }
//                            }
//                            return state+"";
//                        } else {
//
//                        }
//                        return state+"";
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return state+"";
//
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//
//                if (state == Constants.HTTP_REQUEST_SUCCESS) {
//
//                    ((JobInfoActivity)uniqueInstance.activity).companyType.setSelection(((JobInfoActivity) uniqueInstance.activity).companyType_i);
//                    ((JobInfoActivity)uniqueInstance.activity).department.setText(((JobInfoActivity) uniqueInstance.activity).department_s);
//                    ((JobInfoActivity)uniqueInstance.activity).phone.setText(((JobInfoActivity)uniqueInstance.activity).phone_s);
//                    ((JobInfoActivity)uniqueInstance.activity).address.setText(((JobInfoActivity)uniqueInstance.activity).address_s);
//
//                    if ( (echoUrl != null)&&(echoUrl.length > 0) ) {
//
//                        ((JobInfoActivity)uniqueInstance.activity).choice.check(R.id.choice_two);
//
//                        ((JobInfoActivity)uniqueInstance.activity).photoUploadComponent.showMore(echoUrl.length - 1);
//                        ImageLoader imageLoader = ImageLoader.getInstance();
//                        for (int i=0;i<echoUrl.length;i++) {
//                            imageLoader.displayImage(echoUrl[i],((JobInfoActivity)uniqueInstance.activity).photoUploadComponent.photo_img[i]);
//                        }
//                    }
//
//                    if ( ((JobInfoActivity)uniqueInstance.activity).status == Constants.UNPASS ) {
//                        ((JobInfoActivity)uniqueInstance.activity).tips.setTextColor(uniqueInstance.activity.getResources().getColor(R.color.text_red));
//                        ((JobInfoActivity)uniqueInstance.activity).tips.setGravity(Gravity.LEFT);
//                        ((JobInfoActivity)uniqueInstance.activity).tips.setText(((JobInfoActivity) uniqueInstance.activity).tips_s);
//                        ((JobInfoActivity)uniqueInstance.activity).tips.setVisibility(View.VISIBLE);
//                    } else {
//                        ((JobInfoActivity)uniqueInstance.activity).tips.setVisibility(View.GONE);
//                    }
//
//                }
//
//            }
//        };
//
//        pullWorkInfoTask.execute();
//
//    }

//    public void pullBannerInfo() {
//
//        AsyncTaskWithLoadingBar pullBannerInfoTask = new AsyncTaskWithLoadingBar() {
//
//            private BufferedReader in;
//            private int state;
//            private JSONObject jsonObject;
//
//            @Override
//            protected String doInBackground(String... params) {
//
//                try {
//                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
//                } catch (IOException e) {
//                    if (e instanceof ConnectTimeoutException)
//                        Log.i("Connection Time Out",".");
//                    e.printStackTrace();
//                }
//
//                StringBuffer string = new StringBuffer("");
//                try {
//                    in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
//                    String lineStr = "";
//                    while ((lineStr = in.readLine()) != null) {
//                        string.append(lineStr + "\n");
//                    }
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Log.i("Banner_httpResult",string.toString());
//
//                try {
//
//                    jsonObject = new JSONObject(string.toString());
//                    state = jsonObject.getInt("code");
//
//                    if (jsonObject.get("data") == JSONObject.NULL) {
//                        return "data null";
//                    } else if (jsonObject.get("data") == null) {
//                        return "data null";
//                    } else {
//
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        Log.i("dataArray", jsonArray.toString());
//
//                        if (jsonArray.length() < 1) {
//                            return "data null";
//                        } else {
//
//                            App.banners = new ArrayList<>();
//                            for (int i=0;i<jsonArray.length();i++) {
//
//                                Banner banner = new Banner();
//                                jsonObject = jsonArray.getJSONObject(i);
//                                banner.setAdAndroidUrl(jsonObject.optString("adAndroidurl",""));
//                                banner.setAdName(jsonObject.optString("adName", ""));
//                                banner.setAdTarget(jsonObject.optString("adTarget",""));
//                                App.banners.add(banner);
//
//                            }
//
////                            while (App.banners.size() < 4) {
////                                Banner banner1 = new Banner();
////                                banner1.setAdAndroidUrl("");
////                                banner1.setAdName("");
////                                banner1.setAdTarget("");
////                                App.banners.add(banner1);
////                                Log.i("banner size",App.banners.size()+"");
////                            }
//
//                        }
//
//                        return state+"";
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return state+"";
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//
//                if (s.equals("data null")) {
//                    return;
//                }
//
//            }
//
//            @Override
//            protected void onPreExecute() {
//
//            }
//        };
//
//        pullBannerInfoTask.execute();
//
//    }

    public void pullUserLoginSIA() {

        AsyncTaskWithLoadingBar pullUserLoginSIATask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {

                        jsonObject = jsonObject.getJSONObject("data");
                        Log.i("dataObject",jsonObject.toString());
                        setUserLogin(jsonObject);

                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (App.userLogin != null) {

                    switch ( App.userLogin.getIncomeStatusInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((SIAStepOneResultActivity)uniqueInstance.activity).authOneImg.setImageResource(R.drawable.income_grey);
                            break;
                        case Constants.AUDITING:
                            ((SIAStepOneResultActivity)uniqueInstance.activity).authOneImg.setImageResource(R.drawable.income_green);
                            break;
                        case Constants.PASSED:
                            ((SIAStepOneResultActivity)uniqueInstance.activity).authOneImg.setImageResource(R.drawable.income_green);
                            break;
                    }
                    switch ( App.userLogin.getMobileStatusInt() ) {
                        case Constants.WAIT_SUBMIT:
                        case Constants.UNPASS:
                            ((SIAStepOneResultActivity)uniqueInstance.activity).authTwoImg.setImageResource(R.drawable.mobile_phone_grey);
                            break;
                        case Constants.AUDITING:
                            ((SIAStepOneResultActivity)uniqueInstance.activity).authTwoImg.setImageResource(R.drawable.mobile_phone_green);
                            break;
                        case Constants.PASSED:
                            ((SIAStepOneResultActivity)uniqueInstance.activity).authTwoImg.setImageResource(R.drawable.mobile_phone_green);
                            break;
                    }
                    ((SIAStepOneResultActivity)uniqueInstance.activity).authOne.setOnClickListener((SIAStepOneResultActivity)uniqueInstance.activity);
                    ((SIAStepOneResultActivity)uniqueInstance.activity).authTwo.setOnClickListener((SIAStepOneResultActivity)uniqueInstance.activity);

                } else {
                    Toast.makeText(uniqueInstance.activity,"获取数据失败，请检查你的网络连接，稍后再试",Toast.LENGTH_LONG).show();
                }

            }
        };

        pullUserLoginSIATask.execute();

    }

    public void pullJobInfo() {

        AsyncTaskWithLoadingBar pullJobInfoTask = new AsyncTaskWithLoadingBar() {

            private BufferedReader in;
            private int state;
            private JSONObject jsonObject;
            private String[] echoUrl;

            @Override
            protected String doInBackground(String... params) {

                try {
                    uniqueInstance.httpResponse = uniqueInstance.httpClient.execute(uniqueInstance.request);
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

                    jsonObject = new JSONObject(string.toString());
                    state = jsonObject.getInt("code");

                    if (jsonObject.get("data") == JSONObject.NULL) {
                        return "data null";
                    } else if (jsonObject.get("data") == null) {
                        return "data null";
                    } else {
                        jsonObject = jsonObject.getJSONObject("data");

                        JSONObject work;
                        if (jsonObject.optJSONObject("authwork") != null) {
                            work = jsonObject.getJSONObject("authwork");
                            ((JobInfoActivity)uniqueInstance.activity).status = work.optInt("status", Constants.WAIT_SUBMIT);
                            ((JobInfoActivity)uniqueInstance.activity).tips_s = work.optString("memo", "");

                            ((JobInfoActivity)uniqueInstance.activity).companyType_i = work.optInt("companyTypeInt", 0);
                            ((JobInfoActivity)uniqueInstance.activity).department_s = work.optString("position", "");
                            ((JobInfoActivity)uniqueInstance.activity).phone_s = work.optString("companyPhone", "");
                            ((JobInfoActivity)uniqueInstance.activity).address_s = work.optString("companyAddr","");

                        } else {

                        }

                        JSONArray material;
                        if (jsonObject.optJSONArray("material") != null) {
                            material = jsonObject.getJSONArray("material");
                            echoUrl = new String[material.length()];
                            for (int j=0;j<material.length();j++) {
                                echoUrl[j] = material.getJSONObject(j).getString("materialUrl");
                            }
                            ((JobInfoActivity)uniqueInstance.activity).echoUrl = echoUrl;
                            if (echoUrl != null) {
                                for (int i=0;i<5;i++) {
                                    if (i < echoUrl.length) {
                                        ((JobInfoActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = echoUrl[i];
                                    } else {
                                        ((JobInfoActivity)uniqueInstance.activity).photoUploadComponent.uri[i] = null;
                                    }
                                }
                            }
                            return state+"";
                        } else {

                        }
                        return state+"";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return state+"";

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (state == Constants.HTTP_REQUEST_SUCCESS) {

                    ((JobInfoActivity)uniqueInstance.activity).companyType.setSelection(((JobInfoActivity) uniqueInstance.activity).companyType_i);
                    ((JobInfoActivity)uniqueInstance.activity).department.setText(((JobInfoActivity) uniqueInstance.activity).department_s);
                    ((JobInfoActivity)uniqueInstance.activity).phone.setText(((JobInfoActivity)uniqueInstance.activity).phone_s);
                    ((JobInfoActivity)uniqueInstance.activity).address.setText(((JobInfoActivity)uniqueInstance.activity).address_s);

                    if ( (echoUrl != null)&&(echoUrl.length > 0) ) {

                        ((JobInfoActivity)uniqueInstance.activity).photoUploadComponent.showMore(echoUrl.length - 1);
                        ImageLoader imageLoader = ImageLoader.getInstance();
                        for (int i=0;i<echoUrl.length;i++) {
                            imageLoader.displayImage(echoUrl[i],((JobInfoActivity)uniqueInstance.activity).photoUploadComponent.photo_img[i]);
                        }
                    }

                    if ( ((JobInfoActivity)uniqueInstance.activity).status == Constants.UNPASS ) {
                        ((JobInfoActivity)uniqueInstance.activity).tips.setTextColor(uniqueInstance.activity.getResources().getColor(R.color.text_red));
                        ((JobInfoActivity)uniqueInstance.activity).tips.setGravity(Gravity.LEFT);
                        ((JobInfoActivity)uniqueInstance.activity).tips.setText(((JobInfoActivity) uniqueInstance.activity).tips_s);
                        ((JobInfoActivity)uniqueInstance.activity).tips.setVisibility(View.VISIBLE);
                    } else {
                        ((JobInfoActivity)uniqueInstance.activity).tips.setVisibility(View.GONE);
                    }

                }

            }
        };

        pullJobInfoTask.execute();

    }

    abstract class AsyncTaskWithLoadingBar extends AsyncTask<String,Integer,String> {

        private LoadingBar loadingBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (loadingBar == null)
            {
                loadingBar = new LoadingBar(uniqueInstance.activity);
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

            if (GenericUtils.checkDigit(s)) {

                int code = Integer.parseInt(s);
                if (1003 == code)
                {
                    App.signOut(uniqueInstance.activity,1);
                    Toast.makeText(uniqueInstance.activity, "验证失败", Toast.LENGTH_SHORT).show();
                } else if (2001 == code)
                {
                    App.signOut(uniqueInstance.activity, 1);
                } else if (2002 == code || 2003 == code || 2004 == code || 2005 == code || 2006 == code || 2007 == code || 2008 == code || 2009 == code || 2014 == code || 2024 == code || 2025 == code)
                {
                    String desc = "";
                    switch (code) {
                        case 2002:
                            desc = "手机号码格式不正确";
                            break;
                        case 2003:
                            desc = "密码格式不正确";
                            break;
                        case 2004:
                            desc = "密码不匹配";
                            break;
                        case 2005:
                            desc = "用户账号被锁";
                            break;
                        case 2006:
                            desc = "验证码格式不正确";
                            break;
                        case 2007:
                            desc = "短信验证码过期或无效";
                            break;
                        case 2008:
                            desc = "获取验证码操作过于频繁";
                            break;
                        case 2009:
                            desc = "新旧密码相同";
                            break;
                        case 2014:
                            desc = "用户不存在";
                            break;
                        case 2024:
                            desc = "金额有误";
                            break;
                        case 2025:
                            desc = "被授权者未开户";
                            break;
                    }
                    Toast.makeText(uniqueInstance.activity, desc, Toast.LENGTH_SHORT).show();
                }

            }

        }

    }

}