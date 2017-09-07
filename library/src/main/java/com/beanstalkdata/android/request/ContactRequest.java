/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.request;

import android.text.TextUtils;

import com.beanstalkdata.android.model.Contact;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.beanstalkdata.android.request.ContactRequest.Parameters.BIRTHDAY;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.CONTACT_ID;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.EMAIL;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.EMAIL_OPT_IN;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.FB_ID;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.FB_TOKEN;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.FIRST_NAME;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.F_KEY;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.GENDER;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.GOOGLE_ID;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.GOOGLE_TOKEN;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.LAST_NAME;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.PHONE;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.PREFERRED_REWARD;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.PROSPECT;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.SOURCE;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.TXT_OPT_IN;
import static com.beanstalkdata.android.request.ContactRequest.Parameters.ZIP_CODE;

/**
 * Data model for Contact request.
 */
public class ContactRequest {

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

    public ContactRequest(String birthday, String email, String firstName, String lastName, String phone, String zip, String preferredReward) {
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

    public void setTxtOptIn(boolean txtOptIn) {
        if (original != null) {
            boolean existed = original.getTxtOptIn();
            if (txtOptIn == existed) {
                return;
            }
        }
        params.put(TXT_OPT_IN, String.valueOf(txtOptIn));
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
        setGender(isMale ? "Male" : "Female");
    }

    public void setGender(String gender) {
        if (original != null) {
            if (gender.equalsIgnoreCase(original.getGender())) {
                return;
            }
        }
        params.put(GENDER, gender);
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

    public void setPassword(String value) {
        password = value;
    }

    public String getEmail() {
        return params.get(EMAIL);
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

    public String getFKey() {
        return params.get(F_KEY);
    }

    public void setFKey(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }
        if (original != null && value.equalsIgnoreCase(original.getFKey())) {
            return;
        }
        params.put(F_KEY, value);
    }

    public String getPhone() {
        return params.get(PHONE);
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

    public String getFbId() {
        return params.get(FB_ID);
    }

    public void setFbId(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }

        if (original != null && value.equalsIgnoreCase(original.getFbId())) {
            return;
        }
        params.put(FB_ID, value);
    }

    public String getFbToken() {
        return params.get(FB_TOKEN);
    }

    public void setFbToken(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }

        if (original != null && value.equalsIgnoreCase(original.getFbToken())) {
            return;
        }
        params.put(FB_TOKEN, value);
    }

    public String getGoogleId() {
        return params.get(GOOGLE_ID);
    }

    public void setGoogleId(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }

        if (original != null && value.equalsIgnoreCase(original.getGoogleId())) {
            return;
        }
        params.put(GOOGLE_ID, value);
    }

    public String getGoogleToken() {
        return params.get(GOOGLE_TOKEN);
    }

    public void setGoogleToken(String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }

        if (original != null && value.equalsIgnoreCase(original.getGoogleToken())) {
            return;
        }
        params.put(GOOGLE_TOKEN, value);
    }

    public void setParam(String key, String value) {
        if (TextUtils.isEmpty(value)) {
            clearParam(key);
            return;
        }
        params.put(key, value);
    }

    public String getParam(String key) {
        return params.get(key);
    }

    public String clearParam(String key) {
        return params.remove(key);
    }

    public Map<String, String> asParams() {
        return Collections.unmodifiableMap(params);
    }

    public void applyUpdate() {
        for (String key : params.keySet()) {
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
            if (key.equalsIgnoreCase(F_KEY)) {
                original.setFKey(value);
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
                original.setEmailOptIn(Integer.parseInt(value));
            }

            if (key.equalsIgnoreCase(TXT_OPT_IN)) {
                original.setTxtOptIn(Integer.parseInt(value));
            }
        }
        params.clear();
    }

    public interface Parameters {
        String CONTACT_ID = "ContactID";
        String FIRST_NAME = "FirstName";
        String LAST_NAME = "LastName";
        String ZIP_CODE = "ZipCode";
        String EMAIL = "Email";
        String F_KEY = "FKey";
        String PHONE = "Cell_Number";
        String BIRTHDAY = "Birthday";
        String PROSPECT = "Prospect";
        String PREFERRED_REWARD = "custom_PreferredReward";
        String GENDER = "Gender";
        String EMAIL_OPT_IN = "Email_Optin";
        String TXT_OPT_IN = "Txt_Optin";
        String PUSH_NOTIFICATION_OPT_IN = "PushNotification_Optin";
        String INBOX_MESSAGE_OPT_IN = "InboxMessage_Optin";
        String SOURCE = "Source";
        String LOYALTY_PASSWORD = "Password";
        String LOYALTY_PHONE = "CellNumber";
        String FB_ID = "FBid";
        String FB_TOKEN = "FBToken";
        String GOOGLE_ID = "GoogleId";
        String GOOGLE_TOKEN = "GoogleToken";
    }

}
