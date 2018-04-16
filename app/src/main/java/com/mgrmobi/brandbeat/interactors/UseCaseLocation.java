package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.request.RequestLocation;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseLocation extends UseCase {

    private RequestLocation location;

    public void setLocation(RequestLocation location) {
        this.location = location;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.updateLocation(location);
    }
}
