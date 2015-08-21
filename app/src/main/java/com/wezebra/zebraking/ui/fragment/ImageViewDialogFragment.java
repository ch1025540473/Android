package com.wezebra.zebraking.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.wezebra.zebraking.R;
import com.wezebra.zebraking.widget.ExtendedJazzyViewPager;
import com.wezebra.zebraking.widget.TouchImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * User: superalex
 * Date: 2015/3/3
 * Time: 17:51
 */
public class ImageViewDialogFragment extends DialogFragment implements View.OnClickListener
{
    static final String POS = "pos";
    static final String LIST = "list";
    private List<Integer> images;
    private int pos;
    private ExtendedJazzyViewPager viewPager;

    public static ImageViewDialogFragment newInstance(int pos, ArrayList<Integer> list)
    {
        ImageViewDialogFragment fragment = new ImageViewDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(POS, pos);
        bundle.putIntegerArrayList(LIST, list);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        Bundle bundle = getArguments();
        pos = bundle.getInt(POS);
        images = bundle.getIntegerArrayList(LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialogfragment_viewpager, null);
        viewPager = (ExtendedJazzyViewPager) v.findViewById(R.id.view_pager);
        viewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        viewPager.setAdapter(new TouchImageAdapter());
        viewPager.setCurrentItem(pos);
    }

    @Override
    public void onClick(View v)
    {
        dismiss();
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
            img.setOnClickListener(ImageViewDialogFragment.this);

            img.setImageResource(images.get(position));

            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            viewPager.setObjectForPosition(img,position);
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

}
