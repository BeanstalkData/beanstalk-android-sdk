/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.beanstalkdata.android.sample.R;
import com.google.android.gms.gcm.GcmListenerService;

public class NotificationsListenerService extends GcmListenerService {

    private static final String PUSH_NOTIFICATION_TAG = "Sample";
    private static final String PUSH_NOTIFICATION_TITLE = "Sample Title";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Context context = getBaseContext();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(PUSH_NOTIFICATION_TITLE)
                .setAutoCancel(true);
        manager.notify(PUSH_NOTIFICATION_TAG, 0, builder.build());
    }
}
