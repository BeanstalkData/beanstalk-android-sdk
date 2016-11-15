package com.beanstalkdata.android.model;

import com.beanstalkdata.android.model.type.ImageType;
import com.beanstalkdata.android.model.type.MessageContentType;
import com.beanstalkdata.android.model.type.PlatformType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data model for Push Notification.
 */
public class PushNotification {

    @Expose
    @PlatformType
    @SerializedName("OS")
    private String operationSystem;

    @Expose
    @MessageContentType
    @SerializedName("Type")
    private String type;

    @Expose
    @SerializedName("Title")
    private String title;

    @Expose
    @SerializedName("TitleImage")
    private String titleImage;

    @Expose
    @SerializedName("Image")
    private String image;

    @Expose
    @ImageType
    @SerializedName("Image_Type")
    private String imageType;

    @Expose
    @SerializedName("Content")
    private String content;

    @Expose
    @SerializedName("body")
    private String body;

    @Expose
    @SerializedName("AppHook")
    private String appHook;

    @PlatformType
    public String getOperationSystem() {
        return operationSystem;
    }

    public void setOperationSystem(@PlatformType String operationSystem) {
        this.operationSystem = operationSystem;
    }

    @MessageContentType
    public String getType() {
        return type;
    }

    public void setType(@MessageContentType String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @ImageType
    public String getImageType() {
        return imageType;
    }

    public void setImageType(@ImageType String imageType) {
        this.imageType = imageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAppHook() {
        return appHook;
    }

    public void setAppHook(String appHook) {
        this.appHook = appHook;
    }

}
