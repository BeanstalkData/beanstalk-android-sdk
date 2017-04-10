/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model;

import java.util.HashMap;

/**
 * Data model for Contact.
 */
public class Contact {

    private HashMap<String, String> params = new HashMap<>();

    public String getFirstName() {
        return params.get(Parameters.FIRST_NAME);
    }

    public void setFirstName(String firstName) {
        params.put(Parameters.FIRST_NAME, firstName);
    }

    public String getLastName() {
        return params.get(Parameters.LAST_NAME);
    }

    public void setLastName(String lastName) {
        params.put(Parameters.LAST_NAME, lastName);
    }

    public String getEmail() {
        return params.get(Parameters.EMAIL);
    }

    public void setEmail(String email) {
        params.put(Parameters.EMAIL, email);
    }

    public String getZipCode() {
        return params.get(Parameters.ZIP_CODE);
    }

    public void setZipCode(String zipCode) {
        params.put(Parameters.ZIP_CODE, zipCode);
    }

    public String getPhone() {
        return params.get(Parameters.CELL_NUMBER);
    }

    public void setPhone(String phone) {
        params.put(Parameters.CELL_NUMBER, phone);
    }

    public String getBirthDay() {
        return params.get(Parameters.BIRTHDAY);
    }

    public void setBirthDay(String birthDay) {
        params.put(Parameters.BIRTHDAY, birthDay);
    }

    public String getFKey() {
        return params.get(Parameters.F_KEY);
    }

    public void setFKey(String fKey) {
        params.put(Parameters.F_KEY, fKey);
    }

    public boolean getEmailOptIn() {
        return parseInteger(params.get(Parameters.EMAIL_OPT_IN)) != 0;
    }

    public void setEmailOptIn(int emailOptIn) {
        params.put(Parameters.EMAIL_OPT_IN, String.valueOf(emailOptIn));
    }

    public boolean getTxtOptIn() {
        return parseInteger(params.get(Parameters.TXT_OPT_IN)) != 0;
    }

    public void setTxtOptIn(int txtOptIn) {
        params.put(Parameters.TXT_OPT_IN, String.valueOf(txtOptIn));
    }

    public boolean isMale() {
        return "Male".equalsIgnoreCase(getGender());
    }

    public String getGender() {
        return params.get(Parameters.GENDER);
    }

    public void setGender(String gender) {
        params.put(Parameters.GENDER, gender);
    }

    public String getContactId() {
        return params.get(Parameters.ID);
    }

    public void setContactId(String contactId) {
        params.put(Parameters.ID, contactId);
    }

    public String getProspect() {
        return params.get(Parameters.PROSPECT);
    }

    public void setProspect(String prospect) {
        params.put(Parameters.PROSPECT, prospect);
    }

    public String getPreferredReward() {
        return params.get(Parameters.PREFERRED_REWARD);
    }

    public void setPreferredReward(String preferredReward) {
        params.put(Parameters.PREFERRED_REWARD, preferredReward);
    }

    public String getDeviceToken() {
        return params.get(Parameters.DEVICE_TOKEN_EXT);
    }

    public void setDeviceToken(String deviceToken) {
        params.put(Parameters.DEVICE_TOKEN_EXT, deviceToken);
    }

    public int getPushNotificationOptin() {
        return parseInteger(params.get(Parameters.PUSH_NOTIFICATION_OPT_IN));
    }

    public void setPushNotificationOptin(int pushNotificationOptin) {
        params.put(Parameters.PUSH_NOTIFICATION_OPT_IN, String.valueOf(pushNotificationOptin));
    }

    public int getInboxMessageOptin() {
        return parseInteger(params.get(Parameters.INBOX_MESSAGE_OPT_IN));
    }

    public void setInboxMessageOptin(int inboxMessageOptin) {
        params.put(Parameters.INBOX_MESSAGE_OPT_IN, String.valueOf(inboxMessageOptin));
    }

    public String getParam(String key) {
        return params.get(key);
    }

    public void setParam(String key, String value) {
        params.put(key, value);
    }

    private int parseInteger(String s) {
        return ((s != null) && !s.isEmpty()) ? Integer.valueOf(s) : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (getTxtOptIn() != contact.getTxtOptIn()) return false;
        if (getEmailOptIn() != contact.getEmailOptIn()) return false;
        if (getPushNotificationOptin() != contact.getPushNotificationOptin()) return false;
        if (getInboxMessageOptin() != contact.getInboxMessageOptin()) return false;
        if (getContactId() != null ? !getContactId().equals(contact.getContactId()) : contact.getContactId() != null)
            return false;
        if (getFirstName() != null ? !getFirstName().equals(contact.getFirstName()) : contact.getFirstName() != null)
            return false;
        if (getLastName() != null ? !getLastName().equals(contact.getLastName()) : contact.getLastName() != null)
            return false;
        if (getZipCode() != null ? !getZipCode().equals(contact.getZipCode()) : contact.getZipCode() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(contact.getEmail()) : contact.getEmail() != null)
            return false;
        if (getProspect() != null ? !getProspect().equals(contact.getProspect()) : contact.getProspect() != null)
            return false;
        if (getGender() != null ? !getGender().equals(contact.getGender()) : contact.getGender() != null)
            return false;
        if (getBirthDay() != null ? !getBirthDay().equals(contact.getBirthDay()) : contact.getBirthDay() != null)
            return false;
        if (getFKey() != null ? !getFKey().equals(contact.getFKey()) : contact.getFKey() != null)
            return false;
        if (getPhone() != null ? !getPhone().equals(contact.getPhone()) : contact.getPhone() != null)
            return false;
        if (!getDeviceToken().equals(contact.getDeviceToken())) return false;
        return getPreferredReward() != null ? getPreferredReward().equals(contact.getPreferredReward()) : contact.getPreferredReward() == null;

    }

    @Override
    public int hashCode() {
        int result = getContactId() != null ? getContactId().hashCode() : 0;
        result = 31 * result + (getFirstName() != null ? getFirstName().hashCode() : 0);
        result = 31 * result + (getLastName() != null ? getLastName().hashCode() : 0);
        result = 31 * result + (getZipCode() != null ? getZipCode().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getProspect() != null ? getProspect().hashCode() : 0);
        result = 31 * result + (getGender() != null ? getGender().hashCode() : 0);
        result = 31 * result + (getBirthDay() != null ? getBirthDay().hashCode() : 0);
        result = 31 * result + (getFKey() != null ? getFKey().hashCode() : 0);
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getTxtOptIn() ? 1 : 0);
        result = 31 * result + (getEmailOptIn() ? 1 : 0);
        result = 31 * result + (getPreferredReward() != null ? getPreferredReward().hashCode() : 0);
        result = 31 * result + (getDeviceToken() != null ? getDeviceToken().hashCode() : 0);
        return result;
    }

    public interface Parameters {
        String ID = "contactId";
        String FIRST_NAME = "contactFirstName";
        String LAST_NAME = "contactLastName";
        String ZIP_CODE = "contactZipCode";
        String EMAIL = "contactEmail";
        String PROSPECT = "Prospect";
        String GENDER = "gender";
        String BIRTHDAY = "contactBirthday";
        String F_KEY = "FKey";
        String CELL_NUMBER = "Cell_Number";
        String TXT_OPT_IN = "Txt_Optin";
        String EMAIL_OPT_IN = "Email_Optin";
        String PREFERRED_REWARD = "PreferredReward";
        String DEVICE_TOKEN_EXT = "DeviceTokenExt";
        String PUSH_NOTIFICATION_OPT_IN = "PushNotification_Optin";
        String INBOX_MESSAGE_OPT_IN = "InboxMessage_Optin";
    }

}