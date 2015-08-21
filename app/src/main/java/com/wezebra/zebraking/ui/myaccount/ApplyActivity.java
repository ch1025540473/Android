package com.wezebra.zebraking.ui.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.wezebra.zebraking.http.data.OrdersData;
import com.wezebra.zebraking.model.Order;
import com.wezebra.zebraking.ui.adapter.ApplyListAdapter;
import com.wezebra.zebraking.ui.installment.AssessCountdownActivity;
import com.wezebra.zebraking.ui.installment.GrantActivity;
import com.wezebra.zebraking.ui.installment.SIAStepOneResultActivity;
import com.wezebra.zebraking.ui.installment.SIAStepThreeResultActivity;
import com.wezebra.zebraking.ui.installment.SIAStepTwoResultActivity;
import com.wezebra.zebraking.ui.installment.SubmitInstallmentApplicationStepOneActivity;
import com.wezebra.zebraking.ui.installment.SubmitInstallmentApplicationStepThreeActivity;
import com.wezebra.zebraking.ui.installment.SubmitInstallmentApplicationStepTwoActivity;
import com.wezebra.zebraking.util.Constants;

public class ApplyActivity extends BaseListActivity<Order>
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        TAG = "ApplyActivity";
        adapter = new ApplyListAdapter(this);
        clazz = OrdersData.class;
        api = Constants.API_GET_ORDERS;
        long code = getIntent().getLongExtra("code", 0);

        // 推送消息
        if (code > 0)
        {
            Intent intent = new Intent(this, RepaymentActivity.class);
            intent.putExtra("code", code);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Order order = adapter.getItem(position);
        int status = order.getStatus().getState();
        Intent intent = new Intent();
        Class clazz = null;

        switch (status)
        {
            case Constants.ORDER_WAITING_REPAY:
                //成功
            case Constants.ORDER_SUCCESS:

                intent.putExtra("code", order.getOrderCode());
                clazz = RepaymentActivity.class;
                break;
            //申请关闭
            case Constants.ORDER_CLOSED:
                Toast.makeText(this, "您的申请已关闭，如有需要请重新申请", Toast.LENGTH_SHORT).show();
                return;
            //申请未通过
            case Constants.ORDER_UNPASS:
                Toast.makeText(this, "您的申请未通过，如有需要请重新申请", Toast.LENGTH_SHORT).show();
                return;

            case Constants.ORDER_AUDITING_FIRST:
                clazz = AssessCountdownActivity.class;
                break;
            case Constants.ORDER_WAITING_IMPROVE:
                clazz = SubmitInstallmentApplicationStepOneActivity.class;
                break;
            case Constants.ORDER_AUDITING_BASIC:
                clazz = SIAStepOneResultActivity.class;
                break;
            case Constants.ORDER_WAITING_ADDED:
                clazz = SubmitInstallmentApplicationStepTwoActivity.class;
                break;
            case Constants.ORDER_AUDITING_ADDED:
                clazz = SIAStepTwoResultActivity.class;
                break;
            case Constants.ORDER_WAITING_MODIFY:
                switch (order.getStep())
                {
                    case 2:
                        clazz = SubmitInstallmentApplicationStepOneActivity.class;
                        break;
                    case 3:
                        clazz = SubmitInstallmentApplicationStepTwoActivity.class;
                        break;
                }
                break;
            case Constants.ORDER_WAITING_PAY_FIRST:
                if (null != order.getIsOnline() &&order.getIsOnline().equals("1"))
                {
                    intent.putExtra("isOnLine", true);
                } else
                {
                    intent.putExtra("isOnLine", false);
                }
                clazz = SubmitInstallmentApplicationStepThreeActivity.class;
                break;
            //
            case Constants.ORDER_WAITING_GRANT:
                clazz = GrantActivity.class;
                break;
            //放款
            case Constants.ORDER_AUDITING_LOAN:
                clazz = SIAStepThreeResultActivity.class;
                break;
            default:

                break;

        }

        if (clazz != null)
        {
            intent.setClass(this, clazz);
            startActivity(intent);
        }

    }
}
