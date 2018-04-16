package com.mgrmobi.brandbeat.network;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RequestProfile {
    private String id;

    public RequestProfile(String mId) {
        id = mId;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
