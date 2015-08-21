package com.wezebra.zebraking.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.wezebra.zebraking.http.data.TendersData;
import com.wezebra.zebraking.model.Tender;
import com.wezebra.zebraking.ui.adapter.TenderListAdapter;
import com.wezebra.zebraking.util.Constants;

public class TenderActivity extends BaseListActivity<Tender>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        TAG = "TenderActivity";
        adapter = new TenderListAdapter(this);
        clazz = TendersData.class;
        api = Constants.API_GET_TENDERS;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Tender tender = adapter.getItem(position);
        Intent intent = new Intent(this,InvestActivity.class);
        intent.putExtra("proId",tender.getProId());
        intent.putExtra("limit",tender.getBorrTotAmt()-tender.getAmt());
        startActivity(intent);
    }
}
