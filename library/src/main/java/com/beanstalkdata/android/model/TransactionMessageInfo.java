/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model;

import com.google.gson.annotations.Expose;

/**
 * Data model for Transaction Message Info.
 */
public class TransactionMessageInfo {

    @Expose
    private int code;

    @Expose
    private TransactionMessage message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public TransactionMessage getMessage() {
        return message;
    }

    public void setMessage(TransactionMessage message) {
        this.message = message;
    }

}
