/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Card Balance.
 */
public class CardBalance {

    public static final String DEFAULT = "$0.00";

    @SerializedName("balanceAmount")
    private BalanceAmount amount;

    public String getDisplayAmount() {
        if (amount == null) {
            return DEFAULT;
        } else {
            double value = amount.amount;
            return String.format("$%.2f", value);
        }
    }

    public static class BalanceAmount {
        @SerializedName("amount")
        private double amount;
    }
}
