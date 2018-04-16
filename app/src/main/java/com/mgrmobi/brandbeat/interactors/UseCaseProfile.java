package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.RequestProfile;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseProfile extends UseCase {
    private RequestProfile request;

    public void setRequest(String id)
    {
        request = new RequestProfile(id);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse<ResponseProfile>> objectObservable = restApiBean.getProfile(request);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
