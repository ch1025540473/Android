package com.wezebra.zebraking.ui.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.http.CusSuccessListener;
import com.wezebra.zebraking.http.TaskResponse;
import com.wezebra.zebraking.http.ZebraTask;
import com.wezebra.zebraking.util.Constants;
import com.wezebra.zebraking.util.L;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2015/7/28.
 */
public class BankDialogFragment extends Fragment{


    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> list;
    private int RESULT_OK = 0;

    public BankDialogFragment(){}

    public BankDialogFragment(ArrayList<String> list){
        this.list = list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_choice,null);

        listView = (ListView) view.findViewById(R.id.bank_choice);

        adapter = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("name", adapter.getItem(position) + "");
                Intent intent = new Intent();
                intent.putExtra("bank_name",adapter.getItem(position).toString());
                getActivity().setResult(RESULT_OK,intent);
                getActivity().finish();
            }
        });
        return view;
    }

}
