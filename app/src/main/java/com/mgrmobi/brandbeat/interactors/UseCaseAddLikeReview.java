package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseAddLikeReview extends UseCase {

    private String idReview;

    public void setIdReview(String idReview) {
        this.idReview = idReview;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.addLikeReview(idReview);
    }
}
