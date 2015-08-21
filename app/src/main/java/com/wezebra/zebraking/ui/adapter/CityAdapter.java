package com.wezebra.zebraking.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wezebra.zebraking.model.City;
import com.wezebra.zebraking.util.GenericUtils;

import java.util.List;

/**
 * User: superalex
 * Date: 2015/1/31
 * Time: 23:48
 */
public class CityAdapter extends BaseAdapter
{
    private List<City> cities;
    private Context context;

    public CityAdapter(Context context)
    {
        this.context = context;
    }

    public CityAdapter(Context context, List<City> cities)
    {
        this.context = context;
        this.cities = cities;
    }

    public void setItems(List<City> cities)
    {
        this.cities = cities;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        if (cities == null)
        {
            return 0;
        }
        return cities.size();
    }

    @Override
    public City getItem(int position)
    {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            int padding = (int) GenericUtils.dp2px(5f, context);
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(padding, padding, padding, padding);
            convertView = textView;
        }

        ((TextView) convertView).setText(getItem(position).getName());

        return convertView;
    }
}
