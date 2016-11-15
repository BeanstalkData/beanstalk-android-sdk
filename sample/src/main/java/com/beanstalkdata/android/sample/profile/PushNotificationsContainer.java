package com.beanstalkdata.android.sample.profile;


import android.support.v4.util.ArrayMap;

import com.beanstalkdata.android.model.PushNotification;

import java.util.List;

class PushNotificationsContainer {

    private ArrayMap<String, List<PushNotification>> pushNotificationsHolder;

    private PushNotificationsContainer() {
        this.pushNotificationsHolder = new ArrayMap<>();
    }

    static PushNotificationsContainer getInstance() {
        return PushNotificationsContainerHolder.INSTANCE;
    }

    List<PushNotification> getPushNotifications(String pushMessageId) {
        return pushNotificationsHolder.get(pushMessageId);
    }

    void putPushNotifications(String pushMessageId, List<PushNotification> pushNotifications) {
        pushNotificationsHolder.put(pushMessageId, pushNotifications);
    }

    void removePushNotifications(String pushMessageId) {
        pushNotificationsHolder.remove(pushMessageId);
    }

    private static class PushNotificationsContainerHolder {
        private static final PushNotificationsContainer INSTANCE = new PushNotificationsContainer();
    }

}
