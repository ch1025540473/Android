package com.wezebra.zebraking.util;

import java.io.File;

/**
 * Created by HQDev on 2015/4/16.
 */
public class Constants {

    public static final String APP_NAME = "ZebraKingdom";

    public static String EXTERNAL_DIR = FileUtils.getExternalStorageDir().getAbsolutePath() + File.separator + APP_NAME;
    public static String CACHE_DIR = EXTERNAL_DIR + File.separator + "cache";
    public static String LOGS_DIR = EXTERNAL_DIR + File.separator + "logs";
    public static String IMAGE_DIR = EXTERNAL_DIR + File.separator + "images";

    // 微信支付
    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    public static final String APP_ID = "wx2cf9392d96e23313";
    //商户号
    public static final String MCH_ID = "1242429402";
    //  API密钥，在商户平台设置
    public static final  String API_KEY="1DA458320915A574E59FC21CDB173AD6";

    //HTTP相关
    public static final String _SCHEMA_HTTP = "http://";
    public static final String _SCHEMA_HTTPS = "https://";

    /**
     * Release
     */
//    public static final String _HOST = _SCHEMA_HTTPS + "m.wezebra.com";
    public static final String _HOST = _SCHEMA_HTTPS + "test.m.wezebra.com";
    public static final String _URL = _HOST + "/king/api/data";
    public static final String FILE_UPLOAD_URL = _HOST + "/king/common/upload";
    public static final String URL_QUESTION = _HOST + "/king/app/help.do";
    public static final String URL_AGREEMENT = _HOST + "/wezebra/agree/app-agreement.html";
    public static final String URL_ABOUT = _HOST + "/king/app/about.do";
    public static final String URL_LOAN_AGREEMENT = _HOST + "/king/agree/loancontract";

    /**
     * Debug
     */
//    public static final String _HOST = _SCHEMA_HTTPS + "test.m.wezebra.com"; // "192.168.1.199:8080";"121.40.78.61:9070";"test.m.wezebra.com";
//    public static final String _URL = _HOST + "/zf/api/data";
//    public static final String FILE_UPLOAD_URL = _HOST + "/zf/common/upload";
//    public static final String URL_QUESTION = _HOST + "/zf/app/help.do";
//    public static final String URL_AGREEMENT = _HOST + "/wezebra/agree/app-agreement.html";
//    public static final String URL_ABOUT = _HOST + "/zf/app/about.do";
//    public static final String URL_LOAN_AGREEMENT = _HOST + "/zf/agree/loancontract";


    public static final int CONNECTION_TIME_OUT = 30000;
    public static final int SOCKET_TIME_OUT = 30000;
    public static final int PICTURE_UPLOAD_CONNECTION_TIME_OUT = 60000;
    public static final int PICTURE_UPLOAD_SO_TIME_OUT = 60000;
    public static final int PICTURE_DOWNLOAD_CONNECTION_TIME_OUT = 60000;
    public static final int PICTURE_DOWNLOAD_READ_TIME_OUT = 60000;

    // API
    public static final String API_LOGIN = "login";
    public static final String API_GET_SMS_CODE = "getSmsCode";
    public static final String API_SIGN_IN = "signin";
    public static final String API_SIGN_OUT = "signout";
    public static final String API_UPDATE_PWD = "updatepwd";
    public static final String API_CHARGE = "charge";
    public static final String API_CASH = "cash";
    public static final String API_PAYMONEY = "payMoney";
    public static final String API_PAY_FIRST = "payFirst";
    public static final String API_GRANT = "grant";
    public static final String API_OPEN_ACCOUNT = "openAccount";
    public static final String API_GET_USER_DETAIL = "getUserDetail";
    public static final String API_DEVICE_UPDATE = "deviceUpdate";
    public static final String API_ACTIVE_APPLY = "activeApply";
    public static final String API_GET_ORDERS = "getOrders";
    public static final String API_GET_ORDERS_DETAIL = "getOrderDetail";
    public static final String API_GET_TRANS = "getTrans";
    public static final String API_GET_TENDERS = "getTenders";
    public static final String API_INVESTTENDER = "investTenderApi";
    public static final String API_FEED_BACK = "feedBack";
    public static final String API_BANNER = "adsInfo";

    // ORDER STATUS
    public static final int ORDER_AUDITING_FIRST = 10;
    public static final int ORDER_WAITING_IMPROVE = 11;
    public static final int ORDER_AUDITING_BASIC = 31;
    public static final int ORDER_WAITING_ADDED = 32;
    public static final int ORDER_AUDITING_ADDED = 34;
    public static final int ORDER_WAITING_MODIFY = 35;
    public static final int ORDER_WAITING_PAY_FIRST = 37;
    public static final int ORDER_WAITING_GRANT = 38;
    public static final int ORDER_AUDITING_LOAN = 41;
    public static final int ORDER_WAITING_REPAY = 42;
    public static final int ORDER_SUCCESS = 43;
    public static final int ORDER_CLOSED = -99;
    public static final int ORDER_UNPASS = -98;

    //上传图片相关
    public static int UPLOAD_LIMIT = 5;
    public static final String[] SELECT_PHOTO = new String[]{"打开相机", "打开相册"};
    public static final String[] ONLY_CAMERA = new String[]{"打开相机"};
    public static final int BITMAP_SCALE_MIN_WIDTH = 720;
    public static final int BITMAP_SCALE_MIN_HEIGHT = 720;

    //图片相关
    public static final int PREVIEW_BITMAP_SCALE_MIN_WIDTH = 120;
    public static final int PREVIEW_BITMAP_SCALE_MIN_HEIGHT = 120;

    //房租分期申请流程相关
    public static final String INFO_SUBMIT = "InfoSubmit";
    public static final int NO_INFO_SUBMIT = -1;
    public static final int BASE_INFO_SUBMIT = 1;
    public static final int ID_INFO_SUBMIT = 2;
    public static final int JOB_OR_EDUCATION_INFO_SUBMIT = 3;
    public static final int CONTACTS_INFO_SUBMIT = 4;
    public static final int LANDLORD_INFO_SUBMIT = 5;
    public static final int HOUSE_DATA_SUBMIT = 6;
    public static final int INCOME_INFO_SUBMIT = 7;
    public static final int SOCIAL_SECURITY_SUBMIT = 8;
    public static final int PAF_SUBMIT = 9;
    public static final String INFO_AUTH_STATE = "Info_Auth_State";
    public static final String BASE_INFO_STATE = "Base_Info_State";
    public static final String ID_INFO_STATE = "ID_Info_State";
    public static final String JOB_OR_EDUCATION = "Job_Or_Education";
    public static final String JOB_OR_EDUCATION_INFO_STATE = "Job_Or_Education_State";
    public static final String CONTACTS_INFO_STATE = "Contacts_Info_State";
    public static final String LANDLORD_INFO_STATE = "Landlord_Info_State";
    public static final String HOUSE_DATA_INFO_STATE = "House_Data_Info_State";
    public static final int INFO_UNFILLED = -1;
    public static final int INFO_FILLED = 1;
    public static final int DEFAULT_INFO_STATE = INFO_UNFILLED;
    public static final int IS_JOB_AUTH = 1;
    public static final int IS_EDUCATION_AUTH = 2;
    public static final int DEFAULT_AUTH_ABOUT_JOB＿AND＿EDU = IS_JOB_AUTH;
    public static final String JOB_AUTH = "工作信息";
    public static final String EDU_AUTH = "学历信息";

    //审核状态码
    public static final int WAIT_SUBMIT = -1;
    public static final int AUDITING = 1;
    public static final int PASSED = 2;
    public static final int UNPASS = 3;

    //Activity跳转标志
    public static final String ENTRANCE_FLAG = "Entrance_Flag";
    public static final int IS_FROM_APPLICATION = 1;
    public static final int IS_FROM_PROFILE = 2;
    public static final int IS_FROM_STEP_TWO = 3;
    public static final int IS_FROM_SIA_ONE = 4;
    public static final int DEFAULT_ENTRANCE = IS_FROM_APPLICATION;

    //Spinner填充项
    public static final String[] CITY = new String[]{"-- 请选择 --", "成都", "北京"};

    //动画相关
    public static final int PROGRESSBAR_ANIM_TIME = 600;

    //颜色相关
    public static final int TEXT_COLOR_DEFAULT = 0xff606366;

    //控件相关
    public static final boolean SCROLL_VIEW_VERTICAL_SCROLL_BAR_ENANBLE = false;

    //Http状态码
    public static final int HTTP_REQUEST_SUCCESS = 2000;
    public static final int HTTP_REQUEST_ERROR = 2040;

    //资料重填跳转标志
    public static final int BASIC_INFO = 1;
    public static final int IDENTITY_INFO = 2;
    public static final int JOB_INFO = 3;
    public static final int EDUCATION_INFO  = 4;
    public static final int CONTACTS_INFO = 5;
    public static final int INCOME_INFO = 6;
    public static final int SS_INFO = 7;
    public static final int PAF_INFO = 8;
    public static final int PHONE_INFO = 9;
    public static final int EMAIL_INFO = 10;
    public static final int ON_JOB = 11;

    //状态页面自动拉取刷新当前订单数据时间间隔
    public static final int REFRESH_PULL_TIME = 3000;

    //线上线下付款标识IsOnline默认值
    public static final boolean IS_ONLINE_DEFAULT = false;

    public static final int PICTURE_COMPRESS = 70 ;

}
