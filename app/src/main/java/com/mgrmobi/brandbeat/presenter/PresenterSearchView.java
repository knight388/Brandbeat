package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseRecentBrand;
import com.mgrmobi.brandbeat.interactors.UseCaseSearch;
import com.mgrmobi.brandbeat.interactors.UseCaseTrendingBrand;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseComment;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerSearchView;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterSearchView extends BasePresenter {

    private ContainerSearchView view;
    private ArrayList<ResponseBrand> responseBrands;
    private UseCaseSearch useCaseSearch = new UseCaseSearch();
    private UseCaseTrendingBrand useCaseTrendingBrand = new UseCaseTrendingBrand();
    private UseCaseRecentBrand useCaseRecentBrand = new UseCaseRecentBrand();

    public void getSearchResult(String searchString) {
        useCaseSearch.setSearchString(searchString);
        useCaseSearch.execute(this);
    }

    public void getTrendingBrand() {
        useCaseTrendingBrand.execute(this);
    }

    public void getRecentBrand()
    {
        useCaseRecentBrand.execute(this);
    }

    public void setView(ContainerSearchView view) {
        this.view = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseSearch.unsubscribe();
        useCaseTrendingBrand.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseSearch.unsubscribe();
        useCaseTrendingBrand.unsubscribe();
    }


    @Override
    public void onCompleted() {
        view.hideProgress();
        view.searchResult(responseBrands);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        view.hideProgress();
        view.showError(e);
    }

    @Override
    public void onNext(Object t) {
        if(((BaseResponse) t).getData() instanceof ArrayList) {
            responseBrands = (ArrayList<ResponseBrand>) ((BaseResponse) t).getData();
        }
    }
}
