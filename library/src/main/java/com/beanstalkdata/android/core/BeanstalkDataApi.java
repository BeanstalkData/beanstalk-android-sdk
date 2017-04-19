/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.core;

import com.beanstalkdata.android.model.Contact;
import com.beanstalkdata.android.model.LoyaltyUser;
import com.beanstalkdata.android.model.type.ImageType;
import com.beanstalkdata.android.model.type.MessageContentType;
import com.beanstalkdata.android.model.type.MessageType;
import com.beanstalkdata.android.model.type.PlatformType;
import com.beanstalkdata.android.response.CardBalanceResponse;
import com.beanstalkdata.android.response.ContactDeletedResponse;
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
import com.beanstalkdata.android.response.SuccessResponse;
import com.beanstalkdata.android.response.TrackTransactionResponse;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BeanstalkDataApi {

    @GET("/contacts?type=email")
    Call<Contact[]> getContactByEmail(@Query("key") String apiKey, @Query("q") String email);

    @GET("/contacts?type=cell_number")
    Call<Contact[]> getContactByPhone(@Query("key") String apiKey, @Query("q") String phone);

    @GET("/contacts?type=fkey")
    Call<Contact[]> getContactByFkey(@Query("key") String apiKey, @Query("q") String fkey);

    @POST("/addContact/")
    @FormUrlEncoded
    Call<ResponseBody> createContact(@Query("key") String apiKey, @FieldMap Map<String, String> request);

    @POST("/addContact/")
    @FormUrlEncoded
    Call<String[]> updateContact(@Query("key") String apiKey, @FieldMap Map<String, String> request);

    @POST("/addUser/")
    @FormUrlEncoded
    Call<String> createUser(@Field("email") String email, @Field("password") String password, @Field("key") String key, @Field("contact") String contact);

    @POST("/addPaymentLoyaltyAccount/")
    @FormUrlEncoded
    Call<LoyaltyUser> createLoyaltyAccount(@Query("key") String apiKey, @FieldMap Map<String, String> request);

    @POST("/authenticateUser/")
    @FormUrlEncoded
    Call<ResponseBody> authenticateUser(@Field("email") String email, @Field("password") String password, @Field("key") String key, @Field("time") String time);

    @POST("/checkSession/")
    @FormUrlEncoded
    Call<String> checkSession(@Field("contact") String contact, @Field("token") String token);

    @POST("/bsdLoyalty/ResetPassword.php")
    @FormUrlEncoded
    Call<String> resetPassword(@Query("key") String apiKey, @Field("user") String email);

    @POST("/bsdLoyalty/?function=updatePassword")
    @FormUrlEncoded
    Call<String> updatePassword(@FieldMap Map<String, String> request);

    @GET("/bsdPayment/list")
    Call<GiftCardListResponse> getGiftCardList(@Query("key") String apiKey, @Query("contactId") String contactId, @Query("token") String token);

    @GET("/bsdPayment/list")
    Call<ResponseBody> getGiftCardListRaw(@Query("key") String apiKey, @Query("contactId") String contactId, @Query("token") String token);

    @POST("/bsdPayment/register/")
    @FormUrlEncoded
    Call<RegisterGiftCardResponse> registerGiftCard(
            @Field("key") String apiKey, @Field("contactId") String contactId, @Field("token") String token,
            @Field("cardNumber") String giftCardNumber, @Field("pinNumber") String giftPinNumber);

    @POST("/bsdPayment/startPayment/")
    @FormUrlEncoded
    Call<PaymentTokenResponse> startPayment(@Field("key") String apiKey, @Field("contactId") String contactId, @Field("loyaltyId") String loyaltyId,
                                            @Field("token") String token, @Field("paymentId") String paymentId, @Field("coupons") String coupons);

    @POST("/bsdPayment/preferred/")
    @FormUrlEncoded
    Call<String> setPreferredCard(@Field("key") String apiKey, @Field("contactId") String contactId,
                                  @Field("token") String token, @Field("cardNumber") String cardNumber);

    @POST("/bsdPayment/inquiry/")
    @FormUrlEncoded
    Call<CardBalanceResponse> inquireAboutCard(@Field("key") String apiKey, @Field("contactId") String contactId,
                                               @Field("token") String token, @Field("cardNumber") String cardNumber);

    @POST("/bsdPayment/inquiry/")
    @FormUrlEncoded
    Call<ResponseBody> inquireAboutCardRaw(@Field("key") String apiKey, @Field("contactId") String contactId,
                                           @Field("token") String token, @Field("cardNumber") String cardNumber);

    @GET("/bsdLoyalty/indentifyCustomer.php?field=CustomerID")
    Call<String> getLoyaltyInformation(@Query("key") String apiKey, @Query("value") String fKey);

    @GET("/contacts")
    Call<Contact[]> getContact(@Query("key") String apiKey, @Query("q") String contactId);

    @GET("/contacts")
    Call<ResponseBody> getContactRaw(@Query("key") String apiKey, @Query("q") String contactId);

    @GET("/deleteContact/")
    Call<ContactDeletedResponse> deleteContact(@Query("key") String apiKey, @Query("ContactID") String contactId);

    @POST("bsdLoyalty/getProgress.php")
    @FormUrlEncoded
    Call<RewardsCountResponse> getProgress(@Query("key") String apiKey, @Field("contact") String contactId);

    @GET("bsdLoyalty/getOffersM.php")
    Call<CouponResponse> getUserOffers(@Query("key") String apiKey, @Query("Card") String contactId);

    @POST("logoutUser/")
    @FormUrlEncoded
    Call<String> logoutUser(@Field("contact") String contactId, @Field("token") String token);

    @POST("bsdLoyalty/maintainLoyaltyCards.php")
    @FormUrlEncoded
    Call<String> paperCardRegistration(@Query("key") String apiKey, @Field("function") String function, @Field("cardNumber") String cardNumber,
                                       @Field("security") String security, @Field("contact") String contact);

    @GET("bsdStores/locate/")
    Call<StoresResponse> checkLocation(@Query("key") String apiKey, @Query("lat") double lat, @Query("long") double lng);

    @GET("bsdStores/locate/")
    Call<StoresResponse> getAllStoresLocations(@Query("key") String apiKey);

    @GET("pushNotificationEnroll")
    Call<PushSuccessResponse> enrollPushNotification(@Query("key") String apiKey, @Query("contact_id") String contactId,
                                                     @Query("deviceToken") String deviceToken, @PlatformType @Query("platform") String platform);

    @GET("pushNotificationModify")
    Call<PushSuccessResponse> modifyPushNotification(@Query("key") String apiKey, @Query("contact_id") String contactId,
                                                     @Query("deviceToken") String deviceToken, @PlatformType @Query("platform") String platform);

    @GET("pushNotificationDelete")
    Call<PushSuccessResponse> deletePushNotification(@Query("key") String apiKey, @Query("contact_id") String contactId);

    @POST("pushNotification/getMessages")
    @FormUrlEncoded
    Call<PushMessagesResponse> getContactMessages(@Field("key") String apiKey, @Field("contactId") String contactId, @Field("maxResults") int maxResults);

    @POST("pushNotification/getMessagesByOsAndType")
    @FormUrlEncoded
    Call<PushMessagesResponse> getMessagesByOsAndType(@Field("key") String apiKey, @MessageContentType @Field("message_type") String messageType, @PlatformType @Field("platform") String platform);

    @POST("pushNotification/getMessagesByImageType")
    @FormUrlEncoded
    Call<PushMessagesResponse> getMessagesByImageType(@Field("key") String apiKey, @Field("contactId") String contactId, @ImageType @Field("image_type") String imageType);

    @POST("pushNotification/updateStatus")
    @FormUrlEncoded
    Call<PushSuccessResponse> updateMessageStatus(@Field("key") String apiKey, @Field("message_id") String messageId, @MessageType @Field("action") String messageStatus);

    @POST("pushNotification/getMessageById")
    @FormUrlEncoded
    Call<PushMessageByIdResponse> getMessageById(@Field("key") String apiKey, @Field("msg_id") String messageId);

    @POST("bsdTransactions/add/")
    @FormUrlEncoded
    Call<TrackTransactionResponse> trackTransaction(@Query("key") String apiKey, @Query("contact") String contactId, @Query("username") String beanstalkUsername, @Field("details") String details);

    @GET("bsdLoyalty/getLocations.php")
    Call<LocationsResponse> getLocations(@Query("key") String apiKey);

    @GET("bsdLoyalty/GetLocation.php")
    Call<StoreInfoResponse> getStoreInfo(@Query("key") String apiKey, @Query("locationId") String locationId);

    @Deprecated
    @GET("https://maps.googleapis.com/maps/api/geocode/json")
    Call<LocationResponse> getLocationByZipCode(@Query("key") String googleMapsApiKey, @Query("address") String zip);

}
