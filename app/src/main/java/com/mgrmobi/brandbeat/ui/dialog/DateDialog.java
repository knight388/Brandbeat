package com.mgrmobi.brandbeat.ui.dialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
@SuppressLint("ValidFragment")
public class DateDialog   extends DialogFragment {
    private ChooseDate mChooseDate;

    public DateDialog(ChooseDate chooseDate)
    {
        mChooseDate = chooseDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateCallBack dateSetting=new DateCallBack(getActivity(), mChooseDate);
        Calendar calendar= Calendar.getInstance();
        int year= calendar.get(calendar.YEAR);
        int month = Calendar.JANUARY;
        int day = 1;
        DatePickerDialog dialog;
        dialog=new DatePickerDialog(getActivity(),dateSetting,year,month,day);
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        return dialog;
    }
}