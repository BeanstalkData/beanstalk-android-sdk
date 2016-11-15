package com.beanstalkdata.android.model;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Gift Card.
 */
public class GiftCard {

    @SerializedName("Id")
    private String id;

    @SerializedName("cardNumber")
    private String number;

    @SerializedName("balance")
    private String balance;

    public void setId(String id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDisplayNumber() {
        if (number.length() > 4) {
            return String.format("XXXXXXXXXXXX%s", number.substring(number.length() - 4));
        } else {
            return "XXXXXXXXXXXXXXXX";
        }
    }

    public String getDisplayBalance(){
        if(TextUtils.isEmpty(balance)){
            return CardBalance.DEFAULT;
        }
        return balance;
    }

    public String getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GiftCard giftCard = (GiftCard) o;

        return number.equals(giftCard.number);

    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    public static GiftCard fromPrefs(SharedPreferences prefs) {
        GiftCard giftCard = new GiftCard();
        String number = prefs.getString("_gc_number", null);
        if(number == null){
            return null;
        }
        String id = prefs.getString("_gc_id", null);
        if(id == null){
            return null;
        }
        giftCard.id = id;
        giftCard.number = number;
        giftCard.balance = prefs.getString("_gc_balance", null);
        return giftCard;
    }

    public void toPrefs(SharedPreferences.Editor edit) {
        edit.putString("_gc_id", id);
        edit.putString("_gc_number", number);
        edit.putString("_gc_balance", balance);
    }
}
