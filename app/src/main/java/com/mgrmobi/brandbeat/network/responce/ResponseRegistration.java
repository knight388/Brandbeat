package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseRegistration implements Serializable
{
    @SerializedName("data")
    private ResponseRegistration registration;
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("accessTokenExpire")
    private String accessTokenExpire;
    @SerializedName("refreshToken")
    private String refreshToken;
    @SerializedName("authInfo")
    private ResponseAuthInfo responseAuthInfo;

    public ResponseAuthInfo getResponseAuthInfo() {
        return responseAuthInfo;
    }

    public void setResponseAuthInfo(final ResponseAuthInfo responseAuthInfo) {
        this.responseAuthInfo = responseAuthInfo;
    }

    public ResponseRegistration getRegistration() {
        return registration;
    }

    public void setRegistration(final ResponseRegistration registration) {
        this.registration = registration;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenExpire() {
        return accessTokenExpire;
    }

    public void setAccessTokenExpire(final String accessTokenExpire) {
        this.accessTokenExpire = accessTokenExpire;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
