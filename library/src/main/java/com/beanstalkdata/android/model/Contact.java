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

    @SerializedName("Novadine_User")
    private boolean novadineUser;

    public String getFKey() {
        return fKey;
    }

    public String getFirstName() {
        return contactFirstName;
    }

    public String getLastName() {
        return contactLastName;
    }

    public String getEmail() {
        return contactEmail;
    }

    public String getZipCode() {
        return contactZipCode;
    }

    public String getPhone() {
        return cellNumber;
    }

    public String getBirthDay() {
        return contactBirthday;
    }

    public boolean getEmailOptIn() {
        return emailOptin != 0;
    }

    public boolean getTxtOptIn() {
        return txtOptin != 0;
    }

    public boolean isMale() {
        return "Male".equalsIgnoreCase(gender);
    }

    public String getGender() {
        return gender;
    }

    public String getContactId() {
        return contactId;
    }

    public String getProspect() {
        return prospect;
    }

    public String getPreferredReward() {
        return preferredReward;
    }

    public void setFirstName(String firstName) {
        this.contactFirstName = firstName;
    }

    public void setLastName(String lastName) {
        this.contactLastName = lastName;
    }

    public void setEmail(String email) {
        this.contactEmail = email;
    }

    public void setZipCode(String zipCode) {
        this.contactZipCode = zipCode;
    }

    public void setBirthDay(String birthDay) {
        this.contactBirthday = birthDay;
    }

    public void setPhone(String phone) {
        this.cellNumber = phone;
    }

    public void setPreferredReward(String preferredReward) {
        this.preferredReward = preferredReward;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmailOptIn(boolean emailOptIn) {
        this.emailOptin = emailOptIn ? 1 : 0;
    }

    public void setTxtOptIn(boolean txtOptIn) {
        this.txtOptin = txtOptIn ? 1 : 0;
    }

    public boolean isNovadineUser() {
        return novadineUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (txtOptin != contact.txtOptin) return false;
        if (emailOptin != contact.emailOptin) return false;
        if (novadineUser != contact.novadineUser) return false;
        if (contactId != null ? !contactId.equals(contact.contactId) : contact.contactId != null) return false;
        if (contactFirstName != null ? !contactFirstName.equals(contact.contactFirstName) : contact.contactFirstName != null)
            return false;
        if (contactLastName != null ? !contactLastName.equals(contact.contactLastName) : contact.contactLastName != null)
            return false;
        if (contactZipCode != null ? !contactZipCode.equals(contact.contactZipCode) : contact.contactZipCode != null)
            return false;
        if (contactEmail != null ? !contactEmail.equals(contact.contactEmail) : contact.contactEmail != null)
            return false;
        if (prospect != null ? !prospect.equals(contact.prospect) : contact.prospect != null) return false;
        if (gender != null ? !gender.equals(contact.gender) : contact.gender != null) return false;
        if (contactBirthday != null ? !contactBirthday.equals(contact.contactBirthday) : contact.contactBirthday != null)
            return false;
        if (fKey != null ? !fKey.equals(contact.fKey) : contact.fKey != null) return false;
        if (cellNumber != null ? !cellNumber.equals(contact.cellNumber) : contact.cellNumber != null) return false;
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
        result = 31 * result + (novadineUser ? 1 : 0);
        return result;
    }
}
