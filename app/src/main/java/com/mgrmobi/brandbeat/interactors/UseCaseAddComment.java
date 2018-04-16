package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseAddComment extends UseCase {
    private String reviewId;
    private String comment;

    public void setRequest(String reviewId, String comment)
    {
        this.reviewId = reviewId;
        this.comment = comment;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.addComment(reviewId, comment);
    }
}
