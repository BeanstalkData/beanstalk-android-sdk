/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.utils.InputUtils;
import com.beanstalkdata.android.sample.utils.ToastUtils;

public class ResetPasswordFragment extends BaseFragment implements View.OnClickListener {

    private EditText emailInput;

    public static ResetPasswordFragment newInstance() {
        return new ResetPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        emailInput = (EditText) view.findViewById(R.id.input_email);
        view.findViewById(R.id.reset_password).setOnClickListener(this);
        return view;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.reset_password;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_password:
                view.setEnabled(false);
                String resetEmail = InputUtils.getInputValue(emailInput);
                if (InputUtils.validEmail(resetEmail)) {
                    activityContract.showProgress();
                    getService().resetPassword(resetEmail, new OnReturnDataListener<String>() {
                        @Override
                        public void onFinished(String data, String error) {
                            if (activityContract != null) {
                                activityContract.hideProgress();
                            }
                            FragmentActivity activity = getActivity();
                            if (activity != null) {
                                if (data != null) {
                                    ToastUtils.showLong(activity, data);
                                    if (activityContract != null) {
                                        activityContract.popBack();
                                    }
                                } else if (error != null) {
                                    ToastUtils.showLong(activity, error);
                                }
                            }
                        }
                    });
                }
                break;
        }
    }

}