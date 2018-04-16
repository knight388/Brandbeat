package com.mgrmobi.brandbeat.network.responce;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseBrand implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("status")
    private String status;
    @SerializedName("image")
    private String image;
    @SerializedName("text")
    private String text;
    @SerializedName("id")
    private String id;
    @SerializedName("avgRate")
    private String avgRate;
    @SerializedName("reviewsWithTextSum")
    private String reviewsSum;
    @SerializedName("iSubscriber")
    private boolean iSubscriber;
    @SerializedName("subcategoryId")
    private String subCategoryId;
    @SerializedName("reviewsSum")
    private String ratedSum;
    @SerializedName("avgRateCount")
    private String rateCount;
    @SerializedName("brandPrev")
    private String brandPrevId;
    @SerializedName("brandNext")
    private String brandNext;
    @SerializedName("trending")
    private ResponseTranding responseTranding;
    @SerializedName("category")
    private ResponseCategories categories;
    @SerializedName("subcategory")
    private ResponseCategories subCategories;

    public ResponseCategories getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(final ResponseCategories subCategories) {
        this.subCategories = subCategories;
    }

    public ResponseTranding getResponseTranding() {
        return responseTranding;
    }

    public void setResponseTranding(final ResponseTranding responseTranding) {
        this.responseTranding = responseTranding;
    }

    public String getAvgRateSumm() {
        return  ratedSum;
    }

    public String getBrandPrevId() {
        return brandPrevId;
    }

    public void setBrandPrevId(final String brandPrevId) {
        this.brandPrevId = brandPrevId;
    }

    public String getBrandNext() {
        return brandNext;
    }

    public void setBrandNext(final String brandNext) {
        this.brandNext = brandNext;
    }

    public String getRateCount() {
        return rateCount;
    }

    public void setRateCount(final String rateCount) {
        this.rateCount = rateCount;
    }

    public String getRatedSum() {
        return ratedSum;
    }

    public void setRatedSum(final String ratedSum) {
        this.ratedSum = ratedSum;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(final String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public boolean iSubscriber() {
        return iSubscriber;
    }

    public void setiSubscriber(final boolean iSubscriber) {
        this.iSubscriber = iSubscriber;
    }

    public String getReviewsSum() {
        return reviewsSum;
    }

    public void setReviewsSum(final String reviewsSum) {
        this.reviewsSum = reviewsSum;
    }

    public String getAvgRate() {
        if(avgRate == null) return "0.0";
        double d = Double.parseDouble(avgRate);
        d = d*10;
        int i = (int) Math.round(d);
        return String.valueOf((double)i/10);
    }

    public void setAvgRate(final String avgRate) {
        this.avgRate = avgRate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getImage(PhotoSize photoSize, Context context) {
        if(image == null || image.equals("")) return image;
        if(context == null) return image;
        return image + context.getString(photoSize.value);
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public ResponseCategories getCategories() {
        return categories;
    }

    public void setCategories(final ResponseCategories categories) {
        this.categories = categories;
    }
}