package com.mgrmobi.brandbeat.network.responce;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import com.google.gson.annotations.SerializedName;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.entity.TypeNatifications;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;

import java.lang.reflect.Type;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseNotification {

    @SerializedName("text")
    private String text;
    @SerializedName("id")
    private String id;
    @SerializedName("userId")
    private String userId;
    @SerializedName("type")
    private String type;
    @SerializedName("status")
    private String status;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("reviewId")
    private String reviewId;
    @SerializedName("commentId")
    private String commentId;
    @SerializedName("reviewParentId")
    private String reviewParentId;
    @SerializedName("image")
    private String image;
    @SerializedName("from")
    private String fromName;
    @SerializedName("brand")
    private ResponseBrand brand;
    @SerializedName("achievement")
    private ResponseAchievement responseAchievement;
    @SerializedName("follower")
    private ResponseProfile userProfile;


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

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(final String reviewId) {
        this.reviewId = reviewId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(final String commentId) {
        this.commentId = commentId;
    }

    public String getReviewParentId() {
        return reviewParentId;
    }

    public void setReviewParentId(final String reviewParentId) {
        this.reviewParentId = reviewParentId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(final String fromName) {
        this.fromName = fromName;
    }

    public ResponseBrand getBrand() {
        return brand;
    }

    public void setBrand(final ResponseBrand brand) {
        this.brand = brand;
    }

    public ResponseAchievement getResponseAchievement() {
        return responseAchievement;
    }

    public void setResponseAchievement(final ResponseAchievement responseAchievement) {
        this.responseAchievement = responseAchievement;
    }

    public ResponseProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(final ResponseProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getTitleText() {
        TypeNatifications typeNatifications = getEnum();
        String text = "";
        int positionStartString = 0;
        int positionBoldEndString = 0;
        switch (typeNatifications) {
            case FOLLOW:
                if (userProfile.getFirstName() != null) {
                    text += userProfile.getFirstName() + " ";
                }
                if (userProfile.getLastName() != null) {
                    text += userProfile.getLastName();
                }
                if (text.equals("")) {
                    text = userProfile.getUsername();
                }
                break;
            case LIKE_REVIEW:
                if (userProfile.getFirstName() != null) {
                    text += userProfile.getFirstName() + " ";
                }
                if (userProfile.getLastName() != null) {
                    text += userProfile.getLastName();
                }
                if (text.equals("")) {
                    text = userProfile.getUsername();
                }
                break;
            case COMMENT_REVIEW:
                if (userProfile.getFirstName() != null) {
                    text += userProfile.getFirstName() + " ";
                }
                if (userProfile.getLastName() != null) {
                    text += userProfile.getLastName();
                }
                if (text.equals("")) {
                    text = userProfile.getUsername();
                }
                break;
            case LIKE_COMMENT:
                if (userProfile.getFirstName() != null) {
                    text += userProfile.getFirstName() + " ";
                }
                if (userProfile.getLastName() != null) {
                    text += userProfile.getLastName();
                }
                if (text.equals("")) {
                    text = userProfile.getUsername();
                }
                break;
            case REPLIED:
                if (userProfile.getFirstName() != null) {
                    text += userProfile.getFirstName() + " ";
                }
                if (userProfile.getLastName() != null) {
                    text += userProfile.getLastName();
                }
                if (text.equals("")) {
                    text = userProfile.getUsername();
                }
                break;

            case ACHIEVEMENT:
                text = responseAchievement.getTitle();
                break;
            case ANNOUNCEMENTS:
                text = fromName;
                break;
            case ADVERTSING:
                text = fromName;
                break;
            case ACCEPTED_BRAND:
                text = brand.getTitle();
                break;
            case REJECTED_BRAND:
                text = brand.getTitle();
                break;
        }

        return text;
    }

    public SpannableString createText() {
        TypeNatifications typeNatifications = getEnum();
        String text = "";
        int positionStartString = 0;
        int positionBoldEndString = 0;
        switch (typeNatifications) {
            case FOLLOW:
                text = BrandBeatApplication.getInstance().getString(typeNatifications.textResource);
                break;
            case LIKE_REVIEW:
                text = BrandBeatApplication.getInstance().getString(typeNatifications.textResource);
                break;
            case COMMENT_REVIEW:
                text = BrandBeatApplication.getInstance().getString(typeNatifications.textResource);
                break;
            case LIKE_COMMENT:
                text = BrandBeatApplication.getInstance().getString(typeNatifications.textResource);
                break;
            case REPLIED:
                text = BrandBeatApplication.getInstance().getString(typeNatifications.textResource);
                break;

            case ACHIEVEMENT:
                text = BrandBeatApplication.getInstance().getString(typeNatifications.textResource) + " " +
                        responseAchievement.getTitle();
                break;
            case ANNOUNCEMENTS:
                if (responseAchievement != null && responseAchievement.getTitle() != null)
                    text = BrandBeatApplication.getInstance().getString(typeNatifications.textResource) + " " +
                            responseAchievement.getTitle();

                String tempString1 = BrandBeatApplication.getInstance().getString(typeNatifications.textResource);
                if (responseAchievement != null && responseAchievement.getTitle() != null)
                    text = tempString1 + " " + responseAchievement.getTitle();
                positionStartString = tempString1.length();
                positionBoldEndString = text.length();
                if(text.equals(""))
                    text = this.text;

                break;
            case ADVERTSING:
                String tempString = BrandBeatApplication.getInstance().getString(typeNatifications.textResource);
                if (responseAchievement != null && responseAchievement.getTitle() != null)
                    text = tempString + " " + responseAchievement.getTitle();
                positionStartString = tempString.length();
                positionBoldEndString = text.length();
                break;
            case ACCEPTED_BRAND:
                if (responseAchievement != null && responseAchievement.getTitle() != null) {
                    text = responseAchievement.getTitle() + " " + BrandBeatApplication.getInstance().getString(typeNatifications.textResource);
                    positionBoldEndString = responseAchievement.getTitle().length();
                }
                else {
                    text = BrandBeatApplication.getInstance().getString(typeNatifications.textResource);
                }
                break;
            case REJECTED_BRAND:
                if (responseAchievement != null && responseAchievement.getTitle() != null) {
                    text = responseAchievement.getTitle() + " " + BrandBeatApplication.getInstance().getString(typeNatifications.textResource);
                    positionBoldEndString = responseAchievement.getTitle().length();
                }
                else {
                    text = BrandBeatApplication.getInstance().getString(typeNatifications.textResource);
                }
                break;
        }
        SpannableString spannableString = new SpannableString(text);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        if (positionStartString < positionBoldEndString)
            spannableString.setSpan(boldSpan, positionStartString, positionBoldEndString, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public TypeNatifications getEnum() {
        return TypeNatifications.valueByType(Integer.parseInt(type));
    }

    public String getPhotoUrl(Context context) {
        if (image != null && !image.equals(""))
            return image;
        else if (userProfile != null && userProfile.getPhoto(PhotoSize.SIZE_SMALL , context) != null && !userProfile.getPhoto(PhotoSize.SIZE_SMALL , context).equals(""))
            return userProfile.getPhoto(PhotoSize.SIZE_SMALL , context);
        else if (brand != null && brand.getImage(PhotoSize.SIZE_SMALL, context) != null && !brand.getImage(PhotoSize.SIZE_SMALL, context).equals(""))
            return brand.getImage(PhotoSize.SIZE_SMALL, context);
        else if (responseAchievement != null && responseAchievement.getImage(PhotoSize.SIZE_SMALL , context) != null && !responseAchievement.getImage(PhotoSize.SIZE_SMALL , context).equals(""))
            return responseAchievement.getImage(PhotoSize.SIZE_SMALL , context);
        else
            return null;
    }
}