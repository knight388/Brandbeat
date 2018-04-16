package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseUnFollowUser extends UseCase {

    private String id;

    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse> objectObservable = restApiBean.unFollowUser(id);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
