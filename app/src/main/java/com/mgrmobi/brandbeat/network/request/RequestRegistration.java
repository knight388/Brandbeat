package com.mgrmobi.brandbeat.network.request;

import com.google.gson.annotations.SerializedName;
import com.mgrmobi.brandbeat.utils.Utils;

import java.io.Serializable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RequestRegistration implements Serializable
{
    @SerializedName("username")
    private String firstName;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("location")
    private RequestLocation location;
    @SerializedName("gender")
    private Integer gender;
    @SerializedName("dob")
    private String dob;
    @SerializedName("meta")
    private MetaRequest metaRequest;

    private RequestRegistration()
    {

    }

    public RequestRegistration(String name, String email, String password, RequestLocation location, int gender, String dob, MetaRequest metaRequest)
    {
        firstName = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.gender = gender;
        this.dob = dob;
        this.metaRequest = metaRequest;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public RequestLocation getLocation() {
        return location;
    }

    public void setLocation(final RequestLocation location) {
        this.location = location;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(final Integer gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(final String dob) {
        this.dob = dob;
    }
}
