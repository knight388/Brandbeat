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
public class UseCaseSubCategory extends UseCase {

    private String id;
    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.getSubCategories(id);
    }
}
