package com.wezebra.zebraking.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.MoneyFlow;
import com.wezebra.zebraking.util.DateUtils;
import com.wezebra.zebraking.widget.PinnedSectionListView;

import java.util.List;

/**
 * Created by 俊杰 on 2015/4/8.
 */
public class TransactionListAdapter extends BaseListAdapter<MoneyFlow> implements PinnedSectionListView.PinnedSectionListAdapter
{
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_TIME = -1;
    private Drawable bg_income;
    private Drawable bg_unfreeze;
    private Drawable bg_freeze;
    private int text_green;
    private int text_red;
    private int text_light;
    private int text_white;

    public TransactionListAdapter(Context context)
    {
        this(context, null);
    }

    public TransactionListAdapter(Context context, List<MoneyFlow> items)
    {
        super(context,items);
        bg_income = mContext.getResources().getDrawable(R.drawable.transaction_income);
        bg_unfreeze = mContext.getResources().getDrawable(R.drawable.transaction_unfreeze);
        bg_freeze = mContext.getResources().getDrawable(R.drawable.transaction_freeze);
        text_green = mContext.getResources().getColor(R.color.text_green);
        text_light = mContext.getResources().getColor(R.color.text_light);
        text_red = mContext.getResources().getColor(R.color.text_red);
        text_white = mContext.getResources().getColor(R.color.white);
    }

    @Override
    public boolean isItemViewTypePinned(int viewType)
    {
        return viewType == TYPE_TIME;
    }

    @Override
    public int getViewTypeCount()
    {
        return 3;
    }

    @Override
    public int getItemViewType(int position)
    {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        ViewHolderTime holderTime = null;
        int viewType = getItemViewType(position);
        if (convertView == null)
        {
            switch (viewType)
            {
                case TYPE_TIME:
                    convertView = mInflater.inflate(R.layout.item_transaction_header, null);
                    holderTime = new ViewHolderTime(convertView);
                    convertView.setTag(holderTime);
                    break;
                case TYPE_NORMAL:
                    convertView = mInflater.inflate(R.layout.item_transaction, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                    break;
            }
        } else
        {
            switch (viewType)
            {
                case TYPE_TIME:
                    holderTime = (ViewHolderTime) convertView.getTag();
                    break;
                case TYPE_NORMAL:
                    holder = (ViewHolder) convertView.getTag();
                    break;
            }
        }
        MoneyFlow transaction = getItem(position);
        switch (viewType)
        {
            case TYPE_TIME:
                holderTime.header.setText(DateUtils.toTime(transaction.getCreateDate(), DateUtils.DATE_FORMAT_YEAR_MONTH));
                break;
            case TYPE_NORMAL:
                holder.name.setText(transaction.getMark());
                holder.time.setText(DateUtils.toTime(transaction.getCreateDate(), DateUtils.DATE_FORMAT_ALL));
                switch (transaction.getTransType())
                {
                    case MoneyFlow.STATE_INCOME:
                        holder.type.setBackgroundDrawable(bg_income);
                        holder.type.setTextColor(text_white);
                        holder.type.setText("收");
                        holder.amount.setTextColor(text_green);
                        holder.amount.setText("+" + transaction.getAmtStr());
                        break;
                    case MoneyFlow.STATE_OUTCOME:
                        holder.type.setBackgroundDrawable(bg_income);
                        holder.type.setTextColor(text_white);
                        holder.type.setText("支");
                        holder.amount.setTextColor(text_red);
                        holder.amount.setText(transaction.getAmtStr());
                        break;
                    case MoneyFlow.STATE_UNFREEZE:
                        holder.type.setBackgroundDrawable(bg_unfreeze);
                        holder.type.setTextColor(text_green);
                        holder.type.setText("解");
                        holder.amount.setTextColor(text_light);
                        holder.amount.setText(transaction.getInvolveAmtStr());
                        break;
                    case MoneyFlow.STATE_FREEZE:
                        holder.type.setBackgroundDrawable(bg_freeze);
                        holder.type.setTextColor(text_light);
                        holder.type.setText("冻");
                        holder.amount.setTextColor(text_light);
                        holder.amount.setText(transaction.getInvolveAmtStr());
                        break;
                }
        }
        return convertView;
    }

    private class ViewHolderTime
    {
        TextView header;

        ViewHolderTime(View convertView)
        {
            header = (TextView) convertView.findViewById(R.id.transaction_header);
        }
    }

    private class ViewHolder
    {
        TextView type;
        TextView name;
        TextView time;
        TextView amount;

        ViewHolder(View convertView)
        {
            type = (TextView) convertView.findViewById(R.id.transaction_type);
            name = (TextView) convertView.findViewById(R.id.transaction_name);
            time = (TextView) convertView.findViewById(R.id.transaction_time);
            amount = (TextView) convertView.findViewById(R.id.transaction_amount);
        }
    }
}
