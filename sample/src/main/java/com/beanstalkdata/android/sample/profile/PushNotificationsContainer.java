/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.profile;

import android.support.v4.util.ArrayMap;

import com.beanstalkdata.android.model.PushMessage;

import java.util.List;

class PushNotificationsContainer {

    private ArrayMap<String, List<PushMessage>> pushNotificationsHolder;

    private PushNotificationsContainer() {
        this.pushNotificationsHolder = new ArrayMap<>();
    }

    static PushNotificationsContainer getInstance() {
        return PushNotificationsContainerHolder.INSTANCE;
    }

    List<PushMessage> getPushNotifications(String pushMessageId) {
        return pushNotificationsHolder.get(pushMessageId);
    }

    void putPushNotifications(String pushMessageId, List<PushMessage> pushNotifications) {
        pushNotificationsHolder.put(pushMessageId, pushNotifications);
    }

    void removePushNotifications(String pushMessageId) {
        pushNotificationsHolder.remove(pushMessageId);
    }

    private static class PushNotificationsContainerHolder {
        private static final PushNotificationsContainer INSTANCE = new PushNotificationsContainer();
    }

}
