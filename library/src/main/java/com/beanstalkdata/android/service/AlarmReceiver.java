/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * AlarmReceiver is a broadcast receiver for system Alarm events.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 1234567890;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, ContactRelocationService.class);
        context.startService(newIntent);
    }

}
