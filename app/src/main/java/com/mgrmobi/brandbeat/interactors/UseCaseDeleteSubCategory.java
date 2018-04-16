package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseDeleteSubCategory extends UseCase {
    private ArrayList<String> ids;
    public void setId(ArrayList<String> ids)
    {
        this.ids = ids;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse> objectObservable = restApiBean.deleteSubCategories(ids);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
