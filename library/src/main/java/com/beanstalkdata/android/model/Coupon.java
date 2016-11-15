package com.beanstalkdata.android.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Data model for Coupon.
 */
public class Coupon implements Parcelable {

    @SerializedName("CouponNo")
    private String number;

    @SerializedName("CouponText")
    private String text;

    @SerializedName("CouponReceiptText")
    private String receiptText;

    //@SerializedName("CreationDate")
    //private String creationDate;

    @SerializedName("ExpirationDate")
    private String expirationDate;

    @SerializedName("DiscountCode")
    private String discountCode;

    @SerializedName("EffectiveDate")
    private String effectiveDate;

    //@SerializedName("CouponHandlingAttributes")
    //private String handlingAttributes;

    @SerializedName("Image")
    private String image;

    //@SerializedName("AImage")
    //private String aImage;

    public Coupon() {
    }

    protected Coupon(Parcel in) {
        number = in.readString();
        text = in.readString();
        receiptText = in.readString();
        expirationDate = in.readString();
        image = in.readString();
        discountCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(text);
        dest.writeString(receiptText);
        dest.writeString(expirationDate);
        dest.writeString(image);
        dest.writeString(discountCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Coupon> CREATOR = new Creator<Coupon>() {
        @Override
        public Coupon createFromParcel(Parcel in) {
            return new Coupon(in);
        }

        @Override
        public Coupon[] newArray(int size) {
            return new Coupon[size];
        }
    };

    @Override
    public String toString() {
        return getText();
    }

    public String getText() {
        return text;
    }

    @Deprecated
    public void setText(String text) {
        this.text = text;
    }

    public Date getExpired() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .parse(expirationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNumber() {
        return number;
    }

    public String getImgUrl() {
        return "http:" + image;
    }

    public String getReceiptText() {
        return receiptText;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
