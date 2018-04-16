package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */

public class ResponseLogin implements Serializable
{
    @SerializedName("login")
    private String userId;

    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("data")
    private ResponseLogin responseLogin;

    @SerializedName("accessTokenExpire")
    private String accessTokenExpire;

    @SerializedName("authInfo")
    private ResponseAuthInfo responseAuthInfo;

    @SerializedName("status")
    private boolean isNew;

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(final boolean isNew) {
        this.isNew = isNew;
    }

    public String getAccessTokenExpire() {
        return accessTokenExpire;
    }

    public void setAccessTokenExpire(final String accessTokenExpire) {
        this.accessTokenExpire = accessTokenExpire;
    }

    public ResponseAuthInfo getResponseAuthInfo() {
        return responseAuthInfo;
    }

    public void setResponseAuthInfo(final ResponseAuthInfo responseAuthInfo) {
        this.responseAuthInfo = responseAuthInfo;
    }

    public ResponseLogin getResponseLogin() {
        return responseLogin;
    }

    public void setResponseLogin(final ResponseLogin responseLogin) {
        this.responseLogin = responseLogin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
