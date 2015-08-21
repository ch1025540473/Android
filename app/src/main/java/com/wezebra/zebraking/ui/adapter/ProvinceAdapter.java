package com.wezebra.zebraking.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wezebra.zebraking.model.Province;
import com.wezebra.zebraking.util.GenericUtils;

import java.util.List;

/**
 * User: superalex
 * Date: 2015/1/31
 * Time: 23:48
 */
public class ProvinceAdapter extends BaseAdapter
{
    private List<Province> provinces;
    private Context context;

    public ProvinceAdapter(Context context)
    {
        this.context = context;
    }

    public ProvinceAdapter(Context context, List<Province> provinces)
    {
        this.context = context;
        this.provinces = provinces;
    }

    public void setItems(List<Province> cities)
    {
        this.provinces = cities;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        if (provinces == null)
        {
            return 0;
        }
        return provinces.size();
    }

    @Override
    public Province getItem(int position)
    {
        return provinces.get(position);
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
