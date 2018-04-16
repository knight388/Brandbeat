package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseAddLikeReview;
import com.mgrmobi.brandbeat.interactors.UseCaseDeleteLike;
import com.mgrmobi.brandbeat.interactors.UseCaseGetReviewsByHashTag;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerHashTag;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterGetHashTag extends BasePresenter {

    private UseCaseGetReviewsByHashTag useCaseGetReviewsByHasTag = new UseCaseGetReviewsByHashTag();
    private ArrayList<ResponseReview> responseReviews;
    private ContainerHashTag containerHachTag;
    private UseCaseAddLikeReview useCaseAddLikeReview = new UseCaseAddLikeReview();
    private UseCaseDeleteLike useCaseDeleteLike = new UseCaseDeleteLike();

    public void getReviews(String s) {
        useCaseGetReviewsByHasTag.setRequest(s);
        useCaseGetReviewsByHasTag.execute(this);
    }

    public void addLike(String reviewId) {
        useCaseAddLikeReview.setIdReview(reviewId);
        useCaseAddLikeReview.execute(this);
    }

    public void disLike(String reviewId) {
        useCaseDeleteLike.setReviewId(reviewId);
        useCaseDeleteLike.execute(this);
    }

    public void setView(ContainerHashTag views) {
        containerHachTag = views;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseAddLikeReview.unsubscribe();
        useCaseDeleteLike.unsubscribe();
        useCaseGetReviewsByHasTag.unsubscribe();
    }

    @Override
    public void destroy() {
useCaseAddLikeReview.unsubscribe();
        useCaseDeleteLike.unsubscribe();
        useCaseGetReviewsByHasTag.unsubscribe();
    }

    @Override
    public void onCompleted() {

        if (responseReviews != null) {
            containerHachTag.getReviews(responseReviews);
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerHachTag.hideProgress();
        containerHachTag.showError(e);
    }

    @Override
    public void onNext(Object t) {
        if (((BaseResponse) t).getData() instanceof ArrayList) {
            responseReviews = (ArrayList<ResponseReview>) ((BaseResponse) t).getData();
        }
    }
}
