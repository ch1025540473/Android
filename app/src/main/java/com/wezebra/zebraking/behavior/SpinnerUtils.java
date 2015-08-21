package com.wezebra.zebraking.behavior;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wezebra.zebraking.R;

/**
 * Created by HQDev on 2015/4/29.
 */
public class SpinnerUtils {

    private static final int LAYOUT_LEFT = R.layout.spinner_left;
    private static final int LAYOUT_CENTER = R.layout.spinner_center;
    private static final int DROP_DOWN_LAYOUT = R.layout.spinner_dropdown;

    public static final void initDiploma(final Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.diploma));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.diploma));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initCities(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.cities));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.cities));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initIncome(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.income));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.income));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initSourceOfIncome(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.source_of_income));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.source_of_income));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initExpend(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.expend));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.expend));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initMarriage(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.marriage));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.marriage));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initCompanySize(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.company_size));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.company_size));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initHowToPay(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.how_to_pay));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.how_to_pay));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initBudget(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.budget));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.budget));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initHouseType(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.house_type));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.house_type));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initCompanyType(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.company_type));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.company_type));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initFamilyRelation(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.family_relation));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.family_relation));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static final void initFriendRelation(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.friend_relation));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.friend_relation));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });;
    }

    public static final void initSelfPayAmount(Activity activity, final Spinner spinner) {
        final ArrayAdapter arrayAdapter_center = new ArrayAdapter(activity, LAYOUT_CENTER,activity.getResources().getStringArray(R.array.self_pay_amount));
        final ArrayAdapter arrayAdapter_left = new ArrayAdapter(activity,LAYOUT_LEFT,activity.getResources().getStringArray(R.array.self_pay_amount));
        arrayAdapter_center.setDropDownViewResource(DROP_DOWN_LAYOUT);
        arrayAdapter_left.setDropDownViewResource(DROP_DOWN_LAYOUT);
        spinner.setAdapter(arrayAdapter_center);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    spinner.setAdapter(arrayAdapter_center);
                } else {
                    spinner.setAdapter(arrayAdapter_left);
                    spinner.setSelection(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
