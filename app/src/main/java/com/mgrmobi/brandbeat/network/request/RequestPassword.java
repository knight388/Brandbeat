package com.mgrmobi.brandbeat.network.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RequestPassword {
    @SerializedName("oldPassword")
    public String oldPassword;
    @SerializedName("newPassword")
    public String newPassword;
}
