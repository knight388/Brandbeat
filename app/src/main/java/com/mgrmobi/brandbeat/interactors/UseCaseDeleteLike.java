package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseDeleteLike extends UseCase {

    private String idReview;

    public void setReviewId(String idReview) {
        this.idReview = idReview;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.deleteLike(idReview);
    }
}
