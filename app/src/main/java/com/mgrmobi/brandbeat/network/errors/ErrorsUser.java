package com.mgrmobi.brandbeat.network.errors;

import com.google.gson.annotations.SerializedName;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ErrorsUser {
    @SerializedName("errorMessage")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
