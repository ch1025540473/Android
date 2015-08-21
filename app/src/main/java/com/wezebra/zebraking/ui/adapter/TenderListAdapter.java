package com.wezebra.zebraking.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.Tender;

import java.util.List;

/**
 * Created by 俊杰 on 2015/4/9.
 */
public class TenderListAdapter extends BaseListAdapter<Tender>
{
//    private List<Tender> items;
//    private Context mContext;
//    private LayoutInflater mInflater;

    public TenderListAdapter(Context context)
    {
        this(context, null);
    }

    public TenderListAdapter(Context context, List<Tender> items)
    {
//        mContext = context;
//        mInflater = LayoutInflater.from(mContext);
//        this.items = items;
        super(context,items);
    }

//    public void setItems(List<Tender> items)
//    {
//        this.items = items;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getCount()
//    {
//        if (items == null)
//        {
//            return 0;
//        }
//        return items.size();
//    }
//
//    @Override
//    public Tender getItem(int position)
//    {
//        return items.get(position);
//    }
//
//    @Override
//    public long getItemId(int position)
//    {
//        return 0;
//    }

    @Override
    public boolean isEnabled(int position)
    {
        return 2 == getItem(position).getTenderStatus().getState();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.item_tender, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Tender tender = getItem(position);
//        holder.name.setText(tender.getName());
        holder.proId.setText(tender.getProId());
        holder.rate.setText(tender.getFormatRate() + "%");
        holder.time.setText(tender.getStagesNum() + "个月");
        holder.amount.setText(tender.getBorrTotAmtFormart() + "元");
        holder.status.setText(tender.getTenderStatus().getDescription());
        if (tender.getTenderStatus().getState() == 2)
        {
            holder.status.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.main_color));
            holder.status.setText("立即投资");
        } else
        {
            holder.status.setTextColor(mContext.getResources().getColor(R.color.text_dark));
            holder.status.setBackgroundColor(mContext.getResources().getColor(R.color.unknown_grey));
        }
        return convertView;
    }

    private class ViewHolder
    {
        TextView name;
        TextView rate;
        TextView time;
        TextView amount;
        TextView status;
        TextView proId;

        ViewHolder(View convertView)
        {
            name = (TextView) convertView.findViewById(R.id.invest_name);
            rate = (TextView) convertView.findViewById(R.id.invest_rate);
            time = (TextView) convertView.findViewById(R.id.invest_time);
            amount = (TextView) convertView.findViewById(R.id.invest_amount);
            status = (TextView) convertView.findViewById(R.id.invest_status);
            proId = (TextView) convertView.findViewById(R.id.invest_proId);
        }
    }
}
