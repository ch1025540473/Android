package com.wezebra.zebraking.ui.myaccount;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.data.TransData;
import com.wezebra.zebraking.model.MoneyFlow;
import com.wezebra.zebraking.ui.adapter.TransactionListAdapter;
import com.wezebra.zebraking.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionActivity extends BaseListActivity<MoneyFlow>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        TAG = "TransactionActivity";
        adapter = new TransactionListAdapter(this);
        clazz = TransData.class;
        api = Constants.API_GET_TRANS;
        layoutResource = R.layout.activity_transaction;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void addItems(List<MoneyFlow> newItems)
    {
        items.addAll(newItems);

        if (items.size() > 0)
        {
            hideEmptyLayout();
            adapter.setItems(formatItems(items));
        }
    }

    private List<MoneyFlow> formatItems(List<MoneyFlow> items)
    {
        List<MoneyFlow> newItems = new ArrayList<>();

        int oldMonth = -1;

        for (MoneyFlow entity : items)
        {
//            L.d(TAG, "oldMonth: " + oldMonth);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(entity.getCreateDate());

            int month = calendar.get(Calendar.MONTH);
//            L.d(TAG, "month: " + month);
            if (month != oldMonth)
            {
                MoneyFlow monthEntity = new MoneyFlow();
                monthEntity.setCreateDate(entity.getCreateDate());
                monthEntity.setViewType(TransactionListAdapter.TYPE_TIME);
                newItems.add(monthEntity);
                oldMonth = month;
            }

            entity.setViewType(TransactionListAdapter.TYPE_NORMAL);
            newItems.add(entity);
        }
        return newItems;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

    }
}
