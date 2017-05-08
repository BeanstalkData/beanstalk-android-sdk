/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model;

import com.google.gson.annotations.SerializedName;


/**
 * Data model for ContactAsset.
 */
public class ContactAsset {

    @SerializedName("CurrentImage")
    private String currentImage;

    @SerializedName("DefaultImage")
    private String defaultImage;

    @SerializedName("ERROR")
    private String error;

    public String getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(String currentImage) {
        this.currentImage = currentImage;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return error == null;
    }

}
