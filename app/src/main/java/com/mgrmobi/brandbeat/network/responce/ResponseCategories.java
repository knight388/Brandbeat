package com.mgrmobi.brandbeat.network.responce;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseCategories implements Serializable
{
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;
    @SerializedName("data")
    private ArrayList<ResponseCategories> responseCategories;
    @SerializedName("iSubscriber")
    private boolean isSubscriber;
    @SerializedName("brands")
    private List<ResponseBrand> responseBrands;

    public List<ResponseBrand> getResponseBrands() {
        return responseBrands;
    }

    public void setResponseBrands(final List<ResponseBrand> responseBrands) {
        this.responseBrands = responseBrands;
    }

    public boolean isSubscriber() {
        return isSubscriber;
    }

    public void setIsSubscriber(final boolean isSubscriber) {
        this.isSubscriber = isSubscriber;
    }

    private boolean isChecked;

    public ArrayList<ResponseCategories> getResponseCategories() {
        return responseCategories;
    }

    public void setResponseCategories(final ArrayList<ResponseCategories> responseCategories) {
        this.responseCategories = responseCategories;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getImage(PhotoSize photoSize, Context context) {
        if(image == null || image.equals("")) return image;
        if(context == null) return image;
        return image + context.getString(photoSize.value);
    }
    public void setImage(final String image) {
        this.image = image;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(final boolean isChecked) {
        this.isChecked = isChecked;
    }
}
