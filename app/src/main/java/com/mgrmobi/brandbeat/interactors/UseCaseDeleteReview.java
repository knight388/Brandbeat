package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseDeleteReview extends UseCase{
    private String idReview;
    public void setIdReview(String idReview)
    {
        this.idReview = idReview;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse> objectObservable = restApiBean.deleteReview(idReview);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
