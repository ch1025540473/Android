package com.wezebra.zebraking.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.wezebra.zebraking.R;

/**
 * Created by 俊杰 on 2015/4/10.
 */
public class CustomEditText extends EditText
{
    private Drawable dRight;
    private Rect rBounds;
    private Drawable showing;
    private Drawable hiding;
    private boolean isShowing = false;

    public CustomEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    public CustomEditText(Context context)
    {
        super(context);
        init(context);
    }

    private void init(Context context)
    {
        showing = context.getResources().getDrawable(R.drawable.ic_password_state_showing);
        hiding = context.getResources().getDrawable(R.drawable.ic_password_state_hiding);
        showing.setBounds(0, 0, showing.getMinimumWidth(), showing.getMinimumHeight());
        hiding.setBounds(0, 0, hiding.getMinimumWidth(), hiding.getMinimumHeight());
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom)
    {
        if (right != null)
        {
            dRight = right;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        if (event.getAction() == MotionEvent.ACTION_UP && dRight != null)
        {
            rBounds = dRight.getBounds();
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            System.out.println("x:/y: "+x+"/"+y);
            //check to make sure the touch event was within the bounds of the drawable
            if (x >= (this.getRight() - rBounds.width() - 20 - getPaddingRight()) && x <= this.getRight())
            {
                if (isShowing)
                {
                    setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    setCompoundDrawables(null, null, hiding, null);
                    isShowing = false;
                } else
                {
                    setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    setCompoundDrawables(null, null, showing, null);
                    isShowing = true;
                }
                event.setAction(MotionEvent.ACTION_CANCEL);//use this to prevent the keyboard from coming up
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable
    {
        dRight = null;
        rBounds = null;
        super.finalize();
    }


}
