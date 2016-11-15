/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
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
import com.beanstalkdata.android.request.AuthenticateUserRequest;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.profile.ProfileActivity;
import com.beanstalkdata.android.sample.utils.InputUtils;
import com.beanstalkdata.android.sample.utils.ToastUtils;

public class SignInFragment extends BaseFragment implements View.OnClickListener {

    private EditText emailInput;
    private EditText passwordInput;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        emailInput = (EditText) view.findViewById(R.id.input_email);
        passwordInput = (EditText) view.findViewById(R.id.input_password);
        view.findViewById(R.id.sign_in).setOnClickListener(this);
        return view;
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.sign_in;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.sign_in:
                view.setEnabled(false);
                String signInEmail = InputUtils.getInputValue(emailInput);
                String signInPassword = InputUtils.getInputValue(passwordInput);
                if (InputUtils.validEmail(signInEmail) && InputUtils.notEmpty(signInPassword)) {
                    activityContract.showProgress();
                    AuthenticateUserRequest authRequest = new AuthenticateUserRequest(signInEmail, signInPassword);
                    getService().authenticateUser(authRequest, new OnReturnDataListener<Boolean>() {
                        @Override
                        public void onFinished(Boolean isSuccessful, String error) {
                            if (activityContract != null) {
                                activityContract.hideProgress();
                            }
                            if (isSuccessful) {
                                if (activityContract != null) {
                                    activityContract.startActivity(ProfileActivity.class);
                                }
                            } else {
                                view.setEnabled(true);
                                if (error != null) {
                                    FragmentActivity activity = getActivity();
                                    if (activity != null) {
                                        ToastUtils.showLong(activity, error);
                                    }
                                }
                            }
                        }
                    });
                }
                break;
        }
    }
}
