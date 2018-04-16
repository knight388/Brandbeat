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
public class ResponseProfile implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("gender")
    private String gender;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("dob")
    private String dob;
    @SerializedName("phone")
    private String phone;
    @SerializedName("photo")
    private String photo;
    //@SerializedName("location")
  //  private ResponseAddress address;
    @SerializedName("reviews")
    private ArrayList<String> reviews;
    @SerializedName("achievements")
    private ArrayList<ResponseAchievement> achievements;
    @SerializedName("reviewsSum")
    private String reviewSum;
    @SerializedName("followersSum")
    private String followerSum;
    @SerializedName("reviewsImageSum")
    private String reviewImageSum;
    @SerializedName("iSubscriber")
    private boolean isSubscriber;
    @SerializedName("address")
    private ResponseAddress responseAddress;
    @SerializedName("incomeRange")
    private String incomeRange;
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getIncomeRange() {
        return incomeRange;
    }

    public void setIncomeRange(final String incomeRange) {
        this.incomeRange = incomeRange;
    }

    public ResponseAddress getResponseAddress() {
        return responseAddress;
    }

    public void setResponseAddress(final ResponseAddress responseAddress) {
        this.responseAddress = responseAddress;
    }

    public boolean isSubscriber() {
        return isSubscriber;
    }

    public void setIsSubscriber(final boolean isSubscriber) {
        this.isSubscriber = isSubscriber;
    }

    public String getReviewImageSum() {
        return reviewImageSum;
    }

    public void setReviewImageSum(final String reviewImageSum) {
        this.reviewImageSum = reviewImageSum;
    }

    public String getFollowerSum() {
        return followerSum;
    }

    public void setFollowerSum(final String followerSum) {
        this.followerSum = followerSum;
    }

    public String getReviewSum() {
        return reviewSum;
    }

    public void setReviewSum(final String reviewSum) {
        this.reviewSum = reviewSum;
    }

    public ArrayList<ResponseAchievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(final ArrayList<ResponseAchievement> achievements) {
        this.achievements = achievements;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(final ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

/*    public ResponseAddress getAddress() {
        return address;
    }

    public void setAddress(final ResponseAddress address) {
        this.address = address;
    }*/

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(final String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getPhoto(PhotoSize photoSize, Context context) {
        if (photo == null || photo.equals("")) return photo;
        return photo + context.getString(photoSize.value);
    }

    public String getPhotoSave() {
        return photo;
    }

    public void setPhoto(final String photo) {
        this.photo = photo;
    }
}
