/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Message Content types definition.
 */
@Retention(SOURCE)
@StringDef({MessageContentType.HTML, MessageContentType.STANDARD})
public @interface MessageContentType {
    String HTML = "HTML";
    String STANDARD = "STANDARD";
}
