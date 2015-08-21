package com.wezebra.zebraking.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 俊杰 on 2015/5/25.
 */
public abstract class BaseListAdapter<D> extends BaseAdapter
{
    protected List<D> items;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public BaseListAdapter(Context context)
    {
        this(context, null);
    }

    public BaseListAdapter(Context context, List<D> items)
    {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.items = items;
    }

    public void setItems(List<D> items)
    {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        if (items == null)
        {
            return 0;
        }
        return items.size();
    }

    @Override
    public D getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }
}