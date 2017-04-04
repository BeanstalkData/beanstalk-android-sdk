/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Contact Deleted response.
 */
public class ContactDeletedResponse {

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess() {
        return status.equals("success");
    }
}
