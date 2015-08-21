package com.wezebra.zebraking.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.OrderLog;
import com.wezebra.zebraking.util.DateUtils;

import java.util.List;

/**
 * Created by 俊杰 on 2015/5/21.
 */
public class OrderLogListAdapter extends BaseAdapter
{
    private List<OrderLog> items;
    private Context mContext;
    private LayoutInflater mInflater;

    public OrderLogListAdapter(Context context)
    {
        this(context,null);
    }

    public OrderLogListAdapter(Context context, List<OrderLog> items)
    {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.items = items;
    }

    public void setItems(List<OrderLog> items)
    {
        this.items = items;
        notifyDataSetChanged();;
    }

    @Override
    public int getCount()
    {
        if(null == items)
        {
            return 0;
        }
        return items.size();
    }

    @Override
    public OrderLog getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder= null;
        if(null==convertView)
        {
            convertView  = mInflater.inflate(R.layout.item_order_log,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.time.setText(DateUtils.toTime(getItem(position).getOpTime(),DateUtils.DATE_FORMAT_TRANSACTION));
        holder.memo.setText(getItem(position).getMemo());
        return convertView;
    }

    private class ViewHolder
    {
        TextView time;
        TextView memo;

        ViewHolder(View convertView)
        {
            time = (TextView)convertView.findViewById(R.id.log_time);
            memo = (TextView)convertView.findViewById(R.id.log_memo);
        }
    }
}
