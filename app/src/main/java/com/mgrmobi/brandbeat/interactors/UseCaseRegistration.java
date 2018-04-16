package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.request.MetaRequest;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.network.request.RequestRegistration;
import com.mgrmobi.brandbeat.network.responce.ResponseRegistration;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseRegistration extends UseCase {

    private RequestRegistration requestRegistration;

    public void setRequest(String name, String email, String password, RequestLocation location, int gender, String dob, MetaRequest metaRequest) {
        requestRegistration = new RequestRegistration(name, email, password, location, gender, dob, metaRequest);
    }

    public void setRequest(String name, String email, String password, RequestLocation location, int gender, MetaRequest metaRequest) {
        requestRegistration = new RequestRegistration(name, email, password, location, gender, null, metaRequest);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<ResponseRegistration> objectObservable = restApiBean.register(requestRegistration);
        objectObservable.map(new Func1<Object, ResponseRegistration>() {
            @Override
            public ResponseRegistration call(final Object o) {
                return null;
            }

        });
        return objectObservable;
    }
}
