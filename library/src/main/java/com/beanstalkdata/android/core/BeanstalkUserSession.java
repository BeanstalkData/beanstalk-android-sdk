/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.beanstalkdata.android.model.GiftCard;

class BeanstalkUserSession {
    private static final String CORE_PREFS_NAME = "_pt_session";
    private static final String DEFAULT_CARD_PREFS_NAME = "_pt_default_card";

    private static final String TOKEN = "_token";
    private static final String CONTACT_ID = "_contact_id";
    private static final String CONTACT_JSON = "_contact_json";
    private static final String F_KEY = "_f_key";
    private static final String LOCATION_FLAG = "_location";

    private final SharedPreferences coreSharedPreferences;
    private final SharedPreferences defaultCardSharedPreferences;

    BeanstalkUserSession(Context context) {
        this.coreSharedPreferences = context.getSharedPreferences(CORE_PREFS_NAME, Context.MODE_PRIVATE);
        this.defaultCardSharedPreferences = context.getSharedPreferences(DEFAULT_CARD_PREFS_NAME, Context.MODE_PRIVATE);
    }

    void save(String contactId, String token) {
        coreSharedPreferences.edit().putString(CONTACT_ID, contactId).putString(TOKEN, token).apply();
    }

    void release() {
        coreSharedPreferences.edit().clear().apply();
        defaultCardSharedPreferences.edit().clear().apply();
    }

    String getToken() {
        return coreSharedPreferences.getString(TOKEN, null);
    }

    String getContactId() {
        return coreSharedPreferences.getString(CONTACT_ID, null);
    }

    boolean isLoggedIn() {
        return (getContactId() != null) && (getToken() != null);
    }

    String getFKey() {
        return coreSharedPreferences.getString(F_KEY, null);
    }

    void setFKey(String fKey) {
        coreSharedPreferences.edit().putString(F_KEY, fKey).apply();
    }

    void setContactJson(String json) {
        coreSharedPreferences.edit().putString(CONTACT_JSON, json).apply();
    }

    GiftCard getDefaultCard() {
        return GiftCard.fromPrefs(defaultCardSharedPreferences);
    }

    void setDefaultCard(GiftCard item) {
        SharedPreferences.Editor edit = defaultCardSharedPreferences.edit();
        item.toPrefs(edit);
        edit.apply();
    }

    boolean isDetectLocation() {
        return false;
//        return coreSharedPreferences.getBoolean(LOCATION_FLAG, false);
    }

    void skipLocation() {
        coreSharedPreferences.edit().putBoolean(LOCATION_FLAG, false).apply();
    }
}
