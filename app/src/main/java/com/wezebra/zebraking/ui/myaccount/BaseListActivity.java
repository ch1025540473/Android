package com.wezebra.zebraking.ui.myaccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusFailListener;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.DefaultErrorListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.BaseItemData;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.adapter.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 俊杰 on 2015/5/25.
 */
public abstract class BaseListActivity<D> extends BaseActivity implements AdapterView.OnItemClickListener
{
    protected static String TAG;
    protected ListView list;
    protected BaseListAdapter<D> adapter;
    protected List<D> items = new ArrayList<>();
    protected int currentPageNo = 1;
    protected int pageCount = 1;
    protected RelativeLayout emptyLayout;
    protected String api;
    protected Class clazz;
    protected int layoutResource = -1;
    private boolean isRefreshing = false;
    private View footer;
    private boolean isFirstLoad = true;
    protected FrameLayout footerFrame;

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener()
    {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState)
        {
            if ((view.getLastVisiblePosition() == view.getCount() - 1) && isRefreshing == false && hasMoreItems())
            {
                isRefreshing = true;
                getItems(currentPageNo + 1);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
        {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (layoutResource < 0)
        {
            layoutResource = R.layout.activity_base_list;
        }
        setContentView(layoutResource);
        initView();
        getItems(currentPageNo);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
//        items.clear();
//        adapter.notifyDataSetChanged();
//        getItems(1);
    }

    protected void initView()
    {
        list = (ListView) findViewById(R.id.list);

        footer = LayoutInflater.from(this).inflate(R.layout.item_progress, null);
        footerFrame = new FrameLayout(this);
        list.addFooterView(footerFrame);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        list.setOnScrollListener(onScrollListener);
        emptyLayout = (RelativeLayout) findViewById(R.id.list_empty);

    }

    protected void getItems(final int pageNo)
    {
        Map<String, String> params = new TreeMap<>();

        params.put("api", api);
        params.put("pageNo", pageNo + "");

        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                BaseItemData<D> data = (BaseItemData<D>) response.getData();
                currentPageNo = data.getPage().getPageNo();
                pageCount = data.getPage().getTotalPages();

                if (hasMoreItems())
                {
                    showFooter();
                } else
                {
                    hideFooter();
                }

                isRefreshing = false;
                isFirstLoad = false;

                addItems(data.getList());
            }
        },clazz).setCusFailListener(new CusFailListener()
        {
            @Override
            public void onFail(TaskResponse response)
            {
                showEmptyLayout();
                ZebraTask.handleFailResponse(BaseListActivity.this, response);
            }
        }).setCusErrorListener(new DefaultErrorListener.CusErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                showEmptyLayout();
            }
        }).setShowProgress(isFirstLoad).build().execute();
    }

    protected void addItems(List<D> newItems)
    {
        items.addAll(newItems);

        if (items.size() > 0)
        {
            hideEmptyLayout();
            adapter.setItems(items);
        }
    }

    protected boolean hasMoreItems()
    {
        return currentPageNo < pageCount;
    }

    protected void showEmptyLayout()
    {
        emptyLayout.setVisibility(View.VISIBLE);
    }

    protected void hideEmptyLayout()
    {
        emptyLayout.setVisibility(View.GONE);
    }

    protected void showFooter()
    {
        footerFrame.removeAllViews();
        footerFrame.addView(footer);

    }

    protected void hideFooter()
    {
        footerFrame.removeAllViews();
    }
}
