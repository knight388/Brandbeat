package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseDeleteSubCategory;
import com.mgrmobi.brandbeat.interactors.UseCaseFollowBrand;
import com.mgrmobi.brandbeat.interactors.UseCaseSetSubCategories;
import com.mgrmobi.brandbeat.interactors.UseCaseSubCategory;
import com.mgrmobi.brandbeat.interactors.UseCaseUnFollowBrand;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerSubCategory;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterSubCategory extends BasePresenter {

    private UseCaseSubCategory useCaseSubCategory = new UseCaseSubCategory();
    private UseCaseSetSubCategories useCaseSetSubCategories = new UseCaseSetSubCategories();
    private UseCaseDeleteSubCategory useCaseDeleteSubCategory = new UseCaseDeleteSubCategory();
    private UseCaseFollowBrand useCaseFollowBrand = new UseCaseFollowBrand();
    private UseCaseUnFollowBrand useCaseUnFollowBrand = new UseCaseUnFollowBrand();
    private ContainerSubCategory containerSubCategory;
    private ArrayList<ResponseCategories> responseCategories;
    public boolean enterAdd = false;
    public boolean enterDelete = false;

    private boolean enterAddLood = false;
    private boolean isEnterDeleteLoad = false;

    public void setView(ContainerSubCategory view) {
        containerSubCategory = view;
    }

    public void getSubCategory(String categoryId) {
        useCaseSubCategory.setId(categoryId);
        useCaseSubCategory.execute(this);
    }

    public void setSetSubCategories(ArrayList<String> setSubCategories) {
        enterAddLood = true;
        useCaseSetSubCategories.setIds(setSubCategories);
        useCaseSetSubCategories.execute(this);
    }

    public void delete(ArrayList<String> ids) {
        isEnterDeleteLoad = true;
        useCaseDeleteSubCategory.setId(ids);
        useCaseDeleteSubCategory.execute(this);
    }

    public void unFollowBrand(String brandId) {
        useCaseUnFollowBrand.setId(brandId);
        useCaseUnFollowBrand.execute(this);
    }

    public void followBrand(String brandId) {
        useCaseFollowBrand.setRequest(brandId);
        useCaseFollowBrand.execute(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        useCaseSubCategory.unsubscribe();
        useCaseSetSubCategories.unsubscribe();
        useCaseDeleteSubCategory.unsubscribe();
    }

    @Override
    public void onCompleted() {
        if (enterAddLood)
            enterAdd = true;
        if (isEnterDeleteLoad)
            enterDelete = true;
        if (enterAdd && enterDelete)
            containerSubCategory.setCategories();
        if (responseCategories != null)
            containerSubCategory.getSubCategory(responseCategories);
    }


    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerSubCategory.setCategories();
    }

    @Override
    public void onNext(Object t) {
        ArrayList<ResponseCategories> requestReviews = ((ArrayList<ResponseCategories>) ((BaseResponse) t).getData());
        if (requestReviews != null && requestReviews.size() > 0) {
            this.responseCategories = requestReviews;
        }
    }
}
