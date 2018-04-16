package com.mgrmobi.brandbeat.network.responce;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseLocalFeed implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("authorId")
    private String authorId;
    @SerializedName("brandId")
    private String brandId;
    @SerializedName("text")
    private String text;
    @SerializedName("comments")
    private ArrayList<String> comments;
    @SerializedName("likes")
    private ArrayList<String> likes;
    @SerializedName("images")
    private List<String> image;
    @SerializedName("location")
    private RequestLocation location;
    @SerializedName("brand")
    private ResponseBrand responseBrand;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("user")
    private ResponseProfile profile;
    @SerializedName("rate")
    private String rate;

    public String getRate() {
        return rate;
    }

    public void setRate(final String rate) {
        this.rate = rate;
    }

    public ResponseProfile getProfile() {
        return profile;
    }

    public void setProfile(final ResponseProfile profile) {
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(final String authorId) {
        this.authorId = authorId;
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

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(final ArrayList<String> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(final ArrayList<String> likes) {
        this.likes = likes;
    }
    public List<String> getImage(PhotoSize photoSize, Context context) {

        if(image == null || image.size() == 0) return image;
        ArrayList<String> strings = new ArrayList<>();
        for(int i =0; i <image.size(); i++)
        {
            strings.add(image.get(i)+  context.getString(photoSize.value));
        }
        return strings;
    }
    public void setImage(final List<String> image) {
        this.image = image;
    }

    public RequestLocation getLocation() {
        return location;
    }

    public void setLocation(final RequestLocation location) {
        this.location = location;
    }

    public ResponseBrand getResponseBrand() {
        return responseBrand;
    }

    public void setResponseBrand(final ResponseBrand responseBrand) {
        this.responseBrand = responseBrand;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public void addLike(String id) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(id);
    }

    public void deslike(String id) {
        likes.remove(id);
    }
}
