package com.mgrmobi.brandbeat.presenter;

import android.support.annotation.NonNull;

import com.mgrmobi.brandbeat.interactors.UseCaseFollowBrand;
import com.mgrmobi.brandbeat.interactors.UseCaseUnFollowBrand;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerFollowBrand;

import java.lang.reflect.Method;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class FollowPresenter extends BasePresenter {
    private ContainerFollowBrand containerFollowBrand;
    private UseCaseFollowBrand useCaseFollowBrand = new UseCaseFollowBrand();
    private UseCaseUnFollowBrand useCaseUnFollowBrand = new UseCaseUnFollowBrand();
    private BaseResponse baseResponce;

    public void setView(@NonNull final ContainerFollowBrand view) {
        containerFollowBrand = view;
    }

    public void getFollowBrand(String userId) {
        useCaseFollowBrand.setRequest(userId);
        useCaseFollowBrand.execute(this);
    }

    public void unFollowBrand(String brandId) {
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
    }

    @Override
    public void destroy() {
        useCaseFollowBrand.unsubscribe();
        useCaseUnFollowBrand.unsubscribe();
    }

    @Override
    public void onCompleted() {
   //     containerFollowBrand.hideProgress();
        if (baseResponce != null)
            containerFollowBrand.followReview(baseResponce);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
  //      containerFollowBrand.hideProgress();
    }

    @Override
    public void onNext(Object t) {
        baseResponce = (BaseResponse) t;
       // containerFollowBrand.hideProgress();
    }
}
