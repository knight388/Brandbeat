package com.mgrmobi.brandbeat.network.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RequestIdsCategory
{
    @SerializedName("categories")
    private ArrayList<String> strings;

    public ArrayList<String> getStrings() {
        return strings;
    }

    public void setStrings(final ArrayList<String> strings) {
        this.strings = strings;
    }
}
