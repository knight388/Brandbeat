package com.mgrmobi.brandbeat.network.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RequestLogin
{
    @SerializedName("username")
    private String login;

    @SerializedName("password")
    private String password;

    @SerializedName("location")
    private RequestLocation location;

    @SerializedName("meta")
    private MetaRequest metaRequest;

    public MetaRequest getMetaRequest() {
        return metaRequest;
    }

    public void setMetaRequest(final MetaRequest metaRequest) {
        this.metaRequest = metaRequest;
    }

    public RequestLocation getLocation() {
        return location;
    }

    public void setLocation(final RequestLocation location) {
        this.location = location;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public RequestLogin(String login, String password, RequestLocation location, MetaRequest metaRequest)
    {
        this.login = login;
        this.password = password;
        this.location = location;
        this.metaRequest = metaRequest;
    }
}
