/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model.deserializer;

import com.beanstalkdata.android.model.Contact;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

public class ContactDeserializer implements JsonDeserializer<Contact> {

    @Override
    public Contact deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Contact contact = new Contact();
        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if ((value != null) && value.isJsonPrimitive()) {
                switch (key) {
                    case Contact.Parameters.ID:
                        contact.setContactId(value.getAsString());
                        break;
                    case Contact.Parameters.FIRST_NAME:
                        contact.setFirstName(value.getAsString());
                        break;
                    case Contact.Parameters.LAST_NAME:
                        contact.setLastName(value.getAsString());
                        break;
                    case Contact.Parameters.ZIP_CODE:
                        contact.setZipCode(value.getAsString());
                        break;
                    case Contact.Parameters.EMAIL:
                        contact.setEmail(value.getAsString());
                        break;
                    case Contact.Parameters.PROSPECT:
                        contact.setProspect(value.getAsString());
                        break;
                    case Contact.Parameters.GENDER:
                        contact.setGender(value.getAsString());
                        break;
                    case Contact.Parameters.BIRTHDAY:
                        contact.setBirthDay(value.getAsString());
                        break;
                    case Contact.Parameters.F_KEY:
                        contact.setFKey(value.getAsString());
                        break;
                    case Contact.Parameters.CELL_NUMBER:
                        contact.setPhone(value.getAsString());
                        break;
                    case Contact.Parameters.TXT_OPT_IN:
                        contact.setTxtOptIn(value.getAsInt());
                        break;
                    case Contact.Parameters.EMAIL_OPT_IN:
                        contact.setEmailOptIn(value.getAsInt());
                        break;
                    case Contact.Parameters.PUSH_NOTIFICATION_OPT_IN:
                        contact.setPushNotificationOptin(value.getAsInt());
                        break;
                    case Contact.Parameters.INBOX_MESSAGE_OPT_IN:
                        contact.setInboxMessageOptin(value.getAsInt());
                        break;
                    case Contact.Parameters.PREFERRED_REWARD:
                        contact.setPreferredReward(value.getAsString());
                        break;
                    case Contact.Parameters.DEVICE_TOKEN_EXT:
                        contact.setDeviceToken(value.getAsString());
                        break;
                    default:
                        contact.setParam(key, value.getAsString());
                        break;
                }
            }
        }
        return contact;
    }

}
