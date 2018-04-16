package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class BaseResponse<T> {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private T data;

    @SerializedName("code")
    private long code;

    @SerializedName("errors")
    private ResponseErrors responseErrors;


    public ResponseErrors getResponseErrors() {
        return responseErrors;
    }

    public void setResponseErrors(final ResponseErrors responseErrors) {
        this.responseErrors = responseErrors;
    }

    public boolean isSuccess() {
        return success;
    }

    public long getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public static Object getData(final ResponseLogin responseLogin) {
        return responseLogin;
    }
}
