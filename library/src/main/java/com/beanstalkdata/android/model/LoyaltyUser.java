/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model;

import com.google.gson.annotations.Expose;

/**
 * Data model for loyalty program user.
 */
public class LoyaltyUser {

    @Expose
    private String contactId;

    @Expose
    private String giftCardNumber;

    @Expose
    private String giftCardPin;

    @Expose
    private Boolean giftCardRegistrationStatus;

    @Expose
    private String giftCardTrack2;

    @Expose
    private String sessionToken;

    public String getContactId() {
        return contactId;
    }

    public String getGiftCardNumber() {
        return giftCardNumber;
    }

    public String getGiftCardPin() {
        return giftCardPin;
    }

    public Boolean getGiftCardRegistrationStatus() {
        return giftCardRegistrationStatus;
    }

    public String getGiftCardTrack2() {
        return giftCardTrack2;
    }

    public String getSessionToken() {
        return sessionToken;
    }

}