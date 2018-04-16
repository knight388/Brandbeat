package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseInterests extends UseCase
{

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse<ArrayList<ResponseCategories>>> objectObservable = restApiBean.getCategories();
        objectObservable.map(response -> response);
        return objectObservable;
    }
}
