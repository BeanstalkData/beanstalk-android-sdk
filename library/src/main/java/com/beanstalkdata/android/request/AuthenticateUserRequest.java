package com.beanstalkdata.android.request;

/**
 * Data model for Authentication request.
 */
public class AuthenticateUserRequest {

    private final String email;

    private final String password;

    private final String time = "-1";

    public AuthenticateUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getTime() {
        return time;
    }
}
