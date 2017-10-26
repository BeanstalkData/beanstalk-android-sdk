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

        public void setIdHolder(IdHolder idHolder) {
            this.idHolder = idHolder;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getGeoEnabled() {
            return geoEnabled;
        }

        public void setGeoEnabled(String geoEnabled) {
            this.geoEnabled = geoEnabled;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getStoreNumber() {
            return storeNumber;
        }

        public void setStoreNumber(String storeNumber) {
            this.storeNumber = storeNumber;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public boolean isPaymentLoyaltyParticipation() {
            return paymentLoyaltyParticipation;
        }

        public void setPaymentLoyaltyParticipation(boolean paymentLoyaltyParticipation) {
            this.paymentLoyaltyParticipation = paymentLoyaltyParticipation;
        }

        public String getOpenHours() {
            return openHours;
        }

        public void setOpenHours(String openHours) {
            this.openHours = openHours;
        }

        public boolean isDriveThrough() {
            return driveThrough;
        }

        public void setDriveThrough(boolean driveThrough) {
            this.driveThrough = driveThrough;
        }

        public String getStoreType() {
            return storeType;
        }

        public void setStoreType(String storeType) {
            this.storeType = storeType;
        }

        public String getSchemaVersion() {
            return schemaVersion;
        }

        public void setSchemaVersion(String schemaVersion) {
            this.schemaVersion = schemaVersion;
        }

    }

    public static class IdHolder {

        @SerializedName("$id")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public void setFirstAddressLine(String firstAddressLine) {
            this.firstAddressLine = firstAddressLine;
        }

        public String getSecondAddressLine() {
            return secondAddressLine;
        }

        public void setSecondAddressLine(String secondAddressLine) {
            this.secondAddressLine = secondAddressLine;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double[] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(double[] coordinates) {
            this.coordinates = coordinates;
        }

    }

}
