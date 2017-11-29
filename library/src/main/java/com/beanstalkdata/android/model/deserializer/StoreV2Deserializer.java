/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model.deserializer;

import com.beanstalkdata.android.response.StoresResponseV2;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

public class StoreV2Deserializer implements JsonDeserializer<StoresResponseV2.Store> {

    @Override
    public StoresResponseV2.Store deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        StoresResponseV2.Store store = new StoresResponseV2.Store();
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value != null) {
                switch (key) {
                    case StoresResponseV2.Store.Parameters.ID_HOLDER:
                        if (value.isJsonObject()) {
                            store.setIdHolder(deserializeObject(context, value.getAsJsonObject(), StoresResponseV2.IdHolder.class));
                        } else if (value.isJsonPrimitive()) {
                            store.setParam(key, value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.CUSTOMER_ID:
                        if (value.isJsonPrimitive()) {
                            store.setCustomerId(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.GEO_ENABLED:
                        if (value.isJsonPrimitive()) {
                            store.setGeoEnabled(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.NAME:
                        if (value.isJsonPrimitive()) {
                            store.setName(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.PHONE_NUMBER:
                        if (value.isJsonPrimitive()) {
                            store.setPhoneNumber(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.WEBSITE:
                        if (value.isJsonPrimitive()) {
                            store.setWebsite(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.EMAIL:
                        if (value.isJsonPrimitive()) {
                            store.setEmail(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.STORE_NUMBER:
                        if (value.isJsonPrimitive()) {
                            store.setStoreNumber(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.LOCATION:
                        if (value.isJsonObject()) {
                            store.setLocation(deserializeObject(context, value.getAsJsonObject(), StoresResponseV2.Location.class));
                        } else if (value.isJsonPrimitive()) {
                            store.setParam(key, value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.TIME_ZONE:
                        if (value.isJsonPrimitive()) {
                            store.setTimeZone(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.PAYMENT_LOYALTY_PARTICIPATION:
                        if (value.isJsonPrimitive()) {
                            store.setPaymentLoyaltyParticipation(value.getAsBoolean());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.OPEN_HOURS:
                        if (value.isJsonPrimitive()) {
                            store.setOpenHours(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.DRIVE_THROUGH:
                        if (value.isJsonPrimitive()) {
                            store.setDriveThrough(value.getAsBoolean());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.STORE_TYPE:
                        if (value.isJsonPrimitive()) {
                            store.setStoreType(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Store.Parameters.SCHEMA_VERSION:
                        if (value.isJsonPrimitive()) {
                            store.setSchemaVersion(value.getAsString());
                        }
                        break;
                    default:
                        if (value.isJsonPrimitive()) {
                            store.setParam(key, value.getAsString());
                        }
                        break;
                }
            }
        }
        return store;
    }

    private <T> T deserializeObject(JsonDeserializationContext context, JsonObject jsonObject, Class<T> cls) {
        return context.deserialize(jsonObject, cls);
    }

}
