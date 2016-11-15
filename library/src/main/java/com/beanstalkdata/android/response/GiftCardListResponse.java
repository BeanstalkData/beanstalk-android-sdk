package com.beanstalkdata.android.response;

import com.beanstalkdata.android.model.GiftCard;
import com.google.gson.annotations.SerializedName;

/**
 * Data model for Gift Cards List response.
 */
public class GiftCardListResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("success")
    private SuccessResponse success;

    public boolean isFailed() {
        return !status
                || (success == null)
                || (success.message == null)
                || (success.message.response == null)
                || !success.message.response.success
                || (success.message.response.cards == null);
    }

    public GiftCard[] getGiftCards() {
        return success.message.response.cards;
    }

    public static class SuccessResponse {

        @SerializedName("code")
        private int code;

        @SerializedName("message")
        private Message message;
    }

    public static class Message {
        @SerializedName("response")
        private CardListResponse response;
    }

    public static class CardListResponse {
        @SerializedName("success")
        private boolean success;

        @SerializedName("cards")
        private GiftCard[] cards;
    }
}
