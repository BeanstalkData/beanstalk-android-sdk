package com.beanstalkdata.android.response;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Payment Token response.
 */
public class PaymentTokenResponse {

    @SerializedName("paymentToken")
    private String paymentToken;

    public String getPaymentToken() {
        return paymentToken;
    }
}
