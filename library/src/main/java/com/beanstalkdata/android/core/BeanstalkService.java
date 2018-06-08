/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.core;

import android.app.AlarmManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.beanstalkdata.android.callback.OnReturnDataListener;
import com.beanstalkdata.android.callback.OnReturnListener;
import com.beanstalkdata.android.model.CardBalance;
import com.beanstalkdata.android.model.Contact;
import com.beanstalkdata.android.model.ContactAsset;
import com.beanstalkdata.android.model.Coupon;
import com.beanstalkdata.android.model.GiftCard;
import com.beanstalkdata.android.model.LoyaltyUser;
import com.beanstalkdata.android.model.PushMessage;
import com.beanstalkdata.android.model.TransactionEvent;
import com.beanstalkdata.android.model.deserializer.ContactDeserializer;
import com.beanstalkdata.android.model.deserializer.IdHolderV2Deserializer;
import com.beanstalkdata.android.model.deserializer.LocationV2Deserializer;
import com.beanstalkdata.android.model.deserializer.MultiDateFormatDeserializer;
import com.beanstalkdata.android.model.deserializer.PushMessagesDeserializer;
import com.beanstalkdata.android.model.deserializer.StoreV2Deserializer;
import com.beanstalkdata.android.model.type.ImageType;
import com.beanstalkdata.android.model.type.MessageContentType;
import com.beanstalkdata.android.model.type.MessageType;
import com.beanstalkdata.android.model.type.PlatformType;
import com.beanstalkdata.android.request.AuthenticateUserFacebookRequest;
import com.beanstalkdata.android.request.AuthenticateUserGoogleRequest;
import com.beanstalkdata.android.request.AuthenticateUserRequest;
import com.beanstalkdata.android.request.ContactRequest;
import com.beanstalkdata.android.response.CardBalanceResponse;
import com.beanstalkdata.android.response.ContactDeletedResponse;
import com.beanstalkdata.android.response.ContactUsResponse;
import com.beanstalkdata.android.response.CouponResponse;
import com.beanstalkdata.android.response.GiftCardListResponse;
import com.beanstalkdata.android.response.LocationResponse;
import com.beanstalkdata.android.response.LocationsResponse;
import com.beanstalkdata.android.response.PaymentTokenResponse;
import com.beanstalkdata.android.response.PushMessageByIdResponse;
import com.beanstalkdata.android.response.PushMessagesResponse;
import com.beanstalkdata.android.response.PushSuccessResponse;
import com.beanstalkdata.android.response.RegisterGiftCardResponse;
import com.beanstalkdata.android.response.RelocateResponse;
import com.beanstalkdata.android.response.RewardsCountResponse;
import com.beanstalkdata.android.response.StoreInfoResponse;
import com.beanstalkdata.android.response.StoresResponse;
import com.beanstalkdata.android.response.StoresResponseV2;
import com.beanstalkdata.android.response.TrackTransactionResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.HttpUrl;
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
    private static final String SCHEME = "https";
    private static final String HOST = "proc.beanstalkdata.com";
    private static final String PUBLIC_KEY = "sha256/Hnfu+TUYgdkXIUunnIl6yIovY/WYZdOdIw/1TdZy79Y=";

    private final BeanstalkDataApi service;
    private final BeanstalkUserSession beanstalkUserSession;
    private final String beanstalkApiKey;
    private final String googleMapsApiKey;
    private final SimpleDateFormat transactionDateFormat;

    // Workaround for empty response for push notification messages
    private final PushMessagesResponse defaultMessages;

    private boolean isLoggingEnabled;

    /**
     * Constructor to use when creating service instance with default Beanstalk user session.
     *
     * @param context          Context that will be used for Default Beanstalk User Session.
     * @param beanstalkApiKey  API key for Beanstalk Data.
     * @param googleMapsApiKey API key for Google Maps.
     */
    public BeanstalkService(@NonNull Context context, @NonNull String beanstalkApiKey, @NonNull String googleMapsApiKey) {
        this(new DefaultBeanstalkUserSession(context), beanstalkApiKey, googleMapsApiKey);
    }

    /**
     * Base constructor to use when creating service instance.
     *
     * @param beanstalkApiKey  API key for Beanstalk Data.
     * @param googleMapsApiKey API key for Google Maps.
     */
    public BeanstalkService(@NonNull BeanstalkUserSession beanstalkUserSession, @NonNull String beanstalkApiKey, @NonNull String googleMapsApiKey) {
        this.beanstalkUserSession = beanstalkUserSession;
        this.beanstalkApiKey = beanstalkApiKey;
        this.googleMapsApiKey = googleMapsApiKey;

        transactionDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        defaultMessages = new PushMessagesResponse();
        defaultMessages.setPushMessages(new ArrayList<PushMessage>());

        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add(HOST, PUBLIC_KEY)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .certificatePinner(certificatePinner)
                .build();

        HttpUrl baseUrl = new HttpUrl.Builder().scheme(SCHEME).host(HOST).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
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
     * Get Beanstalk user session.
     *
     * @return Current user session.
     */
    public BeanstalkUserSession getBeanstalkUserSession() {
        return beanstalkUserSession;
    }

    /**
     * Get contacts by email.
     *
     * @param email    user's email.
     * @param listener Callback that will run after network request is completed.
     */
    public void getContactsByEmail(String email, final OnReturnDataListener<Contact[]> listener) {
        Call<Contact[]> call = service.getContactByEmail(beanstalkApiKey, email);

        call.enqueue(new Callback<Contact[]>() {

            @Override
            public void onResponse(Call<Contact[]> call, Response<Contact[]> response) {
                if (listener != null) {
                    if (response != null && response.isSuccessful()) {
                        listener.onFinished(response.body(), null);
                    } else {
                        listener.onFinished(null, Error.CONTACTS_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contact[]> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.CONTACTS_FAILED);
                }
            }

        });
    }

    /**
     * Get contacts by phone.
     *
     * @param phone    user's phone.
     * @param listener Callback that will run after network request is completed.
     */
    public void getContactsByPhone(String phone, final OnReturnDataListener<Contact[]> listener) {
        Call<Contact[]> call = service.getContactByPhone(beanstalkApiKey, phone);

        call.enqueue(new Callback<Contact[]>() {

            @Override
            public void onResponse(Call<Contact[]> call, Response<Contact[]> response) {
                if (listener != null) {
                    if (response != null && response.isSuccessful()) {
                        listener.onFinished(response.body(), null);
                    } else {
                        listener.onFinished(null, Error.CONTACTS_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contact[]> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.CONTACTS_FAILED);
                }
            }

        });
    }

    /**
     * Get contacts by fKey.
     *
     * @param fkey     fKey (Foreign key).
     * @param listener Callback that will run after network request is completed.
     */
    public void getContactsByFkey(String fkey, final OnReturnDataListener<Contact[]> listener) {
        Call<Contact[]> call = service.getContactByFkey(beanstalkApiKey, fkey);

        call.enqueue(new Callback<Contact[]>() {

            @Override
            public void onResponse(Call<Contact[]> call, Response<Contact[]> response) {
                if (listener != null) {
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        listener.onFinished(response.body(), null);
                    } else {
                        listener.onFinished(null, Error.CONTACTS_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contact[]> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.CONTACTS_FAILED);
                }
            }

        });
    }

    /**
     * Get contact by fKey.
     *
     * @param fkey     fKey (Foreign key).
     * @param listener Callback that will run after network request is completed.
     */
    public void getContactByFkey(String fkey, final OnReturnDataListener<Contact> listener) {
        Call<Contact[]> call = service.getContactByFkey(beanstalkApiKey, fkey);

        call.enqueue(new Callback<Contact[]>() {

            @Override
            public void onResponse(Call<Contact[]> call, Response<Contact[]> response) {
                if (listener != null) {
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        Contact contact = response.body()[0];
                        String fKey = contact.getFKey();
                        if (fKey != null) {
                            beanstalkUserSession.setFKey(fKey);
                        }
                        listener.onFinished(contact, null);
                    } else {
                        listener.onFinished(null, Error.CONTACT_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contact[]> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.CONTACT_FAILED);
                }
            }

        });
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
        Call<CouponResponse> call = service.getUserOffers(beanstalkApiKey, contactId);
        call.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if (response.isSuccessful()) {
                    CouponResponse data = response.body();
                    if (listener != null) {
                        listener.onFinished(data != null ? data.getCoupons() : new Coupon[0], null);
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
     * Get contact assets.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void getContactAsset(final OnReturnDataListener<ContactAsset> listener) {
        String contactId = beanstalkUserSession.getContactId();
        Call<ContactAsset> request = service.getContactAsset(beanstalkApiKey, contactId);
        request.enqueue(new Callback<ContactAsset>() {
            @Override
            public void onResponse(Call<ContactAsset> call, Response<ContactAsset> response) {
                ContactAsset contactAsset = response.body();
                if (contactAsset != null) {
                    if (contactAsset.isSuccess()) {
                        listener.onFinished(contactAsset, null);
                    } else {
                        if (listener != null) {
                            listener.onFinished(null, contactAsset.getError());
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null, Error.CONTACT_ASSET_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<ContactAsset> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.CONTACT_ASSET_FAILED);
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
                        listener.onFinished(null, Error.CONTACT_FAILED);
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
                    listener.onFinished(null, Error.CONTACT_FAILED);
                }
            }
        });
    }

    /**
     * Delete contact and wipe user session.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void deleteContact(final OnReturnListener listener) {
        String contactId = beanstalkUserSession.getContactId();

        Call<ContactDeletedResponse> call = service.deleteContact(beanstalkApiKey, contactId);

        call.enqueue(new Callback<ContactDeletedResponse>() {

            @Override
            public void onResponse(Call<ContactDeletedResponse> call, Response<ContactDeletedResponse> response) {
                if ((response != null) && (response.body() != null) && response.body().isSuccess()) {
                    beanstalkUserSession.release();
                    if (listener != null) {
                        listener.onFinished(null);
                    }
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<ContactDeletedResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.DELETE_CONTACT_FAILED);
                }
            }

        });
    }

    /**
     * Relocate a contact.
     *
     * @param latitude  user's latitude coordinate.
     * @param longitude user's longitude coordinate.
     * @param listener  Callback that will run after network request is completed.
     */
    public void relocateContact(float latitude, float longitude, final OnReturnListener listener) {
        String contactId = beanstalkUserSession.getContactId();
        Call<RelocateResponse> request = service.relocateContact(beanstalkApiKey, contactId, latitude, longitude);
        request.enqueue(new Callback<RelocateResponse>() {
            @Override
            public void onResponse(Call<RelocateResponse> call, Response<RelocateResponse> response) {
                if (response != null && response.isSuccessful()) {
                    if (listener != null) {
                        listener.onFinished(null);
                    }
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<RelocateResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.CONTACT_RELOCATE_FAILED);
                }
            }
        });
    }

    /**
     * Start contact location tracking with the default tracking interval 1 hour.
     */
    public void startLocationTracking() {
        beanstalkUserSession.startContactRelocation(AlarmManager.INTERVAL_HOUR);
    }

    /**
     * Start contact location tracking with interval set. The interval is in seconds.
     * The interval can't be less than 15 minutes.
     *
     * @param interval period in seconds for relocate contact updates.
     */
    public void startLocationTracking(long interval) {
        beanstalkUserSession.startContactRelocation(interval);
    }

    /**
     * Stop contact location tracking.
     */
    public void stopLocationTracking() {
        beanstalkUserSession.stopContactRelocation();
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
                            listener.onFinished(data != null ? data.getGiftCards() : new GiftCard[0], null);
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
     * Sends message with new password to specified email.
     *
     * @param email    Email address to which letter should be sent.
     * @param listener Callback that will run after network request is completed.
     */
    public void resetPasswordV2(String email, final OnReturnDataListener<String> listener) {
        Call<String> call = service.resetPasswordV2(beanstalkApiKey, email, "2");
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
                authUser(request, listener);
            }

            @Override
            public void onFailure(Call<Contact[]> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(false, Error.AUTHORIZATION_FAILED);
                }
            }
        });
    }

    /**
     * Authenticate user using Google.
     *
     * @param request  Request information for user authentication using Google.
     * @param listener Callback that will run after network request is completed.
     */
    public void authenticateUserGoogle(final AuthenticateUserGoogleRequest request, final OnReturnDataListener<Boolean> listener) {
        Call<ResponseBody> authRequest = service.authenticateUserGoogle(beanstalkApiKey, request.getGoogleId(), request.getGoogleToken());
        authRequest.enqueue(new Callback<ResponseBody>() {
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
                        listener.onFinished(false, Error.AUTHORIZATION_GOOGLE_FAILED);
                    }
                    return;
                }
                String[] authResponseArgs = parseAuthUserResponse(body);
                if (authResponseArgs != null) {
                    if (authResponseArgs.length == 2) {
                        String contactId = authResponseArgs[0];
                        String token = authResponseArgs[1];
                        if (listener != null) {
                            beanstalkUserSession.save(contactId, token);
                            listener.onFinished(true, null);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(false, Error.AUTHORIZATION_GOOGLE_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(false, Error.AUTHORIZATION_GOOGLE_FAILED);
                }
            }
        });
    }

    /**
     * Authenticate user using Facebook.
     *
     * @param request  Request information for user authentication using Facebook.
     * @param listener Callback that will run after network request is completed.
     */
    public void authenticateUserFacebook(final AuthenticateUserFacebookRequest request, final OnReturnDataListener<Boolean> listener) {
        Call<ResponseBody> authRequest = service.authenticateUserFacebook(beanstalkApiKey, request.getFacebookId(), request.getFacebookToken());
        authRequest.enqueue(new Callback<ResponseBody>() {
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
                        listener.onFinished(false, Error.AUTHORIZATION_FACEBOOK_FAILED);
                    }
                    return;
                }
                String[] authResponseArgs = parseAuthUserResponse(body);
                if (authResponseArgs != null) {
                    if (authResponseArgs.length == 2) {
                        String contactId = authResponseArgs[0];
                        String token = authResponseArgs[1];
                        if (listener != null) {
                            beanstalkUserSession.save(contactId, token);
                            listener.onFinished(true, null);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(false, Error.AUTHORIZATION_FACEBOOK_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(false, Error.AUTHORIZATION_FACEBOOK_FAILED);
                }
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
        final Map<String, String> params = request.asParams();
        if (params.size() <= 1) {
            listener.onFinished(null);
        } else {
            Call<String[]> contact = service.updateContact(beanstalkApiKey, params);

            contact.enqueue(new Callback<String[]>() {
                @Override
                public void onResponse(Call<String[]> call, Response<String[]> response) {
                    String[] body = response.body();
                    log("onResponse() - " + Arrays.toString(body));
                    if (body == null) {
                        if (listener != null) {
                            listener.onFinished(Error.CONTACT_UPDATE_FAILED);
                        }
                    } else {
                        String fkey = params.get(ContactRequest.Parameters.F_KEY);
                        if (fkey != null) {
                            beanstalkUserSession.setFKey(fkey);
                        }
                        if (listener != null) {
                            listener.onFinished(null);
                        }
                    }
                }

                @Override
                public void onFailure(Call<String[]> call, Throwable t) {
                    log("onFailure()" + t.toString());
                    if (listener != null) {
                        listener.onFinished(Error.CONTACT_UPDATE_FAILED);
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
    public void addContactWithEmail(final ContactRequest request, final OnReturnListener listener) {
        checkUserByEmail(request, new OnReturnListener() {

            @Override
            public void onFinished(String error) {
                if (error != null) {
                    if (listener != null) {
                        listener.onFinished(error);
                    }
                } else {
                    createContact(request, listener, true);
                }
            }

        });
    }

    /**
     * Add loyalty contact.
     *
     * @param request  Request information for adding contact.
     * @param listener Callback that will run after network request is completed.
     */
    public void addLoyaltyContact(final ContactRequest request, final OnReturnDataListener<LoyaltyUser> listener) {
        createLoyaltyAccount(request, listener);
    }

    /**
     * Create contact and persist Contact ID in Beanstalk User Session.
     *
     * @param request          Request information for creating contact.
     * @param listener         Callback that will run after network request is completed.
     * @param shouldCreateUser Should create user after creating contact.
     */
    public void createContact(final ContactRequest request, final OnReturnListener listener, final boolean shouldCreateUser) {
        createContact(request, new OnReturnDataListener<String>() {

            @Override
            public void onFinished(String contactId, String error) {
                listener.onFinished(error);
            }

        }, shouldCreateUser);
    }

    /**
     * Create contact, persist Contact ID in Beanstalk User Session and return it using callback.
     *
     * @param request          Request information for creating contact.
     * @param listener         Callback that will run after network request is completed.
     * @param shouldCreateUser Should create user after creating contact.
     */
    public void createContact(final ContactRequest request, final OnReturnDataListener<String> listener, final boolean shouldCreateUser) {
        createContact(request, new OnReturnDataListener<Contact>() {

            @Override
            public void onFinished(Contact contact, String error) {
                if (error == null) {
                    listener.onFinished(contact.getContactId(), null);
                } else {
                    listener.onFinished(null, error);
                }
            }

        }, shouldCreateUser, false);
    }

    /**
     * Create contact, persist Contact ID in Beanstalk User Session and return Contact using callback.
     * <p>
     * <b>Note:</b><br/>
     * Current server API returns only <b>contactId</b> on create request. So in order to return contact model (if requested) - <b>getContact()</b> request is performed. There might be situations (bad network conditions, etc.) when contact is created but <b>getContact()</b> request failed, so only <b>contactId</b> will be available.
     * </p>
     *
     * @param request            Request information for creating contact.
     * @param listener           Callback that will run after network request is completed.
     * @param shouldCreateUser   Should create user after creating contact or not.
     * @param shouldFetchContact Should fetch and return contact after creating contact.
     */
    public void createContact(final ContactRequest request, final OnReturnDataListener<Contact> listener, final boolean shouldCreateUser, final boolean shouldFetchContact) {
        Call<ResponseBody> contact = service.createContact(beanstalkApiKey, request.asParams());

        contact.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String body = "error";
                try {
                    if (response != null && response.body() != null) {
                        body = response.body().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log("createContact() " + body);
                if ("error".equalsIgnoreCase(body)) {
                    if (listener != null) {
                        listener.onFinished(null, Error.SIGN_IN_FAILED);
                    }
                    return;
                }

                String contactId = parseCreateContactResponse(body);
                if (contactId == null) {
                    if (listener != null) {
                        listener.onFinished(null, Error.SIGN_IN_FAILED);
                    }
                } else {
                    log("contact id : " + contactId);
                    beanstalkUserSession.save(contactId, null);
                    if (shouldCreateUser) {
                        Call<String> user = service.createUser(request.getEmail(), request.getPassword(), beanstalkApiKey, contactId);
                        user.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String body = response.body();
                                log("addContactWithEmail() - create user " + body);
                                if ("Success".equalsIgnoreCase(body)) {
                                    fetchContact(listener, shouldFetchContact);
                                } else {
                                    if (listener != null) {
                                        listener.onFinished(null, Error.SIGN_IN_FAILED);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                if (listener != null) {
                                    listener.onFinished(null, Error.SIGN_IN_FAILED);
                                }
                            }
                        });
                    } else {
                        fetchContact(listener, shouldFetchContact);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.SIGN_IN_FAILED);
                }
            }
        });
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
    @Deprecated
    public void checkStores(String zip, final OnReturnDataListener<LocationResponse> listener) {
        Call<LocationResponse> locationByZipCode = service.getLocationByZipCode(googleMapsApiKey, zip);
        locationByZipCode.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                LocationResponse body = response.body();
                if (body != null && !body.isFailed()) {
                    if (listener != null) {
                        listener.onFinished(body, null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null, Error.SIGN_UP_ZIP_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.SIGN_UP_ZIP_FAILED);
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
    public void checkLocation(LocationResponse.Location location, final OnReturnDataListener<StoresResponse> listener) {
        Call<StoresResponse> storesResponseCall = service.checkLocation(beanstalkApiKey, location.getLatitude(), location.getLongitude());
        storesResponseCall.enqueue(new Callback<StoresResponse>() {
            @Override
            public void onResponse(Call<StoresResponse> call, Response<StoresResponse> response) {
                StoresResponse body = response.body();
                if (body != null && !body.isFailed()) {
                    if (listener != null) {
                        listener.onFinished(body, null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null, Error.SIGN_UP_LOCATION_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<StoresResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.SIGN_UP_LOCATION_FAILED);
                }
            }
        });
    }

    /**
     * Get all available stores locations.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void getAllStoresLocations(final OnReturnDataListener<StoresResponse> listener) {
        Call<StoresResponse> storesResponseCall = service.getAllStoresLocations(beanstalkApiKey);
        storesResponseCall.enqueue(new Callback<StoresResponse>() {
            @Override
            public void onResponse(Call<StoresResponse> call, Response<StoresResponse> response) {
                StoresResponse body = response.body();
                if (body != null && !body.isFailed()) {
                    if (listener != null) {
                        listener.onFinished(body, null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null, Error.SIGN_UP_LOCATION_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<StoresResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.SIGN_UP_LOCATION_FAILED);
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
    public void getStoreInfo(String storeId, final OnReturnDataListener<StoreInfoResponse> listener) {
        Call<StoreInfoResponse> storeInfoResponseCall = service.getStoreInfo(beanstalkApiKey, storeId);
        storeInfoResponseCall.enqueue(new Callback<StoreInfoResponse>() {
            @Override
            public void onResponse(Call<StoreInfoResponse> call, Response<StoreInfoResponse> response) {
                StoreInfoResponse body = response.body();
                if (body != null && !body.isFailed()) {
                    if (listener != null) {
                        listener.onFinished(body, null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null, Error.STORE_INFO_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<StoreInfoResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.STORE_INFO_FAILED);
                }
            }
        });
    }

    /**
     * Get all stores locations.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void getAllStores(final OnReturnDataListener<LocationsResponse> listener) {
        Call<LocationsResponse> locationsResponseCall = service.getLocations(beanstalkApiKey);
        locationsResponseCall.enqueue(new Callback<LocationsResponse>() {
            @Override
            public void onResponse(Call<LocationsResponse> call, Response<LocationsResponse> response) {
                LocationsResponse body = response.body();
                if (body != null && !body.isFailed()) {
                    if (listener != null) {
                        listener.onFinished(body, null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null, Error.STORE_INFO_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationsResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.STORES_LOCATIONS_FAILED);
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
                .enqueue(new PushSuccessResponseCallback(Error.ENROLL_PUSH_NOTIFICATION_FAILED, listener, null));
    }

    /**
     * Update authenticated user device token for push notifications.
     *
     * @param deviceToken GCM device token.
     * @param listener    Callback that will run after network request is completed.
     */
    public void modifyPushNotification(String deviceToken, OnReturnDataListener<PushSuccessResponse> listener) {
        service.modifyPushNotification(beanstalkApiKey, beanstalkUserSession.getContactId(), deviceToken, PlatformType.ANDROID)
                .enqueue(new PushSuccessResponseCallback(Error.MODIFY_PUSH_NOTIFICATION_FAILED, listener, null));
    }

    /**
     * Delete push notifications for authenticated user.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void deletePushNotification(OnReturnDataListener<PushSuccessResponse> listener) {
        service.deletePushNotification(beanstalkApiKey, beanstalkUserSession.getContactId())
                .enqueue(new PushSuccessResponseCallback(Error.DELETE_PUSH_NOTIFICATION_FAILED, listener, null));
    }

    /**
     * Returns messages assigned to authenticated user.
     *
     * @param maxResults Maximum number of messages desired.
     * @param listener   Callback that will run after network request is completed.
     */
    public void getContactMessages(int maxResults, OnReturnDataListener<PushMessagesResponse> listener) {
        service.getContactMessages(beanstalkApiKey, beanstalkUserSession.getContactId(), maxResults)
                .enqueue(new SimpleCallback<>(Error.GET_MESSAGES_FAILED, listener, defaultMessages));
    }

    /**
     * Returns messages of specified message type for authenticated user.
     *
     * @param messageContentType Required message content type ({@link MessageContentType}).
     * @param listener           Callback that will run after network request is completed.
     */
    public void getMessagesByOsAndType(@MessageContentType String messageContentType, OnReturnDataListener<PushMessagesResponse> listener) {
        service.getMessagesByOsAndType(beanstalkApiKey, messageContentType, PlatformType.ANDROID)
                .enqueue(new SimpleCallback<>(Error.GET_MESSAGES_FAILED, listener, defaultMessages));
    }

    /**
     * Returns messages of specified message type for authenticated user.
     *
     * @param imageType Required message content type ({@link ImageType}).
     * @param listener  Callback that will run after network request is completed.
     */
    public void getMessagesByImageType(@ImageType String imageType, OnReturnDataListener<PushMessagesResponse> listener) {
        service.getMessagesByImageType(beanstalkApiKey, beanstalkUserSession.getContactId(), imageType)
                .enqueue(new SimpleCallback<>(Error.GET_MESSAGES_FAILED, listener, defaultMessages));
    }

    /**
     * Returns message for specified message id.
     *
     * @param messageId Message id.
     * @param listener  Callback that will run after network request is completed.
     */
    public void getMessageById(String messageId, OnReturnDataListener<PushMessageByIdResponse> listener) {
        service.getMessageById(beanstalkApiKey, messageId)
                .enqueue(new SimpleCallback<>(Error.GET_MESSAGES_FAILED, listener, null));
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
                .enqueue(new SimpleCallback<>(Error.GET_MESSAGES_FAILED, listener, null));
    }

    /**
     * Retrieve all transaction events for authenticated user.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void getTransactionEvents(OnReturnDataListener<TransactionEvent[]> listener) {
        getTransactionEvents(0, 0, listener);
    }

    /**
     * Retrieve transaction events from start date to end date for authenticated user.
     * <p>
     * <b>Notes:</b><br/>
     * <ul>
     * <li>If no dates are passed, all events for authenticated user will be returned.</li>
     * <li>To retrieve events for a specific date, set startDate and endDate to the same date.</li>
     * <li>If startDate and endDate are both set, all events in that range will be returned.</li>
     * <li>If only startDate is set, all events from that day until now will be returned.</li>
     * <li>If only endDate is set, all events until that day will be returned.</li>
     * </ul>
     * </p>
     *
     * @param startDate Start date for transaction events.
     * @param endDate   End date for transaction events.
     */
    public void getTransactionEvents(long startDate, long endDate, OnReturnDataListener<TransactionEvent[]> listener) {
        service.getTransactionEvents(beanstalkApiKey, beanstalkUserSession.getContactId(), getTransactionEventDate(startDate), getTransactionEventDate(endDate))
                .enqueue(new SimpleCallback<>(Error.GET_TRANSACTIONS_FAILED, listener, null));
    }

    /**
     * Add transaction event to authenticated user.
     * <p>
     * <b>Note:</b><br/>
     * This method can only be performed if <b>username</b> is provided.
     * </p>
     *
     * @param username Beanstalk username.
     * @param details  Any string. Can interpret JSON as parsed array and create searchable transaction object which can be used for campaigns and other generic transactional workflow.
     * @param listener Callback that will run after network request is completed.
     */
    public void trackTransaction(String username, String details, final OnReturnDataListener<TrackTransactionResponse> listener) {
        service.trackTransaction(beanstalkApiKey, beanstalkUserSession.getContactId(), username, details)
                .enqueue(new SimpleCallback<>(Error.TRACK_TRANSACTION_FAILED, listener, null));
    }

    /**
     * Maintain default loyalty cards for logged in user.
     * <p>
     * <b>Note:</b>
     * By default loyalty card number is equal to contact ID.
     * </p>
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void maintainLoyaltyCards(OnReturnDataListener<String> listener) {
        maintainLoyaltyCards(beanstalkUserSession.getContactId(), listener);
    }

    /**
     * Maintain loyalty cards for logged in user.
     *
     * @param cardNumber Loyalty card number.
     * @param listener   Callback that will run after network request is completed.
     */
    public void maintainLoyaltyCards(String cardNumber, final OnReturnDataListener<String> listener) {
        Call<String> request = service.maintainLoyaltyCards(beanstalkApiKey, beanstalkUserSession.getContactId(), cardNumber);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = response.body();
                if ("card added".equals(body)) {
                    if (listener != null) {
                        listener.onFinished(body, null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null, Error.LOYALTY_PROGRAM_ERROR);
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.LOYALTY_PROGRAM_ERROR);
                }
            }
        });
    }

    /**
     * Send comments to Contact Us responder.
     *
     * @param firstName   First name of the user.
     * @param lastName    Last name of the user.
     * @param fromEmail   Email  of the user.
     * @param toEmail     Email of the responder.
     * @param phoneNumber Phone number  of the use.
     * @param comments    Comments.
     * @param listener    Callback that will run after network request is completed.
     */
    public void contactUs(String firstName, String lastName, String fromEmail, String toEmail, String phoneNumber, String comments, final OnReturnListener listener) {
        Call<ContactUsResponse> request = service.contactUs(beanstalkApiKey, firstName, lastName, fromEmail, toEmail, phoneNumber, comments);
        request.enqueue(new Callback<ContactUsResponse>() {

            @Override
            public void onResponse(Call<ContactUsResponse> call, Response<ContactUsResponse> response) {
                ContactUsResponse body = response.body();
                if (body != null) {
                    if (listener != null) {
                        String error = body.getError();
                        listener.onFinished((error != null) ? error : null);
                    }
                } else {
                    onFailure(call, null);
                }
            }

            @Override
            public void onFailure(Call<ContactUsResponse> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(Error.CONTACT_US_ERROR);
                }
            }

        });

    }

    /**
     * Check stores availability at specified location.
     *
     * @param latitude  Location latitude.
     * @param longitude Location longitude.
     * @param listener  Callback that will run after network request is completed.
     */
    public void checkLocationV2(double latitude, double longitude, final OnReturnDataListener<StoresResponseV2> listener) {
        Call<StoresResponseV2> storesResponseCall = service.checkLocationV2(beanstalkApiKey, latitude, longitude);
        storesResponseCall.enqueue(new Callback<StoresResponseV2>() {
            @Override
            public void onResponse(Call<StoresResponseV2> call, Response<StoresResponseV2> response) {
                StoresResponseV2 body = response.body();
                if (body != null && !body.isFailed()) {
                    if (listener != null) {
                        listener.onFinished(body, null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null, Error.STORES_LOCATIONS_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<StoresResponseV2> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.STORES_LOCATIONS_FAILED);
                }
            }
        });
    }

    /**
     * Get all available stores locations.
     *
     * @param listener Callback that will run after network request is completed.
     */
    public void getAllStoresLocationsV2(final OnReturnDataListener<StoresResponseV2> listener) {
        Call<StoresResponseV2> storesResponseCall = service.getAllStoresLocationsV2(beanstalkApiKey);
        storesResponseCall.enqueue(new Callback<StoresResponseV2>() {
            @Override
            public void onResponse(Call<StoresResponseV2> call, Response<StoresResponseV2> response) {
                StoresResponseV2 body = response.body();
                if (body != null && !body.isFailed()) {
                    if (listener != null) {
                        listener.onFinished(body, null);
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(null, Error.STORES_LOCATIONS_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<StoresResponseV2> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.STORES_LOCATIONS_FAILED);
                }
            }
        });
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

    private Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(Contact.class, new ContactDeserializer())
                .registerTypeAdapter(StoresResponseV2.Store.class, new StoreV2Deserializer())
                .registerTypeAdapter(StoresResponseV2.IdHolder.class, new IdHolderV2Deserializer())
                .registerTypeAdapter(StoresResponseV2.Location.class, new LocationV2Deserializer())
                .registerTypeAdapter(PushMessagesResponse.class, new PushMessagesDeserializer())
                .registerTypeAdapter(Date.class, new MultiDateFormatDeserializer())
                .create();
    }

    @Deprecated
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

    private void authUser(AuthenticateUserRequest request, final OnReturnDataListener<Boolean> listener) {
        Call<ResponseBody> authenticateUser = service.authenticateUser(request.getEmail(), request.getPassword(), beanstalkApiKey, request.getTime());
        authenticateUser.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String body = "error";
                try {
                    if (response != null) {
                        if (response.body() != null) {
                            body = response.body().string();
                        }
                    }
                } catch (Exception e) {
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
                            if (listener != null) {
                                listener.onFinished(null);
                            }
                        } else {
                            if (listener != null) {
                                listener.onFinished(Error.CONTACT_EXISTED_PHONE);
                            }
                        }
                    } else {
                        if (listener != null) {
                            listener.onFinished(null);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onFinished(Error.SIGN_IN_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<Contact[]> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null);
                }
            }
        });
    }

    private void createLoyaltyAccount(final ContactRequest request, final OnReturnDataListener<LoyaltyUser> listener) {
        request.setParam(ContactRequest.Parameters.LOYALTY_PASSWORD, request.getPassword());
        request.setParam(ContactRequest.Parameters.LOYALTY_PHONE, request.clearParam(ContactRequest.Parameters.PHONE));

        service.createLoyaltyAccount(beanstalkApiKey, request.asParams()).enqueue(new Callback<LoyaltyUser>() {

            @Override
            public void onResponse(Call<LoyaltyUser> call, Response<LoyaltyUser> response) {
                if (listener != null) {
                    if (response.isSuccessful()) {
                        listener.onFinished(response.body(), null);
                    } else {
                        listener.onFinished(null, Error.SIGN_IN_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoyaltyUser> call, Throwable t) {
                if (listener != null) {
                    listener.onFinished(null, Error.SIGN_IN_FAILED);
                }
            }

        });
    }

    private void fetchContact(final OnReturnDataListener<Contact> listener, final boolean shouldFetchContact) {
        if (shouldFetchContact) {
            getContact(new OnReturnDataListener<Contact>() {
                @Override
                public void onFinished(Contact contact, String error) {
                    if (listener != null) {
                        listener.onFinished((error == null) ? contact : emptyContact(), null);
                    }
                }
            });
        } else {
            if (listener != null) {
                listener.onFinished(emptyContact(), null);
            }
        }
    }

    private Contact emptyContact() {
        Contact contact = new Contact();
        contact.setContactId(beanstalkUserSession.getContactId());
        return contact;
    }

    private String getTransactionEventDate(long date) {
        return (date > 0) ? transactionDateFormat.format(new Date(date)) : null;
    }

    private void log(String msg) {
        if (isLoggingEnabled()) {
            Log.d(TAG, msg);
        }
    }

    private static class PushSuccessResponseCallback extends SimpleCallback<PushSuccessResponse> {

        private PushSuccessResponseCallback(String error, OnReturnDataListener<PushSuccessResponse> listener, PushSuccessResponse defaultBody) {
            super(error, listener, defaultBody);
        }

        @Override
        protected PushSuccessResponse getResponseBody(Response<PushSuccessResponse> response) {
            PushSuccessResponse pushSuccessResponse = response.body();
            if (response.isSuccessful() && pushSuccessResponse == null) {
                pushSuccessResponse = new PushSuccessResponse();
                pushSuccessResponse.setSuccess(true);
            }
            return pushSuccessResponse;
        }

    }

    private static class SimpleCallback<T> implements Callback<T> {

        private final String error;
        private final OnReturnDataListener<T> listener;
        private final T defaultBody;

        private SimpleCallback(String error, OnReturnDataListener<T> listener, T defaultBody) {
            this.error = error;
            this.listener = listener;
            this.defaultBody = defaultBody;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response != null) {
                T responseBody = getResponseBody(response);
                if (responseBody == null) {
                    responseBody = defaultBody;
                }
                if (responseBody != null) {
                    if (listener != null) {
                        listener.onFinished(responseBody, null);
                    }
                } else {
                    onFailure(call, null);
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

        protected T getResponseBody(Response<T> response) {
            T responseBody = response.body();
            return response.isSuccessful() && (responseBody != null) ? responseBody : null;
        }

    }

}
