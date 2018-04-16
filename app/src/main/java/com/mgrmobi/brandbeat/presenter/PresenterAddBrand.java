package com.mgrmobi.brandbeat.presenter;

import android.graphics.Bitmap;

import com.google.gson.internal.LinkedTreeMap;
import com.mgrmobi.brandbeat.interactors.UseCaseCategory;
import com.mgrmobi.brandbeat.interactors.UseCaseCreateBrand;
import com.mgrmobi.brandbeat.interactors.UseCaseSubCategory;
import com.mgrmobi.brandbeat.interactors.UseCaseUploadPhotoBrand;
import com.mgrmobi.brandbeat.network.request.RequestBrand;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerAddBrend;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterAddBrand extends BasePresenter {

    private UseCaseCategory useCaseCategory = new UseCaseCategory();
    private UseCaseSubCategory useCaseSubCategory = new UseCaseSubCategory();
    private UseCaseUploadPhotoBrand useCaseUploadPhotoBrand = new UseCaseUploadPhotoBrand();
    private UseCaseCreateBrand useCaseCreateBrand = new UseCaseCreateBrand();

    private ResponsePhotoUrl responsePhotoUrl;
    private ContainerAddBrend presenterBrandView;
    private ArrayList<ResponseCategories> responseCategories;
    private ArrayList<ResponseCategories> subCategory;
    private boolean isSubCategory = false;
    private boolean createBrandComplete = false;

    public void setView(ContainerAddBrend view) {
        presenterBrandView = view;
    }

    public void uploadPhoto(Bitmap bitmap) {
        useCaseUploadPhotoBrand.setBitmap(bitmap);
        useCaseUploadPhotoBrand.execute(this);
        if(presenterBrandView != null)
            presenterBrandView.showProgress();
    }

    public void getCategories() {
        useCaseCategory.execute(this);
    }

    public void getSubCategories(String id) {
        useCaseSubCategory.setId(id);
        presenterBrandView.showProgress();
        useCaseSubCategory.execute(this);
        isSubCategory = true;
    }

    public void createBrand(RequestBrand requestBrand)
    {
        useCaseCreateBrand.setRequestBrand(requestBrand);
        useCaseCreateBrand.execute(this);
        presenterBrandView.showProgress();
    }

    @Override
    public void resume() {
        return;
    }

    @Override
    public void pause() {
        useCaseCategory.unsubscribe();
        useCaseCreateBrand.unsubscribe();
        useCaseUploadPhotoBrand.unsubscribe();
        useCaseSubCategory.unsubscribe();
        return;
    }

    @Override
    public void destroy() {
        useCaseCategory.unsubscribe();
        useCaseCreateBrand.unsubscribe();
        useCaseUploadPhotoBrand.unsubscribe();
        useCaseSubCategory.unsubscribe();
        return;
    }

    @Override
    public void onCompleted() {
        presenterBrandView.hideProgress();
        if(createBrandComplete)
        {
            presenterBrandView.createBrand();
            return;
        }
        if(responseCategories != null) {
            presenterBrandView.getCategory(responseCategories);
            responseCategories = null;
        }
        if(responsePhotoUrl != null) {
            presenterBrandView.getPhoto(responsePhotoUrl);
            responsePhotoUrl = null;
        }
        if(subCategory != null)
        {
            presenterBrandView.getSubCategory(subCategory);
            subCategory = null;
        }
    }
    public boolean isShowNetworkError = false;

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        presenterBrandView.hideProgress();
        presenterBrandView.showError(e);
    }

    @Override
    public void onNext(Object t) {
        presenterBrandView.hideProgress();
        if(((BaseResponse) t).getData() instanceof LinkedTreeMap)
        {
            createBrandComplete = true;
        }
        else
        if(t instanceof BaseResponse)
            if(((BaseResponse) t).getData() instanceof ResponsePhotoUrl) {
                responsePhotoUrl = (ResponsePhotoUrl) ((BaseResponse) t).getData();
            }
            else
            {
                if(!isSubCategory) {
                    responseCategories = (ArrayList) ((BaseResponse) t).getData();
                }
                else
                {
                    isSubCategory = false;
                    subCategory = (ArrayList) ((BaseResponse) t).getData();
                }
            }
    }
}
