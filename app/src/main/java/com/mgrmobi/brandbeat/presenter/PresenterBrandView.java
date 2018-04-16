package com.mgrmobi.brandbeat.presenter;

import android.support.annotation.NonNull;

import com.mgrmobi.brandbeat.interactors.UseCaseBrandReview;
import com.mgrmobi.brandbeat.interactors.UseCaseBrandView;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerBrand;
import com.mgrmobi.brandbeat.ui.base.ContainerReviewInBrand;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterBrandView  extends BasePresenter {

    UseCaseBrandReview useCaseBrandReview = new UseCaseBrandReview();

    UseCaseBrandView useCaseBrandView = new UseCaseBrandView();

    private ArrayList<ResponseReview> reviews;
    private ResponseBrand responseBrand;
    private ContainerReviewInBrand containerReviewInBrand;
    private ContainerBrand containerBrand;

    public void setView(@NonNull final ContainerReviewInBrand containerProfile, ContainerBrand containerBrand) {
        this.containerReviewInBrand = containerProfile;
        this.containerBrand = containerBrand;
    }

    public void getReview(String userId)
    {
        useCaseBrandReview.setRequest(userId, 1 + "", 20 + "");
        useCaseBrandReview.execute(this);
    }

    public void getBrand(String id)
    {
        useCaseBrandView.setRequest(id);
        containerReviewInBrand.showProgress();
        useCaseBrandView.execute(this);
    }


    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseBrandReview.unsubscribe();
        useCaseBrandView.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseBrandReview.unsubscribe();
        useCaseBrandView.unsubscribe();
    }

    @Override
    public void onCompleted() {
        containerReviewInBrand.hideProgress();
        if(reviews != null) {

            reviews = null;
        }
        if(responseBrand != null) {

            responseBrand = null;
        }
    }

    @Override
    public void onError(Throwable e) {
        //containerReviewInBrand.showError(e);
        containerReviewInBrand.hideProgress();
    }

    @Override
    public void onNext(Object t) {
        if(((BaseResponse) t).getData() instanceof ArrayList<?>) {
            reviews = (ArrayList<ResponseReview>) ((BaseResponse) t).getData();
            containerReviewInBrand.getReview(reviews);
        }
        else {
            responseBrand = (ResponseBrand) ((BaseResponse) t).getData();
            containerBrand.getBrand(responseBrand);
        }

        containerReviewInBrand.hideProgress();
    }
}
