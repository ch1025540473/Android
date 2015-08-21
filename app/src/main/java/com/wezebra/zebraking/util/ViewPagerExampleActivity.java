package com.wezebra.zebraking.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.ui.BaseActivity;
import com.wezebra.zebraking.widget.ExtendedViewPager;
import com.wezebra.zebraking.widget.TouchImageView;

import java.util.List;

public class ViewPagerExampleActivity extends BaseActivity implements View.OnClickListener
{

    /**
     * Step 1: Download and set up v4 support library:
     * http://developer.android.com/tools/support-library/setup.html Step 2:
     * Create ExtendedViewPager wrapper which calls
     * TouchImageView.canScrollHorizontallyFroyo Step 3: ExtendedViewPager is a
     * custom view and must be referred to by its full package name in XML Step
     * 4: Write TouchImageAdapter, located below Step 5. The ViewPager in the
     * XML should be ExtendedViewPager
     */
    private List<String> images;
    private int pos;
//    private CirclePageIndicator mIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_example);

        Intent intent = getIntent();
        images = intent.getStringArrayListExtra("imgs");

        System.out.println(images);
        pos = intent.getIntExtra("pos", 0);
        ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TouchImageAdapter());
        mViewPager.setCurrentItem(pos);

        /**
         * 初始化pageIndicator
         */
//        mIndicator = (CirclePageIndicator) findViewById(R.id.item_image_indicator);
//        if (images.size() <= 1)
//        {
//            mIndicator.setVisibility(View.INVISIBLE);
//        }
//        mIndicator.setViewPager(mViewPager);
//        mIndicator.setRadius(10);
//        mIndicator.setStrokeWidth(0);
//        mIndicator.setFillColor(getResources().getColor(R.color.main_color));
//        mIndicator.setPageColor(getResources().getColor(android.R.color.white));
    }

    @Override
    public void onClick(View v)
    {
        finish();
    }

    class TouchImageAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            if (images == null)
            {
                return 0;
            }
            return images.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position)
        {
            TouchImageView img = new TouchImageView(container.getContext());

            img.setAdjustViewBounds(true);
            img.setOnClickListener(ViewPagerExampleActivity.this);

            int id = nameConvertToId(images.get(position));

            L.d("postion" + position, "id: " + id);

            img.setImageResource(id);

            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

    }

    private int nameConvertToId(String name)
    {
        return getResources().getIdentifier(name, "drawable", getPackageName());
    }
}
