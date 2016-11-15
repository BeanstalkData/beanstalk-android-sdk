package com.beanstalkdata.android.sample;

import android.app.Application;

import com.beanstalkdata.android.core.BeanstalkService;

public class SampleApp extends Application {

    private BeanstalkService service;

    @Override
    public void onCreate() {
        super.onCreate();
        service = new BeanstalkService(this, BuildConfig.APP_KEY, BuildConfig.GOOGLE_MAPS_KEY);
    }

    public BeanstalkService getService() {
        return service;
    }

}
