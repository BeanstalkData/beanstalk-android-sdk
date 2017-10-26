/*
 * Copyright (C) 2017 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.google.gson.annotations.SerializedName;

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

        @SerializedName("_id")
        private IdHolder idHolder;

        @SerializedName("customer_id")
        private String customerId;

        @SerializedName("geo_enabled")
        private String geoEnabled;

        private String name;

        @SerializedName("phone_number")
        private String phoneNumber;

        private String website;

        private String email;

        @SerializedName("store_number")
        private String storeNumber;

        @SerializedName("loc")
        private Location location;

        @SerializedName("time_zone")
        private String timeZone;

        @SerializedName("payment_loyalty_participation")
        private boolean paymentLoyaltyParticipation;

        @SerializedName("hours")
        private String openHours;

        @SerializedName("drive_thru")
        private boolean driveThrough;

        @SerializedName("store_type")
        private String storeType;

        @SerializedName("schema_version")
        private String schemaVersion;

        public IdHolder getIdHolder() {
            return idHolder;
        }

        public String getCustomerId() {
            return customerId;
        }

        public String getGeoEnabled() {
            return geoEnabled;
        }

        public String getName() {
            return name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getWebsite() {
            return website;
        }

        public String getEmail() {
            return email;
        }

        public String getStoreNumber() {
            return storeNumber;
        }

        public Location getLocation() {
            return location;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public boolean isPaymentLoyaltyParticipation() {
            return paymentLoyaltyParticipation;
        }

        public String getOpenHours() {
            return openHours;
        }

        public boolean isDriveThrough() {
            return driveThrough;
        }

        public String getStoreType() {
            return storeType;
        }

        public String getSchemaVersion() {
            return schemaVersion;
        }

    }

    public static class IdHolder {

        @SerializedName("$id")
        private String id;

        public String getId() {
            return id;
        }

    }

    public static class Location {

        @SerializedName("address_1")
        private String firstAddressLine;

        @SerializedName("address_2")
        private String secondAddressLine;

        private String city;

        private String state;

        private String zip;

        private String country;

        private String category;

        private String type;

        private double[] coordinates;

        public String getFirstAddressLine() {
            return firstAddressLine;
        }

        public String getSecondAddressLine() {
            return secondAddressLine;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getZip() {
            return zip;
        }

        public String getCountry() {
            return country;
        }

        public String getCategory() {
            return category;
        }

        public String getType() {
            return type;
        }

        public double[] getCoordinates() {
            return coordinates;
        }

    }

}
