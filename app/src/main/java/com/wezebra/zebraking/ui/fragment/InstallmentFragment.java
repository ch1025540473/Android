package com.wezebra.zebraking.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;
import com.wezebra.zebraking.App;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusFailListener;
import com.wezebra.zebraking.http.CusResponseListener;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.DefaultErrorListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.http.data.ActiveApplyData;
import com.wezebra.zebraking.model.Banner;
import com.wezebra.zebraking.ui.WebViewActivity;
import com.wezebra.zebraking.ui.installment.AssessActivity;
import com.wezebra.zebraking.ui.installment.AssessCountdownActivity;
import com.wezebra.zebraking.ui.installment.GrantActivity;
import com.wezebra.zebraking.ui.installment.PreCalculationActivity;
import com.wezebra.zebraking.ui.installment.SIAStepOneResultActivity;
import com.wezebra.zebraking.ui.installment.SIAStepThreeResultActivity;
import com.wezebra.zebraking.ui.installment.SIAStepTwoResultActivity;
import com.wezebra.zebraking.ui.installment.SubmitInstallmentApplicationStepOneActivity;
import com.wezebra.zebraking.ui.installment.SubmitInstallmentApplicationStepThreeActivity;
import com.wezebra.zebraking.ui.installment.SubmitInstallmentApplicationStepTwoActivity;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;
import com.wezebra.zebraking.util.Utils;
import com.wezebra.zebraking.widget.BGABanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;


@BGAALayout(R.layout.fragment_installment)
public class InstallmentFragment extends Fragment implements View.OnClickListener
{
    public static final String TAG = "InstallmentFragment";
    private TextView pre_calculate;
    @BGAAView(R.id.banner)
    public BGABanner bgaBanner;
    private RelativeLayout r1, r2, r3, r4;

    private ImageView defaultBanner;

    private ImageView apply_now;
    private ImageView apply_anim;
    private Animation animation;
    // 用于真正的实现 onResume(), onPause()
    private int count = -1;

    private ImageView auditingStateDot, auditingStateImg;
    private TextView auditingStateDesc, applyNowText;

    private int status;

    private List<Banner> banners;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);

        View view = BGAA.injectView2ViewHolderOrFragment(this, getActivity());
        defaultBanner = (ImageView) view.findViewById(R.id.default_banner);

        pre_calculate = (TextView) view.findViewById(R.id.pre_calculate);
        pre_calculate.setOnClickListener(this);

        apply_now = (ImageView) view.findViewById(R.id.apply_now);
        apply_now.setOnClickListener(this);

//        banner.setViewPagerViews(getDatas(App.banners));
//        banner.setBanners(App.banners);

        apply_anim = (ImageView) view.findViewById(R.id.apply_anim);


        auditingStateDot = (ImageView) view.findViewById(R.id.auditing_state_dot);
        auditingStateImg = (ImageView) view.findViewById(R.id.auditing_state_img);
        auditingStateDesc = (TextView) view.findViewById(R.id.auditing_state_desc);
        applyNowText = (TextView) view.findViewById(R.id.apply_now_text);

        getBanners();
        return view;
    }
    /*当activity和fragment的这个是在onCreateView（）用后 视图都被初始化的时候*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_apply_now);
        animation.setInterpolator(new LinearInterpolator());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        L.d(TAG, "onResume");
        apply_anim.startAnimation(animation);
        count++;
        setUserVisibleHint(true);
        if (isAdded()){
            getActiveApplay();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        apply_anim.clearAnimation();
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

        if (count > 0)
        {
            MobclickAgent.onPageStart(TAG);
        } else
        {
            MobclickAgent.onPageEnd(TAG);
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent();
        switch (v.getId())
        {
            case R.id.pre_calculate:  //提前预算
                intent.setClass(getActivity(), PreCalculationActivity.class);
//                intent.setClass(getActivity(), HouseDataAuthActivity.class);
                startActivity(intent);
                break;
            case R.id.apply_now:
//                intent.setClass(getActivity(), SubmitInstallmentApplicationStepOneActivity.class);

                apply_now.setEnabled(false);
                getActiveApplay(true);

                break;
        }
    }

    private void jump()
    {
        Intent intent = new Intent();
        switch (status)
        {
            case Constants.ORDER_WAITING_REPAY:
            case Constants.ORDER_SUCCESS:
            case Constants.ORDER_CLOSED:
                //订单未通过
            case Constants.ORDER_UNPASS:
                intent.setClass(getActivity(), AssessActivity.class);
                startActivity(intent);
                break;
            //订单审核
            case Constants.ORDER_AUDITING_FIRST:
                intent.setClass(getActivity(), AssessCountdownActivity.class);
                startActivity(intent);
                break;
            //订单申请第一步
            case Constants.ORDER_WAITING_IMPROVE:
                intent.setClass(getActivity(), SubmitInstallmentApplicationStepOneActivity.class);
                startActivity(intent);
                break;
            //审核中
            case Constants.ORDER_AUDITING_BASIC:
                intent.setClass(getActivity(), SIAStepOneResultActivity.class);
                startActivity(intent);
                break;

            case Constants.ORDER_WAITING_ADDED:
                intent.setClass(getActivity(), SubmitInstallmentApplicationStepTwoActivity.class);
                startActivity(intent);
                break;
            case Constants.ORDER_AUDITING_ADDED:
                intent.setClass(getActivity(), SIAStepTwoResultActivity.class);
                startActivity(intent);
                break;
            case Constants.ORDER_WAITING_MODIFY:

                switch (App.INSTANCE.getActiveApplyData().getStep())
                {
                    case 2:
                        intent.setClass(getActivity(), SubmitInstallmentApplicationStepOneActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(getActivity(), SubmitInstallmentApplicationStepTwoActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case Constants.ORDER_WAITING_PAY_FIRST:
                intent.setClass(getActivity(), SubmitInstallmentApplicationStepThreeActivity.class);
                startActivity(intent);
                break;
            case Constants.ORDER_WAITING_GRANT:
                intent.setClass(getActivity(), GrantActivity.class);
                startActivity(intent);
                break;
            case Constants.ORDER_AUDITING_LOAN:
                intent.setClass(getActivity(), SIAStepThreeResultActivity.class);
                startActivity(intent);
                break;
            default:
                intent.setClass(getActivity(), AssessActivity.class);
                startActivity(intent);
                break;

        }
    }

    /**
     * 获取当前订单信息
     */
    private void getActiveApplay(final boolean... first)
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_ACTIVE_APPLY);

        new ZebraTask.Builder(getActivity(), params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {

                ActiveApplyData data = (ActiveApplyData) response.getData();
                App.INSTANCE.setActiveApplyData(data);
                setStatus();

                if (first.length > 0 && first[0])
                {
                    jump();
                }
            }
        }, ActiveApplyData.class).setResponseListener(new CusResponseListener()
        {
            @Override
            public void onResponse(TaskResponse response)
            {
                apply_now.setEnabled(true);
            }
        }).setCusFailListener(new CusFailListener()
        {
            @Override
            public void onFail(TaskResponse response)
            {
                auditingStateHide();
                if ((getActivity() != null) && isAdded())
                {
                    applyNowText.setText(getString(R.string.apply_now));
                }

                if (first.length > 0 && first[0])
                {
                    Toast.makeText(getActivity(), getString(R.string.access_server_errors), Toast.LENGTH_LONG).show();
                }

                ZebraTask.handleFailResponse(getActivity(), response);
            }
        }).setCusErrorListener(new DefaultErrorListener.CusErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                apply_now.setEnabled(true);
            }
        }).setShowProgress(false).build().execute();
    }

    public List<View> getBannerViews(List<Banner> banners)
    {
        List<View> views = new ArrayList<>();

        if (banners.size() > 1)
        {
            Banner banner = banners.get(banners.size() - 1);
            addBanner(views, banner);
        }

        for (final Banner banner : banners)
        {
            addBanner(views, banner);
        }

        if (banners.size() > 1)
        {
            Banner banner = banners.get(0);
            addBanner(views, banner);
        }

        return views;
    }

    private void addBanner(List<View> views, final Banner banner)
    {
        ImageView iv = new ImageView(getActivity());
        iv.setAdjustViewBounds(true);
        iv.setImageResource(R.drawable.default_banner_img);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        if (!TextUtils.isEmpty(banner.getAdAndroidurl()))
        {
            File cacheDir = Utils.getDiskCacheDir(getActivity());
            if (!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            L.i("imagecachepath", cacheDir.getPath());
            // 图片缓存的相关配置
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getActivity()).diskCache(new UnlimitedDiscCache(cacheDir)).diskCacheFileCount(50).diskCacheFileNameGenerator(new Md5FileNameGenerator()).build();
            ImageLoader.getInstance().init(configuration); // 对配置进行初始化
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).build();
            ImageLoader.getInstance().displayImage(banner.getAdAndroidurl(), iv,options);


            if (!TextUtils.isEmpty(banner.getAdTarget()))
            {
                iv.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        WebViewActivity.redirectToWebViewActivity(getActivity(),
                                banner.getAdTarget(),
                                banner.getAdName());
                    }
                });
            }
        }

        views.add(iv);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_zebrakingdom, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.questions:

                WebViewActivity.redirectToWebViewActivity(getActivity(), Constants.URL_QUESTION, "有疑问");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setStatus()
    {
        if (App.INSTANCE.getActiveApplyData() != null)
        {
            status = (App.INSTANCE.getActiveApplyData().getStatus());
            switch (status)
            {

                case Constants.ORDER_WAITING_REPAY:
                case Constants.ORDER_SUCCESS:
                case Constants.ORDER_CLOSED:
                case Constants.ORDER_UNPASS:
                    auditingStateHide();
                    applyNowText.setText(getString(R.string.apply_now));
                    break;
                case Constants.ORDER_AUDITING_FIRST:
                    auditingStateDesc.setText(getString(R.string.order_auditing_first));
                    applyNowText.setText(getString(R.string.check_now));
                    auditingStateShow();
                    break;
                case Constants.ORDER_WAITING_IMPROVE:
                    auditingStateDesc.setText(getString(R.string.order_waiting_improve));
                    applyNowText.setText(getString(R.string.check_now));
                    auditingStateShow();
                    break;
                case Constants.ORDER_AUDITING_BASIC:
                    auditingStateDesc.setText(getString(R.string.order_auditing_basic));
                    applyNowText.setText(getString(R.string.check_now));
                    auditingStateShow();
                    break;
                case Constants.ORDER_WAITING_ADDED:
                    auditingStateDesc.setText(getString(R.string.order_waiting_added));
                    applyNowText.setText(getString(R.string.check_now));
                    auditingStateShow();
                    break;
                case Constants.ORDER_AUDITING_ADDED:
                    auditingStateDesc.setText(getString(R.string.order_auditing_added));
                    applyNowText.setText(getString(R.string.check_now));
                    auditingStateShow();
                    break;
                case Constants.ORDER_WAITING_MODIFY:
                    auditingStateDesc.setText(getString(R.string.order_waiting_modify));
                    applyNowText.setText(getString(R.string.check_now));
                    auditingStateShow();
                    break;
                case Constants.ORDER_WAITING_PAY_FIRST:
                    auditingStateDesc.setText(getString(R.string.order_waiting_pay_first));
                    applyNowText.setText(getString(R.string.check_now));
                    auditingStateShow();
                    break;
                case Constants.ORDER_WAITING_GRANT:
                    auditingStateDesc.setText(getString(R.string.order_waiting_grant));
                    applyNowText.setText(getString(R.string.check_now));
                    auditingStateShow();
                    break;
                case Constants.ORDER_AUDITING_LOAN:
                    auditingStateDesc.setText(getString(R.string.order_auditing_loan));
                    applyNowText.setText(getString(R.string.check_now));
                    auditingStateShow();
                    break;

            }

        } else
        {
            auditingStateHide();
            applyNowText.setText(getString(R.string.apply_now));
        }

    }

    private void auditingStateShow()
    {
        auditingStateDot.setVisibility(View.VISIBLE);
        auditingStateImg.setVisibility(View.VISIBLE);
        auditingStateDesc.setVisibility(View.VISIBLE);
    }

    private void auditingStateHide()
    {
        auditingStateDot.setVisibility(View.INVISIBLE);
        auditingStateImg.setVisibility(View.INVISIBLE);
        auditingStateDesc.setVisibility(View.INVISIBLE);
    }

    private void getBanners()
    {
        Map<String, String> params = new TreeMap<>();
        params.put("api", Constants.API_BANNER);
        params.put("deviceId", App.getDeviceId());

        new ZebraTask.Builder(getActivity(), params, new CusSuccessListener()
        {
            @Override
            public void onSuccess(TaskResponse response)
            {
                List<Banner> banners = (List<Banner>) response.getData();
                if (banners != null && banners.size() > 0)
                {
                    bgaBanner.setViewPagerViews(getBannerViews(banners));
                    bgaBanner.setVisibility(View.VISIBLE);
                    defaultBanner.setVisibility(View.GONE);
                }
            }
        }, List.class, Banner.class).setShowProgress(false).build().execute();
    }
}
