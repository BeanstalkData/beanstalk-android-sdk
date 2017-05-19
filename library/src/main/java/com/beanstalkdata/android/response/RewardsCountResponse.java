/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Rewards Count response.
 */
public class RewardsCountResponse {

    @SerializedName("Category")
    private Category[] categories;

    public Double getCount() {
        if(categories != null && categories.length > 0){
            Category category = categories[0];
            return Double.valueOf(category.count);
        }
        return null;
    }

    public static class Category {
        @SerializedName("Name")
        private String name;

        @SerializedName("Count")
        private int count;
    }
}
