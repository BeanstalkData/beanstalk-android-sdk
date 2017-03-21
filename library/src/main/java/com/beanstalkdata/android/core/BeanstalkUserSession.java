/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.core;

import com.beanstalkdata.android.model.GiftCard;

/**
 * Beanstalk user session info contract.
 */
public interface BeanstalkUserSession {

    /**
     * Save Beanstalk user contact id and token.
     */
    void save(String contactId, String token);

    /**
     * Wipe User session data.
     */
    void release();

    /**
     * Get Beanstalk user session token.
     *
     * @return Beanstalk user session token.
     */
    String getToken();

    /**
     * Get Beanstalk user contact id.
     *
     * @return Beanstalk user contact id.
     */
    String getContactId();

    /**
     * Check if user is logged in.
     *
     * @return Beanstalk user is logged in or not.
     */
    boolean isLoggedIn();

    /**
     * Get Beanstalk user F key.
     *
     * @return Beanstalk user F key.
     */
    String getFKey();

    /**
     * Set Beanstalk user F key.
     */
    void setFKey(String fKey);

    /**
     * Get Beanstalk user default gift card.
     *
     * @return Beanstalk user default gift card.
     */
    GiftCard getDefaultCard();

    /**
     * Set Beanstalk user default gift card.
     */
    void setDefaultCard(GiftCard item);

}