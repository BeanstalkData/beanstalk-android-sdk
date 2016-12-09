/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.core;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.callback.OnReturnListener;
import com.beanstalkdata.android.model.CardBalance;
import com.beanstalkdata.android.model.Contact;
import com.beanstalkdata.android.model.Coupon;
import com.beanstalkdata.android.model.GiftCard;
import com.beanstalkdata.android.model.type.ImageType;
import com.beanstalkdata.android.model.type.MessageContentType;
import com.beanstalkdata.android.model.type.MessageType;
import com.beanstalkdata.android.model.type.PlatformType;
import com.beanstalkdata.android.request.AuthenticateUserRequest;
import com.beanstalkdata.android.request.ContactRequest;
import com.beanstalkdata.android.response.CardBalanceResponse;
import com.beanstalkdata.android.response.CouponResponse;
import com.beanstalkdata.android.response.GiftCardListResponse;
import com.beanstalkdata.android.response.LocationResponse;
import com.beanstalkdata.android.response.LocationsResponse;
import com.beanstalkdata.android.response.PaymentTokenResponse;
import com.beanstalkdata.android.response.PushMessageByIdResponse;
import com.beanstalkdata.android.response.PushMessagesResponse;
import com.beanstalkdata.android.response.PushSuccessResponse;
import com.beanstalkdata.android.response.RegisterGiftCardResponse;
import com.beanstalkdata.android.response.RewardsCountResponse;
import com.beanstalkdata.android.response.StoreInfoResponse;
import com.beanstalkdata.android.response.StoresResponse;
import com.beanstalkdata.android.response.TrackTransactionResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Service for communication with Beanstalk API.
 */
public class BeanstalkService {

    private static final String TAG = BeanstalkService.class.getSimpleName();

    private final BeanstalkDataApi service;
    private final BeanstalkUserSession beanstalkUserSession;
    private final String beanstalkApiKey;
    private final String googleMapsApiKey;

    private String rewardName;
    private boolean isLoggingEnabled;

    /**
     * Base constructor to use when creating service instance.
     *
     * @param context          Context that will be used for accessing Resources and handling User Session.
     * @param beanstalkApiKey  API key for Beanstalk Data.
     * @param googleMapsApiKey API key for Google Maps.
     */
    public BeanstalkService(@NonNull Context context, @NonNull String beanstalkApiKey, @NonNull String googleMapsApiKey) {
        this.beanstalkUserSession = new BeanstalkUserSession(context);
        this.beanstalkApiKey = beanstalkApiKey;
        this.googleMapsApiKey = googleMapsApiKey;
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://proc.beanstalkdata.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(new Converter.Factory() {
                    @Override
                    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                        final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
                        return new Converter<ResponseBody, Object>() {
                            @Override
                            public Object convert(ResponseBody body) throws IOException {
                                return (body.contentLength() == 0) ? null : delegate.convert(body);
                            }
                        };
                    }
                })
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(okHttpClient)
                .build();

        service = retrofit.create(BeanstalkDataApi.class);
    }

    /**
     * Log out authenticated user.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void logoutUser(final OnReturnListener listener) {
        String contactId = beanstalkUserSession.getContactId();
        String token = beanstalkUserSession.getToken();

        beanstalkUserSession.release();

        Call<String> call = service.logoutUser(contactId, token);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                log("response " + body);
                if ("logged out".equalsIgnoreCase(body)
                        || "error".equalsIgnoreCase(body)) {
                    if (listener != null) {
                        listener.onFinished(null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(Error.LOGOUT_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.LOGOUT_FAILED);
                }
            }
        });
    }

    /**
     * Get coupons for authenticated user.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void getUserOffers(final OnReturnDataListener<Coupon[]> listener) {
        String contactId = beanstalkUserSession.getContactId();
        log("getUserOffers: " + contactId);

        Call<CouponResponse> call = service.getUserOffers(beanstalkApiKey, contactId);

        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if (response.isSuccessful()) {
                    CouponResponse data = response.body();
                    if (listener != null) {
                        listener.onFinished(data != null ?
                                data.getCoupons() : new Coupon[0], null);
                    }
                    return;
                }
                if (listener != null) {
                    listener.onFinished(null, Error.OFFERS_FAILED);
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.OFFERS_FAILED);
                }
            }
        });
    }

    /**
     * Get authenticated user progress towards earning their next offer.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void getProgress(final OnReturnDataListener<Double> listener) {
        String contactId = beanstalkUserSession.getContactId();

        Call<RewardsCountResponse> call = service.getProgress(beanstalkApiKey, contactId);

        call.enqueue(new Callback<RewardsCountResponse>() {
            @Override
            public void onResponse(Call<RewardsCountResponse> call, Response<RewardsCountResponse> response) {
                RewardsCountResponse data = response.body();
                if (data == null) {
                    if (listener != null) {
                        listener.onFinished(null, Error.OFFERS_FAILED);
                    }
                } else {
                    Double count = data.getCount();
                    if (count == null) {
                        if (listener != null) {
                            listener.onFinished(null, Error.OFFERS_FAILED);
                        }
                    } else {
                        if (listener != null) {
                            listener.onFinished(count, null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RewardsCountResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.OFFERS_FAILED);
                }
            }
        });
    }


    /**
     * Get contact for authenticated user.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void getContact(final OnReturnDataListener<Contact> listener) {
        String contactId = beanstalkUserSession.getContactId();
        log(" contact" + contactId);

        Call<Contact[]> call = service.getContact(beanstalkApiKey, contactId);

        call.enqueue(new Callback<Contact[]>() {
            @Override
            public void onResponse(Call<Contact[]> call, Response<Contact[]> response) {
                Contact[] data = response.body();
                if (data == null || data.length == 0) {
                    if (listener != null) {
                        listener.onFinished(null, Error.OFFERS_FAILED);
                    }
                } else {
                    Contact contact = data[0];
                    String fKey = contact.getFKey();
                    if (fKey != null) {
                        beanstalkUserSession.setFKey(fKey);
                    }
                    if (listener != null) {
                        listener.onFinished(contact, null);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contact[]> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.OFFERS_FAILED);
                }
            }
        });
    }

    /**
     * Get loyalty information for authenticated user.
     *
     * @param listener Callback that will run after network request is completed.
     */
    @Deprecated
    public void getLoyaltyInformation(final OnReturnListener listener) {
        String fKey = beanstalkUserSession.getFKey();

        Call<String> call = service.getLoyaltyInformation(beanstalkApiKey, fKey);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject data = null;
                try {
                    data = new JSONObject(response.body());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (data == null) {
                    if (listener != null) {
                        listener.onFinished(Error.LOYALTY_INFO_FAILED);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.LOYALTY_INFO_FAILED);
                }
            }
        });
    }

    /**
     * Query the status of a specific card.
     *
     * @param cardNumber Gift card number.
     * @param listener   Callback that will run after network request is completed.
     */
    public void inquireAboutCard(String cardNumber, final OnReturnDataListener<String> listener) {
        String contactId = beanstalkUserSession.getContactId();
        String token = beanstalkUserSession.getToken();
        log("inquireAboutCard() - [token, contactId, cardNumber] = [ " + token + " , " + contactId + " , " + cardNumber + " ]");

        Call<CardBalanceResponse> call = service.inquireAboutCard(beanstalkApiKey, contactId, token, cardNumber);

        call.enqueue(new Callback<CardBalanceResponse>() {
            @Override
            public void onResponse(Call<CardBalanceResponse> call, Response<CardBalanceResponse> response) {
                CardBalanceResponse data = response.body();
                if (data == null) {
                    if (listener != null) {
                        listener.onFinished(CardBalance.DEFAULT, Error.INQUIRE_CARD_FAILED);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(data.getDisplayBalance(), getErrorFromCBResponse(data));
                    }
                }
            }

            @Override
            public void onFailure(Call<CardBalanceResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(CardBalance.DEFAULT, Error.INQUIRE_CARD_FAILED);
                }
            }
        });
    }

    /**
     * Set preferred card for payments.
     *
     * @param cardNumber Card number user wishes to make preferred.
     * @param listener   Callback that will run after network request is completed.
     */
    @Deprecated
    public void setPreferredCard(String cardNumber, final OnReturnListener listener) {
        String contactId = beanstalkUserSession.getContactId();
        String token = beanstalkUserSession.getToken();

        Call<String> call = service.setPreferredCard(beanstalkApiKey, contactId, token, cardNumber);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject data = null;
                try {
                    data = new JSONObject(response.body());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (data == null) {
                    if (listener != null) {
                        listener.onFinished(Error.PREFERRED_CARD_FAILED);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.PREFERRED_CARD_FAILED);
                }
            }
        });
    }

    /**
     * Start payment transaction for POS.
     *
     * @param paymentId Card used for payments.
     * @param coupons   Comma delimited coupon numbers.
     * @param listener  Callback that will run after network request is completed.
     */
    public void startPaymentWithPaymentId(String paymentId, List<String> coupons, final OnReturnDataListener<String> listener) {
        String contactId = beanstalkUserSession.getContactId();
        String token = beanstalkUserSession.getToken();
        String couponsStr = getCouponsString(coupons);
        log("coupons : " + couponsStr);
        log("payment : " + paymentId);

        Call<PaymentTokenResponse> call = service.startPayment(beanstalkApiKey, contactId, contactId, token, paymentId, couponsStr);

        call.enqueue(new Callback<PaymentTokenResponse>() {
            @Override
            public void onResponse(Call<PaymentTokenResponse> call, Response<PaymentTokenResponse> response) {
                PaymentTokenResponse data = response.body();
                if (data == null || isPaymentTokenEmpty(data)) {
                    if (listener != null) {
                        listener.onFinished(null, Error.PAYMENT_FAILED);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(data.getPaymentToken(), null);
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentTokenResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.PAYMENT_FAILED);
                }
            }
        });
    }

    /**
     * Register payment devices or gift cards for authenticated user.
     *
     * @param giftCardNumber Gift card number.
     * @param giftPinNumber  Gift card pin number (Optional).
     * @param listener       Callback that will run after network request is completed.
     */
    public void registerNewGiftCard(String giftCardNumber, String giftPinNumber, final OnReturnListener listener) {
        final String contactId = beanstalkUserSession.getContactId();
        String token = beanstalkUserSession.getToken();

        Call<RegisterGiftCardResponse> call = service.registerGiftCard(beanstalkApiKey, contactId, token, giftCardNumber, giftPinNumber);

        call.enqueue(new Callback<RegisterGiftCardResponse>() {
            @Override
            public void onResponse(Call<RegisterGiftCardResponse> call, Response<RegisterGiftCardResponse> response) {
                RegisterGiftCardResponse data = response.body();
                if (data == null || data.isFailed()) {
                    if (listener != null) {
                        String error = Error.ADD_CARD_FAILED;
                        if (data != null) {
                            error = String.format(Error.ADD_CARD_FAILED, getErrorFromRGCResponse(data));
                        }
                        listener.onFinished(error);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterGiftCardResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.GIFT_LIST_FAILED);
                }
            }
        });
    }

    /**
     * Get list of available gift cards for authenticated user.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void getGiftCardList(final OnReturnDataListener<GiftCard[]> listener) {
        String contactId = beanstalkUserSession.getContactId();
        String token = beanstalkUserSession.getToken();

        Call<GiftCardListResponse> call = service.getGiftCardList(beanstalkApiKey, contactId, token);

        call.enqueue(new Callback<GiftCardListResponse>() {
            @Override
            public void onResponse(Call<GiftCardListResponse> call, Response<GiftCardListResponse> response) {
                if (response.isSuccessful()) {
                    GiftCardListResponse data = response.body();
                    log("onResponse: " + data);
                    if (data == null || !data.isFailed()) {
                        if (listener != null) {
                            listener.onFinished(data != null ?
                                    data.getGiftCards() : new GiftCard[0], null);
                        }
                        return;
                    }
                }
                if (listener != null) {
                    listener.onFinished(null, Error.GIFT_LIST_FAILED);
                }
            }

            @Override
            public void onFailure(Call<GiftCardListResponse> call, Throwable t) {
                log("onFailure: " + t);
                if (listener != null) {
                    listener.onFinished(null, Error.GIFT_LIST_FAILED);
                }
            }
        });
    }

    /**
     * Sends message with new password to specified email.
     *
     * @param email    Email address to which letter should be sent.
     * @param listener Callback that will run after network request is completed.
     */
    public void resetPassword(String email, final OnReturnDataListener<String> listener) {
        Call<String> call = service.resetPassword(beanstalkApiKey, email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (listener != null) {
                    listener.onFinished(response.body(), null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.RESET_PASSWORD_FAILED);
                }
            }
        });
    }

    /**
     * Update authenticated user password.
     *
     * @param password New password.
     * @param listener Callback that will run after network request is completed.
     */
    public void updatePassword(String password, final OnReturnDataListener<String> listener) {
        Map<String, String> request = new HashMap<>(4);
        request.put("contact", beanstalkUserSession.getContactId());
        request.put("token", beanstalkUserSession.getToken());
        request.put("password", password);
        request.put("key", beanstalkApiKey);

        Call<String> call = service.updatePassword(request);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (listener != null) {
                    listener.onFinished(response.body(), null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.UPDATE_PASSWORD_FAILED);
                }
            }
        });
    }

    /**
     * Authenticate user.
     *
     * @param request  Request information for user authentication.
     * @param listener Callback that will run after network request is completed.
     */
    public void authenticateUser(final AuthenticateUserRequest request, final OnReturnDataListener<Boolean> listener) {
        Call<Contact[]> contactByEmail = service.getContactByEmail(beanstalkApiKey, request.getEmail());

        contactByEmail.enqueue(new Callback<Contact[]>() {
            @Override
            public void onResponse(Call<Contact[]> call, Response<Contact[]> response) {
                boolean success = response.isSuccessful();
                log("checkUserByEmail() - response status " + success);
                boolean isNovadineUser = false;
                if (success) {
                    Contact[] contacts = response.body();
                    if (contacts != null && contacts.length > 0 && contacts[0] != null) {
                        Contact contact = contacts[0];
                        isNovadineUser = contact.isNovadineUser();
                    }
                }
                authUser(request, isNovadineUser, listener);
            }

            @Override
            public void onFailure(Call<Contact[]> call, Throwable t) {
                authUser(request, false, listener);
            }
        });
    }

    /**
     * Check user session.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void checkUserSession(final OnReturnDataListener<Boolean> listener) {
        String contactId = beanstalkUserSession.getContactId();
        String token = beanstalkUserSession.getToken();

        if ((contactId == null) || (token == null)) {
            if (listener != null) {
                listener.onFinished(false, null);
                return;
            }
        }

        Call<String> call = service.checkSession(contactId, token);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                boolean status = false;
                String statusMessage = Error.SESSION_INVALID;

                if ("valid".equals(body)) {
                    status = true;
                    statusMessage = Error.SESSION_VALID;
                }

                if (listener != null) {
                    listener.onFinished(status, statusMessage);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(false, Error.SESSION_INVALID);
                }
            }

        });
    }

    /**
     * Update contact.
     *
     * @param request  Request information for updating contact.
     * @param listener Callback that will run after network request is completed.
     */
    public void updateContact(final ContactRequest request, final OnReturnListener listener) {
        Map<String, String> params = request.asParams();
        if (params.size() <= 1) {
            listener.onFinished(null);
        } else {
            Call<String[]> contact = service.updateContact(beanstalkApiKey, params);

            contact.enqueue(new Callback<String[]>() {
                @Override
                public void onResponse(Call<String[]> call, Response<String[]> response) {
                    String[] body = response.body();
                    log("onResponse() - " + Arrays.toString(body));
                    if (body == null || body.length != 1) {
                        if (listener != null) {
                            listener.onFinished(Error.REGISTRATION_FAILED);
                        }
                    } else {
                        if (listener != null) {
                            request.applyUpdate();
                            listener.onFinished(null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<String[]> call, Throwable t) {
                    log("onFailure()" + t.toString());
                    if (listener != null) {
                        listener.onFinished(Error.REGISTRATION_FAILED);
                    }
                }
            });
        }
    }

    /**
     * Add contact with email.
     *
     * @param request  Request information for adding contact.
     * @param listener Callback that will run after network request is completed.
     */
    public void addContactWithEmail(ContactRequest request, final OnReturnListener listener) {
        checkUserByEmail(request, listener);
    }

    /**
     * Add Novadine contact.
     *
     * @param request  Request information for adding contact.
     * @param listener Callback that will run after network request is completed.
     */
    public void addNovadineContact(ContactRequest request, final OnReturnListener listener) {
        createContact(request, listener, false);
    }

    /**
     * Credit card registration.
     *
     * @param cardNumber Card number.
     * @param cardPin    Card pin (Optional).
     * @param listener   Callback that will run after network request is completed.
     */
    public void paperCardRegistration(String cardNumber, String cardPin, final OnReturnListener listener) {
        String contactId = beanstalkUserSession.getContactId();
        Call<String> call = service.paperCardRegistration(beanstalkApiKey, "addNewCard", cardNumber, cardPin, contactId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                if ("card added".equals(body)) {
                    if (listener != null) {
                        listener.onFinished(null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(Error.REGISTER_CARD_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.REGISTER_CARD_FAILED);
                }
            }
        });
    }

    /**
     * Check stores availability at location of the specified zip code.
     *
     * @param zip      Zip code.
     * @param listener Callback that will run after network request is completed.
     */
    public void checkStores(String zip, final OnReturnListener listener) {
        Call<LocationResponse> locationByZipCode = service.getLocationByZipCode(googleMapsApiKey, zip);
        locationByZipCode.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                LocationResponse body = response.body();
                if (body != null && !body.isFailed()) {
                    checkLocation(body.getLocation(), listener);
                } else {
                    if (listener != null) {
                        listener.onFinished(Error.SIGN_UP_ZIP_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.SIGN_UP_ZIP_FAILED);
                }
            }
        });
    }

    /**
     * Check stores availability at specified location.
     *
     * @param location Location coordinates.
     * @param listener Callback that will run after network request is completed.
     */
    public void checkLocation(Location location, final OnReturnListener listener) {
        Call<StoresResponse> storesResponseCall = service.checkLocation(beanstalkApiKey, location.getLatitude(), location.getLongitude());
        storesResponseCall.enqueue(new Callback<StoresResponse>() {
            @Override
            public void onResponse(Call<StoresResponse> call, Response<StoresResponse> response) {
                StoresResponse body = response.body();
                if (body != null && !body.isFailed()) {
                    if (listener != null) {
                        listener.onFinished(null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(Error.SIGN_UP_LOCATION_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<StoresResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.SIGN_UP_LOCATION_FAILED);
                }
            }
        });
    }

    /**
     * Get store info by store Id.
     *
     * @param storeId  store Id.
     * @param listener Callback that will run after network request is completed.
     */
    public void getStoreInfo(String storeId, final OnReturnListener listener) {
        Call<StoreInfoResponse> storeInfoResponseCall = service.getStoreInfo(beanstalkApiKey, storeId);
        storeInfoResponseCall.enqueue(new Callback<StoreInfoResponse>() {
            @Override
            public void onResponse(Call<StoreInfoResponse> call, Response<StoreInfoResponse> response) {
                StoreInfoResponse body = response.body();
                if (body != null && !body.isFailed()) {
                    if (listener != null) {
                        listener.onFinished(null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(Error.STORE_INFO_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreInfoResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.STORE_INFO_FAILED);
                }
            }
        });
    }

    /**
     * Get all stores locations.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void getAllStores(final OnReturnListener listener) {
        Call<LocationsResponse> locationsResponseCall = service.getLocations(beanstalkApiKey);
        locationsResponseCall.enqueue(new Callback<LocationsResponse>() {
            @Override
            public void onResponse(Call<LocationsResponse> call, Response<LocationsResponse> response) {
                LocationsResponse body = response.body();
                if (body != null && !body.isFailed()) {
                    if (listener != null) {
                        listener.onFinished(null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(Error.STORE_INFO_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationsResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.STORES_LOCATIONS_FAILED);
                }
            }
        });
    }

    /**
     * Enroll push notifications for authenticated user.
     *
     * @param deviceToken GCM device token.
     * @param listener    Callback that will run after network request is completed.
     */
    public void enrollPushNotification(String deviceToken, OnReturnDataListener<PushSuccessResponse> listener) {
        service.enrollPushNotification(beanstalkApiKey, beanstalkUserSession.getContactId(), deviceToken, PlatformType.ANDROID)
                .enqueue(new SimpleCallback<>(Error.ENROLL_PUSH_NOTIFICATION_FAILED, listener));
    }

    /**
     * Update authenticated user device token for push notifications.
     *
     * @param deviceToken GCM device token.
     * @param listener    Callback that will run after network request is completed.
     */
    public void modifyPushNotification(String deviceToken, OnReturnDataListener<PushSuccessResponse> listener) {
        service.modifyPushNotification(beanstalkApiKey, beanstalkUserSession.getContactId(), deviceToken, PlatformType.ANDROID)
                .enqueue(new SimpleCallback<>(Error.MODIFY_PUSH_NOTIFICATION_FAILED, listener));
    }

    /**
     * Delete push notifications for authenticated user.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void deletePushNotification(OnReturnDataListener<PushSuccessResponse> listener) {
        service.deletePushNotification(beanstalkApiKey, beanstalkUserSession.getContactId())
                .enqueue(new SimpleCallback<>(Error.DELETE_PUSH_NOTIFICATION_FAILED, listener));
    }

    /**
     * Returns messages assigned to authenticated user.
     *
     * @param maxResults Maximum number of messages desired.
     * @param listener   Callback that will run after network request is completed.
     */
    public void getContactMessages(int maxResults, OnReturnDataListener<PushMessagesResponse> listener) {
        service.getContactMessages(beanstalkApiKey, beanstalkUserSession.getContactId(), maxResults)
                .enqueue(new SimpleCallback<>(Error.GET_MESSAGES_FAILED, listener));
    }

    /**
     * Returns messages of specified message type for authenticated user.
     *
     * @param messageContentType Required message content type ({@link MessageContentType}).
     * @param listener           Callback that will run after network request is completed.
     */
    public void getMessagesByOsAndType(@MessageContentType String messageContentType, OnReturnDataListener<PushMessagesResponse> listener) {
        service.getMessagesByOsAndType(beanstalkApiKey, messageContentType, PlatformType.ANDROID)
                .enqueue(new SimpleCallback<>(Error.GET_MESSAGES_FAILED, listener));
    }

    /**
     * Returns messages of specified message type for authenticated user.
     *
     * @param imageType Required message content type ({@link ImageType}).
     * @param listener  Callback that will run after network request is completed.
     */
    public void getMessagesByImageType(@ImageType String imageType, OnReturnDataListener<PushMessagesResponse> listener) {
        service.getMessagesByImageType(beanstalkApiKey, beanstalkUserSession.getContactId(), imageType)
                .enqueue(new SimpleCallback<>(Error.GET_MESSAGES_FAILED, listener));
    }

    /**
     * Returns message for specified message id.
     *
     * @param messageId Message id.
     * @param listener  Callback that will run after network request is completed.
     */
    public void getMessageById(String messageId, OnReturnDataListener<PushMessageByIdResponse> listener) {
        service.getMessageById(beanstalkApiKey, messageId)
                .enqueue(new SimpleCallback<>(Error.GET_MESSAGES_FAILED, listener));
    }

    /**
     * Update status for message with specified id.
     *
     * @param messageId     Message id.
     * @param messageStatus New message status ({@link MessageType}).
     * @param listener      Callback that will run after network request is completed.
     */
    public void updateMessageStatus(String messageId, @MessageType String messageStatus, OnReturnDataListener<PushSuccessResponse> listener) {
        service.updateMessageStatus(beanstalkApiKey, messageId, messageStatus)
                .enqueue(new SimpleCallback<>(Error.GET_MESSAGES_FAILED, listener));
    }

    /**
     * Add transaction event to authenticated user.
     *
     * @param username Beanstalk username.
     * @param details  Any string. Can interpret JSON as parsed array and create searchable transaction object which can be used for campaigns and other generic transactional workflow.
     * @param listener Callback that will run after network request is completed.
     */
    public void trackTransaction(String username, String details, final OnReturnDataListener<TrackTransactionResponse> listener) {
        service.trackTransaction(beanstalkApiKey, beanstalkUserSession.getContactId(), username, details)
                .enqueue(new SimpleCallback<>(Error.TRACK_TRANSACTION_FAILED, listener));
    }

    /**
     * Check logging state.
     *
     * @return Logging is enabled or not.
     */
    public boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    /**
     * Set logging enabled state.
     *
     * @param enable Logging state.
     */
    public void enableLogging(boolean enable) {
        this.isLoggingEnabled = enable;
    }

    String getErrorFromCBResponse(CardBalanceResponse data) {
        return data.getError();
    }

    boolean isPaymentTokenEmpty(PaymentTokenResponse data) {
        return TextUtils.isEmpty(data.getPaymentToken());
    }

    String getCouponsString(List<String> coupons) {
        return TextUtils.join(",", coupons);
    }

    String getErrorFromRGCResponse(RegisterGiftCardResponse data) {
        return data.getError();
    }

    String[] parseAuthUserResponse(String body) {
        try {
            JSONArray jsonArray = new JSONArray(body);
            if (jsonArray.length() == 2) {
                String[] strings = new String[2];
                strings[0] = jsonArray.optString(0);
                strings[1] = jsonArray.optString(1);
                return strings;
            }
        } catch (JSONException e) {
            log("authenticateUser exception:" + e);
        }
        return null;
    }

    String parseCreateContactResponse(String body) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray == null || jsonArray.length() != 2) {
            return null;
        } else {
            return jsonArray.optString(0);
        }
    }

    void addNewContact(ContactRequest request, boolean novadine, OnReturnListener onReturnListener) {
        if (novadine) {
            addNovadineContact(request, onReturnListener);
        } else {
            addContactWithEmail(request, onReturnListener);
        }
    }

    private Gson getGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
    }

    private void checkLocation(LocationResponse.Location location, final OnReturnListener listener) {
        Call<StoresResponse> storesResponseCall = service.checkLocation(beanstalkApiKey, location.getLatitude(), location.getLongitude());
        storesResponseCall.enqueue(new Callback<StoresResponse>() {
            @Override
            public void onResponse(Call<StoresResponse> call, Response<StoresResponse> response) {
                StoresResponse body = response.body();
                if (body != null && !body.isFailed()) {
                    if (listener != null) {
                        listener.onFinished(null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(Error.SIGN_UP_ZIP_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<StoresResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.SIGN_UP_ZIP_FAILED);
                }
            }
        });
    }

    private void checkUserByEmail(final ContactRequest request, final OnReturnListener listener) {
        log("checkUserByEmail() - " + request.getEmail());
        final Call<Contact[]> contactByEmail = service.getContactByEmail(beanstalkApiKey, request.getEmail());

        contactByEmail.enqueue(new Callback<Contact[]>() {

            @Override
            public void onResponse(Call<Contact[]> call, Response<Contact[]> response) {
                boolean success = response.isSuccessful();
                log("checkUserByEmail() - response status " + success);
                if (success) {
                    Contact[] contacts = response.body();
                    log("checkUserByEmail() - contacts found " + Arrays.toString(contacts));
                    if (contacts != null && contacts.length > 0 && contacts[0] != null) {
                        Contact contact = contacts[0];
                        log("checkUserByEmail() - contact found " + contact);
                        String prospect = contact.getProspect();
                        log("checkUserByEmail() - prospect found " + prospect);
                        if ("eclub".equals(prospect)) {
                            checkUserByPhone(request, listener);
                        } else {
                            if (listener != null) {
                                listener.onFinished(Error.CONTACT_EXISTED_EMAIL);
                            }
                        }
                    } else {
                        checkUserByPhone(request, listener);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(Error.SIGN_IN_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contact[]> call, Throwable t) {
                checkUserByPhone(request, listener);
            }
        });
    }

    private void authUser(AuthenticateUserRequest request, final boolean isNovadineUser, final OnReturnDataListener<Boolean> listener) {
        Call<ResponseBody> authenticateUser = service.authenticateUser(request.getEmail(), request.getPassword(), beanstalkApiKey, request.getTime());
        authenticateUser.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "error";
                try {
                    body = response.body().string();
                } catch (IOException e) {
                    log("authenticateUser exception: " + e);
                }
                log("authenticateUser: " + body);
                if ("error".equalsIgnoreCase(body)) {
                    if (listener != null) {
                        listener.onFinished(false, Error.AUTHORIZATION_FAILED);
                    }
                    return;
                }
                String[] authResponseArgs = parseAuthUserResponse(body);
                if (authResponseArgs != null) {
                    if (authResponseArgs.length == 2) {
                        String contactId = authResponseArgs[0];
                        String token = authResponseArgs[1];
                        if (listener != null) {
                            // TODO: it isn't clear what to do for novadine users.
                            if (isNovadineUser) {
                                log("authenticateUser: Novadine user logged in.");
                            }
                            beanstalkUserSession.save(contactId, token);
                            listener.onFinished(true, null);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(false, Error.AUTHORIZATION_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(false, Error.AUTHORIZATION_FAILED);
                }
            }
        });
    }

    private void checkUserByPhone(final ContactRequest request, final OnReturnListener listener) {
        Call<Contact[]> contactByPhone = service.getContactByPhone(beanstalkApiKey, request.getPhone());
        contactByPhone.enqueue(new Callback<Contact[]>() {
            @Override
            public void onResponse(Call<Contact[]> call, Response<Contact[]> response) {
                boolean success = response.isSuccessful();
                log("checkUserByPhone() - response status " + success);
                if (success) {
                    Contact[] contacts = response.body();
                    if (contacts != null && contacts.length > 0 && contacts[0] != null) {
                        Contact contact = contacts[0];
                        log("checkUserByPhone() - contact found " + contact);
                        String prospect = contact.getProspect();
                        log("checkUserByPhone() - prospect found " + prospect);
                        if ("eclub".equals(prospect)) {
                            createContact(request, listener, true);
                        } else {
                            if (listener != null) {
                                listener.onFinished(Error.CONTACT_EXISTED_PHONE);
                            }
                        }
                    } else {
                        createContact(request, listener, true);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(Error.SIGN_IN_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contact[]> call, Throwable t) {
                createContact(request, listener, true);
            }
        });
    }

    private void createContact(final ContactRequest request, final OnReturnListener listener, final boolean createUser) {
        Call<ResponseBody> contact = service.createContact(beanstalkApiKey, request.asParams());

        contact.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String body = "error";
                try {
                    body = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log("addContactWithEmail() - create contact " + body);
                if ("error".equalsIgnoreCase(body)) {
                    if (listener != null) {
                        listener.onFinished(Error.SIGN_IN_FAILED);
                    }
                    return;
                }

                if (parseCreateContactResponse(body) == null) {
                    if (listener != null) {
                        listener.onFinished(Error.SIGN_IN_FAILED);
                    }
                } else {
                    String contactId = parseCreateContactResponse(body);
                    log("contact id : " + contactId);
                    if (createUser) {
                        Call<String> user = service.createUser(request.getEmail(),
                                request.getPassword(),
                                beanstalkApiKey,
                                contactId);
                        user.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String body = response.body();
                                log("addContactWithEmail() - create user " + body);
                                if ("Success".equalsIgnoreCase(body)) {
                                    if (listener != null) {
                                        listener.onFinished(null);
                                    }
                                } else {
                                    if (listener != null) {
                                        listener.onFinished(Error.SIGN_IN_FAILED);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                if (listener != null) {
                                    listener.onFinished(Error.SIGN_IN_FAILED);
                                }
                            }
                        });
                    } else {
                        if (listener != null) {
                            listener.onFinished(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.SIGN_IN_FAILED);
                }
            }
        });
    }

    private void log(String msg) {
        if (isLoggingEnabled()) {
            Log.d(TAG, msg);
        }
    }

    private static class SimpleCallback<T> implements Callback<T> {

        private final String error;
        private final OnReturnDataListener<T> listener;

        private SimpleCallback(String error, OnReturnDataListener<T> listener) {
            this.error = error;
            this.listener = listener;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if ((response != null) && (response.body() != null) && response.isSuccessful()) {
                if (listener != null) {
                    listener.onFinished(response.body(), null);
                }
            } else {
                onFailure(call, null);
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if (listener != null) {
                listener.onFinished(null, error);
            }
        }

    }

}
