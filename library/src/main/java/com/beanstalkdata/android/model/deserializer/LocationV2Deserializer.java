/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model.deserializer;

import com.beanstalkdata.android.response.StoresResponseV2;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

public class LocationV2Deserializer implements JsonDeserializer<StoresResponseV2.Location> {

    @Override
    public StoresResponseV2.Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        StoresResponseV2.Location location = new StoresResponseV2.Location();
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value != null) {
                switch (key) {
                    case StoresResponseV2.Location.Parameters.FIRST_ADDRESS_LINE:
                        if (value.isJsonPrimitive()) {
                            location.setFirstAddressLine(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Location.Parameters.SECOND_ADDRESS_LINE:
                        if (value.isJsonPrimitive()) {
                            location.setSecondAddressLine(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Location.Parameters.CITY:
                        if (value.isJsonPrimitive()) {
                            location.setCity(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Location.Parameters.STATE:
                        if (value.isJsonPrimitive()) {
                            location.setState(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Location.Parameters.ZIP:
                        if (value.isJsonPrimitive()) {
                            location.setZip(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Location.Parameters.COUNTRY:
                        if (value.isJsonPrimitive()) {
                            location.setCountry(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Location.Parameters.CATEGORY:
                        if (value.isJsonPrimitive()) {
                            location.setCategory(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Location.Parameters.TYPE:
                        if (value.isJsonPrimitive()) {
                            location.setType(value.getAsString());
                        }
                        break;
                    case StoresResponseV2.Location.Parameters.COORDINATES:
                        if (value.isJsonArray()) {
                            location.setCoordinates(buildCoordinates(value.getAsJsonArray()));
                        } else if (value.isJsonPrimitive()) {
                            location.setParam(key, value.getAsString());
                        }
                        break;
                    default:
                        if (value.isJsonPrimitive()) {
                            location.setParam(key, value.getAsString());
                        }
                        break;
                }
            }
        }
        return location;
    }

    private double[] buildCoordinates(JsonArray array) {
        int size = array.size();
        double[] coordinates = new double[size];
        for (int index = 0; index < size; index++) {
            JsonElement element = array.get(index);
            if (element.isJsonPrimitive()) {
                coordinates[index] = element.getAsDouble();
            }
        }
        return coordinates;
    }

}
