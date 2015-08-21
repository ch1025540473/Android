package com.wezebra.zebraking.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

/**
 * User: superalex
 * Date: 2015/2/12
 * Time: 13:51
 */
public class MonthPickerDialog extends DatePickerDialog
{
    public MonthPickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
    {
        super(context, callBack, year, monthOfYear, dayOfMonth);
        init();
    }

    private void init()
    {
        try
        {
            java.lang.reflect.Field[] datePickerDialogFields = getClass().getSuperclass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields)
            {
                if (datePickerDialogField.getName().equals("mDatePicker"))
                {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(this);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields)
                    {
                        if ("mDaySpinner".equals(datePickerField.getName()))
                        {
                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }

            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day)
    {
        super.onDateChanged(view, year, month, day);

        if (view.isShown())
        {
            String mYear = "" + year;
            String mMonth = "";
            if (month > 8)
            {
                mMonth = mMonth + (month + 1);
            } else
            {
                mMonth = mMonth + "0"
                        + (month + 1);
            }

            setTitle(mYear + "-" + mMonth);
        }
    }
}
