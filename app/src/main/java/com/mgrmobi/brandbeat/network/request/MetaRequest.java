package com.mgrmobi.brandbeat.network.request;

import com.google.gson.annotations.SerializedName;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class MetaRequest
{
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("versionApp")
    private String versionApp;
    @SerializedName("pushDeviceId")
    private String pushId;
    @SerializedName("deviceType")
    private String deviceType = "android";

    public MetaRequest(String deviceId, String versionApp, String pushId)
    {
        this.deviceId =deviceId;
        this.versionApp = versionApp;
        this.pushId = pushId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(final String deviceId) {
        this.deviceId = deviceId;
    }

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(final String versionApp) {
        this.versionApp = versionApp;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(final String pushId) {
        this.pushId = pushId;
    }
}
