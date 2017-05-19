/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.beanstalkdata.android.model.Coupon;
import com.google.gson.annotations.SerializedName;

/**
 * Data model for Coupon response.
 */
public class CouponResponse {

    @SerializedName("Coupon")
    private Coupon[] coupons;

    public Coupon[] getCoupons() {
        if(coupons == null){
            coupons = new Coupon[0];
        }
        return coupons;
    }
}
