package com.beanstalkdata.android.model.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Platform types definition.
 */
@Retention(SOURCE)
@StringDef({PlatformType.ANDROID, PlatformType.IOS})
public @interface PlatformType {
    String ANDROID = "Android";
    String IOS = "iOS";
}
