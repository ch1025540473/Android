package com.wezebra.zebraking.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 俊杰 on 2015/5/8.
 */
public class CustomViewPager extends ViewPager
{
    private int childId;

    public CustomViewPager(Context context)
    {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (childId > 0 && getCurrentItem() == 0)
        {
            View scroll = findViewById(childId);
            if (scroll != null)
            {
                int left = scroll.getLeft();
                int right = scroll.getRight();
                int top = scroll.getTop();
                int bottom = scroll.getBottom();
                float x = ev.getX();
                float y = ev.getY();

                if (x <= right && x >= left && y <= bottom && y >= top)
                {
                    return false;
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setChildId(int id)
    {
        this.childId = id;
    }
}
