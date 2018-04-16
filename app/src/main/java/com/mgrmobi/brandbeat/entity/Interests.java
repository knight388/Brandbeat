package com.mgrmobi.brandbeat.entity;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class Interests {
    private String id;
    private String name;
    private String icon;
    private boolean isSubscriber;

    public boolean isSubscriber() {
        return isSubscriber;
    }

    public void setIsSubscriber(final boolean isSubscriber) {
        this.isSubscriber = isSubscriber;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }
}
