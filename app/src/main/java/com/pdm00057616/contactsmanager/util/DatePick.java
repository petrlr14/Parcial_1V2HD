package com.pdm00057616.contactsmanager.util;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePick implements DatePickerDialog.OnDateSetListener {
    private TextView editText;

    public DatePick(TextView editText){
        this.editText=editText;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date=dayOfMonth+"-"+month+"-"+year;
        editText.setText(date);
    }
}
