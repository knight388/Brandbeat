package com.mgrmobi.brandbeat.presenter;

import com.google.gson.internal.LinkedTreeMap;
import com.mgrmobi.brandbeat.interactors.UseCaseGetBrandsInSubactegory;
import com.mgrmobi.brandbeat.interactors.UseCaseGetSubCategory;
import com.mgrmobi.brandbeat.interactors.UseCaseSubCategory;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.mgrmobi.brandbeat.network.responce.ResponseSubCategotry;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerGetBrandsInSubCategory;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterGetSubCategory extends BasePresenter {

    private ResponseSubCategotry createBrandComplete;
    private UseCaseGetBrandsInSubactegory useCaseGetSubCategory = new UseCaseGetBrandsInSubactegory();
    private ContainerGetBrandsInSubCategory container;

    public void setView(ContainerGetBrandsInSubCategory view) {
        container = view;
    }

    public void getSubCategory(String subCategoryID) {
        useCaseGetSubCategory.setSubcategory(subCategoryID);
        useCaseGetSubCategory.execute(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }


    @Override
    public void onCompleted() {
        container.setSubCategory(createBrandComplete);
    }

    public boolean isShowNetworkError = false;

    @Override
    public void onError(Throwable e) {
        super.onError(e);
    }

    @Override
    public void onNext(Object t) {
        if (((BaseResponse) t).getData() instanceof ResponseSubCategotry) {
            createBrandComplete = (ResponseSubCategotry) ((BaseResponse) t).getData();
        }
    }
}

