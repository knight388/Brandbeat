package com.mgrmobi.brandbeat.presenter;

import android.support.annotation.NonNull;

import com.mgrmobi.brandbeat.interactors.UseCaseAddLikeReview;
import com.mgrmobi.brandbeat.interactors.UseCaseDeleteComment;
import com.mgrmobi.brandbeat.interactors.UseCaseDeleteLike;
import com.mgrmobi.brandbeat.interactors.UseCaseDeleteReview;
import com.mgrmobi.brandbeat.interactors.UseCaseReview;
import com.mgrmobi.brandbeat.interactors.UseCaseReviewComments;
import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseComment;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerReview;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterReview extends BasePresenter {
    private ContainerReview containerReview;
    private UseCaseReview useCaseReview = new UseCaseReview();
    private UseCaseReviewComments useCaseReviewComments = new UseCaseReviewComments();
    private UseCaseDeleteReview useCaseDeleteReview = new UseCaseDeleteReview();
    private UseCaseDeleteComment useCaseDeleteComment = new UseCaseDeleteComment();
    private UseCaseAddLikeReview useCaseAddLikeReview = new UseCaseAddLikeReview();
    private UseCaseDeleteLike useCaseDeleteLike = new UseCaseDeleteLike();
    private ResponseReview requestReview;
    private ArrayList<ResponseComment> responseComments;
    private boolean isDeleteComment;
    private int positionComment = -1;
    private boolean isLikeAction = false;
    private boolean isDeleteReview = false;

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseAddLikeReview.unsubscribe();
        useCaseDeleteComment.unsubscribe();
        useCaseDeleteLike.unsubscribe();
        useCaseDeleteReview.unsubscribe();
        useCaseReview.unsubscribe();
        useCaseReviewComments.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseAddLikeReview.unsubscribe();
        useCaseDeleteComment.unsubscribe();
        useCaseDeleteLike.unsubscribe();
        useCaseDeleteReview.unsubscribe();
        useCaseReview.unsubscribe();
        useCaseReviewComments.unsubscribe();
    }

    public void deleteLike(String idReview) {
        isLikeAction = true;
        useCaseDeleteLike.setReviewId(idReview);
        useCaseDeleteLike.execute(this);
    }

    public void addLike(String idReview) {
        isLikeAction = true;
        useCaseAddLikeReview.setIdReview(idReview);
        useCaseAddLikeReview.execute(this);
    }

    public void deleteReview(String idReview) {
        isDeleteReview = true;
        useCaseDeleteReview.setIdReview(idReview);
        useCaseDeleteReview.execute(this);
    }

    public void deleteComment(String idComment, int position) {
        isDeleteComment = true;
        containerReview.showProgress();
        useCaseDeleteComment.setId(idComment);
        useCaseDeleteComment.execute(this);
        positionComment = position;
    }

    public void setView(@NonNull final ContainerReview containerReview) {
        this.containerReview = containerReview;
    }

    public void getReview(String reviewId) {
        useCaseReview.setReview(reviewId);
        containerReview.showProgress();
        useCaseReview.execute(this);
    }

    public void getReviewComments(String reviewId) {
        useCaseReviewComments.setReviewId(reviewId);
        containerReview.showProgress();
        useCaseReviewComments.execute(this);
    }

    @Override
    public void onCompleted() {
        containerReview.hideProgress();
        if (requestReview != null) {
            containerReview.getReview(requestReview);
            requestReview = null;
        }
        if (responseComments != null) {
            containerReview.getComments(responseComments);
            responseComments = null;
        }

    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerReview.hideProgress();
        containerReview.showError(e);
    }

    @Override
    public void onNext(Object t) {
        if (((BaseResponse) t).getData() instanceof ResponseReview)
            requestReview = (ResponseReview) ((BaseResponse) t).getData();
        else if (((BaseResponse) t).getData() instanceof ArrayList<?>) {
            responseComments = (ArrayList<ResponseComment>) ((BaseResponse) t).getData();
        }
        else {
            if (isLikeAction) {
                containerReview.likeAction();
            }
            if (isDeleteComment) {
                containerReview.hideProgress();
                containerReview.deleteComment(positionComment);
                isDeleteComment = false;
            }
            if (isDeleteReview) {
                containerReview.hideProgress();
                containerReview.deleteReview(null);
            }
        }
    }
}