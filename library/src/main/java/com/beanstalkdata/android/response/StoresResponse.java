/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Stores response.
 */
public class StoresResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("stores")
    private Store[] stores;

    public boolean isFailed() {
        return !status || (stores == null) || (stores.length == 0);
    }

    public static class Store {

        @SerializedName("_id")
        private Id id;

        @SerializedName("CustomerId")
        private String customerId;

        @SerializedName("DriveThru")
        private String driveThru;

        @SerializedName("Date")
        private UnixDate date;

        @SerializedName("StoreId")
        private String storeId;

        @SerializedName("StoreName")
        private String storeName;

        @SerializedName("Country")
        private String country;

        @SerializedName("Address1")
        private String address1;

        @SerializedName("Address2")
        private String address2;

        @SerializedName("City")
        private String city;

        @SerializedName("State")
        private String state;

        @SerializedName("Zip")
        private String zip;

        @SerializedName("DMA")
        private String dma;

        @SerializedName("Phone")
        private String phone;

        @SerializedName("Fax")
        private String fax;

        @SerializedName("Concept")
        private String concept;

        @SerializedName("Venue")
        private String venue;

        @SerializedName("SubVenue")
        private String subVenue;

        @SerializedName("Region")
        private String region;

        @SerializedName("RegionName")
        private String regionName;

        @SerializedName("Longitude")
        private String lon;

        @SerializedName("Latitude")
        private String lat;

        @SerializedName("loc")
        private Loc loc;

        @SerializedName("geoEnabled")
        private String geoEnabled;

        @SerializedName("PaymentLoyaltyParticipation")
        private boolean paymentLoyaltyParticipation;

        public Id getId() {
            return id;
        }

        public String getCustomerId() {
            return customerId;
        }

        public UnixDate getDate() {
            return date;
        }

        public String getStoreId() {
            return storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public String getCountry() {
            return country;
        }

        public String getAddress1() {
            return address1;
        }

        public String getAddress2() {
            return address2;
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

        public String getDma() {
            return dma;
        }

        public String getPhone() {
            return phone;
        }

        public String getFax() {
            return fax;
        }

        public String getConcept() {
            return concept;
        }

        public String getVenue() {
            return venue;
        }

        public String getSubVenue() {
            return subVenue;
        }

        public String getRegion() {
            return region;
        }

        public String getRegionName() {
            return regionName;
        }

        public String getLon() {
            return lon;
        }

        public String getLat() {
            return lat;
        }

        public Loc getLoc() {
            return loc;
        }

        public String getGeoEnabled() {
            return geoEnabled;
        }

        public boolean isPaymentLoyaltyParticipation() {
            return paymentLoyaltyParticipation;
        }

        public String getDriveThru() {
            return driveThru;
        }

        public boolean hasDriveThru() {
            return driveThru.equalsIgnoreCase("yes");
        }

        public boolean isOpen() {
            return geoEnabled.equals("1");
        }

        public static Store makeMockStore(String name, String address1, String address2, String phone, boolean status, float lat, float lon) {
            Store store = new Store();
            store.storeName = name;
            store.address1 = address1;
            store.address2 = address2;
            store.phone = phone;
            store.geoEnabled = status ? "1" : "";
            store.lat = String.valueOf(lat);
            store.lon = String.valueOf(lon);
            return store;
        }
    }

    public static class Id {

        @SerializedName("$id")
        private String id;

        public String getId() {
            return id;
        }

    }

    public static class UnixDate {

        @SerializedName("sec")
        private int sec;

        @SerializedName("usec")
        private int usec;

        public int getSec() {
            return sec;
        }

        public int getUsec() {
            return usec;
        }

    }

    public static class Loc {

        @SerializedName("type")
        private String type;

        @SerializedName("coordinates")
        private float[] coordinates;

        @SerializedName("category")
        private String category;

        public String getType() {
            return type;
        }

        public float[] getCoordinates() {
            return coordinates;
        }

        public String getCategory() {
            return category;
        }

    }

    public boolean isStatus() {
        return status;
    }

    public Store[] getStores() {
        return stores;
    }

}
