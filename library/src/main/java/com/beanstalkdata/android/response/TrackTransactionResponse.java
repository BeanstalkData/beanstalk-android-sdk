/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.beanstalkdata.android.model.TransactionMessageInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data model for Track Transaction response.
 */
public class TrackTransactionResponse {

    @Expose
    private boolean status;

    @Expose
    @SerializedName("success")
    private TransactionMessageInfo transactionMessageInfo;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public TransactionMessageInfo getTransactionMessageInfo() {
        return transactionMessageInfo;
    }

    public void setTransactionMessageInfo(TransactionMessageInfo transactionMessageInfo) {
        this.transactionMessageInfo = transactionMessageInfo;
    }

}
