package com.wezebra.zebraking.http.data;

import com.wezebra.zebraking.model.Bill;
import com.wezebra.zebraking.model.OrderDetail;
import com.wezebra.zebraking.model.OrderLog;
import com.wezebra.zebraking.model.Payee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 俊杰 on 2015/5/20.
 */
public class OrderDetailData
{
    private List<Bill> billList;
    private OrderDetail orderDetail;
    private ArrayList<OrderLog> orderLogList;
    private List<Payee> payeeList;

    public List<Bill> getBillList()
    {
        return billList;
    }

    public void setBillList(List<Bill> billList)
    {
        this.billList = billList;
    }

    public OrderDetail getOrderDetail()
    {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail)
    {
        this.orderDetail = orderDetail;
    }

    public ArrayList<OrderLog> getOrderLogList()
    {
        return orderLogList;
    }

    public void setOrderLogList(ArrayList<OrderLog> orderLogList)
    {
        this.orderLogList = orderLogList;
    }

    public List<Payee> getPayeeList()
    {
        return payeeList;
    }

    public void setPayeeList(List<Payee> payeeList)
    {
        this.payeeList = payeeList;
    }
}
