package com.wezebra.zebraking.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.OrderDetailData;
import com.wezebra.zebraking.model.Bill;
import com.wezebra.zebraking.model.OrderDetail;
import com.wezebra.zebraking.model.Payee;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.ui.adapter.RepaymentListAdapter;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.wxapi.WxPayActivity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
//还款页面，支付
public class RepaymentActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener
{
    public static final String TAG = "RepaymentActivity";
    private ListView list;
    private RepaymentListAdapter adapter;
    private TextView applyDetail;
    //这个是一次申请的订单号
    private long orderCode;
    //订单的详细数据
    private OrderDetailData data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment);

        orderCode = getIntent().getLongExtra("code", 0);

        init();

        if (orderCode == 0 && savedInstanceState != null)
        {
            orderCode = savedInstanceState.getLong("code");
        }
//        getOrderDetail(orderCode);
    }
    //每次都需要去获取订单的数据
    @Override
    protected void onResume()
    {
        super.onResume();
        getOrderDetail(orderCode);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState)
    {
        outState.putLong("code", orderCode);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void init()
    {
        applyDetail = (TextView) findViewById(R.id.apply_detail);
        applyDetail.setOnClickListener(this);
        list = (ListView) findViewById(R.id.list);
        adapter = new RepaymentListAdapter(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    private void getOrderDetail(Long orderCode)
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_GET_ORDERS_DETAIL);
        params.put("orderCode", orderCode + "");

        new ZebraTask.Builder(this, params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                data = (OrderDetailData) response.getData();
                adapter.setItems(data.getBillList());
            }
        },OrderDetailData.class).build().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        //账单，这个是每个月应该多少
        Bill bill = adapter.getItem(position);
        //会转跳到还款详情页面，这个是现下支付的方式，仅仅会提示还款到那个账号
        Intent intent = new Intent(this, RepaymentDetailActivity.class);
        // test,这个是做测试用的，因为我的的是线下，为了测试方便，这个要删除
        //逾期
        intent.putExtra(RepaymentDetailActivity.OVERCOUNT, bill.getOverCount());
        //偿还
        intent.putExtra(RepaymentDetailActivity.REPAYAMOUNT, bill.getRepayAmount());
        //逾期费用
        intent.putExtra(RepaymentDetailActivity.OVERDUEFEE, bill.getOverdueFee());
        //得到分期编号
        intent.putExtra("billCode", bill.getBillCode());
        //一个分期申请单号
        intent.putExtra("orderCode", bill.getOrderCode());

        Log.e("reapyment一起摇摆",bill.getBillCode()+"");
        //如果是在线上支付，调用微信支付接口
       if (data.getOrderDetail().getIsOnline() != null && data.getOrderDetail().getIsOnline().equals("1"))
            {
            /**
             *如果有两种支付方式，那么这个微信支付的界面就不能放在这里，
             *如果是线上支付的方式，应该进入选择付款方式的界面，需要把这个订单传递过去
             */

                final  Intent intent2 = new Intent(this,SelectPayWayActivity.class);
                bill.setType(WxPayActivity.PAY_REPAYMENT);
                intent2.putExtra("bill",bill);
                Map<String, String> params = new TreeMap<String,String>();
                params.put("api","lastPay");

                new ZebraTask.Builder(this, params, new CusSuccessListener()
                {
                    @Override
                    public void onSuccess(TaskResponse response)
                    {
                        LinkedHashMap<String ,Object> lastPay= (LinkedHashMap<String,Object>)response.getData();

                        if(!TextUtils.isEmpty((String)lastPay.get("payBy"))){
                            //支付方式
                            intent2.putExtra("payBy",(String)lastPay.get("payBy"));
                            //是否使用过连连支付
                            intent2.putExtra("hasUsedll",(boolean)lastPay.get("hasUsedll"));
                            startActivity(intent2);
                        }else {
                            //发生错误的时候会走到这里
                            throw new  RuntimeException(RepaymentActivity.class.toString()+"157 line error");
                        }
                    }
                }).setShowProgress(false).build().execute();
            return;
        } else
        {   //采用现下支付，这个是线下支付模式
            intent.putExtra("isOnline", false);
         }


        //进行网络访问查询上次的支付方式

      /*  Map<String, String> params = new TreeMap<String,String>();连连支付所需参数
        params.put("api","lianlianPay");
        params.put("payfor","repay");
        params.put("orderCode",bill.getOrderCode()+"");
        params.put("billCode",bill.getBillCode()+"");
        params.put("idNo","500236198908173318");
        params.put("name","熊本安");
        params.put("channel","android");*/





        //如果是线下的支付，就会执行到这里
       startActivity(intent);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(RepaymentActivity.this, ApplyDetailActivity.class);
        Payee payee = data.getPayeeList().get(0);
        intent.putExtra("name", payee.getName());
        intent.putExtra("identity", payee.getPayIdentity());
        intent.putExtra("phone", payee.getAccountPhone());
        intent.putExtra("code", payee.getFormatAccountNo());
        intent.putExtra("address", payee.getCityName());

        OrderDetail detail = data.getOrderDetail();
        intent.putExtra("amount", detail.getMonthlyFee());
        intent.putExtra("stage", detail.getStagingMonth());
        intent.putExtra("rate", detail.getRate());
        intent.putExtra("servFee", detail.getServFee());

        intent.putParcelableArrayListExtra("list", data.getOrderLogList());
        startActivity(intent);
    }
}
