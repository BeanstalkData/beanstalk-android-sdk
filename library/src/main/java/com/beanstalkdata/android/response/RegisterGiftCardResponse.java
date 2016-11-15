package com.beanstalkdata.android.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Register Gift Card response.
 */
public class RegisterGiftCardResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("success")
    private SuccessResponse successResponse;

    @SerializedName("error")
    private ErrorResponse errorResponse;

    public boolean isFailed() {
        return !status
                || (successResponse == null)
                || (successResponse.message == null)
                || !successResponse.message.success;
    }

    public String getError() {
        String error = null;
        if (!status && errorResponse != null) {
            error = errorResponse.error;
        }
        if (error == null) {
            error = successResponse.message.error;
        }
        if (TextUtils.isEmpty(error)) {
            error = "";
        }
        return error;
    }

    public static class SuccessResponse {

        @SerializedName("code")
        private int code;

        @SerializedName("message")
        private Message message;
    }

    public static class ErrorResponse {

        @SerializedName("code")
        private int code;

        @SerializedName("message")
        private String error;
    }

    public static class Message {

        @SerializedName("success")
        private boolean success;

        @SerializedName("error")
        private String error;
    }

}
