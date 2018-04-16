package com.mgrmobi.brandbeat.network.request;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RequestSocial {
    @SerializedName("social")
    private String social;
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("accessTokenSecret")
    private String accessTokenSecret;
    @SerializedName("username")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("gender")
    private String gender;
    @SerializedName("dob")
    private String dob;
    @SerializedName("password")
    private String password;
    @SerializedName("location")
    private RequestLocation requestLocation;
    @SerializedName("expiredAt")
    private String data;
    @SerializedName("photo")
    private String photo;

    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("meta")
    private MetaRequest metaRequest;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(final String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public RequestLocation getRequestLocation() {
        return requestLocation;
    }

    public void setRequestLocation(final RequestLocation requestLocation) {
        this.requestLocation = requestLocation;
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public MetaRequest getMetaRequest() {
        return metaRequest;
    }

    public void setMetaRequest(final MetaRequest metaRequest) {
        this.metaRequest = metaRequest;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(final String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public RequestSocial(final String name, final String email, final String password,
                         final RequestLocation location, final int gender, final Calendar dob,
                         final String social, final String accessToken) {
        this.social = social;
        this.name = name;
        this.email = email;
        this.password = password;
        if (dob != null)
            this.dob = dob.getTime().getTime() + "";
        this.requestLocation = location;
        this.gender = gender + "";
        this.accessToken = accessToken;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(final String social) {
        this.social = social;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public RequestSocial(String social, String accessToken, RequestLocation location, MetaRequest metaRequest) {
        this.social = social;
        this.accessToken = accessToken;
        this.requestLocation = location;
        this.metaRequest = metaRequest;
    }
}
