/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.callback;

/**
 * Callback for API request which can only return error string.
 */
public interface OnReturnListener {

    void onFinished(String error);

}
