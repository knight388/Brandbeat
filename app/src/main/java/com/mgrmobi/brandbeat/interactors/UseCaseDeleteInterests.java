package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import java.util.List;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseDeleteInterests extends UseCase
{
    private List<String> deleteId;
    public void setDeleteId(List<String> deleteId)
    {
        this.deleteId = deleteId;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse> objectObservable = restApiBean.deleteCategories(deleteId);
        objectObservable.map(response -> response);
        return objectObservable;
    }
}
