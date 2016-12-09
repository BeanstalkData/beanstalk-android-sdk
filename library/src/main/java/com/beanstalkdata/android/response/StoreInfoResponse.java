/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Location Info response.
 */
public class StoreInfoResponse {

    @SerializedName("storeInfo")
    private Store store;

    public boolean isFailed() {
        return store == null;
    }

    public static class Store {

        @SerializedName("Id")
        private int id;

        @SerializedName("Ownership")
        private String ownership;

        @SerializedName("OwnershipID")
        private String ownershipId;

        @SerializedName("Primary_Id")
        private String primaryId;

        @SerializedName("StoreName")
        private String storeName;

        @SerializedName("FranchiseName")
        private String franchiseName;

        @SerializedName("ClosingDate")
        private String closingDate;

        @SerializedName("Region")
        private String region;

        @SerializedName("RegionID")
        private String regionId;

        @SerializedName("OpeningDate")
        private String openingDate;

        @SerializedName("Email")
        private String email;

        @SerializedName("FaxNumber")
        private String faxNumber;

        @SerializedName("CustomerId")
        private String customerId;

        @SerializedName("PhoneNumber")
        private String phoneNumber;

        @SerializedName("DisplayName")
        private String displayName;

        @SerializedName("PostalCode")
        private String postalCode;

        @SerializedName("City")
        private String city;

        @SerializedName("State")
        private String state;

        @SerializedName("Address1")
        private String address1;

        @SerializedName("DMAID")
        private String dmaId;

        @SerializedName("Address2")
        private String address2;

        @SerializedName("DMA")
        private String dma;

        @SerializedName("AreaID")
        private String areaId;

        @SerializedName("Area")
        private String area;

        @SerializedName("DistrictID")
        private String districtId;

        @SerializedName("District")
        private String district;

        @SerializedName("MarketID")
        private String marketId;

        @SerializedName("Market")
        private String market;

        public int getId() {
            return id;
        }

        public String getOwnership() {
            return ownership;
        }

        public String getOwnershipId() {
            return ownershipId;
        }

        public String getPrimaryId() {
            return primaryId;
        }

        public String getStoreName() {
            return storeName;
        }

        public String getFranchiseName() {
            return franchiseName;
        }

        public String getClosingDate() {
            return closingDate;
        }

        public String getRegion() {
            return region;
        }

        public String getRegionId() {
            return regionId;
        }

        public String getOpeningDate() {
            return openingDate;
        }

        public String getEmail() {
            return email;
        }

        public String getFaxNumber() {
            return faxNumber;
        }

        public String getCustomerId() {
            return customerId;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getAddress1() {
            return address1;
        }

        public String getDmaId() {
            return dmaId;
        }

        public String getAddress2() {
            return address2;
        }

        public String getDma() {
            return dma;
        }

        public String getAreaId() {
            return areaId;
        }

        public String getArea() {
            return area;
        }

        public String getDistrictId() {
            return districtId;
        }

        public String getDistrict() {
            return district;
        }

        public String getMarketId() {
            return marketId;
        }

        public String getMarket() {
            return market;
        }

    }

    public Store getStore() {
        return store;
    }

}
