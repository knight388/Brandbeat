package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.entity.Interests;
import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseSetCategory extends UseCase {
    public ArrayList<String> ids = new ArrayList<>();
    public void setCategoriesIds(ArrayList<Interests> interestses)
    {
        ids = new ArrayList<>();
        for(Interests interes : interestses)
        {
            ids.add(interes.getId());
        }
    }
    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse> objectObservable = restApiBean.updateCategory(ids);
        objectObservable.map(response -> response);
        return objectObservable;
    }
}
