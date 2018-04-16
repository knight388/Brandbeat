package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseFeatureBrand;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerFeatureBrand;

import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterFeatureBrand extends BasePresenter {

    private ContainerFeatureBrand containerFeatureBrand;
    private List<ResponseBrand> featureBrand;
    private UseCaseFeatureBrand useCaseFeatureBrand = new UseCaseFeatureBrand();

    public void setView(ContainerFeatureBrand view) {
        containerFeatureBrand = view;
    }

    public void getFeatureBrand() {
        useCaseFeatureBrand.execute(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseFeatureBrand.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseFeatureBrand.unsubscribe();
    }

    @Override
    public void onCompleted() {
        if (featureBrand != null) {
            containerFeatureBrand.setFeatureBrand(featureBrand);
            featureBrand = null;
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerFeatureBrand.hideProgress();
        containerFeatureBrand.showError(e);
    }

    @Override
    public void onNext(Object t) {
        containerFeatureBrand.hideProgress();
        Object o = ((BaseResponse) t).getData();
        if (o instanceof List) {
            featureBrand = (List<ResponseBrand>) o;
        }
    }

}
