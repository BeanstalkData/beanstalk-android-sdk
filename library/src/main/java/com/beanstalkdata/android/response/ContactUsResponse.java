/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.google.gson.annotations.Expose;

/**
 * Data model for Contact Us response.
 */
public class ContactUsResponse {

    @Expose
    private String success;

    @Expose
    private String error;

    public String getSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

}