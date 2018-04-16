package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerAddBrend extends ContainerProgress
{
    public void getCategory(ArrayList<ResponseCategories> responseCategories);

    public void getSubCategory(ArrayList<ResponseCategories> responseCategories);

    public void getPhoto(ResponsePhotoUrl responsePhotoUrl);

    public void createBrand();
}
