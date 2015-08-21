package com.wezebra.zebraking.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.util.PreferenceUtils;
import com.wezebra.zebraking.widget.BGABanner;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_guide)
public class GuideActivity extends ActionBarActivity
{
    @BGAAView(R.id.banner)
    private BGABanner banner;
    private List<View> views = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);

        ImageView guideOne = new ImageView(this);
        guideOne.setAdjustViewBounds(true);
        guideOne.setScaleType(ImageView.ScaleType.CENTER_CROP);

        guideOne.setImageDrawable(getResources().getDrawable(R.mipmap.guideone));
        ImageView guideTwo = new ImageView(this);
        guideTwo.setAdjustViewBounds(true);
        guideTwo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        guideTwo.setImageDrawable(getResources().getDrawable(R.mipmap.guidetwo));
        views.add(guideOne);
        views.add(guideTwo);
        View view = getLayoutInflater().inflate(R.layout.last_guide_page, null);
        ImageButton btn = (ImageButton) view.findViewById(R.id.go_to_login);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PreferenceUtils.setIsFirstOpen(false);
                Intent intent = new Intent(GuideActivity.this, PhoneNumberActivity.class);
                startActivity(intent);
                finish();
            }
        });

        views.add(view);

        banner.setViewPagerViews(views);
    }
}
