package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseStatistics implements Serializable
{
    @SerializedName("avgRate")
    private Double avgRate;
    @SerializedName("avgRateSum")
    private Double avgRateSum;
    @SerializedName("reviewsSum")
    private Double reviewsSum;
    @SerializedName("reviewsWithTextSum")
    private String reviewsWithTextSum;
    @SerializedName("reviewsRatedSum")
    private String reviewsRatedSum;
    @SerializedName("reviewsWithCommentsSum")
    private String reviewsWithCommentsSum;
    @SerializedName("rates")
    private Map<String, Double> rates;
    @SerializedName("reviewsImageSum")
    private String reviewsImageSum;

    public String getReviewsImageSum() {
        return reviewsImageSum;
    }

    public void setReviewsImageSum(final String reviewsImageSum) {
        this.reviewsImageSum = reviewsImageSum;
    }

    public String getAvgRate() {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        if(avgRate == null)
            return "0.0";
        String format = decimalFormat.format(avgRate);
        return format;
    }


    public Double getAvgDubleValue()
    {
        return avgRate;
    }

    public void setAvgRate(final Double avgRate) {
        this.avgRate = avgRate;
    }

    public Double getAvgRateSum() {
        return avgRateSum;
    }

    public void setAvgRateSum(final Double avgRateSum) {
        this.avgRateSum = avgRateSum;
    }

    public Double getReviewsSum() {
        return reviewsSum;
    }

    public void setReviewsSum(final Double reviewsSum) {
        this.reviewsSum = reviewsSum;
    }

    public String getReviewsWithTextSum() {
        return reviewsWithTextSum;
    }

    public void setReviewsWithTextSum(final String reviewsWithTextSum) {
        this.reviewsWithTextSum = reviewsWithTextSum;
    }

    public String getReviewsRatedSum() {
        return reviewsRatedSum;
    }

    public void setReviewsRatedSum(final String reviewsRatedSum) {
        this.reviewsRatedSum = reviewsRatedSum;
    }

    public String getReviewsWithCommentsSum() {
        return reviewsWithCommentsSum;
    }

    public void setReviewsWithCommentsSum(final String reviewsWithCommentsSum) {
        this.reviewsWithCommentsSum = reviewsWithCommentsSum;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(final Map<String, Double> rates) {
        this.rates = rates;
    }
}
