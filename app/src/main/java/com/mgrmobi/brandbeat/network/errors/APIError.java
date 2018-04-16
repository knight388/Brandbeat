package com.mgrmobi.brandbeat.network.errors;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class APIError implements Serializable
{
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;
    @SerializedName("errors")
    private ArrayList<ErrorsUser> errorsUser;

    public ArrayList<ErrorsUser> getErrorsUser() {
        return errorsUser;
    }

    public void setErrorsUser(final ArrayList<ErrorsUser> errorsUser) {
        this.errorsUser = errorsUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getCode() {

        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }
}
