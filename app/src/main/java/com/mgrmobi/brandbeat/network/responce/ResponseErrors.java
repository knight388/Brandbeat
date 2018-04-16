package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseErrors implements Serializable
{
    @SerializedName("errorMessage")
    private String error;
    @SerializedName("errorCode")
    private String errorCode;
    @SerializedName("errorField")
    private String errorField;

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorField() {
        return errorField;
    }

    public void setErrorField(final String errorField) {
        this.errorField = errorField;
    }
}
