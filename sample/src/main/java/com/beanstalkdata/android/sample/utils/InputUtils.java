/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;

import java.util.regex.Pattern;

public final class InputUtils {

    private static final Pattern ZIP_CODE = Pattern.compile("[0-9]{5}");

    private InputUtils() {

    }

    public static String getInputValue(TextView input) {
        return input.getText().toString();
    }

    public static boolean notEmpty(String value) {
        return !TextUtils.isEmpty(value);
    }

    public static boolean validEmail(String value) {
        return notEmpty(value) && Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

    public static boolean validPassword(String password, String passwordConfirm) {
        return notEmpty(password) && notEmpty(password) && password.equals(passwordConfirm);
    }

    public static boolean validPhoneNumber(String value) {
        return notEmpty(value) && Patterns.PHONE.matcher(value).matches();
    }

    public static boolean validZipCode(String value) {
        return notEmpty(value) && ZIP_CODE.matcher(value).matches();
    }

}
