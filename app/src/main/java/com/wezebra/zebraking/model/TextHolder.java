package com.wezebra.zebraking.model;

import android.graphics.Paint;

/**
 * Created by 俊杰 on 2015/4/20.
 */
public class TextHolder
{
    private float x = 0;
    private float y = 0;
    private float alpha = 1f;
    private float size;
    private String text;
    private Paint paint;

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getAlpha()
    {
        return alpha;
    }

    public void setAlpha(float alpha)
    {
        this.alpha = alpha;
        paint.setAlpha((int) ((alpha * 255f) + .5f));
    }

    public float getSize()
    {
        return size;
    }

    public void setSize(float size)
    {
        this.size = size;
        paint.setTextSize(size);
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Paint getPaint()
    {
        return paint;
    }

    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }
}
