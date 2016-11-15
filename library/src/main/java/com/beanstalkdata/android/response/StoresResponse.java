/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Stores response.
 */
public class StoresResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("stores")
    private Store[] stores;

    public boolean isFailed() {
        return !status || (stores == null) || (stores.length == 0);
    }

    public static class Store {

        @SerializedName("StoreId")
        private int id;
    }
}
