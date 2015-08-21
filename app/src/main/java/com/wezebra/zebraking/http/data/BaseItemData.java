package com.wezebra.zebraking.http.data;

import com.wezebra.zebraking.model.Page;

import java.util.List;

/**
 * Created by 俊杰 on 2015/5/25.
 */
public class BaseItemData<D>
{
    private List<D> list;
    private Page page;

    public List<D> getList()
    {
        return list;
    }

    public void setList(List<D> list)
    {
        this.list = list;
    }

    public Page getPage()
    {
        return page;
    }

    public void setPage(Page page)
    {
        this.page = page;
    }}
