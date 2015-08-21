package com.wezebra.zebraking.orm;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.wezebra.zebraking.model.City;
import com.wezebra.zebraking.model.Province;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: superalex
 * Date: 2015/1/24
 * Time: 10:52
 */
public class DBManager
{
    public static final String TAG = "DBManager";
    private static DBManager manager;
    private DBHelper helper;
    private Dao<Province, Integer> provinceDao;
    private Dao<City, Integer> cityDao;

    private DBManager(Context context)
    {
        helper = OpenHelperManager.getHelper(context, DBHelper.class);
        helper.getWritableDatabase();
        provinceDao = helper.getProvinceDao();
        cityDao = helper.getCityDao();
    }

    public static void create(Context context)
    {
        if (manager == null)
        {
            manager = new DBManager(context);
        }
    }

    public static DBManager getInstance()
    {
        return manager;
    }

    public void close()
    {
        if (helper != null)
        {
            helper.close();
        }
        OpenHelperManager.releaseHelper();
        helper = null;
    }

    /**
     * ************************************ 以下为province操作 *****************************************
     */
    public Province queryProvinceById(int id) throws SQLException
    {
        return provinceDao.queryForId(id);
    }

    public List<Province> queryAllProvince() throws SQLException
    {
        return provinceDao.queryForAll();
    }

    /**
     * ************************************ 以下为city操作 *****************************************
     */
    public City queryCityById(int id) throws SQLException
    {
        return cityDao.queryForId(id);
    }

    public List<City> queryCitiesForProvince(int provinceId) throws SQLException
    {
        List<City> cities = new ArrayList<>();
        Province province = queryProvinceById(provinceId);
        ForeignCollection<City> cityCollection = province.getCities();
        CloseableIterator<City> iterator = cityCollection.closeableIterator();

        while (iterator.hasNext())
        {
            City city = iterator.next();
            cities.add(city);
        }
        return cities;
    }

    public List<City> queryAllCity() throws SQLException
    {
        return cityDao.queryForAll();
    }
}
