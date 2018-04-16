package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseBrandView extends UseCase {
    private String id;
    public void setRequest(String id)
    {
        this.id = id;
    }
    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.getBrand(id);
    }
}
