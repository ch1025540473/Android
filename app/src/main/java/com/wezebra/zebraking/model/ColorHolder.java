package com.wezebra.zebraking.model;

import android.graphics.Color;
import android.view.View;

/**
 * Created by 俊杰 on 2015/4/29.
 */
public class ColorHolder
{
    private View view;
    private int color1;
    private int color2;
    private float ratio;

    public ColorHolder(View view, int color1, int color2)
    {
        this.view = view;
        this.color1 = color1;
        this.color2 = color2;
    }

    public View getView()
    {
        return view;
    }

    public void setView(View button)
    {
        this.view = button;
    }

    public int getColor1()
    {
        return color1;
    }

    public void setColor1(int color1)
    {
        this.color1 = color1;
    }

    public int getColor2()
    {
        return color2;
    }

    public void setColor2(int color2)
    {
        this.color2 = color2;
    }

    public float getRatio()
    {
        return ratio;
    }

    public void setRatio(float ratio)
    {
        this.ratio = ratio;
        int newColor = blendColors(color1, color2, ratio);
        view.setBackgroundColor(newColor);
    }

    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio of which to blend. 1.0 will return {@code color1}, 0.5 will give an even blend,
     *              0.0 will return {@code color2}.
     */
    private static int blendColors(int color1, int color2, float ratio)
    {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }
}
