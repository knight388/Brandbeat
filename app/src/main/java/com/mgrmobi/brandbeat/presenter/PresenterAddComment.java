package com.mgrmobi.brandbeat.presenter;

import com.google.gson.internal.LinkedTreeMap;
import com.mgrmobi.brandbeat.interactors.UseCaseCommentEdit;
import com.mgrmobi.brandbeat.interactors.UseCaseAddComment;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerAddComment;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterAddComment extends BasePresenter {
    private ContainerAddComment containerAddComment;
    private UseCaseAddComment addComment = new UseCaseAddComment();
    private UseCaseCommentEdit useCaseEditComment = new UseCaseCommentEdit();
    private boolean createCommentComplete = false;
    private boolean editCommentComplete = false;

    public void setView(ContainerAddComment containerAddComment) {
        this.containerAddComment = containerAddComment;
    }

    public void addComment(String reviewId, String text) {
        addComment.setRequest(reviewId, text);
        addComment.execute(this);
        containerAddComment.showProgress();
    }

    public void editComment(String commentId, String text) {
        useCaseEditComment.setComment(commentId, text);
        useCaseEditComment.execute(this);
        containerAddComment.showProgress();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseEditComment.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseEditComment.unsubscribe();
    }

    @Override
    public void onCompleted() {
        if(createCommentComplete) {
            containerAddComment.onSuccess();
            return;
        }
        if(editCommentComplete) {
            containerAddComment.editSuccess();
            return;
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerAddComment.hideProgress();
        containerAddComment.showError(e);
    }

    @Override
    public void onNext(Object t) {
        containerAddComment.hideProgress();
        if(((BaseResponse) t).getData() instanceof LinkedTreeMap) {
            createCommentComplete = true;
        }
        if(((BaseResponse) t).getData() instanceof String) {
            editCommentComplete = true;
        }
    }
}
