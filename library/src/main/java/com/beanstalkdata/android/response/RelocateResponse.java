package com.beanstalkdata.android.response;


import com.google.gson.annotations.SerializedName;

/**
 * Data model for RelocateSuccess response.
 */
public class RelocateResponse {

    @SerializedName("Success")
    private boolean success;

    @SerializedName("ERROR")
    private String error;

    public boolean isSuccess() {
        return success && error == null;
    }

    public String getError() {
        return error;
    }

}
