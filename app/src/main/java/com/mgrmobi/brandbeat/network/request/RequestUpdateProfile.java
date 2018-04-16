package com.mgrmobi.brandbeat.network.request;

import com.google.gson.annotations.SerializedName;
import com.mgrmobi.brandbeat.network.responce.ResponseAddress;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RequestUpdateProfile
{
    @SerializedName("username")
    private String userName;
    @SerializedName("email")
    private String email;
    @SerializedName("gender")
    private String gender;
    @SerializedName("dob")
    private String dob;
    @SerializedName("phone")
    private String phone;
    @SerializedName("photo")
    private String photo;
    @SerializedName("incomeRange")
    private String income;
    @SerializedName("location")
    private ResponseAddress requestLocation;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("meta")
    MetaRequest metaRequest;

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

    public ResponseAddress getRequestLocation() {
        return requestLocation;
    }

    public void setRequestLocation(final ResponseAddress requestLocation) {
        this.requestLocation = requestLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
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

    public void setGender(final String gender) {
        this.gender = gender;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(final String photo) {
        this.photo = photo;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(final String income) {
        this.income = income;
    }

    public MetaRequest getMetaRequest() {
        return metaRequest;
    }

    public void setMetaRequest(final MetaRequest metaRequest) {
        this.metaRequest = metaRequest;
    }
}
