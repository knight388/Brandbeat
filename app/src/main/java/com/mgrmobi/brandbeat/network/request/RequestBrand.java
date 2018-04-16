package com.mgrmobi.brandbeat.network.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RequestBrand
{
    @SerializedName("categoryId")
    private String category;
    @SerializedName("subcategoryId")
    private String subcategory;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;
    @SerializedName("text")
    private String text;

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(final String subcategory) {
        this.subcategory = subcategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
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
}
