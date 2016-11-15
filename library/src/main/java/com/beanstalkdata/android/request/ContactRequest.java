/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.request;

import android.text.TextUtils;

import com.beanstalkdata.android.model.Contact;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data model for Contact request.
 */
public class ContactRequest {

    private static final String CONTACT_ID = "ContactID";
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    private static final String ZIP_CODE = "ZipCode";
    private static final String EMAIL = "Email";
    private static final String PHONE = "Cell_Number";
    private static final String BIRTHDAY = "Birthday";
    private static final String PROSPECT = "Prospect";
    private static final String PREFERRED_REWARD = "custom_PreferredReward";
    private static final String GENDER = "Gender";
    private static final String EMAIL_OPT_IN = "Email_Optin";
    private static final String TXT_OPT_IN = "Txt_Optin";
    private static final String SOURCE = "Source";
    private static final String NOVADINE ="custom_Novadine_User";
    private final Map<String, String> params = new LinkedHashMap<>();
    private Contact original;
    private String password;

    public ContactRequest() {
        this.params.put(PROSPECT, "loyalty");
        this.params.put(SOURCE, "androidapp");
    }

    public ContactRequest(Contact original) {
        this.original = original;
        this.params.put(CONTACT_ID, original.getContactId());
    }

    public ContactRequest(String birthday, String email, String firstName, String lastName, String phone,
                          String zip, String preferredReward){
        this.params.put(PROSPECT, "loyalty");
        this.params.put(SOURCE, "androidapp");

        this.params.put(BIRTHDAY, birthday);
        this.params.put(EMAIL, email);
        this.params.put(FIRST_NAME, firstName);
        this.params.put(LAST_NAME, lastName);
        this.params.put(PHONE, phone);
        this.params.put(ZIP_CODE, zip);
       // this.params.put(GENDER, isMan ? "Male" : "Female");
        this.params.put(PREFERRED_REWARD, preferredReward);

    }

    public void setFirstName(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        if (original != null && value.equalsIgnoreCase(original.getFirstName())) {
            return;
        }
        params.put(FIRST_NAME, value);
    }

    public void setLastName(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        if (original != null && value.equalsIgnoreCase(original.getLastName())) {
            return;
        }
        params.put(LAST_NAME, value);
    }

    public void setZipCode(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        if (original != null && value.equalsIgnoreCase(original.getZipCode())) {
            return;
        }
        params.put(ZIP_CODE, value);
    }

    public void setEmailOptIn(boolean emailOptIn) {
        if (original != null) {
            boolean existed = original.getEmailOptIn();
            if (emailOptIn == existed) {
                return;
            }
        }
        params.put(EMAIL_OPT_IN, String.valueOf(emailOptIn));
    }

    public void setNovadine(boolean novadine) {
        params.put(NOVADINE, String.valueOf(novadine ? 1 : 0));
    }

    public void setTxtOptIn(boolean txtOptIn) {
        if (original != null) {
            boolean existed = original.getTxtOptIn();
            if (txtOptIn == existed) {
                return;
            }
        }
        params.put(TXT_OPT_IN, String.valueOf(txtOptIn));
    }

    public void setPhone(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        if (original != null && value.equalsIgnoreCase(original.getPhone())) {
            return;
        }
        params.put(PHONE, value);
    }

    public void setBirthDate(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        if (original != null && value.equalsIgnoreCase(original.getBirthDay())) {
            return;
        }
        params.put(BIRTHDAY, value);
    }

    public void setGender(boolean isMale) {
        String gender = isMale ? "Male" : "Female";
        if (original != null) {
            if (gender.equalsIgnoreCase(original.getGender())) {
                return;
            }
        }
        params.put(GENDER, gender);
    }

    public void setPassword(String value) {
        password = value;
    }

    public void setEmail(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        if (original != null && value.equalsIgnoreCase(original.getEmail())) {
            return;
        }
        params.put(EMAIL, value);
    }

    public void setPreferredReward(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        if (original != null && value.equalsIgnoreCase(original.getPreferredReward())) {
            return;
        }
        params.put(PREFERRED_REWARD, value);
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return params.get(EMAIL);
    }

    public String getPhone() {
        return params.get(PHONE);
    }

    public Map<String, String> asParams() {
        return Collections.unmodifiableMap(params);
    }

    public void applyUpdate() {
        for (String key :
                params.keySet()) {
            String value = params.get(key);
            if (key.equalsIgnoreCase(FIRST_NAME)) {
                original.setFirstName(value);
            }
            if (key.equalsIgnoreCase(LAST_NAME)) {
                original.setLastName(value);
            }
            if (key.equalsIgnoreCase(EMAIL)) {
                original.setEmail(value);
            }
            if (key.equalsIgnoreCase(ZIP_CODE)) {
                original.setZipCode(value);
            }
            if (key.equalsIgnoreCase(BIRTHDAY)) {
                original.setBirthDay(value);
            }

            if (key.equalsIgnoreCase(PHONE)) {
                original.setPhone(value);
            }

            if (key.equalsIgnoreCase(PREFERRED_REWARD)) {
                original.setPreferredReward(value);
            }

            if (key.equalsIgnoreCase(GENDER)) {
                original.setGender(value);
            }

            if (key.equalsIgnoreCase(EMAIL_OPT_IN)) {
                original.setEmailOptIn(Boolean.parseBoolean(value));
            }

            if (key.equalsIgnoreCase(TXT_OPT_IN)) {
                original.setTxtOptIn(Boolean.parseBoolean(value));
            }
        }
        params.clear();
    }
}
