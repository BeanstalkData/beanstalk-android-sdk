/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.beanstalkdata.android.model.GiftCard;

/**
 * Beanstalk user session default implementation.
 */
public class DefaultBeanstalkUserSession implements BeanstalkUserSession {
    private static final String CORE_PREFS_NAME = "_pt_session";
    private static final String DEFAULT_CARD_PREFS_NAME = "_pt_default_card";

    private static final String TOKEN = "_token";
    private static final String CONTACT_ID = "_contact_id";
    private static final String F_KEY = "_f_key";

    private final SharedPreferences coreSharedPreferences;
    private final SharedPreferences defaultCardSharedPreferences;

    DefaultBeanstalkUserSession(Context context) {
        this.coreSharedPreferences = context.getSharedPreferences(CORE_PREFS_NAME, Context.MODE_PRIVATE);
        this.defaultCardSharedPreferences = context.getSharedPreferences(DEFAULT_CARD_PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void save(String contactId, String token) {
        SharedPreferences.Editor editor = coreSharedPreferences.edit();
        if (contactId != null) {
            editor.putString(CONTACT_ID, contactId);
        }
        if (token != null) {
            editor.putString(TOKEN, token);
        }
        editor.apply();
    }

    @Override
    public void release() {
        coreSharedPreferences.edit().clear().apply();
        defaultCardSharedPreferences.edit().clear().apply();
    }

    @Override
    public String getToken() {
        return coreSharedPreferences.getString(TOKEN, null);
    }

    @Override
    public String getContactId() {
        return coreSharedPreferences.getString(CONTACT_ID, null);
    }

    @Override
    public boolean isLoggedIn() {
        return (getContactId() != null) && (getToken() != null);
    }

    @Override
    public String getFKey() {
        return coreSharedPreferences.getString(F_KEY, null);
    }

    @Override
    public void setFKey(String fKey) {
        coreSharedPreferences.edit().putString(F_KEY, fKey).apply();
    }

    @Override
    public GiftCard getDefaultCard() {
        return GiftCard.fromPrefs(defaultCardSharedPreferences);
    }

    @Override
    public void setDefaultCard(GiftCard item) {
        SharedPreferences.Editor edit = defaultCardSharedPreferences.edit();
        item.toPrefs(edit);
        edit.apply();
    }

}