/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.callback;

/**
 * Callback for API request which can return data or error string.
 *
 * @param <T> Data type.
 */
public interface OnReturnDataListener<T> {

    void onFinished(T data, String error);

}
