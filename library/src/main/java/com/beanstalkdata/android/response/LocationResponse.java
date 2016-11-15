package com.beanstalkdata.android.response;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for Location response.
 */
public class LocationResponse {
    @SerializedName("results")
    private Result[] results;

    private String status;

    public boolean isFailed() {
        return !"OK".equalsIgnoreCase(status)
                || (results == null)
                || (results.length == 0)
                || (results[0] == null)
                || (results[0].geometry == null)
                || (results[0].geometry.location == null);
    }

    public Location getLocation() {
        if (isFailed()) {
            return new Location();
        } else {
            return results[0].geometry.location;
        }
    }

    public static class Result {

        @SerializedName("geometry")
        private Geometry geometry;
    }

    public static class Geometry {

        @SerializedName("location")
        private Location location;
    }


    public static class Location {
        @SerializedName("lat")
        private double lat;

        @SerializedName("lng")
        private double lng;

        public double getLatitude() {
            return lat;
        }

        public double getLongitude() {
            return lng;
        }
    }
}
