/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.sample.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.request.AuthenticateUserFacebookRequest;
import com.beanstalkdata.android.request.AuthenticateUserGoogleRequest;
import com.beanstalkdata.android.sample.R;
import com.beanstalkdata.android.sample.base.BaseFragment;
import com.beanstalkdata.android.sample.common.ContactUsFragment;
import com.beanstalkdata.android.sample.profile.ProfileActivity;
import com.beanstalkdata.android.sample.utils.ToastUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MenuFragment extends BaseFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int GOOGLE_SIGN_IN = 7777;

    private GoogleApiClient googleApiClient;
    private CallbackManager callbackManager;
    private LoginButton facebookLoginButton;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        view.findViewById(R.id.sign_in).setOnClickListener(this);
        view.findViewById(R.id.google_sign_in).setOnClickListener(this);
        view.findViewById(R.id.facebook_sign_in).setOnClickListener(this);
        view.findViewById(R.id.sign_up).setOnClickListener(this);
        view.findViewById(R.id.reset_password).setOnClickListener(this);
        view.findViewById(R.id.contact_us).setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .requestId()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        facebookLoginButton = (LoginButton) view.findViewById(R.id.facebook_login_button);

        activityContract.showProgress();
        getService().checkUserSession(new OnReturnDataListener<Boolean>() {
            @Override
            public void onFinished(Boolean isSuccessful, String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (isSuccessful) {
                    if (activityContract != null) {
                        activityContract.startActivity(ProfileActivity.class);
                    }
                } else {
                    FragmentActivity activity = getActivity();
                    if (activity != null && error != null) {
                        ToastUtils.showLong(activity, error);
                    }
                }
            }
        });
    }

    @Override
    protected int getTitleResourceId() {
        return R.string.menu;
    }

    @Override
    protected boolean displayHomeAsUp() {
        return false;
    }
    
    @Override
    public void onStop() {
        if (googleApiClient != null) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
            googleApiClient = null;
        }
        super.onStop();
    }

    @Override
    public void onClick(View button) {
        switch (button.getId()) {
            case R.id.sign_in:
                if (activityContract != null) {
                    activityContract.replaceFragment(SignInFragment.newInstance());
                }
                break;
            case R.id.google_sign_in:
                activityContract.showProgress();
                getActivity().startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(googleApiClient), GOOGLE_SIGN_IN);
                break;
            case R.id.facebook_sign_in:
                AccessToken accesstoken = AccessToken.getCurrentAccessToken();
                if (accesstoken == null) {
                    activityContract.showProgress();
                    callbackManager = CallbackManager.Factory.create();
                    facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(final LoginResult loginResult) {
                            handleFacebookResult(loginResult);
                        }

                        @Override
                        public void onCancel() {
                            activityContract.hideProgress();
                            ToastUtils.showLong(getActivity(), getString(R.string.error_facebook_sign_in));
                        }

                        @Override
                        public void onError(FacebookException error) {
                            activityContract.hideProgress();
                            ToastUtils.showLong(getActivity(), getString(R.string.error_facebook_sign_in_cancelled));
                        }
                    });
                    facebookLoginButton.setReadPermissions("public_profile");
                    facebookLoginButton.performClick();
                } else {
                    handleFacebookResult(null);
                }
                break;
            case R.id.sign_up:
                if (activityContract != null) {
                    activityContract.replaceFragment(ContactInfoFragment.signUp());
                }
                break;
            case R.id.reset_password:
                if (activityContract != null) {
                    activityContract.replaceFragment(ResetPasswordFragment.newInstance());
                }
                break;
            case R.id.contact_us:
                if (activityContract != null) {
                    activityContract.replaceFragment(ContactUsFragment.newInstance());
                }
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ToastUtils.showLong(getActivity(), connectionResult.getErrorMessage());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activityContract.hideProgress();
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleGoogleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount googleAccount = result.getSignInAccount();
            String googleId = googleAccount.getId();
            String googleToken = googleAccount.getIdToken();
            getService().authenticateUserGoogle(new AuthenticateUserGoogleRequest(googleId, googleToken), new OnReturnDataListener<Boolean>() {
                @Override
                public void onFinished(Boolean isSuccessful, String error) {
                    if (activityContract != null) {
                        activityContract.hideProgress();
                    }
                    if (isSuccessful) {
                        if (activityContract != null) {
                            activityContract.startActivity(ProfileActivity.class);
                        }
                    } else {
                        if (error != null) {
                            FragmentActivity activity = getActivity();
                            if (activity != null) {
                                ToastUtils.showLong(activity, error);
                            }
                        }
                    }
                }
            });
        } else {
            activityContract.hideProgress();
            ToastUtils.showLong(getActivity(), getString(R.string.error_google_sign_in));
        }
    }

    private void handleFacebookResult(LoginResult result) {
        activityContract.hideProgress();
        String facebookId;
        String facebookToken;
        if (result != null) {
            facebookId = result.getAccessToken().getUserId();
            facebookToken = result.getAccessToken().getToken();
        } else {
            facebookId = AccessToken.getCurrentAccessToken().getUserId();
            facebookToken = AccessToken.getCurrentAccessToken().getToken();
        }
        getService().authenticateUserFacebook(new AuthenticateUserFacebookRequest(facebookId, facebookToken), new OnReturnDataListener<Boolean>() {
            @Override
            public void onFinished(Boolean isSuccessful, String error) {
                if (activityContract != null) {
                    activityContract.hideProgress();
                }
                if (isSuccessful) {
                    if (activityContract != null) {
                        activityContract.startActivity(ProfileActivity.class);
                    }
                } else {
                    if (error != null) {
                        FragmentActivity activity = getActivity();
                        if (activity != null) {
                            ToastUtils.showLong(activity, error);
                        }
                    }
                }
            }
        });
    }

}
