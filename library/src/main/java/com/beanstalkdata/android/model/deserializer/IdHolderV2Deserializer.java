/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model.deserializer;

import com.beanstalkdata.android.response.StoresResponseV2;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

public class IdHolderV2Deserializer implements JsonDeserializer<StoresResponseV2.IdHolder> {

    @Override
    public StoresResponseV2.IdHolder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        StoresResponseV2.IdHolder idHolder = new StoresResponseV2.IdHolder();
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value != null) {
                switch (key) {
                    case StoresResponseV2.IdHolder.Parameters.ID:
                        if (value.isJsonPrimitive()) {
                            idHolder.setId(value.getAsString());
                        }
                        break;
                    default:
                        if (value.isJsonPrimitive()) {
                            idHolder.setParam(key, value.getAsString());
                        }
                        break;
                }
            }
        }
        return idHolder;
    }

}
