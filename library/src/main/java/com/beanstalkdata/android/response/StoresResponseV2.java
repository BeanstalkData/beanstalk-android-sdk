/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Data model for Stores V2 response.
 */
public class StoresResponseV2 {

    @SerializedName("status")
    private boolean status;

    @SerializedName("stores")
    private StoresResponseV2.Store[] stores;

    public boolean isFailed() {
        return !status || (stores == null) || (stores.length == 0);
    }

    public boolean isStatus() {
        return status;
    }

    public StoresResponseV2.Store[] getStores() {
        return stores;
    }

    public static class Store {

        private Map<String, Object> params = new HashMap<>();

        public IdHolder getIdHolder() {
            Object o = params.get(Parameters.ID_HOLDER);
            return (o instanceof IdHolder) ? (IdHolder) o : null;
        }

        public void setIdHolder(IdHolder idHolder) {
            params.put(Parameters.ID_HOLDER, idHolder);
        }

        public String getCustomerId() {
            Object o = params.get(Parameters.CUSTOMER_ID);
            return (o instanceof String) ? (String) o : null;
        }

        public void setCustomerId(String customerId) {
            params.put(Parameters.CUSTOMER_ID, customerId);
        }

        public String getGeoEnabled() {
            Object o = params.get(Parameters.GEO_ENABLED);
            return (o instanceof String) ? (String) o : null;
        }

        public void setGeoEnabled(String geoEnabled) {
            params.put(Parameters.GEO_ENABLED, geoEnabled);
        }

        public String getName() {
            Object o = params.get(Parameters.NAME);
            return (o instanceof String) ? (String) o : null;
        }

        public void setName(String name) {
            params.put(Parameters.NAME, name);
        }

        public String getPhoneNumber() {
            Object o = params.get(Parameters.PHONE_NUMBER);
            return (o instanceof String) ? (String) o : null;
        }

        public void setPhoneNumber(String phoneNumber) {
            params.put(Parameters.PHONE_NUMBER, phoneNumber);
        }

        public String getWebsite() {
            Object o = params.get(Parameters.WEBSITE);
            return (o instanceof String) ? (String) o : null;
        }

        public void setWebsite(String website) {
            params.put(Parameters.WEBSITE, website);
        }

        public String getEmail() {
            Object o = params.get(Parameters.EMAIL);
            return (o instanceof String) ? (String) o : null;
        }

        public void setEmail(String email) {
            params.put(Parameters.EMAIL, email);
        }

        public String getStoreNumber() {
            Object o = params.get(Parameters.STORE_NUMBER);
            return (o instanceof String) ? (String) o : null;
        }

        public void setStoreNumber(String storeNumber) {
            params.put(Parameters.STORE_NUMBER, storeNumber);
        }

        public Location getLocation() {
            Object o = params.get(Parameters.LOCATION);
            return (o instanceof Location) ? (Location) o : null;
        }

        public void setLocation(Location location) {
            params.put(Parameters.LOCATION, location);
        }

        public String getTimeZone() {
            Object o = params.get(Parameters.TIME_ZONE);
            return (o instanceof String) ? (String) o : null;
        }

        public void setTimeZone(String timeZone) {
            params.put(Parameters.TIME_ZONE, timeZone);
        }

        public boolean isPaymentLoyaltyParticipation() {
            Object o = params.get(Parameters.PAYMENT_LOYALTY_PARTICIPATION);
            return (o instanceof Boolean) ? (Boolean) o : false;
        }

        public void setPaymentLoyaltyParticipation(boolean paymentLoyaltyParticipation) {
            params.put(Parameters.PAYMENT_LOYALTY_PARTICIPATION, paymentLoyaltyParticipation);
        }

        public String getOpenHours() {
            Object o = params.get(Parameters.OPEN_HOURS);
            return (o instanceof String) ? (String) o : null;
        }

        public void setOpenHours(String openHours) {
            params.put(Parameters.OPEN_HOURS, openHours);
        }

        public boolean isDriveThrough() {
            Object o = params.get(Parameters.DRIVE_THROUGH);
            return (o instanceof Boolean) ? (Boolean) o : false;
        }

        public void setDriveThrough(boolean driveThrough) {
            params.put(Parameters.DRIVE_THROUGH, driveThrough);
        }

        public String getStoreType() {
            Object o = params.get(Parameters.STORE_TYPE);
            return (o instanceof String) ? (String) o : null;
        }

        public void setStoreType(String storeType) {
            params.put(Parameters.STORE_TYPE, storeType);
        }

        public String getSchemaVersion() {
            Object o = params.get(Parameters.SCHEMA_VERSION);
            return (o instanceof String) ? (String) o : null;
        }

        public void setSchemaVersion(String schemaVersion) {
            params.put(Parameters.SCHEMA_VERSION, schemaVersion);
        }

        public String getParam(String key) {
            Object o = params.get(key);
            return (o instanceof String) ? (String) o : null;
        }

        public void setParam(String key, String value) {
            params.put(key, value);
        }

        public interface Parameters {
            String ID_HOLDER = "_id";
            String CUSTOMER_ID = "customer_id";
            String GEO_ENABLED = "geo_enabled";
            String NAME = "name";
            String PHONE_NUMBER = "phone_number";
            String WEBSITE = "website";
            String EMAIL = "email";
            String STORE_NUMBER = "store_number";
            String LOCATION = "loc";
            String TIME_ZONE = "time_zone";
            String PAYMENT_LOYALTY_PARTICIPATION = "payment_loyalty_participation";
            String OPEN_HOURS = "hours";
            String DRIVE_THROUGH = "drive_thru";
            String STORE_TYPE = "store_type";
            String SCHEMA_VERSION = "schema_version";
        }

    }

    public static class IdHolder {

        private Map<String, Object> params = new HashMap<>();

        public String getId() {
            Object o = params.get(Parameters.ID);
            return (o instanceof String) ? (String) o : null;
        }

        public void setId(String id) {
            params.put(Parameters.ID, id);
        }

        public String getParam(String key) {
            Object o = params.get(key);
            return (o instanceof String) ? (String) o : null;
        }

        public void setParam(String key, String value) {
            params.put(key, value);
        }

        public interface Parameters {
            String ID = "$id";
        }

    }

    public static class Location {

        private Map<String, Object> params = new HashMap<>();

        public String getFirstAddressLine() {
            Object o = params.get(Parameters.FIRST_ADDRESS_LINE);
            return (o instanceof String) ? (String) o : null;
        }

        public void setFirstAddressLine(String firstAddressLine) {
            params.put(Parameters.FIRST_ADDRESS_LINE, firstAddressLine);
        }

        public String getSecondAddressLine() {
            Object o = params.get(Parameters.SECOND_ADDRESS_LINE);
            return (o instanceof String) ? (String) o : null;
        }

        public void setSecondAddressLine(String secondAddressLine) {
            params.put(Parameters.SECOND_ADDRESS_LINE, secondAddressLine);
        }

        public String getCity() {
            Object o = params.get(Parameters.CITY);
            return (o instanceof String) ? (String) o : null;
        }

        public void setCity(String city) {
            params.put(Parameters.CITY, city);
        }

        public String getState() {
            Object o = params.get(Parameters.STATE);
            return (o instanceof String) ? (String) o : null;
        }

        public void setState(String state) {
            params.put(Parameters.STATE, state);
        }

        public String getZip() {
            Object o = params.get(Parameters.ZIP);
            return (o instanceof String) ? (String) o : null;
        }

        public void setZip(String zip) {
            params.put(Parameters.ZIP, zip);
        }

        public String getCountry() {
            Object o = params.get(Parameters.COUNTRY);
            return (o instanceof String) ? (String) o : null;
        }

        public void setCountry(String country) {
            params.put(Parameters.COUNTRY, country);
        }

        public String getCategory() {
            Object o = params.get(Parameters.CATEGORY);
            return (o instanceof String) ? (String) o : null;
        }

        public void setCategory(String category) {
            params.put(Parameters.CATEGORY, category);
        }

        public String getType() {
            Object o = params.get(Parameters.TYPE);
            return (o instanceof String) ? (String) o : null;
        }

        public void setType(String type) {
            params.put(Parameters.TYPE, type);
        }

        public double[] getCoordinates() {
            Object o = params.get(Parameters.COORDINATES);
            return (o instanceof double[]) ? (double[]) o : null;
        }

        public void setCoordinates(double[] coordinates) {
            params.put(Parameters.COORDINATES, coordinates);
        }

        public String getParam(String key) {
            Object o = params.get(key);
            return (o instanceof String) ? (String) o : null;
        }

        public void setParam(String key, String value) {
            params.put(key, value);
        }

        public interface Parameters {
            String FIRST_ADDRESS_LINE = "address_1";
            String SECOND_ADDRESS_LINE = "address_2";
            String CITY = "city";
            String STATE = "state";
            String ZIP = "zip";
            String COUNTRY = "country";
            String CATEGORY = "category";
            String TYPE = "type";
            String COORDINATES = "coordinates";
        }

    }

}
