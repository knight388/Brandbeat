package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseComment implements Serializable
{
    @SerializedName("id")
    private String id;
    @SerializedName("user")
    private ResponseProfile responseUser;
    @SerializedName("text")
    private String text;
    @SerializedName("createdAt")
    private String createAt;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public ResponseProfile getResponseUser() {
        return responseUser;
    }

    public void setResponseUser(final ResponseProfile responseUser) {
        this.responseUser = responseUser;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(final String createAt) {
        this.createAt = createAt;
    }
}
