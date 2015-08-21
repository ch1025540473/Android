package com.wezebra.zebraking.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * User: superalex
 * Date: 2015/1/24
 * Time: 10:14
 */
@DatabaseTable(tableName = "province")
public class Province
{
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String name;
    @ForeignCollectionField
    private ForeignCollection<City> cities;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ForeignCollection<City> getCities()
    {
        return cities;
    }

    public void setCities(ForeignCollection<City> cities)
    {
        this.cities = cities;
    }
}
