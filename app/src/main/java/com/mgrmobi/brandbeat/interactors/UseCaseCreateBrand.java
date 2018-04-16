package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.request.RequestBrand;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseCreateBrand extends UseCase {
    private RequestBrand requestBrand;
    public void setRequestBrand(RequestBrand requestBrand)
    {
        this.requestBrand = requestBrand;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse> objectObservable = restApiBean.createBrand(requestBrand);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
