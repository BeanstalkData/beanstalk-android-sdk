/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.beanstalkdata.android.sample.base.BaseDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.DatePickerDialog.OnDateSetListener;

public class DatePickerDialogFragment extends BaseDialogFragment {

    private static final String TAG = DatePickerDialogFragment.class.getSimpleName();
    private static final String SELECTED_YEAR = "selected_year";
    private static final String SELECTED_MONTH = "selected_month";
    private static final String SELECTED_DAY = "selected_day";

    private int year = 0;
    private int month = 0;
    private int day = 0;

    public static DatePickerDialogFragment selectDate(Fragment targetFragment) {
        return selectDate(targetFragment, null, null);
    }

    public static DatePickerDialogFragment selectDate(Fragment targetFragment, SimpleDateFormat dateFormat, String dateValue) {
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setTargetFragment(targetFragment, 0);
        fragment.setArguments(buildArguments(dateFormat, dateValue));
        return fragment;
    }

    public static DatePickerDialogFragment selectDate(Fragment targetFragment, int year, int month, int day) {
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setTargetFragment(targetFragment, 0);
        fragment.setArguments(buildArguments(year, month, day));
        return fragment;
    }

    private static Bundle buildArguments(SimpleDateFormat dateFormat, String dateValue) {
        if ((dateFormat == null) || (dateValue == null)) {
            return null;
        }

        Date date = getDate(dateFormat, dateValue);
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(date);
        return buildArguments(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private static Bundle buildArguments(int year, int month, int day) {
        if ((year == 0) && (month == 0) && (day == 0)) {
            return null;
        }

        Bundle arguments = new Bundle();
        arguments.putInt(SELECTED_YEAR, year);
        arguments.putInt(SELECTED_MONTH, month);
        arguments.putInt(SELECTED_DAY, day);
        return arguments;
    }

    private static Date getDate(SimpleDateFormat dateFormat, String dateValue) {
        try {
            return dateFormat.parse(dateValue);
        } catch (ParseException e) {
            Log.e(TAG, "Exception: ", e);
            return new Date(System.currentTimeMillis());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            year = arguments.getInt(SELECTED_YEAR);
            month = arguments.getInt(SELECTED_MONTH);
            day = arguments.getInt(SELECTED_DAY);
        }
        return new DatePickerDialog(getActivity(), (OnDateSetListener) getTargetFragment(), year, month, day);
    }

}
