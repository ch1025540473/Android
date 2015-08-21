package com.wezebra.zebraking.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.Bill;
import com.wezebra.zebraking.util.CommonUtils;

import java.util.List;

/**
 * Created by 俊杰 on 2015/4/8.
 */
public class RepaymentListAdapter extends BaseAdapter
{
    private List<Bill> items;
    private Context mContext;
    private LayoutInflater mInflater;
    private Drawable ic_next;
    private Drawable ic_next_light;
    private int font_light;
    private int font_dark;
    private int font_red;
    private int font_green;

    public RepaymentListAdapter(Context context)
    {
        this(context, null);
    }

    public RepaymentListAdapter(Context context, List<Bill> items)
    {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
//        ic_next = mContext.getResources().getDrawable(R.drawable.ic_action_next_item_dark);
        ic_next = mContext.getResources().getDrawable(R.drawable.ic_next_green);
        ic_next.setBounds(0, 0, ic_next.getMinimumWidth(), ic_next.getMinimumHeight());
        ic_next_light = mContext.getResources().getDrawable(R.drawable.ic_action_next_item_light);
        ic_next_light.setBounds(0, 0, ic_next_light.getMinimumWidth(), ic_next_light.getMinimumHeight());
        font_light = mContext.getResources().getColor(R.color.text_light);
        font_dark = mContext.getResources().getColor(R.color.text_dark);
        font_red = mContext.getResources().getColor(R.color.text_red);
        font_green = mContext.getResources().getColor(R.color.text_green);
        this.items = items;
    }

    public void setItems(List<Bill> items)
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
    public Bill getItem(int position)
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
        int state = getItem(position).getBillStatus().getState();
        return 1 == state;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_repayment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Bill bill = getItem(position);
        holder.stage.setText((position + 1) + "期");
        holder.money.setText(CommonUtils.formatDouble(bill.getOverCount()));
        holder.time.setText(bill.getFormatRepayDate());
        holder.status.setText(bill.getBillStatus().getDescription());
        if (bill.getBillStatus().getState() == 1)
        {
            holder.status.setCompoundDrawables(null, null, ic_next, null);
//            holder.money.setTextColor(font_red);
            holder.status.setTextColor(font_green);
            if (bill.getOverdueDay() == 0)
            {
//                holder.stage.setTextColor(font_dark);
//                holder.money.setTextColor(font_dark);
//                holder.time.setTextColor(font_dark);
//                holder.status.setTextColor(font_dark);
            } else if (bill.getOverdueDay() > 0)
            {
//                holder.stage.setTextColor(font_dark);
//                holder.money.setTextColor(font_red);
//                holder.time.setTextColor(font_red);
//                holder.status.setTextColor(font_dark);
//                holder.status.setText("已逾期");

                String principal = CommonUtils.formatDouble(bill.getRepayAmount());
                String interest = CommonUtils.formatDouble(bill.getOverdueFee());

                SpannableString ss = new SpannableString(principal + "+" + interest + "(逾期)");
                ss.setSpan(new ForegroundColorSpan(font_red),
                        principal.length(), ss.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.money.setText(ss);
            }
        } else
        {
            holder.status.setCompoundDrawables(null, null, ic_next_light, null);
//            holder.stage.setTextColor(font_dark);
//            holder.money.setTextColor(font_dark);
//            holder.time.setTextColor(font_light);
            holder.status.setTextColor(font_dark);
        }
        return convertView;
    }

    private class ViewHolder
    {
        TextView stage;
        TextView money;
        TextView time;
        TextView status;

        ViewHolder(View convertView)
        {
            stage = (TextView) convertView.findViewById(R.id.stage);
            money = (TextView) convertView.findViewById(R.id.money);
            time = (TextView) convertView.findViewById(R.id.time);
            status = (TextView) convertView.findViewById(R.id.status);
        }
    }
}
