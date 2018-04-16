package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseReview extends UseCase {

    private String idReview;
    private String idBrandId;
    public void setReview(String idReview)
    {
        this.idReview = idReview;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse<ResponseReview>> objectObservable = restApiBean.getReview(idReview, idBrandId);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
