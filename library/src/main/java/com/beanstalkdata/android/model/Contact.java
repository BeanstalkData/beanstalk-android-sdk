/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Contact.
 */
public class Contact {

    @SerializedName("contactId")
    private String contactId;

    @SerializedName("contactFirstName")
    private String contactFirstName;

    @SerializedName("contactLastName")
    private String contactLastName;

    @SerializedName("contactZipCode")
    private String contactZipCode;

    @SerializedName("contactEmail")
    private String contactEmail;

    @SerializedName("Prospect")
    private String prospect;

    @SerializedName("gender")
    private String gender;

    @SerializedName("contactBirthday")
    private String contactBirthday;

    @SerializedName("FKey")
    private String fKey;

    @SerializedName("Cell_Number")
    private String cellNumber;

    @SerializedName("Txt_Optin")
    private int txtOptin;

    @SerializedName("Email_Optin")
    private int emailOptin;

    @SerializedName("PreferredReward")
    private String preferredReward;

    @SerializedName("DeviceTokenExt")
    private String deviceToken;

    @SerializedName("PushNotification_Optin")
    private int pushNotificationOptin;

    @SerializedName("InboxMessage_Optin")
    private int inboxMessageOptin;

    public String getFirstName() {
        return contactFirstName;
    }

    public void setFirstName(String firstName) {
        this.contactFirstName = firstName;
    }

    public String getLastName() {
        return contactLastName;
    }

    public void setLastName(String lastName) {
        this.contactLastName = lastName;
    }

    public String getEmail() {
        return contactEmail;
    }

    public void setEmail(String email) {
        this.contactEmail = email;
    }

    public String getZipCode() {
        return contactZipCode;
    }

    public void setZipCode(String zipCode) {
        this.contactZipCode = zipCode;
    }

    public String getPhone() {
        return cellNumber;
    }

    public void setPhone(String phone) {
        this.cellNumber = phone;
    }

    public String getBirthDay() {
        return contactBirthday;
    }

    public void setBirthDay(String birthDay) {
        this.contactBirthday = birthDay;
    }

    public String getFKey() {
        return fKey;
    }

    public void setFKey(String fKey) {
        this.fKey = fKey;
    }

    public boolean getEmailOptIn() {
        return emailOptin != 0;
    }

    public void setEmailOptIn(boolean emailOptIn) {
        this.emailOptin = emailOptIn ? 1 : 0;
    }

    public boolean getTxtOptIn() {
        return txtOptin != 0;
    }

    public void setTxtOptIn(boolean txtOptIn) {
        this.txtOptin = txtOptIn ? 1 : 0;
    }

    public boolean isMale() {
        return "Male".equalsIgnoreCase(gender);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getProspect() {
        return prospect;
    }

    public void setProspect(String prospect) {
        this.prospect = prospect;
    }

    public String getPreferredReward() {
        return preferredReward;
    }

    public void setPreferredReward(String preferredReward) {
        this.preferredReward = preferredReward;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public int getPushNotificationOptin() {
        return pushNotificationOptin;
    }

    public void setPushNotificationOptin(int pushNotificationOptin) {
        this.pushNotificationOptin = pushNotificationOptin;
    }

    public int getInboxMessageOptin() {
        return inboxMessageOptin;
    }

    public void setInboxMessageOptin(int inboxMessageOptin) {
        this.inboxMessageOptin = inboxMessageOptin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (txtOptin != contact.txtOptin) return false;
        if (emailOptin != contact.emailOptin) return false;
        if (pushNotificationOptin != contact.pushNotificationOptin) return false;
        if (inboxMessageOptin != contact.inboxMessageOptin) return false;
        if (contactId != null ? !contactId.equals(contact.contactId) : contact.contactId != null)
            return false;
        if (contactFirstName != null ? !contactFirstName.equals(contact.contactFirstName) : contact.contactFirstName != null)
            return false;
        if (contactLastName != null ? !contactLastName.equals(contact.contactLastName) : contact.contactLastName != null)
            return false;
        if (contactZipCode != null ? !contactZipCode.equals(contact.contactZipCode) : contact.contactZipCode != null)
            return false;
        if (contactEmail != null ? !contactEmail.equals(contact.contactEmail) : contact.contactEmail != null)
            return false;
        if (prospect != null ? !prospect.equals(contact.prospect) : contact.prospect != null)
            return false;
        if (gender != null ? !gender.equals(contact.gender) : contact.gender != null) return false;
        if (contactBirthday != null ? !contactBirthday.equals(contact.contactBirthday) : contact.contactBirthday != null)
            return false;
        if (fKey != null ? !fKey.equals(contact.fKey) : contact.fKey != null) return false;
        if (cellNumber != null ? !cellNumber.equals(contact.cellNumber) : contact.cellNumber != null)
            return false;
        if (!deviceToken.equals(contact.deviceToken)) return false;
        return preferredReward != null ? preferredReward.equals(contact.preferredReward) : contact.preferredReward == null;

    }

    @Override
    public int hashCode() {
        int result = contactId != null ? contactId.hashCode() : 0;
        result = 31 * result + (contactFirstName != null ? contactFirstName.hashCode() : 0);
        result = 31 * result + (contactLastName != null ? contactLastName.hashCode() : 0);
        result = 31 * result + (contactZipCode != null ? contactZipCode.hashCode() : 0);
        result = 31 * result + (contactEmail != null ? contactEmail.hashCode() : 0);
        result = 31 * result + (prospect != null ? prospect.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (contactBirthday != null ? contactBirthday.hashCode() : 0);
        result = 31 * result + (fKey != null ? fKey.hashCode() : 0);
        result = 31 * result + (cellNumber != null ? cellNumber.hashCode() : 0);
        result = 31 * result + txtOptin;
        result = 31 * result + emailOptin;
        result = 31 * result + (preferredReward != null ? preferredReward.hashCode() : 0);
        result = 31 * result + (deviceToken != null ? deviceToken.hashCode() : 0);
        return result;
    }
}
