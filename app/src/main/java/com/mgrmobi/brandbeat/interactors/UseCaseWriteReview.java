package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.request.RequestReview;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseWriteReview extends UseCase {

    private RequestReview requestReview;
    public void setReview(RequestReview review)
    {
        requestReview = review;
    }
    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse> objectObservable = restApiBean.sendReview(requestReview);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
