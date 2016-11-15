/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.beanstalkdata.android.model.PushMessage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Data model for Push Messages response.
 */
public class PushMessagesResponse {

    @Expose
    @SerializedName("messages")
    private List<PushMessage> pushMessages;

    public List<PushMessage> getPushMessages() {
        return pushMessages;
    }

    public void setPushMessages(List<PushMessage> pushMessages) {
        this.pushMessages = pushMessages;
    }

}
