package com.beanstalkdata.android.model;

import com.google.gson.annotations.Expose;

/**
 * Data model for Transaction Message.
 */
public class TransactionMessage {

    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
