package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseIncomeRange {
    @SerializedName("country")
    private String country;
    @SerializedName("currency")
    private String currency;
    @SerializedName("id")
    private String id;
    @SerializedName("ranges")
    private ArrayList<String> ranges;

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public ArrayList<String> getRanges() {
        return ranges;
    }

    public void setRanges(final ArrayList<String> ranges) {
        this.ranges = ranges;
    }
}
