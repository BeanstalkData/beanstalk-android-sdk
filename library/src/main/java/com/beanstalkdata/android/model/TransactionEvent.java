/*
 * Copyright (C) 2016 Heartland Commerce, Inc. All rights reserved.
 */

package com.beanstalkdata.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Data model for Transaction Event.
 */
public class TransactionEvent {

    @Expose
    @SerializedName("_id")
    private Id transactionId;

    @Expose
    @SerializedName("CustomerId")
    private int customerId;

    @Expose
    @SerializedName("ContactId")
    private int contactId;

    @Expose
    @SerializedName("Date")
    private ComplexDate date;

    @Expose
    @SerializedName("Status")
    private String status;

    @Expose
    @SerializedName("AssignedTo")
    private String assignedTo;

    @Expose
    @SerializedName("Details")
    private ArrayList<String> details;

    @Expose
    @SerializedName("LastModified")
    private ArrayList<ComplexDate> lastModifiedDates;

    @Expose
    @SerializedName("LastUpdated")
    private ArrayList<String> lastUpdated;

    @Expose
    @SerializedName("CustomerKey")
    private String customerKey;

    @Expose
    @SerializedName("_type")
    private ArrayList<String> types;

    public String getTransactionId() {
        return (transactionId != null) ? transactionId.getId() : null;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getContactId() {
        return contactId;
    }

    public ComplexDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public ArrayList<String> getDetails() {
        return details;
    }

    public ArrayList<ComplexDate> getLastModifiedDates() {
        return lastModifiedDates;
    }

    public ArrayList<String> getLastUpdated() {
        return lastUpdated;
    }

    public String getCustomerKey() {
        return customerKey;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    private static class Id {

        @SerializedName("$id")
        private String id;

        public String getId() {
            return id;
        }

    }

    public static class ComplexDate {

        private final SimpleDateFormat complexDateFormat;

        @Expose
        @SerializedName("date")
        private String dateTime;

        @Expose
        @SerializedName("timezone_type")
        private int timezoneType;

        @Expose
        @SerializedName("timezone")
        private String timezone;

        public ComplexDate() {
            this.complexDateFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ssZZZZZ", Locale.US);
        }

        public Date getDate() {
            if (dateTime == null) {
                return null;
            }

            StringBuilder dateStringBuilder = new StringBuilder(dateTime);
            if (timezone != null) {
                dateStringBuilder.append(timezone);
            }
            return parseDate(dateStringBuilder.toString());
        }

        public int getTimezoneType() {
            return timezoneType;
        }

        private Date parseDate(String date) {
            try {
                return complexDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

}