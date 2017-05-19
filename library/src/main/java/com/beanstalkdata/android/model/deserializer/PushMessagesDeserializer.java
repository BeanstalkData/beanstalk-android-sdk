/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model.deserializer;


import com.beanstalkdata.android.model.PushMessage;
import com.beanstalkdata.android.response.PushMessagesResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PushMessagesDeserializer implements JsonDeserializer<PushMessagesResponse> {

    @Override
    public PushMessagesResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PushMessagesResponse messages = new PushMessagesResponse();
        if (json.isJsonArray()) {
            List<PushMessage> messageList = context.deserialize(json.getAsJsonArray(), new TypeToken<List<PushMessage>>(){}.getType());
            messages.setPushMessages(messageList);
        }
        return messages;
    }

}
