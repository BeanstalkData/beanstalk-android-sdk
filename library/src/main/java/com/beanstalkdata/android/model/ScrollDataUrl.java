/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Scroll Data URL.
 */
public class ScrollDataUrl implements Parcelable {

    @SerializedName("url")
    private String url;

    @SerializedName("description")
    private String description;

    @SerializedName("img_url")
    private String imgUrl;

    public ScrollDataUrl() {
    }

    protected ScrollDataUrl(Parcel in) {
        url = in.readString();
        description = in.readString();
        imgUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(description);
        dest.writeString(imgUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ScrollDataUrl> CREATOR = new Creator<ScrollDataUrl>() {
        @Override
        public ScrollDataUrl createFromParcel(Parcel in) {
            return new ScrollDataUrl(in);
        }

        @Override
        public ScrollDataUrl[] newArray(int size) {
            return new ScrollDataUrl[size];
        }
    };

    public String getUrl() {
        if (TextUtils.isEmpty(url)) {
            url = "http://pollotropical.com/";
        }
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    @Override
    public String toString() {
        return description;
    }
}
