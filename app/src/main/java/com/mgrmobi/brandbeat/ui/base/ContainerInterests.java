package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseCategories;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerInterests
{
    void getInterest(ArrayList<ResponseCategories> interests);

    void onComplete();

    void hideProgress();

    void showProgress();
}
