package com.beanstalkdata.android.request;

/**
 * Data model for Authentication Google request.
 */
public class AuthenticateUserGoogleRequest {

    private final String googleId;

    private final String googleToken;

    public AuthenticateUserGoogleRequest(String googleId, String googleToken) {
        this.googleId = googleId;
        this.googleToken = googleToken;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getGoogleToken() {
        return googleToken;
    }
}
