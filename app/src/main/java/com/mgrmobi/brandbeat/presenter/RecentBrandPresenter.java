package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseRecentBrand;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerRecentBrands;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RecentBrandPresenter  extends BasePresenter {

    private UseCaseRecentBrand useCaseRecentBrand = new UseCaseRecentBrand();
    private ContainerRecentBrands containerRecentBrands;

    public void setView(ContainerRecentBrands view)
    {
        containerRecentBrands = view;
    }

    public void getRecentBrands()
    {
        useCaseRecentBrand.execute(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        useCaseRecentBrand.unsubscribe();
    }


    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerRecentBrands.showError(e);
    }

    @Override
    public void onNext(Object t) {
        if(((BaseResponse) t).getData() instanceof ArrayList)
        {
            containerRecentBrands.getBrands((ArrayList<ResponseBrand>) ((BaseResponse) t).getData());
        }
    }
}
