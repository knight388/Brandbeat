package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseLocalFeed;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerLocalFeed extends ContainerProgress
{
    void getLocalFeed(ArrayList<ResponseLocalFeed> responseFeed);

    void getSuggetedBrand(ArrayList<ResponseBrand> suggestedBrands);
}
