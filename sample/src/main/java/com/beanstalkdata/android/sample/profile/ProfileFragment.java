/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.callback.OnReturnListener;
import com.beanstalkdata.android.model.Contact;
import com.beanstalkdata.android.response.LocationResponse;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.login.ContactInfoFragment;
import com.beanstalkdata.android.sample.login.LoginActivity;
import com.beanstalkdata.android.sample.utils.InputUtils;
import com.beanstalkdata.android.sample.utils.ToastUtils;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private TextView firstNameField;
    private TextView lastNameField;
    private TextView emailField;
    private TextView genderField;
    private TextView birthdayField;
    private TextView phoneNumberField;
    private EditText zipCodeInput;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        firstNameField = (TextView) view.findViewById(R.id.field_first_name);
        lastNameField = (TextView) view.findViewById(R.id.field_last_name);
        emailField = (TextView) view.findViewById(R.id.field_email);
        genderField = (TextView) view.findViewById(R.id.field_gender);
        birthdayField = (TextView) view.findViewById(R.id.field_birthday);
        phoneNumberField = (TextView) view.findViewById(R.id.field_phone_number);
        zipCodeInput = (EditText) view.findViewById(R.id.input_zip_code);
        view.findViewById(R.id.rewards).setOnClickListener(this);
        view.findViewById(R.id.progress).setOnClickListener(this);
        view.findViewById(R.id.gift_cards).setOnClickListener(this);
        view.findViewById(R.id.check_stores).setOnClickListener(this);
        view.findViewById(R.id.messages).setOnClickListener(this);
        view.findViewById(R.id.update_contact).setOnClickListener(this);
        view.findViewById(R.id.log_out).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateContactInfo();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                updateContactInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rewards:
                if (activityContract != null) {
                    activityContract.replaceFragment(RewardsFragment.newInstance());
                }
                break;
            case R.id.progress:
                if (activityContract != null) {
                    activityContract.replaceFragment(ProgressFragment.newInstance());
                }
                break;
            case R.id.gift_cards:
                if (activityContract != null) {
                    activityContract.replaceFragment(GiftCardsFragment.newInstance());
                }
                break;
            case R.id.check_stores:
                String zipCode = zipCodeInput.getText().toString();
                if (InputUtils.notEmpty(zipCode)) {
                    activityContract.showProgress();
                    getService().checkStores(zipCode, new OnReturnDataListener<LocationResponse>() {
                        @Override
                        public void onFinished(LocationResponse data, String error) {
                            if (activityContract != null) {
                                activityContract.hideProgress();
                            }
                            FragmentActivity activity = getActivity();
                            if (activity != null) {
                                if (error == null) {
                                    ToastUtils.showLong(activity, R.string.stores_exist_at_zip_code_location);
                                } else {
                                    ToastUtils.showLong(activity, error);
                                }
                            }
                        }
                    });
                } else {
                    ToastUtils.showLong(getActivity(), R.string.error_zip_code_not_empty);
                }
                break;
            case R.id.messages:
                if (activityContract != null) {
                    activityContract.replaceFragment(MessagesFragment.newInstance());
                }
                break;
            case R.id.update_contact:
                if (activityContract != null) {
                    activityContract.replaceFragment(ContactInfoFragment.update());
                }
                break;
            case R.id.log_out:
                logOut();
                break;
        }
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.profile;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return false;
    }

    private void updateContactInfo() {
        activityContract.showProgress();
        getService().getContact(new OnReturnDataListener<Contact>() {
            @Override
            public void onFinished(Contact contact, String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (getView() != null) {
                    firstNameField.setText(contact.getFirstName());
                    lastNameField.setText(contact.getLastName());
                    emailField.setText(contact.getEmail());
                    genderField.setText(contact.getGender());
                    birthdayField.setText(contact.getBirthDay());
                    phoneNumberField.setText(contact.getPhone());
                    zipCodeInput.setText(contact.getZipCode());
                }
            }
        });
    }

    private void logOut() {
        activityContract.showProgress();
        getService().logoutUser(new OnReturnListener() {
            @Override
            public void onFinished(String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (error == null) {
                    if (activityContract != null) {
                        activityContract.startActivity(LoginActivity.class);
                    }
                } else {
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        ToastUtils.showLong(activity, error);
                    }
                }
            }
        });
    }

}
