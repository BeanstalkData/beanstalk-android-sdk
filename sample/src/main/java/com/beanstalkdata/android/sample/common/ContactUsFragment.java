/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.beanstalkdata.android.callback.OnReturnListener;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.utils.ToastUtils;

public class ContactUsFragment extends BaseFragment implements View.OnClickListener {

    private EditText firstName;
    private EditText lastName;
    private EditText fromEmail;
    private EditText toEmail;
    private EditText phoneNumber;
    private EditText comments;

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        firstName = (EditText) view.findViewById(R.id.input_first_name);
        lastName = (EditText) view.findViewById(R.id.input_last_name);
        fromEmail = (EditText) view.findViewById(R.id.input_from_email);
        toEmail = (EditText) view.findViewById(R.id.input_to_email);
        phoneNumber = (EditText) view.findViewById(R.id.input_phone_number);
        comments = (EditText) view.findViewById(R.id.input_comments);
        view.findViewById(R.id.send_comments).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_comments:
                getService().contactUs(getText(firstName), getText(lastName), getText(fromEmail),
                        getText(toEmail), getText(phoneNumber), getText(comments), new OnReturnListener() {

                            @Override
                            public void onFinished(String error) {
                                if (error == null) {
                                    getFragmentManager().popBackStack();
                                } else {
                                    FragmentActivity activity = getActivity();
                                    if (activity != null) {
                                        ToastUtils.showLong(activity, error);
                                    }
                                }
                            }

                        });
                break;
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.contact_us;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

    private String getText(TextView view) {
        return view.getText().toString();
    }

}