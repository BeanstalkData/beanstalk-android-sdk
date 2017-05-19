/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile;

import android.support.v4.app.Fragment;

import com.beanstalkdata.android.sample.base.BaseActivity;

public class ProfileActivity extends BaseActivity {

    @Override
    protected Fragment getInitFragment() {
        return ProfileFragment.newInstance();
    }

}
