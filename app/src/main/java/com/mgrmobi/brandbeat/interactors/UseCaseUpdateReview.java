package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.request.RequestReview;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseUpdateReview extends UseCase {
    private RequestReview requestReview;
    public void setRequestReview(RequestReview requestReview)
    {
        this.requestReview = requestReview;
    }
    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse> objectObservable = restApiBean.updateReview(requestReview);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
