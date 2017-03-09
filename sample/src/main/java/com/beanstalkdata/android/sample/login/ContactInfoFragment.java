/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.login;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.callback.OnReturnListener;
import com.beanstalkdata.android.model.Contact;
import com.beanstalkdata.android.request.AuthenticateUserRequest;
import com.beanstalkdata.android.request.ContactRequest;
import com.beanstalkdata.android.response.PushSuccessResponse;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.dialog.DatePickerDialogFragment;
import com.beanstalkdata.android.sample.gcm.RegistrationIntentService;
import com.beanstalkdata.android.sample.profile.ProfileActivity;
import com.beanstalkdata.android.sample.utils.InputUtils;
import com.beanstalkdata.android.sample.utils.ToastUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ContactInfoFragment extends BaseFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String KEY_MODE = "key_mode";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

    private EditText emailInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText phoneNumberInput;
    private EditText zipCodeInput;
    private Button birthdayPicker;
    private RadioGroup genderChooser;
    private CheckBox optInEmailInput;
    private CheckBox optInPushInput;
    private Button submit;

    private Calendar calendar;

    public static ContactInfoFragment signUp() {
        return newInstance(Mode.SIGN_UP);
    }

    public static ContactInfoFragment update() {
        return newInstance(Mode.UPDATE);
    }

    private static ContactInfoFragment newInstance(@Mode int mode) {
        Bundle arguments = new Bundle();
        arguments.putInt(KEY_MODE, mode);
        ContactInfoFragment fragment = new ContactInfoFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_info, container, false);
        emailInput = (EditText) view.findViewById(R.id.input_email);
        passwordInput = (EditText) view.findViewById(R.id.input_password);
        passwordConfirmInput = (EditText) view.findViewById(R.id.input_confirm_password);
        firstNameInput = (EditText) view.findViewById(R.id.input_first_name);
        lastNameInput = (EditText) view.findViewById(R.id.input_last_name);
        phoneNumberInput = (EditText) view.findViewById(R.id.input_phone_number);
        zipCodeInput = (EditText) view.findViewById(R.id.input_zip_code);
        birthdayPicker = (Button) view.findViewById(R.id.picker_birthday);
        genderChooser = (RadioGroup) view.findViewById(R.id.gender_chooser);
        optInEmailInput = (CheckBox) view.findViewById(R.id.email_opt_in);
        optInPushInput = (CheckBox) view.findViewById(R.id.push_opt_in);
        submit = (Button) view.findViewById(R.id.submit);
        birthdayPicker.setOnClickListener(this);
        submit.setOnClickListener(this);
        if (getMode() == Mode.UPDATE) {
            passwordInput.setVisibility(View.GONE);
            passwordConfirmInput.setVisibility(View.GONE);
        }
        submit.setText(getSubmitText());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getMode() == Mode.UPDATE) {
            activityContract.showProgress();
            getService().getContact(new OnReturnDataListener<Contact>() {
                @Override
                public void onFinished(Contact contact, String error) {
                    if (activityContract != null) {
                        activityContract.hideProgress();
                    }
                    if (getView() != null) {
                        emailInput.setText(contact.getEmail());
                        firstNameInput.setText(contact.getFirstName());
                        lastNameInput.setText(contact.getLastName());
                        phoneNumberInput.setText(contact.getPhone());
                        zipCodeInput.setText(contact.getZipCode());
                        birthdayPicker.setText(contact.getBirthDay());
                        genderChooser.check(getGenderCheckedId(contact));
                        optInEmailInput.setChecked(contact.getEmailOptIn());
                        optInPushInput.setChecked(contact.getPushNotificationOptin() == 1);
                    }

                }
            });
        }
    }

    @Override
    protected int getTitleResourceId() {
        return getTitle();
    }

    @Override
    protected boolean displayHomeAsUp() {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.picker_birthday:
                pickBirthdayDate();
                break;
            case R.id.submit:
                submit(view);
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        initCalendar();
        setCalendarValues(year, month, dayOfMonth);
        setBirthdayValue();
    }

    @SuppressWarnings("WrongConstant")
    @Mode
    private int getMode() {
        return getArguments().getInt(KEY_MODE);
    }

    private String getSubmitText() {
        switch (getMode()) {
            case Mode.SIGN_UP:
                return getString(R.string.sign_up);
            case Mode.UPDATE:
                return getString(R.string.update);
            default:
                return null;
        }
    }

    @StringRes
    private int getTitle() {
        switch (getMode()) {
            case Mode.SIGN_UP:
                return R.string.sign_up;
            case Mode.UPDATE:
                return R.string.update_contact;
            default:
                return 0;
        }
    }

    private int getGenderCheckedId(Contact contact) {
        String gender = contact.getGender();
        if (getString(R.string.gender_male).equals(gender)) {
            return R.id.gender_male;
        } else if (getString(R.string.gender_female).equals(gender)) {
            return R.id.gender_female;
        } else {
            return View.NO_ID;
        }
    }

    private void pickBirthdayDate() {
        DatePickerDialogFragment.selectDate(this, dateFormat, birthdayPicker.getText().toString()).show(getFragmentManager(), null);
    }

    private void submit(View view) {
        int mode = getMode();
        if ((mode == Mode.SIGN_UP) || (mode == Mode.UPDATE)) {
            String email = InputUtils.getInputValue(emailInput);
            String password = InputUtils.getInputValue(passwordInput);
            String passwordConfirm = InputUtils.getInputValue(passwordConfirmInput);
            String firstName = InputUtils.getInputValue(firstNameInput);
            String lastName = InputUtils.getInputValue(lastNameInput);
            String phoneNumber = InputUtils.getInputValue(phoneNumberInput);
            String zipCode = InputUtils.getInputValue(zipCodeInput);
            String birthDay = InputUtils.getInputValue(birthdayPicker);
            int chosenGender = genderChooser.getCheckedRadioButtonId();
            boolean emailOptIn = optInEmailInput.isChecked();
            boolean pushOptIn = optInPushInput.isChecked();

            if (!InputUtils.validEmail(email)) {
                emailInput.setError(getString(R.string.error_email_required));
                view.setEnabled(true);
                return;
            }

            if ((mode == Mode.SIGN_UP) && !InputUtils.validPassword(password, passwordConfirm)) {
                passwordInput.setError(getString(R.string.error_password_required));
                view.setEnabled(true);
                return;
            }

            if (!InputUtils.notEmpty(firstName)) {
                firstNameInput.setError(getString(R.string.error_first_name_required));
                view.setEnabled(true);
                return;
            }

            if (!InputUtils.notEmpty(lastName)) {
                lastNameInput.setError(getString(R.string.error_last_name_required));
                view.setEnabled(true);
                return;
            }

            if (!InputUtils.validPhoneNumber(phoneNumber)) {
                phoneNumberInput.setError(getString(R.string.error_phone_number_required));
                view.setEnabled(true);
                return;
            }

            if (!InputUtils.validZipCode(zipCode)) {
                zipCodeInput.setError(getString(R.string.error_zip_code_required));
                view.setEnabled(true);
                return;
            }

            if (getString(R.string.birthday).equals(birthDay)) {
                Toast.makeText(getActivity(), R.string.error_birthday_required, Toast.LENGTH_LONG).show();
                view.setEnabled(true);
                return;
            }

            if (chosenGender == View.NO_ID) {
                Toast.makeText(getActivity(), R.string.error_gender_required, Toast.LENGTH_LONG).show();
                view.setEnabled(true);
                return;
            }

            final ContactRequest contactRequest = new ContactRequest();
            contactRequest.setEmail(email);
            if (mode == Mode.SIGN_UP) {
                contactRequest.setPassword(password);
            }
            contactRequest.setFirstName(firstName);
            contactRequest.setLastName(lastName);
            contactRequest.setPhone(phoneNumber);
            contactRequest.setZipCode(zipCode);
            contactRequest.setBirthDate(birthDay);
            contactRequest.setGender(chosenGender == R.id.gender_male);
            contactRequest.setEmailOptIn(emailOptIn);
            if (pushOptIn) {
                contactRequest.setParam(ContactRequest.Parameters.PUSH_NOTIFICATION_OPT_IN, "1");
                contactRequest.setParam(ContactRequest.Parameters.INBOX_MESSAGE_OPT_IN, "1");
            } else {
                contactRequest.setParam(ContactRequest.Parameters.PUSH_NOTIFICATION_OPT_IN, "0");
                contactRequest.setParam(ContactRequest.Parameters.INBOX_MESSAGE_OPT_IN, "0");
            }

            activityContract.showProgress();
            switch (mode) {
                case Mode.SIGN_UP:
                    addContactAndAuthenticateUser(contactRequest);
                    break;
                case Mode.UPDATE:
                    updateContact(contactRequest);
                    break;
            }
        }
    }

    private void addContactAndAuthenticateUser(final ContactRequest contactRequest) {
        getService().addContactWithEmail(contactRequest, new OnReturnListener() {
            @Override
            public void onFinished(String error) {
                if (error == null) {
                    authenticateUser(contactRequest.getEmail(), contactRequest.getPassword());
                } else {
                    if (activityContract != null) {
                        activityContract.hideProgress();
                    }
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        ToastUtils.showLong(activity, error);
                    }
                }
            }
        });
    }

    private void authenticateUser(String email, String password) {
        AuthenticateUserRequest request = new AuthenticateUserRequest(email, password);
        getService().authenticateUser(request, new OnReturnDataListener<Boolean>() {
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
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        ToastUtils.showLong(activity, error);
                    }
                }
            }
        });
    }

    private void updateContact(final ContactRequest contactRequest) {
        getService().updateContact(contactRequest, new OnReturnListener() {
            @Override
            public void onFinished(String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (error == null) {
                    if (isPushNotificationsOptedIn(contactRequest)) {
                        RegistrationIntentService.start(getActivity());
                    } else {
                        getService().deletePushNotification(new OnReturnDataListener<PushSuccessResponse>() {
                            @Override
                            public void onFinished(PushSuccessResponse data, String error) {
                                // TODO: handle error
                            }
                        });
                    }
                    if (activityContract != null) {
                        activityContract.popBack();
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

    private boolean isPushNotificationsOptedIn(ContactRequest contactRequest) {
        return contactRequest.getParam(ContactRequest.Parameters.PUSH_NOTIFICATION_OPT_IN).equals("1")
                && contactRequest.getParam(ContactRequest.Parameters.INBOX_MESSAGE_OPT_IN).equals("1");
    }

    private void initCalendar() {
        if (calendar == null) {
            calendar = Calendar.getInstance(Locale.ENGLISH);
        }
    }

    private void setCalendarValues(int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private void setBirthdayValue() {
        birthdayPicker.setText(dateFormat.format(calendar.getTime()));
    }

    @IntDef({Mode.SIGN_UP, Mode.UPDATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
        int SIGN_UP = 1001;
        int UPDATE = 2002;
    }

}
