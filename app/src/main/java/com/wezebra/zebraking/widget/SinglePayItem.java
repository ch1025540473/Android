package com.wezebra.zebraking.widget;
/*   作者：LJW on 2015/7/24 20:13
 *   邮箱：eric.lian@wezebra.com
 *   版权：斑马网信科技有限公司
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wezebra.zebraking.R;

public class SinglePayItem extends FrameLayout{

    public ImageView  wezebra_method_icon;
    public TextView   wezebra_title_content;
    public TextView   wezebra_title_desc;
    public ImageView  wezebra_select_icon;

    public SinglePayItem(Context context, AttributeSet attrs) {
        super(context,attrs);
        initView(context);
        initData(attrs,context);
    }

   public void  initView(Context context){
       View view=View.inflate(context, R.layout.single_pay_item_view,this);
       wezebra_method_icon=(ImageView)findViewById(R.id.wezebra_method_icon);
       wezebra_title_content=(TextView)findViewById(R.id.wezebra_title_content);
       wezebra_title_desc=(TextView)findViewById(R.id.wezebra_title_desc);
       wezebra_select_icon=(ImageView)findViewById(R.id.wezebra_select_icon);
   };

    public void initData(AttributeSet attrs,Context context){
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.SinglePayItem);
        wezebra_method_icon.setImageResource(typedArray.getResourceId(R.styleable.SinglePayItem_wezebra_method_icon,R.drawable.abc));
        wezebra_select_icon.setImageResource(typedArray.getResourceId(R.styleable.SinglePayItem_wezebra_select_icon,R.drawable.abc));
        wezebra_title_content.setText(typedArray.getString(R.styleable.SinglePayItem_wezebra_title_content));
        wezebra_title_desc.setText(typedArray.getString(R.styleable.SinglePayItem_wezebra_title_desc));

    };
    //设置对勾是否可见
    public void setWezebra_select_icon_visiable(int flag) {
        wezebra_select_icon.setVisibility(flag);
    }
}
