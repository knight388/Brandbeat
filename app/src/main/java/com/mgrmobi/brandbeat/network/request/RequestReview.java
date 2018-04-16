package com.mgrmobi.brandbeat.network.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RequestReview implements Serializable
{
    @SerializedName("rate")
    private String rate;
    @SerializedName("brandId")
    private String brandId;
    @SerializedName("text")
    private String text;
    @SerializedName("review_id")
    private String reviewId;
    @SerializedName("images")
    private List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(final List<String> images) {
        this.images = images;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(final String reviewId) {
        this.reviewId = reviewId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(final String brandId) {
        this.brandId = brandId;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(final String rate) {
        this.rate = rate;
    }
}
