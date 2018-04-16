package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseLocalFeed;

import java.util.List;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseLocalFeed extends UseCase {
    private String lat;
    private String lng;
    private String page;
    public void setlocation(String lat, String lng, String page)
    {
        this.lat = lat;
        this.lng = lng;
        this.page = page;
    }
    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse<List<ResponseLocalFeed>>> objectObservable = restApiBean.getLocalFeed(lat, lng, page);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
