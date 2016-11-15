package com.beanstalkdata.android.response;

import com.beanstalkdata.android.model.PushMessage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data model for Push Message by Id response.
 */
public class PushMessageByIdResponse {

    @Expose
    @SerializedName("message")
    private PushMessage pushMessage;

    public PushMessage getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(PushMessage pushMessage) {
        this.pushMessage = pushMessage;
    }

}
