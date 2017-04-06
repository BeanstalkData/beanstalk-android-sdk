/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model;

import com.beanstalkdata.android.model.type.MessageType;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Data model for Push Message.
 */
public class PushMessage {

    @SerializedName("_id")
    private Id id;

    @SerializedName("AppHook")
    private String appHook;

    @SerializedName("CampaignId")
    private String campaignId;

    @SerializedName("Category")
    private String category;

    @SerializedName("ContactId")
    private String contactId;

    @SerializedName("CustomerId")
    private String customerId;

    @SerializedName("MessageBody")
    private String messageBody;

    @SerializedName("MsgImage")
    private String messageImage;

    @SerializedName("MsgType")
    private String messageType;

    @SerializedName("OS")
    private String os;

    @SerializedName("StepId")
    private String stepId;

    @SerializedName("TitleImage")
    private String titleImage;

    @SerializedName("inboxTitle")
    private String inboxTitle;

    @SerializedName("messageUrl")
    private String messageUrl;

    @MessageType
    @SerializedName("status")
    private String status;

    @SerializedName("subtitle")
    private String subTitle;

    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;

    @SerializedName("title")
    private String title;

    @SerializedName("updated_at")
    private UpdatedAt updatedAt;

    private static class Id {

        @SerializedName("$id")
        private String id;

        public String getId() {
            return id;
        }

    }

    private static class UpdatedAt {

        @SerializedName("sec")
        private int timestamp;

        public int getTimestamp() {
            return timestamp;
        }
    }

    public String getId() {
        return id.getId();
    }

    public String getAppHook() {
        return appHook;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getCategory() {
        return category;
    }

    public String getContactId() {
        return contactId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getMessageImage() {
        return messageImage;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getOs() {
        return os;
    }

    public String getStepId() {
        return stepId;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public String getInboxTitle() {
        return inboxTitle;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public Date getUpdatedAt() {
        return new Date((long)updatedAt.getTimestamp() * 1000);
    }

}
