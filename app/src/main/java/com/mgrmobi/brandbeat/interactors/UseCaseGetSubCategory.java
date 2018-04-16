package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;

import java.util.List;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseGetSubCategory extends UseCase
{
    private String id;
    private String page;
    public void setIdSubCategory(String subCategory, String page)
    {
        id = subCategory;
        this.page = page;
    }
    public void setNext(String page)
    {
        this.page = page;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.getSubCategoryBrands(id, page);
    }
}
