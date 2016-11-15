/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Message types definition.
 */
@Retention(SOURCE)
@StringDef({MessageType.READ, MessageType.UNREAD, MessageType.DELETED})
public @interface MessageType {
    String READ = "READ";
    String UNREAD = "UNREAD";
    String DELETED = "DELETED";
}
