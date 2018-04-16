package com.mgrmobi.brandbeat.network.responce;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseFeed implements Serializable
{
    @SerializedName("image")
    private String image;
    @SerializedName("title")
    private String title;
    @SerializedName("text")
    private String text;
    @SerializedName("avgRate")
    private double avgRate;
    @SerializedName("id")
    private String id;
    @SerializedName("categoryId")
    private String categoryId;
    @SerializedName("subcategoryId")
    private String subcategoryId;
    @SerializedName("createAt")
    private String createAt;
    @SerializedName("subcategory")
    private ResponseCategories subCategories;

    public ResponseCategories getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(final ResponseCategories subCategories) {
        this.subCategories = subCategories;
    }

    @SerializedName("reviewsWithTextSum")
    private String reviewSum;

    public String getReviewSum() {
        return reviewSum;
    }

    public void setReviewSum(final String reviewSum) {
        this.reviewSum = reviewSum;
    }

    private ArrayList<ResponseReview> reviews;

    public String getImage(PhotoSize photoSize, Context context) {
        if(image == null || image.equals("")) return image;
        return image + context.getString(photoSize.value);
    }

    public String getSimpleImage()
    {
        return image;
    }
    public void setImage(final String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public double getAvgRate() {
        double d = avgRate;
        d = d*10;
        int i = (int) Math.round(d);
        return (double)i/10;
    }

    public void setAvgRate(final double avgRate) {
        this.avgRate = avgRate;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(final String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(final String createAt) {
        this.createAt = createAt;
    }

    public ArrayList<ResponseReview> getReviews() {
        return reviews;
    }

    public void setReviews(final ArrayList<ResponseReview> reviews) {
        this.reviews = reviews;
    }
}
