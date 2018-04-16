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
public class ResponseReview implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("brandid")
    private String brandId;
    @SerializedName("text")
    private String text;
    @SerializedName("image")
    private String image;
    @SerializedName("user")
    private ResponseProfile user;
    @SerializedName("likes")
    private ArrayList<String> likes;
    @SerializedName("comments")
    private ArrayList<String> comments;
    @SerializedName("createdAt")
    private String createAt;
    @SerializedName("rate")
    private String rate;
    @SerializedName("brand")
    private ResponseBrand responseBrand;
    @SerializedName("commentsCount")
    private String commentsCount;
    @SerializedName("likesCount")
    private int likesCount;
    @SerializedName("images")
    private List<String> picturies;
    @SerializedName("subcategoryId")
    private String subCategoryId;

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(final String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public List<String> getPicturies(PhotoSize photoSize, Context context) {

        if(picturies == null || picturies.size() == 0) return picturies;
        ArrayList<String> strings = new ArrayList<>();
        for(int i =0; i <picturies.size(); i++)
        {
            strings.add(picturies.get(i)+  context.getString(photoSize.value));
        }
        return strings;
    }
    public void setPicturies(final List<String> picturies) {
        this.picturies = picturies;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(final int likesCount) {
        this.likesCount = likesCount;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(final String commentsCount) {
        this.commentsCount = commentsCount;
    }

    public ResponseBrand getResponseBrand() {
        return responseBrand;
    }

    public void setResponseBrand(final ResponseBrand responseBrand) {
        this.responseBrand = responseBrand;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(final String rate) {
        this.rate = rate;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(final String createAt) {
        this.createAt = createAt;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
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

    public String getImage(PhotoSize photoSize, Context context) {
        if(image == null || image.equals("")) return image;
        return image + context.getString(photoSize.value);
    }
    public void setImage(final String image) {
        this.image = image;
    }

    public ResponseProfile getUser() {
        return user;
    }

    public void setUser(final ResponseProfile user) {
        this.user = user;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(final ArrayList<String> likes) {
        this.likes = likes;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(final ArrayList<String> comments) {
        this.comments = comments;
    }

    public void addNewLike() {
        likesCount++;
    }

    public void deslike()
    {
        likesCount--;
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
