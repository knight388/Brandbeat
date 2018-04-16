package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseRestorePssword extends UseCase {
    private String email;

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse> objectObservable = restApiBean.restorePassword(email);
        objectObservable.map(responce -> responce);
        return objectObservable;
    }
}
