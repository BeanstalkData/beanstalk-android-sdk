/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.response;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Locations response.
 */
public class LocationsResponse {

    @SerializedName("Location")
    private Location[] locations;

    public boolean isFailed() {
        return locations.length == 0;
    }

    public static class Attributes {

        @SerializedName("ID")
        private String id;

        public String getId() {
            return id;
        }

    }

    public static class Location {

        @SerializedName("@attributes")
        private Attributes attributes;

        @SerializedName("LocationNumber")
        private String locationNumber;

        @SerializedName("LocationName")
        private String locationName;

        @SerializedName("StreetAddress")
        private String streetAddress;

        @SerializedName("City")
        private String city;

        @SerializedName("State")
        private String state;

        @SerializedName("Zipcode")
        private String zipcode;

        @SerializedName("Country")
        private String country;

        @SerializedName("X")
        private String x;

        @SerializedName("Y")
        private String y;

        @SerializedName("PhoneNumber")
        private String phoneNumber;

        @SerializedName("Participation")
        private String participation;

        public Attributes getAttributes() {
            return attributes;
        }

        public String getLocationLocationName() {
            return locationName;
        }

        public String getStreetAddress() {
            return streetAddress;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getZipcode() {
            return zipcode;
        }

        public String getCountry() {
            return country;
        }

        public String getX() {
            return x;
        }

        public String getY() {
            return y;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getLocationNumber() {
            return locationNumber;
        }

        public String getParticipation() {
            return participation;
        }

    }

    public Location[] getLocations() {
        return locations;
    }

}
