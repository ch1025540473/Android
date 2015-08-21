package com.wezebra.zebraking.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;

/**
 * User: superalex
 * Date: 2015/3/4
 * Time: 10:47
 */
public class ExtendedJazzyViewPager extends JazzyViewPager
{
    public ExtendedJazzyViewPager(Context context)
    {
        super(context);
    }

    public ExtendedJazzyViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y)
    {
        if (v instanceof TouchImageView)
        {
            //
            // canScrollHorizontally is not supported for Api < 14. To get
            // around this issue,
            // ViewPager is extended and canScrollHorizontallyFroyo, a wrapper
            // around
            // canScrollHorizontally supporting Api >= 8, is called.
            //
            return ((TouchImageView) v).canScrollHorizontallyFroyo(-dx);

        }
        else
        {
            return super.canScroll(v, checkV, dx, x, y);
        }
    }
}
