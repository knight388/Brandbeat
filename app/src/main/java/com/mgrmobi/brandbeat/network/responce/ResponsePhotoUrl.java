package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponsePhotoUrl
{
    @SerializedName("path")
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    @SerializedName("file")
    ArrayList<ResponsePhotoUrl> responsePhotoUrls;

    public ArrayList<ResponsePhotoUrl> getResponsePhotoUrls() {
        return responsePhotoUrls;
    }

    public void setResponsePhotoUrls(final ArrayList<ResponsePhotoUrl> responsePhotoUrls) {
        this.responsePhotoUrls = responsePhotoUrls;
    }
}
