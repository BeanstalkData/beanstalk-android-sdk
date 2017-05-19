/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import android.text.TextUtils;

import com.beanstalkdata.android.model.CardBalance;
import com.google.gson.annotations.SerializedName;

/**
 * Data model for Card Balance response.
 */
public class CardBalanceResponse {
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
                || (successResponse.message.response == null)
                || (successResponse.message.response.cardBalance == null);
    }

    public String getError() {
        String error = null;
        if (!status && errorResponse != null) {
            error = errorResponse.error;
        }
        if (error == null) {
            error = successResponse.message.response.message;
        }
        if (TextUtils.isEmpty(error)) {
            error = "";
        }
        return error;
    }

    public String getDisplayBalance() {
        if (isFailed()) {
            return CardBalance.DEFAULT;
        } else {
            CardBalance cardBalance = successResponse.message.response.cardBalance;
            return cardBalance.getDisplayAmount();
        }
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
        @SerializedName("response")
        private BalanceResponse response;
    }

    public static class BalanceResponse {

        @SerializedName("message")
        private String message;

        @SerializedName("balanceInquiryReturn")
        private CardBalance cardBalance;
    }
}
