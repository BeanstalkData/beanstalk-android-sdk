/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.beanstalkdata.android.sample.R;

public final class ToastUtils {

    private ToastUtils() {

    }

    public static void showShort(Context context, String text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void showShort(Context context, @StringRes int resourceId) {
        show(context, resourceId, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, String text) {
        show(context, text, Toast.LENGTH_LONG);
    }

    public static void showLong(Context context, @StringRes int resourceId) {
        show(context, resourceId, Toast.LENGTH_LONG);
    }

    public static void showFuture(Context context) {
        showLong(context, R.string.nearest_future_feature);
    }

    private static void show(Context context, String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

    private static void show(Context context, @StringRes int resourceId, int duration) {
        Toast.makeText(context, resourceId, duration).show();
    }

}
