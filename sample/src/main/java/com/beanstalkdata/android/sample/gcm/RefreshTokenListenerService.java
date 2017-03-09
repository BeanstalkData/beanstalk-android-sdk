package com.beanstalkdata.android.sample.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class RefreshTokenListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        startService(new Intent(this, RegistrationIntentService.class));
    }

}
