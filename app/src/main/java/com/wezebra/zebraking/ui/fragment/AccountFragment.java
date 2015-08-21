package com.wezebra.zebraking.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.HttpUtils;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.UserInfoData;
import com.wezebra.zebraking.ui.MainActivity;
import com.wezebra.zebraking.ui.myaccount.AboutActivity;
import com.wezebra.zebraking.ui.myaccount.ApplyActivity;
import com.wezebra.zebraking.ui.myaccount.AssetActivity;
import com.wezebra.zebraking.ui.myaccount.PersonalInfoActivity;
import com.wezebra.zebraking.ui.myaccount.TenderActivity;
import com.wezebra.zebraking.ui.myaccount.TransactionActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.PreferenceUtils;

import java.util.Map;
import java.util.TreeMap;

public class AccountFragment extends Fragment implements View.OnClickListener
{
    public static final String TAG = "AccountFragment";
    private MainActivity mainActivity;
    private RelativeLayout personal;
    private RelativeLayout asset;
    private RelativeLayout apply;
    private RelativeLayout invest;
    private RelativeLayout transactionDetail;
    private RelativeLayout about;
    private TextView tvName;
    private TextView tvPhone;
    // 用于真正的实现 onResume(), onPause()
    private int count = -1;

    private boolean dialogDismiss = false;
    public static boolean shouldRefresh = false;
    private boolean isDialogShowing = false;

    private LinearLayout openHFDialog;
    private TextView dialogOk;
    private TextView fastRepay;

    public AccountFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        personal = (RelativeLayout) v.findViewById(R.id.account_personal);
        asset = (RelativeLayout) v.findViewById(R.id.account_asset);
        apply = (RelativeLayout) v.findViewById(R.id.account_apply);
        invest = (RelativeLayout) v.findViewById(R.id.account_invest);
        transactionDetail = (RelativeLayout) v.findViewById(R.id.account_transaction_detail);
        about = (RelativeLayout) v.findViewById(R.id.account_about);
        tvName = (TextView) v.findViewById(R.id.account_name);
        tvPhone = (TextView) v.findViewById(R.id.account_phone);
        personal.setOnClickListener(this);
        asset.setOnClickListener(this);
        apply.setOnClickListener(this);
        invest.setOnClickListener(this);
        transactionDetail.setOnClickListener(this);
        about.setOnClickListener(this);

        //  非投资人隐藏投资项
        if (3 != PreferenceUtils.getRole())
        {
            invest.setVisibility(View.GONE);
        }

        openHFDialog = (LinearLayout) v.findViewById(R.id.openHFDialog);

        // 拦截点击事件
        openHFDialog.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        dialogOk = (TextView) v.findViewById(R.id.dialog_ok);
        dialogOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openHFAccount();
                hideDialog();
            }
        });

        fastRepay = (TextView) v.findViewById(R.id.fast_repay);
        fastRepay.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

//        if (null == App.userDetailData)
//        {
//            refreshUserDetail();
//        }

        getUserInfo();
        tvPhone.setText(PreferenceUtils.getPhoneNum());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        count++;
        setUserVisibleHint(true);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        count--;
        setUserVisibleHint(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser)
        {
            count++;

        } else
        {
            count--;

        }

//        L.d(TAG, "=== setUserVisibleHint: " + isVisibleToUser);
//        L.d(TAG, "=== count: " + count);

        // count >0 可视为 onResume, 反之为 onPause()
        if (count > 0)
        {
            MobclickAgent.onPageStart(TAG);
//            if (null != App.userDetailData)
//            {
//                if (TextUtils.isEmpty(App.userDetailData.getHfAccount().getUsrCustId()))
//                {
//                    if (dialogDismiss)
//                    {
//                        dialogDismiss = false;
//                    } else if (shouldRefresh)
//                    {
//                        refreshUserDetail();
//                    } else
//                    {
////                        showOpenAccountDialog();
//                        showDialog();
//                    }
//
//                } else if (shouldRefresh)
//                {
//                    refreshUserDetail();
//                } else
//                {
//                    tvName.setText(App.userDetailData.getHfAccount().getUsrName());
//                    tvPhone.setText(App.userDetailData.getHfAccount().getUsrMp());
//                    hideDialog();
//                }
//            } else
            if (shouldRefresh)
            {
                refreshUserDetail();
            }
        } else
        {
            MobclickAgent.onPageEnd(TAG);
        }
    }

    private void showOpenAccountDialog()
    {
        final Dialog dialog = new Dialog(getActivity(), R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_open_account);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                mainActivity.setViewPagerPos(0);
            }
        });

        TextView ok = (TextView) dialog.findViewById(R.id.dialog_ok);

        ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openHFAccount();
                dialogDismiss = false;
                dialog.dismiss();
            }
        });

        dialogDismiss = true;
        dialog.show();
    }

    private void showDialog()
    {
        openHFDialog.setVisibility(View.VISIBLE);
        isDialogShowing = true;
    }

    private void hideDialog()
    {
        openHFDialog.setVisibility(View.GONE);
        isDialogShowing = false;
    }

    public void refreshUserDetail()
    {
        HttpUtils.addGetUserDetailRequest(getActivity(), new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                tvName.setText(App.userDetailData.getHfAccount().getUsrName());
                tvPhone.setText(App.userDetailData.getHfAccount().getUsrMp());
                if (shouldRefresh)
                {
                    shouldRefresh = false;
                }
            }
        });
    }

    private void openHFAccount()
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_OPEN_ACCOUNT);

        HttpUtils.addHFRequest(getActivity(), params, "开户");
    }

    private void getUserInfo()
    {
        Map<String,String> params = new TreeMap<>();
        params.put("api","getUserInfo");

        new ZebraTask.Builder(getActivity(), params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                UserInfoData data = (UserInfoData) response.getData();
                tvName.setText(data.getName());
            }
        }, UserInfoData.class).setShowProgress(false).build().execute();
    }

    @Override
    public void onClick(View v)
    {
        Class<? extends Activity> clazz = null;
        switch (v.getId())
        {
            case R.id.account_personal:
                clazz = PersonalInfoActivity.class;
                break;
            case R.id.account_asset:
                clazz = AssetActivity.class;
                break;
            case R.id.account_apply:
                //快速支付,点击我的分期的时候执行这个activity
            case R.id.fast_repay:
                clazz = ApplyActivity.class;
                break;
            case R.id.account_invest:
                clazz = TenderActivity.class;
                break;
            //交易明细
            case R.id.account_transaction_detail:
                clazz = TransactionActivity.class;
                break;
            case R.id.account_about:
                clazz = AboutActivity.class;
                break;
        }

        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }
}
