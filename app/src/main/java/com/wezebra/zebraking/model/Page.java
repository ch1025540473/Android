package com.wezebra.zebraking.model;

/**
 * Created by 俊杰 on 2015/5/20.
 */
public class Page
{
    private int nextPage;
    private int pageNo;
    private int pageSize;
    private int prePage;
    private int totalCount;
    private int totalPages;

    public int getNextPage()
    {
        return nextPage;
    }

    public void setNextPage(int nextPage)
    {
        this.nextPage = nextPage;
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getPrePage()
    {
        return prePage;
    }

    public void setPrePage(int prePage)
    {
        this.prePage = prePage;
    }

    public int getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(int totalCount)
    {
        this.totalCount = totalCount;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public void setTotalPages(int totalPages)
    {
        this.totalPages = totalPages;
    }
}
