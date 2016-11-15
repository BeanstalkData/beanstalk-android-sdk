/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Image types definition.
 */
@Retention(SOURCE)
@StringDef({ImageType.SMALL_ICON, ImageType.LARGE})
public @interface ImageType {
    String SMALL_ICON = "SMALL_ICON";
    String LARGE = "LARGE";
}
