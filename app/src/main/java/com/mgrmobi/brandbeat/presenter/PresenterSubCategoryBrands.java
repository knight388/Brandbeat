package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseFollowBrand;
import com.mgrmobi.brandbeat.interactors.UseCaseGetSubCategory;
import com.mgrmobi.brandbeat.interactors.UseCaseSearch;
import com.mgrmobi.brandbeat.interactors.UseCaseUnFollowBrand;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerRecentBrands;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterSubCategoryBrands extends BasePresenter {

    private UseCaseGetSubCategory useCaseGetSubCategory = new UseCaseGetSubCategory();
    private UseCaseSearch useCaseSearch = new UseCaseSearch();
    private UseCaseFollowBrand useCaseFollowBrand = new UseCaseFollowBrand();
    private UseCaseUnFollowBrand useCaseUnFollowBrand = new UseCaseUnFollowBrand();
    private ContainerRecentBrands containerRecentBrands;
    private ArrayList<ResponseBrand> responseBrands = new ArrayList<>();
    public void getSubCategoryBrand(String subCategoryId) {
        useCaseGetSubCategory.setIdSubCategory(subCategoryId, "1");
        useCaseGetSubCategory.execute(this);
    }
    public void setView(ContainerRecentBrands view)
    {
        containerRecentBrands = view;
    }

    public void getNext(String page)
    {
        useCaseGetSubCategory.setNext(page);
        useCaseGetSubCategory.execute(this);
    }

    public void getSearchResult(String searchString, String id)
    {
        useCaseSearch.setSearchString(searchString);
        useCaseSearch.setSearchCategoryId(id);
        useCaseSearch.execute(this);
    }

    public void followBrand(String brandId)
    {
        useCaseFollowBrand.setRequest(brandId);
        useCaseFollowBrand.execute(this);
    }

    public void unFollowBrand(String brandId)
    {
        useCaseUnFollowBrand.setId(brandId);
        useCaseUnFollowBrand.execute(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseFollowBrand.unsubscribe();
        useCaseUnFollowBrand.unsubscribe();
        useCaseGetSubCategory.unsubscribe();
        useCaseSearch.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseFollowBrand.unsubscribe();
        useCaseUnFollowBrand.unsubscribe();
        useCaseGetSubCategory.unsubscribe();
        useCaseSearch.unsubscribe();
    }

    @Override
    public void onCompleted() {
        containerRecentBrands.hideProgress();
        containerRecentBrands.getBrands(responseBrands);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerRecentBrands.hideProgress();
        containerRecentBrands.showError(e);
    }

    @Override
    public void onNext(Object t) {
        if(((BaseResponse) t).getData() instanceof ArrayList) {
            responseBrands = ((ArrayList<ResponseBrand>) ((BaseResponse) t).getData());
        }
    }
}
