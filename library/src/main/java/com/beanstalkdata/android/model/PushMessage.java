/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model;

import com.beanstalkdata.android.model.type.MessageType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Data model for Push Message.
 */
public class PushMessage {

    @Expose
    @SerializedName("_id")
    private PushMessageId pushMessageId;

    @Expose
    @SerializedName("CustomerId")
    private String customerId;

    @Expose
    @SerializedName("CampaignId")
    private String campaignId;

    @Expose
    @SerializedName("StepId")
    private String stepId;

    @Expose
    @SerializedName("ContactId")
    private String contactId;

    @Expose
    @SerializedName("DateSent")
    private Date sentDate;

    @Expose
    @MessageType
    @SerializedName("Status")
    private String status;

    @Expose
    @SerializedName("StatusLastUpdated")
    private Date statusLastUpdated;

    @Expose
    @SerializedName("Category")
    private String category;

    @Expose
    @SerializedName("notification")
    private List<PushNotification> pushNotifications;

    public PushMessageId getPushMessageId() {
        return pushMessageId;
    }

    public void setPushMessageId(PushMessageId pushMessageId) {
        this.pushMessageId = pushMessageId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStatusLastUpdated() {
        return statusLastUpdated;
    }

    public void setStatusLastUpdated(Date statusLastUpdated) {
        this.statusLastUpdated = statusLastUpdated;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<PushNotification> getPushNotifications() {
        return pushNotifications;
    }

    public void setPushNotifications(List<PushNotification> pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

}
