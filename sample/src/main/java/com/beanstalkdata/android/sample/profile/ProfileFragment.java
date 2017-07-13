/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.callback.OnReturnListener;
import com.beanstalkdata.android.model.Contact;
import com.beanstalkdata.android.model.ContactAsset;
import com.beanstalkdata.android.response.LocationResponse;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.common.ContactUsFragment;
import com.beanstalkdata.android.sample.login.ContactInfoFragment;
import com.beanstalkdata.android.sample.login.LoginActivity;
import com.beanstalkdata.android.sample.utils.InputUtils;
import com.beanstalkdata.android.sample.utils.ToastUtils;

public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    public static final int REQUEST_LOCATION_PERMISSION = 9999;

    private TextView firstNameField;
    private TextView lastNameField;
    private TextView emailField;
    private TextView genderField;
    private TextView birthdayField;
    private TextView phoneNumberField;
    private EditText zipCodeInput;
    private EditText fkeyInput;
    private EditText intervalInput;

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
        fkeyInput = (EditText) view.findViewById(R.id.input_fkey);
        intervalInput = (EditText) view.findViewById(R.id.input_interval);
        view.findViewById(R.id.rewards).setOnClickListener(this);
        view.findViewById(R.id.progress).setOnClickListener(this);
        view.findViewById(R.id.gift_cards).setOnClickListener(this);
        view.findViewById(R.id.transactions).setOnClickListener(this);
        view.findViewById(R.id.check_stores).setOnClickListener(this);
        view.findViewById(R.id.check_fkey).setOnClickListener(this);
        view.findViewById(R.id.messages).setOnClickListener(this);
        view.findViewById(R.id.update_contact).setOnClickListener(this);
        view.findViewById(R.id.delete_contact).setOnClickListener(this);
        view.findViewById(R.id.log_out).setOnClickListener(this);
        view.findViewById(R.id.start_tracking).setOnClickListener(this);
        view.findViewById(R.id.stop_tracking).setOnClickListener(this);
        view.findViewById(R.id.check_default_asset).setOnClickListener(this);
        view.findViewById(R.id.check_current_asset).setOnClickListener(this);
        view.findViewById(R.id.maintain_loyalty_program).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermissions();
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
            case R.id.transactions:
                if (activityContract != null) {
                    activityContract.replaceFragment(TransactionsFragment.newInstance());
                }
                break;
            case R.id.check_stores:
                hideKeyboard(zipCodeInput);
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
            case R.id.check_fkey:
                hideKeyboard(fkeyInput);
                final String fkey = fkeyInput.getText().toString();
                if (InputUtils.notEmpty(fkey)) {
                    activityContract.showProgress();
                    getService().getContactByFkey(fkey, new OnReturnDataListener<Contact>() {
                        @Override
                        public void onFinished(Contact contact, String error) {
                            if (activityContract != null) {
                                activityContract.hideProgress();
                            }
                            FragmentActivity activity = getActivity();
                            if (activity != null) {
                                if (error == null) {
                                    ToastUtils.showLong(activity, R.string.fkey_search_result);
                                } else {
                                    ToastUtils.showLong(activity, error);
                                }
                            }
                        }
                    });
                } else {
                    ToastUtils.showLong(getActivity(), R.string.error_fkey_not_empty);
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
            case R.id.delete_contact:
                if (activityContract != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(R.string.delete_contact_confirm);
                    builder.setCancelable(true);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteContact();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                }
                break;
            case R.id.log_out:
                logOut();
                break;
            case R.id.start_tracking:
                try {
                    int interval = Integer.parseInt(intervalInput.getText().toString());
                    getService().startLocationTracking(interval * 60);
                } catch (NumberFormatException e) {
                    getService().startLocationTracking();
                }
                break;
            case R.id.stop_tracking:
                getService().stopLocationTracking();
                break;
            case R.id.check_default_asset:
                getService().getContactAsset(new OnReturnDataListener<ContactAsset>() {
                    @Override
                    public void onFinished(ContactAsset data, String error) {
                        FragmentActivity activity = getActivity();
                        if (error != null && activity != null) {
                            ToastUtils.showLong(activity, error);
                            return;
                        }
                        if (activityContract != null) {
                            String url = data.getDefaultImage();
                            if (url != null && !url.equals("")) {
                                activityContract.replaceFragment(ContactAssetFragment.newInstance(url, R.string.default_asset));
                            } else {
                                if (activity != null) {
                                    ToastUtils.showLong(activity, R.string.error_default_asset_not_available);
                                }
                            }
                        }
                    }
                });
                break;
            case R.id.check_current_asset:
                getService().getContactAsset(new OnReturnDataListener<ContactAsset>() {
                    @Override
                    public void onFinished(ContactAsset data, String error) {
                        FragmentActivity activity = getActivity();
                        if (error != null && activity != null) {
                            ToastUtils.showLong(activity, error);
                            return;
                        }
                        if (activityContract != null) {
                            String url = data.getCurrentImage();
                            if (url != null && !url.equals("")) {
                                activityContract.replaceFragment(ContactAssetFragment.newInstance(url, R.string.current_asset));
                            } else {
                                if (activity != null) {
                                    ToastUtils.showLong(activity, R.string.error_current_asset_not_available);
                                }
                            }
                        }
                    }
                });
                break;
            case R.id.maintain_loyalty_program:
                activityContract.showProgress();
                getService().maintainLoyaltyCards(new OnReturnDataListener<String>() {
                    @Override
                    public void onFinished(String data, String error) {
                        if (activityContract != null) {
                            activityContract.hideProgress();
                        }
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            if (error != null) {
                                ToastUtils.showLong(activity, error);
                            } else {
                                ToastUtils.showLong(activity, data);
                            }
                        }
                    }
                });
                break;
            case R.id.contact_us:
                activityContract.replaceFragment(ContactUsFragment.newInstance());
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

    private void deleteContact() {
        activityContract.showProgress();
        getService().deleteContact(new OnReturnListener() {
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

    private void hideKeyboard(EditText editText) {
        // Hide soft keyboard on item was selected.
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void checkPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.permission_denied)
                            .setMessage(R.string.permission_description)
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }
                break;
        }
    }

}
