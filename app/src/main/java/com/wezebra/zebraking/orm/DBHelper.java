package com.wezebra.zebraking.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.City;
import com.wezebra.zebraking.model.Province;
import com.wezebra.zebraking.util.GenericUtils;

import java.io.InputStream;
import java.sql.SQLException;

/**
 * User: superalex
 * Date: 2015/1/24
 * Time: 10:41
 */
public class DBHelper extends OrmLiteSqliteOpenHelper
{
    public static final String TAG = "DBHelper";
    private static final String DATABASE_NAME = "zebra_db";
    private static final int DATABASE_VERSION = 1;
    private Dao<Province, Integer> provinceDao;
    private Dao<City, Integer> cityDao;
    private Context context;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource)
    {
        try
        {
            Log.i(DBHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Province.class);
            TableUtils.createTable(connectionSource, City.class);
//            initArea(sqLiteDatabase);
            insertProvinces(sqLiteDatabase);
            insertCities(sqLiteDatabase);
        } catch (SQLException e)
        {
            Log.e(DBHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2)
    {
        try
        {
            Log.i(DBHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Province.class, true);
            TableUtils.dropTable(connectionSource, City.class, true);

            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e)
        {
            Log.e(DBHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close()
    {
        super.close();
        provinceDao = null;
        cityDao = null;
    }

    private void initArea(SQLiteDatabase db)
    {
        InputStream insertProvinceIs = context.getResources().openRawResource(
                R.raw.insert_province);
        InputStream insertCityIs = context.getResources().openRawResource(
                R.raw.insert_city);

        String insertProvinces = GenericUtils.getStringFromStream(insertProvinceIs);
        String insertCities = GenericUtils.getStringFromStream(insertCityIs);

        db.execSQL(insertProvinces);
        db.execSQL(insertCities);
    }

    private void insertProvinces(SQLiteDatabase db)
    {
        db.execSQL("insert into province (id,name) values (10001,'北京')");
        db.execSQL("insert into province (id,name) values (10002,'天津')");
        db.execSQL("insert into province (id,name) values (10003,'上海')");
        db.execSQL("insert into province (id,name) values (10004,'重庆')");
        db.execSQL("insert into province (id,name) values (10005,'河北')");
        db.execSQL("insert into province (id,name) values (10006,'山西')");
        db.execSQL("insert into province (id,name) values (10007,'内蒙古')");
        db.execSQL("insert into province (id,name) values (10008,'辽宁')");
        db.execSQL("insert into province (id,name) values (10009,'吉林')");
        db.execSQL("insert into province (id,name) values (10010,'黑龙江')");
        db.execSQL("insert into province (id,name) values (10011,'江苏')");
        db.execSQL("insert into province (id,name) values (10012,'浙江')");
        db.execSQL("insert into province (id,name) values (10013,'安徽')");
        db.execSQL("insert into province (id,name) values (10014,'福建')");
        db.execSQL("insert into province (id,name) values (10015,'江西')");
        db.execSQL("insert into province (id,name) values (10016,'山东')");
        db.execSQL("insert into province (id,name) values (10017,'河南')");
        db.execSQL("insert into province (id,name) values (10018,'湖北')");
        db.execSQL("insert into province (id,name) values (10019,'湖南')");
        db.execSQL("insert into province (id,name) values (10020,'广东')");
        db.execSQL("insert into province (id,name) values (10021,'广西')");
        db.execSQL("insert into province (id,name) values (10022,'海南')");
        db.execSQL("insert into province (id,name) values (10023,'四川')");
        db.execSQL("insert into province (id,name) values (10024,'贵州')");
        db.execSQL("insert into province (id,name) values (10025,'云南')");
        db.execSQL("insert into province (id,name) values (10026,'西藏')");
        db.execSQL("insert into province (id,name) values (10027,'陕西')");
        db.execSQL("insert into province (id,name) values (10028,'甘肃')");
        db.execSQL("insert into province (id,name) values (10029,'青海')");
        db.execSQL("insert into province (id,name) values (10030,'宁夏')");
        db.execSQL("insert into province (id,name) values (10031,'新疆')");
    }

    private void insertCities(SQLiteDatabase db)
    {
        db.execSQL("insert into `city` (`id`,`prov_id`,`name`,`code`) select 20001 as 'id',10001 as 'prov_id','北京' as 'name','' as 'code' UNION SELECT 20002,10002,'天津','' UNION SELECT 20003,10003,'上海','' UNION SELECT 20004,10004,'重庆','' UNION SELECT 20005,10005,'石家庄','' UNION SELECT 20006,10005,'唐山','' UNION SELECT 20007,10005,'秦皇岛','' UNION SELECT 20008,10005,'邯郸','' UNION SELECT 20009,10005,'邢台','' UNION SELECT 20010,10005,'保定','' UNION SELECT 20011,10005,'张家口','' UNION SELECT 20012,10005,'承德','' UNION SELECT 20013,10005,'沧州','' UNION SELECT 20014,10005,'廊坊','' UNION SELECT 20015,10005,'衡水','' UNION SELECT 20016,10006,'太原','' UNION SELECT 20017,10006,'大同','' UNION SELECT 20018,10006,'阳泉','' UNION SELECT 20019,10006,'长治','' UNION SELECT 20020,10006,'晋城','' UNION SELECT 20021,10006,'朔州','' UNION SELECT 20022,10006,'晋中','' UNION SELECT 20023,10006,'运城','' UNION SELECT 20024,10006,'忻州','' UNION SELECT 20025,10006,'临汾','' UNION SELECT 20026,10006,'吕梁','' UNION SELECT 20027,10007,'呼和浩特','' UNION SELECT 20028,10007,'包头','' UNION SELECT 20029,10007,'乌海','' UNION SELECT 20030,10007,'赤峰','' UNION SELECT 20031,10007,'通辽','' UNION SELECT 20032,10007,'鄂尔多斯','' UNION SELECT 20033,10007,'呼伦贝尔','' UNION SELECT 20034,10007,'巴彦淖尔','' UNION SELECT 20035,10007,'乌兰察布','' UNION SELECT 20036,10007,'锡林郭勒盟','' UNION SELECT 20037,10007,'兴安盟','' UNION SELECT 20038,10007,'阿拉善盟','' UNION SELECT 20039,10008,'沈阳','' UNION SELECT 20040,10008,'大连','' UNION SELECT 20041,10008,'鞍山','' UNION SELECT 20042,10008,'抚顺','' UNION SELECT 20043,10008,'本溪','' UNION SELECT 20044,10008,'丹东','' UNION SELECT 20045,10008,'锦州','' UNION SELECT 20046,10008,'营口','' UNION SELECT 20047,10008,'阜新','' UNION SELECT 20048,10008,'辽阳','' UNION SELECT 20049,10008,'盘锦','' UNION SELECT 20050,10008,'铁岭','' UNION SELECT 20051,10008,'朝阳','' UNION SELECT 20052,10008,'葫芦岛','' UNION SELECT 20053,10009,'长春','' UNION SELECT 20054,10009,'吉林','' UNION SELECT 20055,10009,'四平','' UNION SELECT 20056,10009,'辽源','' UNION SELECT 20057,10009,'通化','' UNION SELECT 20058,10009,'白山','' UNION SELECT 20059,10009,'松原','' UNION SELECT 20060,10009,'白城','' UNION SELECT 20061,10009,'延边朝鲜族','' UNION SELECT 20062,10010,'哈尔滨','' UNION SELECT 20063,10010,'齐齐哈尔','' UNION SELECT 20064,10010,'鹤岗','' UNION SELECT 20065,10010,'双鸭山','' UNION SELECT 20066,10010,'鸡西','' UNION SELECT 20067,10010,'大庆','' UNION SELECT 20068,10010,'伊春','' UNION SELECT 20069,10010,'牡丹江','' UNION SELECT 20070,10010,'佳木斯','' UNION SELECT 20071,10010,'七台河','' UNION SELECT 20072,10010,'黑河','' UNION SELECT 20073,10010,'绥化','' UNION SELECT 20074,10010,'大兴安岭','' UNION SELECT 20075,10011,'南京','' UNION SELECT 20076,10011,'无锡','' UNION SELECT 20077,10011,'徐州','' UNION SELECT 20078,10011,'常州','' UNION SELECT 20079,10011,'苏州','' UNION SELECT 20080,10011,'南通','' UNION SELECT 20081,10011,'连云港','' UNION SELECT 20082,10011,'淮安','' UNION SELECT 20083,10011,'盐城','' UNION SELECT 20084,10011,'扬州','' UNION SELECT 20085,10011,'镇江','' UNION SELECT 20086,10011,'泰州','' UNION SELECT 20087,10011,'宿迁','' UNION SELECT 20088,10012,'杭州','' UNION SELECT 20089,10012,'宁波','' UNION SELECT 20090,10012,'温州','' UNION SELECT 20091,10012,'嘉兴','' UNION SELECT 20092,10012,'湖州','' UNION SELECT 20093,10012,'绍兴','' UNION SELECT 20094,10012,'金华','' UNION SELECT 20095,10012,'衢州','' UNION SELECT 20096,10012,'舟山','' UNION SELECT 20097,10012,'台州','' UNION SELECT 20098,10012,'丽水','' UNION SELECT 20099,10013,'合肥','' UNION SELECT 20100,10013,'芜湖','' UNION SELECT 20101,10013,'蚌埠','' UNION SELECT 20102,10013,'淮南','' UNION SELECT 20103,10013,'马鞍山','' UNION SELECT 20104,10013,'淮北','' UNION SELECT 20105,10013,'铜陵','' UNION SELECT 20106,10013,'安庆','' UNION SELECT 20107,10013,'黄山','' UNION SELECT 20108,10013,'滁州','' UNION SELECT 20109,10013,'阜阳','' UNION SELECT 20110,10013,'宿州','' UNION SELECT 20111,10013,'巢湖','' UNION SELECT 20112,10013,'六安','' UNION SELECT 20113,10013,'亳州','' UNION SELECT 20114,10013,'池州','' UNION SELECT 20115,10013,'宣城','' UNION SELECT 20116,10014,'福州','' UNION SELECT 20117,10014,'厦门','' UNION SELECT 20118,10014,'莆田','' UNION SELECT 20119,10014,'三明','' UNION SELECT 20120,10014,'泉州','' UNION SELECT 20121,10014,'漳州','' UNION SELECT 20122,10014,'南平','' UNION SELECT 20123,10014,'龙岩','' UNION SELECT 20124,10014,'宁德','' UNION SELECT 20125,10015,'南昌','' UNION SELECT 20126,10015,'景德镇','' UNION SELECT 20127,10015,'萍乡','' UNION SELECT 20128,10015,'九江','' UNION SELECT 20129,10015,'新余','' UNION SELECT 20130,10015,'鹰潭','' UNION SELECT 20131,10015,'赣州','' UNION SELECT 20132,10015,'吉安','' UNION SELECT 20133,10015,'宜春','' UNION SELECT 20134,10015,'抚州','' UNION SELECT 20135,10015,'上饶','' UNION SELECT 20136,10016,'济南','' UNION SELECT 20137,10016,'青岛','' UNION SELECT 20138,10016,'淄博','' UNION SELECT 20139,10016,'枣庄','' UNION SELECT 20140,10016,'东营','' UNION SELECT 20141,10016,'烟台','' UNION SELECT 20142,10016,'潍坊','' UNION SELECT 20143,10016,'济宁','' UNION SELECT 20144,10016,'泰安','' UNION SELECT 20145,10016,'威海','' UNION SELECT 20146,10016,'日照','' UNION SELECT 20147,10016,'莱芜','' UNION SELECT 20148,10016,'临沂','' UNION SELECT 20149,10016,'德州','' UNION SELECT 20150,10016,'聊城','' UNION SELECT 20151,10016,'滨州','' UNION SELECT 20152,10016,'菏泽','' UNION SELECT 20153,10017,'郑州','' UNION SELECT 20154,10017,'开封','' UNION SELECT 20155,10017,'洛阳','' UNION SELECT 20156,10017,'平顶山','' UNION SELECT 20157,10017,'安阳','' UNION SELECT 20158,10017,'鹤壁','' UNION SELECT 20159,10017,'新乡','' UNION SELECT 20160,10017,'焦作','' UNION SELECT 20161,10017,'濮阳','' UNION SELECT 20162,10017,'许昌','' UNION SELECT 20163,10017,'漯河','' UNION SELECT 20164,10017,'三门峡','' UNION SELECT 20165,10017,'南阳','' UNION SELECT 20166,10017,'商丘','' UNION SELECT 20167,10017,'信阳','' UNION SELECT 20168,10017,'周口','' UNION SELECT 20169,10017,'驻马店','' UNION SELECT 20170,10017,'济源','' UNION SELECT 20171,10018,'武汉','' UNION SELECT 20172,10018,'黄石','' UNION SELECT 20173,10018,'十堰','' UNION SELECT 20174,10018,'宜昌','' UNION SELECT 20175,10018,'襄阳','' UNION SELECT 20176,10018,'鄂州','' UNION SELECT 20177,10018,'荆门','' UNION SELECT 20178,10018,'孝感','' UNION SELECT 20179,10018,'荆州','' UNION SELECT 20180,10018,'黄冈','' UNION SELECT 20181,10018,'咸宁','' UNION SELECT 20182,10018,'随州','' UNION SELECT 20183,10018,'恩施州','' UNION SELECT 20184,10018,'潜江','' UNION SELECT 20185,10018,'神农架林区','' UNION SELECT 20186,10018,'天门','' UNION SELECT 20187,10018,'仙桃','' UNION SELECT 20188,10019,'长沙','' UNION SELECT 20189,10019,'株洲','' UNION SELECT 20190,10019,'湘潭','' UNION SELECT 20191,10019,'衡阳','' UNION SELECT 20192,10019,'邵阳','' UNION SELECT 20193,10019,'岳阳','' UNION SELECT 20194,10019,'常德','' UNION SELECT 20195,10019,'张家界','' UNION SELECT 20196,10019,'益阳','' UNION SELECT 20197,10019,'郴州','' UNION SELECT 20198,10019,'永州','' UNION SELECT 20199,10019,'怀化','' UNION SELECT 20200,10019,'娄底','' UNION SELECT 20201,10019,'湘西州','' UNION SELECT 20202,10020,'广州','' UNION SELECT 20203,10020,'深圳','' UNION SELECT 20204,10020,'珠海','' UNION SELECT 20205,10020,'汕头','' UNION SELECT 20206,10020,'韶关','' UNION SELECT 20207,10020,'佛山','' UNION SELECT 20208,10020,'江门','' UNION SELECT 20209,10020,'湛江','' UNION SELECT 20210,10020,'茂名','' UNION SELECT 20211,10020,'肇庆','' UNION SELECT 20212,10020,'惠州','' UNION SELECT 20213,10020,'梅州','' UNION SELECT 20214,10020,'汕尾','' UNION SELECT 20215,10020,'河源','' UNION SELECT 20216,10020,'阳江','' UNION SELECT 20217,10020,'清远','' UNION SELECT 20218,10020,'东莞','' UNION SELECT 20219,10020,'中山','' UNION SELECT 20220,10020,'潮州','' UNION SELECT 20221,10020,'揭阳','' UNION SELECT 20222,10020,'云浮','' UNION SELECT 20223,10021,'南宁','' UNION SELECT 20224,10021,'柳州','' UNION SELECT 20225,10021,'桂林','' UNION SELECT 20226,10021,'梧州','' UNION SELECT 20227,10021,'北海','' UNION SELECT 20228,10021,'防城港','' UNION SELECT 20229,10021,'钦州','' UNION SELECT 20230,10021,'贵港','' UNION SELECT 20231,10021,'玉林','' UNION SELECT 20232,10021,'百色','' UNION SELECT 20233,10021,'贺州','' UNION SELECT 20234,10021,'河池','' UNION SELECT 20235,10021,'来宾','' UNION SELECT 20236,10021,'崇左','' UNION SELECT 20237,10022,'海口','' UNION SELECT 20238,10022,'三亚','' UNION SELECT 20239,10022,'三沙','' UNION SELECT 20240,10022,'白沙','' UNION SELECT 20241,10022,'保亭','' UNION SELECT 20242,10022,'昌江','' UNION SELECT 20243,10022,'澄迈','' UNION SELECT 20244,10022,'儋州','' UNION SELECT 20245,10022,'定安','' UNION SELECT 20246,10022,'东方','' UNION SELECT 20247,10022,'乐东','' UNION SELECT 20248,10022,'临高','' UNION SELECT 20249,10022,'陵水','' UNION SELECT 20250,10022,'琼海','' UNION SELECT 20251,10022,'琼中','' UNION SELECT 20252,10022,'屯昌','' UNION SELECT 20253,10022,'万宁','' UNION SELECT 20254,10022,'文昌','' UNION SELECT 20255,10022,'五指山','' UNION SELECT 20256,10023,'成都','' UNION SELECT 20257,10023,'自贡','' UNION SELECT 20258,10023,'攀枝花','' UNION SELECT 20259,10023,'泸州','' UNION SELECT 20260,10023,'德阳','' UNION SELECT 20261,10023,'绵阳','' UNION SELECT 20262,10023,'广元','' UNION SELECT 20263,10023,'遂宁','' UNION SELECT 20264,10023,'内江','' UNION SELECT 20265,10023,'乐山','' UNION SELECT 20266,10023,'南充','' UNION SELECT 20267,10023,'眉山','' UNION SELECT 20268,10023,'宜宾','' UNION SELECT 20269,10023,'广安','' UNION SELECT 20270,10023,'达州','' UNION SELECT 20271,10023,'雅安','' UNION SELECT 20272,10023,'巴中','' UNION SELECT 20273,10023,'资阳','' UNION SELECT 20274,10023,'阿坝州','' UNION SELECT 20275,10023,'甘孜州','' UNION SELECT 20276,10023,'凉山州','' UNION SELECT 20277,10024,'贵阳','' UNION SELECT 20278,10024,'六盘水','' UNION SELECT 20279,10024,'遵义','' UNION SELECT 20280,10024,'安顺','' UNION SELECT 20281,10024,'铜仁','' UNION SELECT 20282,10024,'毕节','' UNION SELECT 20283,10024,'黔西南州','' UNION SELECT 20284,10024,'黔东南州','' UNION SELECT 20285,10024,'黔南州','' UNION SELECT 20286,10025,'昆明','' UNION SELECT 20287,10025,'曲靖','' UNION SELECT 20288,10025,'玉溪','' UNION SELECT 20289,10025,'保山','' UNION SELECT 20290,10025,'昭通','' UNION SELECT 20291,10025,'丽江','' UNION SELECT 20292,10025,'普洱','' UNION SELECT 20293,10025,'临沧','' UNION SELECT 20294,10025,'文山州','' UNION SELECT 20295,10025,'红河哈尼州','' UNION SELECT 20296,10025,'西双版纳州','' UNION SELECT 20297,10025,'楚雄州','' UNION SELECT 20298,10025,'大理州','' UNION SELECT 20299,10025,'德宏州','' UNION SELECT 20300,10025,'怒江州','' UNION SELECT 20301,10025,'迪庆州','' UNION SELECT 20302,10026,'拉萨','' UNION SELECT 20303,10026,'那曲','' UNION SELECT 20304,10026,'昌都','' UNION SELECT 20305,10026,'山南','' UNION SELECT 20306,10026,'日喀则','' UNION SELECT 20307,10026,'阿里','' UNION SELECT 20308,10026,'林芝','' UNION SELECT 20309,10027,'西安','' UNION SELECT 20310,10027,'铜川','' UNION SELECT 20311,10027,'宝鸡','' UNION SELECT 20312,10027,'咸阳','' UNION SELECT 20313,10027,'渭南','' UNION SELECT 20314,10027,'延安','' UNION SELECT 20315,10027,'汉中','' UNION SELECT 20316,10027,'榆林','' UNION SELECT 20317,10027,'安康','' UNION SELECT 20318,10027,'商洛','' UNION SELECT 20319,10028,'兰州','' UNION SELECT 20320,10028,'金昌','' UNION SELECT 20321,10028,'白银','' UNION SELECT 20322,10028,'天水','' UNION SELECT 20323,10028,'嘉峪关','' UNION SELECT 20324,10028,'武威','' UNION SELECT 20325,10028,'张掖','' UNION SELECT 20326,10028,'平凉','' UNION SELECT 20327,10028,'酒泉','' UNION SELECT 20328,10028,'庆阳','' UNION SELECT 20329,10028,'定西','' UNION SELECT 20330,10028,'陇南','' UNION SELECT 20331,10028,'临夏州','' UNION SELECT 20332,10028,'甘南州','' UNION SELECT 20333,10029,'西宁','' UNION SELECT 20334,10029,'海东','' UNION SELECT 20335,10029,'海北州','' UNION SELECT 20336,10029,'黄南州','' UNION SELECT 20337,10029,'海南州','' UNION SELECT 20338,10029,'果洛州','' UNION SELECT 20339,10029,'玉树州','' UNION SELECT 20340,10029,'海西州','' UNION SELECT 20341,10029,'德令哈','' UNION SELECT 20342,10029,'格尔木','' UNION SELECT 20343,10030,'银川','' UNION SELECT 20344,10030,'石嘴山','' UNION SELECT 20345,10030,'吴忠','' UNION SELECT 20346,10030,'固原','' UNION SELECT 20347,10030,'中卫','' UNION SELECT 20348,10031,'乌鲁木齐','' UNION SELECT 20349,10031,'克拉玛依','' UNION SELECT 20350,10031,'吐鲁番','' UNION SELECT 20351,10031,'哈密','' UNION SELECT 20352,10031,'昌吉回族州','' UNION SELECT 20353,10031,'博尔塔拉州','' UNION SELECT 20354,10031,'巴音郭楞州','' UNION SELECT 20355,10031,'阿克苏','' UNION SELECT 20356,10031,'克孜勒苏柯尔克孜州','' UNION SELECT 20357,10031,'喀什','' UNION SELECT 20358,10031,'和田','' UNION SELECT 20359,10031,'伊犁哈萨克州','' UNION SELECT 20360,10031,'塔城','' UNION SELECT 20361,10031,'阿勒泰','' UNION SELECT 20362,10031,'阿拉尔','' UNION SELECT 20363,10031,'北屯','' UNION SELECT 20364,10031,'石河子','' UNION SELECT 20365,10031,'图木舒克','' UNION SELECT 20366,10031,'五家渠','' UNION SELECT 20367,10031,'铁门关','';");
    }

    public boolean deleteDatabase(Context context)
    {
        boolean b = context.deleteDatabase(DATABASE_NAME);
        return b;
    }

    public Dao<Province, Integer> getProvinceDao()
    {
        if (null == provinceDao)
        {
            try
            {
                provinceDao = getDao(Province.class);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return provinceDao;
    }

    public Dao<City, Integer> getCityDao()
    {
        if (null == cityDao)
        {
            try
            {
                cityDao = getDao(City.class);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return cityDao;
    }
}
