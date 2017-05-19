/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.base;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.beanstalkdata.android.core.BeanstalkService;
import com.beanstalkdata.android.sample.SampleApp;
import com.beanstalkdata.android.sample.contract.ActivityContract;

public abstract class BaseFragment extends Fragment {

    protected ActivityContract activityContract;
    private ActionBar supportActionBar;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AppCompatActivity) {
            supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
        }
        if (activity instanceof ActivityContract) {
            activityContract = (ActivityContract) activity;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initToolbar();
    }

    protected BeanstalkService getService() {
        return ((SampleApp) getActivity().getApplication()).getService();
    }

    private void initToolbar() {
        if (supportActionBar != null) {
            supportActionBar.setTitle(getTitleResourceId());
            supportActionBar.setDisplayHomeAsUpEnabled(displayHomeAsUp());
        }
    }

    @StringRes
    protected abstract int getTitleResourceId();

    protected abstract boolean displayHomeAsUp();

}