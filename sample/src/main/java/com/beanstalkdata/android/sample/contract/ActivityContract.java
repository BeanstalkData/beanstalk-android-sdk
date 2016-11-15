/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.contract;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

public interface ActivityContract {

    void startActivity(Class<? extends Activity> cls);

    void replaceFragment(Fragment fragment);

    void showDialog(DialogFragment dialogFragment);

    void popBack();

    void showProgress();

    void hideProgress();

}
