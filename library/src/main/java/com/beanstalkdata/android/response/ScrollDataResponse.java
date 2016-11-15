package com.beanstalkdata.android.response;

import com.beanstalkdata.android.model.ScrollDataUrl;
import com.google.gson.annotations.SerializedName;

/**
 * Data model for Scroll Data response.
 */
public class ScrollDataResponse {

    @SerializedName("urls")
    private ScrollDataUrl[] urls;

    public ScrollDataUrl[] getUrls() {
        if(urls == null){
            urls = new ScrollDataUrl[0];
        }
        return urls;
    }
}
