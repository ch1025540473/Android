package com.wezebra.zebraking.ui.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.wezebra.zebraking.R;

/**
 * Created by ch on 2015/8/17.
 */
public class AuthDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_auth_sesame,container,false);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        getDialog().getWindow().setLayout( 600, getDialog().getWindow().getAttributes().height );
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.auth_costom_backgroud);
    }
}
