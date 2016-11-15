package com.beanstalkdata.android.sample.base;

import android.app.Activity;
import android.support.v4.app.DialogFragment;

import com.beanstalkdata.android.core.BeanstalkService;
import com.beanstalkdata.android.sample.SampleApp;
import com.beanstalkdata.android.sample.contract.ActivityContract;

public abstract class BaseDialogFragment extends DialogFragment {

    protected ActivityContract activityContract;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ActivityContract) {
            activityContract = (ActivityContract) activity;
        }
    }

    protected BeanstalkService getService() {
        return ((SampleApp) getActivity().getApplication()).getService();
    }

}