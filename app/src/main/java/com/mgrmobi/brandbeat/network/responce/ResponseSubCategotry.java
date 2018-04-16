package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseSubCategotry
{
    @SerializedName("brands")
    private ArrayList<String> brandsIds;

    public ArrayList<String> getBrandsIds() {
        return brandsIds;
    }

    public void setBrandsIds(final ArrayList<String> brandsIds) {
        this.brandsIds = brandsIds;
    }
}
