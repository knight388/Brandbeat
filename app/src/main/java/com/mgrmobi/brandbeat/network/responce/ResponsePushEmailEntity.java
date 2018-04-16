package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponsePushEmailEntity
{
    @SerializedName("push")
    private boolean push;

    @SerializedName("email")
    private boolean email;

    public boolean isPush() {
        return push;
    }

    public void setPush(final boolean push) {
        this.push = push;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(final boolean email) {
        this.email = email;
    }
}
