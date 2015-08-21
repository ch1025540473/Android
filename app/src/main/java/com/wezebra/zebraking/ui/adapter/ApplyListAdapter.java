package com.wezebra.zebraking.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.Order;
import com.wezebra.zebraking.util.DateUtils;

import java.util.List;

/**
 * Created by 俊杰 on 2015/4/10.
 */
public class ApplyListAdapter extends BaseListAdapter<Order>
{

    public ApplyListAdapter(Context context)
    {
        this(context, null);
    }

    public ApplyListAdapter(Context context, List<Order> items)
    {
        super(context,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_apply, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = getItem(position);
        holder.status.setText(order.getStatus().getDescription());
        holder.code.setText(order.getOrderCode() + "");
        holder.time.setText(DateUtils.toTime(order.getCreateTime(), DateUtils.DATE_FORMAT_TRANSACTION));
        holder.amount.setText("￥" + order.getFormatAmount());
        if(TextUtils.isEmpty(order.getReferee()))
        {
            holder.refereeLayout.setVisibility(View.GONE);
        }
        else
        {
            holder.refereeLayout.setVisibility(View.VISIBLE);
            holder.referee.setText(order.getReferee());
        }

        return convertView;
    }

    private class ViewHolder
    {
        LinearLayout refereeLayout;
        TextView status;
        TextView code;
        TextView time;
        TextView amount;
        TextView referee;

        ViewHolder(View convertView)
        {
            status = (TextView) convertView.findViewById(R.id.apply_status);
            code = (TextView) convertView.findViewById(R.id.apply_code);
            time = (TextView) convertView.findViewById(R.id.apply_time);
            amount = (TextView) convertView.findViewById(R.id.apply_amount);
            refereeLayout = (LinearLayout)convertView.findViewById(R.id.apply_referee_layout);
            referee = (TextView)convertView.findViewById(R.id.apply_referee);
        }
    }
}
