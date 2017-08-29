package com.beanstalkdata.android.request;

/**
 * Data model for Authentication Facebook request.
 */
public class AuthenticateUserFacebookRequest {

    private final String facebookId;

    private final String facebookToken;

    public AuthenticateUserFacebookRequest(String facebookId, String facebookToken) {
        this.facebookId = facebookId;
        this.facebookToken = facebookToken;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

}
