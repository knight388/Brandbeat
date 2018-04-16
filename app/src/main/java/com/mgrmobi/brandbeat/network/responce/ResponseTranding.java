package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseTranding {
    @SerializedName("index")
    private String index;
    @SerializedName("date")
    private String date;
    @SerializedName("rate")
    private String rate;
    @SerializedName("changeIndex")
    private String changeIndex;

    public String getIndex() {
        return index;
    }

    public void setIndex(final String index) {
        this.index = index;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(final String rate) {
        this.rate = rate;
    }

    public String getChangeIndex() {
        return changeIndex;
    }

    public void setChangeIndex(final String changeIndex) {
        this.changeIndex = changeIndex;
    }
}
