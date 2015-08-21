package com.wezebra.zebraking.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wezebra.zebraking.R;

/**
 * Created by admin on 2015/7/27.
 */
public class BindPhoneComplete extends Fragment {

    private TextView change_complete;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_phone_ok,null);
        change_complete = (TextView) view.findViewById(R.id.change_complete);
        change_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }
}
