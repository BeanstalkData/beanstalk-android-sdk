/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Coupon.
 */
public class Coupon {

    @SerializedName("AImage")
    private String aImage;

    @SerializedName("CouponHandlingAttributes")
    private String couponHandlingAttributes;

    @SerializedName("CouponNo")
    private String couponNo;

    @SerializedName("CouponReceiptText")
    private String couponReceiptText;

    @SerializedName("CouponText")
    private String couponText;

    @SerializedName("CreationDate")
    private String creationDate;

    @SerializedName("DiscountAmt")
    private String discountAmt;

    @SerializedName("DiscountCode")
    private String discountCode;

    @SerializedName("DiscountPct")
    private String discountPct;

    @SerializedName("ExpirationDate")
    private String expirationDate;

    @SerializedName("Image")
    private String image;

    @SerializedName("MaxDiscountAmt")
    private String maxDiscountAmt;

    @SerializedName("OfferType")
    private String offerType;

    public String getaImage() {
        return aImage;
    }

    public void setaImage(String aImage) {
        this.aImage = aImage;
    }

    public String getCouponHandlingAttributes() {
        return couponHandlingAttributes;
    }

    public void setCouponHandlingAttributes(String couponHandlingAttributes) {
        this.couponHandlingAttributes = couponHandlingAttributes;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getCouponReceiptText() {
        return couponReceiptText;
    }

    public void setCouponReceiptText(String couponReceiptText) {
        this.couponReceiptText = couponReceiptText;
    }

    public String getCouponText() {
        return couponText;
    }

    public void setCouponText(String couponText) {
        this.couponText = couponText;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        this.discountAmt = discountAmt;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getDiscountPct() {
        return discountPct;
    }

    public void setDiscountPct(String discountPct) {
        this.discountPct = discountPct;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMaxDiscountAmt() {
        return maxDiscountAmt;
    }

    public void setMaxDiscountAmt(String maxDiscountAmt) {
        this.maxDiscountAmt = maxDiscountAmt;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }
}
