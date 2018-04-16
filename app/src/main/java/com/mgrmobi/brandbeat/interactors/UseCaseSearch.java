package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseSearch extends UseCase {

    private String searchString;

    private String categoryId;

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void setSearchCategoryId(String subcategoryId)
    {
        categoryId = subcategoryId;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.getSearchBrands(searchString, categoryId);
    }
}
