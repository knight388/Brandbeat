package com.mgrmobi.brandbeat.ui.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class DateCallBack  implements DatePickerDialog.OnDateSetListener {
    Context context;
    ChooseDate mChooseDate;
    public DateCallBack (Context context, ChooseDate chooseDate){
        this.context=context;
        mChooseDate = chooseDate;

    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mChooseDate.dateChoose(calendar);
    }
}