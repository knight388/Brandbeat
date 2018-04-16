package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseTrendingBrand;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerTrandingBrands;

import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterTrendingBrand extends BasePresenter {

    private ContainerTrandingBrands containerTrandingBrands;
    private List<ResponseBrand> trandingBrands;
    private UseCaseTrendingBrand useCaseTrendingBrand = new UseCaseTrendingBrand();


    public void setView(ContainerTrandingBrands view) {
        containerTrandingBrands = view;
    }

    public void getTrandingBrands() {
        useCaseTrendingBrand.execute(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseTrendingBrand.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseTrendingBrand.unsubscribe();
    }

    @Override
    public void onCompleted() {
        if (trandingBrands != null) {
            containerTrandingBrands.getTrandingBrands(trandingBrands);
            trandingBrands = null;
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerTrandingBrands.hideProgress();
        containerTrandingBrands.showError(e);
    }

    @Override
    public void onNext(Object t) {
        Object o = ((BaseResponse) t).getData();
        containerTrandingBrands.hideProgress();
        if (o instanceof List) {
            trandingBrands = (List<ResponseBrand>) o;
        }
    }
}
