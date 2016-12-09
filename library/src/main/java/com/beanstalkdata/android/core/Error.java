/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.core;

interface Error {

    String REGISTER_CARD_FAILED = "Unable to register card.";
    String SIGN_IN_FAILED = "Unable to sign up user, please try again later.";
    String REGISTRATION_FAILED = "Unable to complete registration";
    String AUTHORIZATION_FAILED = "Unable to login";
    String LOGOUT_FAILED = "Logout Error";
    String UPDATE_PASSWORD_FAILED = "Unable to update password";
    String RESET_PASSWORD_FAILED = "Unable to reset password";
    String ADD_CARD_FAILED = "Unable to add card. %s";
    String PAYMENT_FAILED = "Failed to start payment";
    String OFFERS_FAILED = "Unable to get offers";
    String LOYALTY_INFO_FAILED = "Unable to get loyalty Info";
    String INQUIRE_CARD_FAILED = "Unable to inquire about card";
    String PREFERRED_CARD_FAILED = "Unable to set preferred card";
    String GIFT_LIST_FAILED = "Error Retrieving Cards Information";
    String ENROLL_PUSH_NOTIFICATION_FAILED = "Unable to enroll push notification";
    String MODIFY_PUSH_NOTIFICATION_FAILED = "Unable to modify push notification";
    String DELETE_PUSH_NOTIFICATION_FAILED = "Unable to delete push notification";
    String GET_MESSAGES_FAILED = "Unable to get messages";
    String TRACK_TRANSACTION_FAILED = "Unable to track transaction";
    String CONTACT_EXISTED_PHONE = "This phone is already registered in our database. Please sign-in into your account. Use the forgot password button in case you need to reset it.";
    String CONTACT_EXISTED_EMAIL = "This email is already registered in our database. Please sign-in into your account. Use the forgot password button in case you need to reset it.";
    String SIGN_UP_ZIP_FAILED = "We apologize for the inconvenience, but our new REWARDS® Loyalty program is currently available only in certain markets. " +
            "We will be expanding the program to all markets this summer, so please check back often for updates!";
    String SIGN_UP_LOCATION_FAILED = "We apologize for the inconvenience, but our new REWARDS® Loyalty program is currently available only in certain markets. " +
            "We will be expanding the program to all markets this summer, so please check back often for updates! You can still place your order by clicking on the Order button below.";
    String SESSION_VALID = "Session valid";
    String SESSION_INVALID = "Session invalid";
    String STORE_INFO_FAILED = "Unable to find this store";
    String STORES_LOCATIONS_FAILED = "Unable to find any stores";

}
