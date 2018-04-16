package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseBrand;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerSearchView extends ContainerProgress {

    public void searchResult(ArrayList<ResponseBrand> responseBrands);
}
