package com.wezebra.zebraking.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.Banner;

import java.util.ArrayList;
import java.util.List;

public class BGABanner extends RelativeLayout
{
    private static final String TAG = "BGABanner";
    private static final int RMP = LayoutParams.MATCH_PARENT;
    private static final int RWC = LayoutParams.WRAP_CONTENT;
    private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;
    private ViewPager mViewPager = null;
    private List<View> mViews = null;
    private LinearLayout mPointContainer = null;
    private List<ImageView> mPoints = null;
    private boolean mPointVisibility = false;
    private boolean mAutoPlayAble = false;
    private boolean mIsAutoPlaying = false;
    private int mAutoPlayInterval = 2000;
    private int mPointGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private int mPointSpacing = 15;
    private int mPointEdgeSpacing = 15;
    private int mPointContainerWidth = RMP;
    private int mPointContainerHeight = RWC;
    private int mCurrentPoint = 0;
    private Drawable mPointFocusedDrawable;
    private Drawable mPointUnfocusedDrawable;
    private Drawable mPointContainerBackgroundDrawable;
    private Handler mPagerHandler;
    private List<Banner> mBanners;
    private Context mContext;

    private Runnable mAutoPlayTask = new Runnable()
    {
        @Override
        public void run()
        {
            if (mAutoPlayAble && mViews.size() > 1)
            {
                if (mViewPager.getCurrentItem() == mViews.size() - 1)
                {
                    mViewPager.setCurrentItem(1, false);
                }
            }
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            mPagerHandler.postDelayed(mAutoPlayTask, mAutoPlayInterval);
        }
    };

    public BGABanner(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public BGABanner(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
        initView(context);
    }

    private void initAttrs(Context context, AttributeSet attrs)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGABanner);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++)
        {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    public void initAttr(int attr, TypedArray typedArray)
    {
        if (attr == R.styleable.BGABanner_banner_pointFocusedImg)
        {
            mPointFocusedDrawable = typedArray.getDrawable(attr);

        } else if (attr == R.styleable.BGABanner_banner_pointUnfocusedImg)
        {
            mPointUnfocusedDrawable = typedArray.getDrawable(attr);

        } else if (attr == R.styleable.BGABanner_banner_pointContainerBackground)
        {
            mPointContainerBackgroundDrawable = typedArray.getDrawable(attr);

        } else if (attr == R.styleable.BGABanner_banner_pointSpacing)
        {
            /**
             * getDimension和getDimensionPixelOffset的功能差不多,都是获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘;两个函数的区别是一个返回float,一个返回int. getDimensionPixelSize则不管写的是dp还是sp还是px,都会乘以denstiy.
             */
            mPointSpacing = typedArray.getDimensionPixelSize(attr, mPointSpacing);

        } else if (attr == R.styleable.BGABanner_banner_pointEdgeSpacing)
        {
            mPointEdgeSpacing = typedArray.getDimensionPixelSize(attr, mPointEdgeSpacing);

        } else if (attr == R.styleable.BGABanner_banner_pointGravity)
        {
            mPointGravity = typedArray.getInt(attr, mPointGravity);

        } else if (attr == R.styleable.BGABanner_banner_pointContainerWidth)
        {
            try
            {
                mPointContainerWidth = typedArray.getDimensionPixelSize(attr, mPointContainerWidth);
            } catch (UnsupportedOperationException e)
            {
                // 如果是指定的wrap_content或者match_parent会执行下面这一行
                mPointContainerWidth = typedArray.getInt(attr, mPointContainerWidth);
            }

        } else if (attr == R.styleable.BGABanner_banner_pointContainerHeight)
        {
            try
            {
                mPointContainerHeight = typedArray.getDimensionPixelSize(attr, mPointContainerHeight);
            } catch (UnsupportedOperationException e)
            {
                mPointContainerHeight = typedArray.getInt(attr, mPointContainerHeight);
            }

        } else if (attr == R.styleable.BGABanner_banner_pointVisibility)
        {
            mPointVisibility = typedArray.getBoolean(attr, mPointVisibility);

        } else if (attr == R.styleable.BGABanner_banner_pointAutoPlayAble)
        {
            mAutoPlayAble = typedArray.getBoolean(attr, mAutoPlayAble);

        } else if (attr == R.styleable.BGABanner_banner_pointAutoPlayInterval)
        {
            mAutoPlayInterval = typedArray.getInteger(attr, mAutoPlayInterval);
        }
    }

    private void initView(Context context)
    {
        mContext = context;
        mViewPager = new ViewPager(context);
        addView(mViewPager, new LayoutParams(RMP, RMP));

        if (mPointVisibility)
        {
            if (mPointFocusedDrawable == null)
            {
                throw new RuntimeException("pointFocusedImg is not allowed to be NULL");
            } else if (mPointUnfocusedDrawable == null)
            {
                throw new RuntimeException("pointUnfocusedImg is not allowed to be NULL");
            }
            mPointContainer = new LinearLayout(context);
            mPointContainer.setOrientation(LinearLayout.HORIZONTAL);
            mPointContainer.setPadding(mPointEdgeSpacing, 0, mPointEdgeSpacing, 0);
            if (mPointContainerBackgroundDrawable != null)
            {
                mPointContainer.setBackgroundDrawable(mPointContainerBackgroundDrawable);
            }
            LayoutParams pointContainerLp = new LayoutParams(mPointContainerWidth, mPointContainerHeight);
            // 处理圆点在顶部还是底部
            if ((mPointGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP)
            {
                pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else
            {
                pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }
            int horizontalGravity = mPointGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
            // 处理圆点在左边、右边还是水平居中
            if (horizontalGravity == Gravity.LEFT)
            {
                mPointContainer.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            } else if (horizontalGravity == Gravity.RIGHT)
            {
                mPointContainer.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            } else
            {
                mPointContainer.setGravity(Gravity.CENTER);
            }
            addView(mPointContainer, pointContainerLp);
        }
    }

    public void setViewPagerViews(List<View> views)
    {
        mViews = views;

        if (mViews.size() <= 1)
        {
            mAutoPlayAble = false;
            mPointVisibility = false;
        }
        mViewPager.setAdapter(new MyAdapter());
        mViewPager.setOnPageChangeListener(new MyListener());

        if (mPointVisibility)
        {
            initPoints();
            processAutoPlay();
        }
    }

    private void initPoints()
    {
        mPointContainer.removeAllViews();
        mViewPager.removeAllViews();
        if (mPoints != null)
        {
            mPoints.clear();
        } else
        {
            mPoints = new ArrayList<ImageView>();
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LWC, LWC);
        int margin = mPointSpacing / 2;
        lp.setMargins(margin, 0, margin, 0);
        ImageView imageView;

        int size = (mAutoPlayAble && mViews.size() > 1) ? mViews.size() - 2 : mViews.size();

        for (int i = 0; i < size; i++)
        {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(lp);
            imageView.setImageDrawable(mPointUnfocusedDrawable);
            mPoints.add(imageView);
            mPointContainer.addView(imageView);
        }
    }

    private void processAutoPlay()
    {
        if (mAutoPlayAble)
        {
            // 有配置自动轮播才去实例化handler
            mPagerHandler = new Handler();
            startAutoPlay();

            if (mViews.size() > 1)
            {
                mViewPager.setCurrentItem(mViews.size() - 1);
            } else
            {
                mViewPager.setCurrentItem(0);
            }

        } else
        {
            switchToPoint(0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                stopAutoPlay();
                break;
            case MotionEvent.ACTION_UP:
                startAutoPlay();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility)
    {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE)
        {
            startAutoPlay();
        } else if (visibility == INVISIBLE)
        {
            stopAutoPlay();
        }
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if (mPagerHandler != null)
        {
            mPagerHandler.removeCallbacks(mAutoPlayTask);
        }
    }

    private void startAutoPlay()
    {
        if (mAutoPlayAble && !mIsAutoPlaying && (mViews != null && mViews.size() > 1))
        {
            mIsAutoPlaying = true;
            mPagerHandler.postDelayed(mAutoPlayTask, mAutoPlayInterval);
        }
    }

    private void stopAutoPlay()
    {
        if (mAutoPlayAble && mIsAutoPlaying && (mViews != null && mViews.size() > 1))
        {
            mIsAutoPlaying = false;
            mPagerHandler.removeCallbacks(mAutoPlayTask);
        }
    }

    private void switchToPoint(int newCurrentPoint)
    {
        mPoints.get(mCurrentPoint).setImageDrawable(mPointUnfocusedDrawable);
        mPoints.get(newCurrentPoint).setImageDrawable(mPointFocusedDrawable);
        mCurrentPoint = newCurrentPoint;
    }

    private final class MyAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return mViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            View view = mViews.get(position);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            // view 要关联的页面, object instantiateItem()方法返回的对象
            // 是否要关联显示页面与 instantiateItem()返回值，这个方法是必须实现的
            return view == object;
        }
    }

    private final class MyListener implements OnPageChangeListener
    {

        @Override
        public void onPageScrollStateChanged(int state)
        {
            switch (state)
            {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    // 开始滑动
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    // 当松开手时
                    // 如果没有其他页显示出来：SCROLL_STATE_DRAGGING --> SCROLL_STATE_IDLE
                    // 如果有其他页有显示出来（不管显示了多少），就会触发正在设置页码
                    // 页码没有改变时：SCROLL_STATE_DRAGGING --> SCROLL_STATE_SETTLING --> SCROLL_STATE_IDLE
                    // 页码有改变时：SCROLL_STATE_DRAGGING --> SCROLL_STATE_SETTLING --> onPageSelected --> SCROLL_STATE_IDLE
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    // 停止滑动
                    if (mAutoPlayAble && mViews.size() > 1)
                    {
                        int cur = mViewPager.getCurrentItem();

                        if (cur == 0)
                        {
                            mViewPager.setCurrentItem(mViews.size() - 2, false);
                        } else if (cur == mViews.size() - 1)
                        {
                            mViewPager.setCurrentItem(1, false);
                        }

                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            // Logger.i(TAG, "onPageScrolled:  position=" + position + "  positionOffset=" + positionOffset + "  positionOffsetPixels=" + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position)
        {
            if (mPointVisibility)
            {
                if (mAutoPlayAble && mViews.size() > 1)
                {
                    if (position == 0 || position == mViews.size() - 2)
                    {
                        switchToPoint(mPoints.size() - 1);
                    } else if (position == 1 || position == mViews.size() - 1)
                    {
                        switchToPoint(0);
                    } else
                    {
                        switchToPoint(position - 1);
                    }
                } else
                {
                    switchToPoint(position);
                }
            }
        }
    }

}