package com.wezebra.zebraking.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.Bill;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.util.CommonUtils;
import com.wezebra.zebraking.widget.SinglePayItem;
import com.wezebra.zebraking.wxapi.WxPayHelper;

import utils.LlPayHelper;

import utils.LlPayHelper;

//选择付款方式  连连支付和微信支付
public class SelectPayWayActivity extends BaseActivity implements View.OnClickListener{

    private TextView wezebra_pay_amount ;
    private Button wezebra_sure_pay ;

    //选择付款的方式
    private String currentSelectPayWay ;

    private Bill bill;
    private String lastBy;
    private boolean hasUsedll;
    private SinglePayItem wezebra_weixin_method;
    private SinglePayItem wezebra_lianlian_method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pay_way);
        initView();
        initData();
        initListener();
    }

    public void initView(){
        wezebra_pay_amount=(TextView)findViewById(R.id.wezebra_pay_amount);
        wezebra_sure_pay=(Button)findViewById(R.id.wezebra_sure_pay);
        wezebra_weixin_method=(SinglePayItem)findViewById(R.id.wezebra_weixin_method);
        wezebra_lianlian_method=(SinglePayItem)findViewById(R.id.wezebra_lianlian_method);
    }

    public void initData(){
        Intent intent=getIntent();
        bill=(Bill)intent.getSerializableExtra("bill");
        lastBy=intent.getStringExtra("payBy");
        //测试
        //hasUsedll=false;
        hasUsedll=intent.getBooleanExtra("hasUsedll",false);

        currentSelectPayWay=lastBy;
        //支付总逾期的金额
        wezebra_pay_amount.setText(CommonUtils.formatDouble(bill.getOverCount())+"元");
        //如果等于微信，要默认选中微信，如果是连连支付，要选中连连支付
        changePayMethod();
    }
    //根据当前选择的支付方式来改变UI界面,如果选择微信，那么就应隐藏连连支付勾号图标
    private void changePayMethod() {
        if(currentSelectPayWay.equals("weixin")){
            wezebra_weixin_method.setWezebra_select_icon_visiable(View.VISIBLE);
            wezebra_lianlian_method.setWezebra_select_icon_visiable(View.INVISIBLE);
        }else if (currentSelectPayWay.equals("lianlian")){
            wezebra_weixin_method.setWezebra_select_icon_visiable(View.INVISIBLE);
            wezebra_lianlian_method.setWezebra_select_icon_visiable(View.VISIBLE);
        }
    }

    public void  initListener(){
        wezebra_sure_pay.setOnClickListener(this);
        wezebra_lianlian_method.setOnClickListener(this);
        wezebra_weixin_method.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //在这里判断是微信支付还是连连支付
        //在按钮点击以后，要设置为不可点击
        switch (view.getId()) {
            case R.id.wezebra_sure_pay:
                //wezebra_sure_pay.setClickable(false);
                if(currentSelectPayWay.equals("weixin")){
                    /*//转跳到微信支付页面
                    Intent intent2 = new Intent(this, WxPayActivity.class);
                    //支付的类型，微信支付
                    intent2.putExtra("type",bill.getType());
                    //订单号码
                    intent2.putExtra("order", bill.getOrderCode());
                    //每一期申请的订单,
                    intent2.putExtra("bill",bill.getBillCode());
                    //还款的金额
                    intent2.putExtra("amount", CommonUtils.formatDouble(bill.getOverCount()));
                    startActivity(intent2);

                    */
                    WxPayHelper.getInstance().pay(SelectPayWayActivity.this,bill);

                }else if(currentSelectPayWay.equals("lianlian") && hasUsedll){
                    //这个地方可能会存在内存泄漏
                    LlPayHelper.getInstance().pay(bill,this);
                }else if(currentSelectPayWay.equals("lianlian") && !hasUsedll){
                    //如果是第一次还需要绑定银行卡
                    Intent intent2 = new Intent(SelectPayWayActivity.this,PersonLLInfoCertificate.class);
                    intent2.putExtra("bill",bill);
                    startActivity(intent2);
                }

                break;
            //还要设置两种付款方式的选项
            case R.id.wezebra_weixin_method :
                currentSelectPayWay="weixin";
                changePayMethod();
                break;
            case R.id.wezebra_lianlian_method:
                currentSelectPayWay="lianlian";
                changePayMethod();
                break;
        }
    }




}
