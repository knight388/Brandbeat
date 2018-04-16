package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseFeed;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerMyFeed extends ContainerProgress
{
    void getMyFeed(ArrayList<ResponseFeed> responseFeed);

    void suggestedBrand(ArrayList<ResponseBrand> responseBrands);
}
