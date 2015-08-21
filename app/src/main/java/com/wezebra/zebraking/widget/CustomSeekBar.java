package com.wezebra.zebraking.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.wezebra.zebraking.R;

/**
 * Created by 俊杰 on 2015/5/4.
 */
public class CustomSeekBar extends View
{
    Drawable background;
    Drawable foreground;
    float progress;

    public CustomSeekBar(Context context)
    {
        this(context, null);
    }

    public CustomSeekBar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init()
    {
        background = getResources().getDrawable(R.drawable.seek_bg);
        foreground = getResources().getDrawable(R.drawable.seek_progress);
    }

    public void setProgress(float progress)
    {
        this.progress = progress;
        invalidate();
    }

    public float getProgress()
    {
        return progress;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        int width = canvas.getWidth();
        background.setBounds(0, 0, width, canvas.getHeight());
        foreground.setBounds(0, 0, (int) (width * progress), canvas.getHeight());
        background.draw(canvas);
        foreground.draw(canvas);
    }
}
