package com.wezebra.zebraking.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * User: superalex
 * Date: 2015/1/24
 * Time: 10:14
 */
@DatabaseTable(tableName = "city")
public class City
{
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String name;
    @DatabaseField(foreign = true,foreignAutoRefresh = true, columnName = "prov_id")
    private Province province;
    @DatabaseField
    private String code;

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

    public Province getProvince()
    {
        return province;
    }

    public void setProvince(Province province)
    {
        this.province = province;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
