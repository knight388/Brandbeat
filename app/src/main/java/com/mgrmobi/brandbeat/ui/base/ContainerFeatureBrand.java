package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseBrand;

import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerFeatureBrand extends ContainerProgress {
    void setFeatureBrand(List<ResponseBrand> responseBrands);
}
