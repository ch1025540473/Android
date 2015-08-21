package com.wezebra.zebraking.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;

import com.wezebra.zebraking.R;
import com.wezebra.zebraking.model.City;
import com.wezebra.zebraking.model.Province;
import com.wezebra.zebraking.orm.DBManager;
import com.wezebra.zebraking.ui.adapter.CityAdapter;
import com.wezebra.zebraking.ui.adapter.ProvinceAdapter;
import com.wezebra.zebraking.widget.ExpandableHeightGridView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: superalex
 * Date: 2015/1/31
 * Time: 23:14
 */
public class AddressDialogFragment extends DialogFragment
{
    private static final int[] hotCityIds = {20256, 20001, 20003, 20203, 20002, 20004};
    private List<City> hotCities = new ArrayList<>();
    private List<Province> provinces;
    private TextView cityTitle;
    private TextView provinceTitle;
    private ExpandableHeightGridView cityGrid;
    private ExpandableHeightGridView provinceGrid;
    private CityAdapter cityAdapter;
    private ProvinceAdapter provinceAdapter;
    private TextView goBack;
    private boolean showProvinces = true;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initHotCities();
        try
        {
            provinces = DBManager.getInstance().queryAllProvince();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.fragment_address_dialog, null);
        cityTitle = (TextView) v.findViewById(R.id.city_title);
        provinceTitle = (TextView) v.findViewById(R.id.province_title);
        goBack = (TextView)v.findViewById(R.id.go_back);
        cityGrid = (ExpandableHeightGridView) v.findViewById(R.id.city_grid);
        provinceGrid = (ExpandableHeightGridView) v.findViewById(R.id.province_grid);
        cityGrid.setExpanded(true);
        provinceGrid.setExpanded(true);
        cityGrid.setOnItemClickListener(cityItemClickListener);
        provinceGrid.setOnItemClickListener(provinceItemClickListener);
        goBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(showProvinces)
                {
                    AddressDialogFragment.this.dismiss();
                }
                else
                {
                    cityTitle.setText("热门城市");
                    cityAdapter.setItems(hotCities);
                    provinceTitle.setVisibility(View.VISIBLE);
                    provinceGrid.setVisibility(View.VISIBLE);
                    showProvinces = true;
                }
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        cityAdapter = new CityAdapter(getActivity(), hotCities);
        provinceAdapter = new ProvinceAdapter(getActivity(),provinces);
        cityGrid.setAdapter(cityAdapter);
        provinceGrid.setAdapter(provinceAdapter);
    }

    private AdapterView.OnItemClickListener cityItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            OnCitySelectedListener listener = (OnCitySelectedListener)getActivity();
            listener.onCitySelected((int)id,cityAdapter.getItem(position).getName());
            showProvinces = true;
            AddressDialogFragment.this.dismiss();
        }
    };

    private AdapterView.OnItemClickListener provinceItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            try
            {
                List<City> cities = DBManager.getInstance().queryCitiesForProvince((int)id);
                cityTitle.setText(provinceAdapter.getItem(position).getName());
                cityAdapter.setItems(cities);
                provinceTitle.setVisibility(View.INVISIBLE);
                provinceGrid.setVisibility(View.GONE);
                showProvinces = false;
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    };

    private void initHotCities()
    {
        for (int id : hotCityIds)
        {
            try
            {
                City city = DBManager.getInstance().queryCityById(id);
                hotCities.add(city);

            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public interface OnCitySelectedListener
    {
        public void onCitySelected(int cityId, String cityName);
    }
}
