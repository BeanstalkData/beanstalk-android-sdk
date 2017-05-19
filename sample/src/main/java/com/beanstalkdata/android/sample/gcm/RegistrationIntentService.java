/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.gcm;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.core.BeanstalkService;
import com.beanstalkdata.android.response.PushSuccessResponse;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.SampleApp;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = RegistrationIntentService.class.getSimpleName();

    public RegistrationIntentService() {
        super(TAG);
    }

    public static void start(Activity activity) {
        if (activity != null) {
            activity.startService(new Intent(activity, RegistrationIntentService.class));
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String token = null;
        try {
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.d(TAG, "onHandleIntent: " + token);
            }
        } catch (IOException e) {
            // TODO: Handle error
        }
        enrollPushNotification(token);
    }

    private void enrollPushNotification(String token) {
        if (token != null) {
            BeanstalkService service = getService();
            if (service != null) {
                service.enrollPushNotification(token, new OnReturnDataListener<PushSuccessResponse>() {

                    @Override
                    public void onFinished(PushSuccessResponse data, String error) {

                    }

                });
            }
        }
    }

    private BeanstalkService getService() {
        return ((SampleApp) getApplication()).getService();
    }

}
