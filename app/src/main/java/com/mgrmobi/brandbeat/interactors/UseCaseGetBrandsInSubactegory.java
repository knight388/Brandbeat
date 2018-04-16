package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseSubCategotry;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseGetBrandsInSubactegory extends UseCase
{
    private String subcategoryId;

    public void setSubcategory(String subcategoryId)
    {
        this.subcategoryId = subcategoryId;
    }

    @Override
    protected Observable<BaseResponse<ResponseSubCategotry>> buildUseCaseObservable() {
        return restApiBean.getSubactegories(subcategoryId);
    }
}
