package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;

import java.util.List;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseBrandReview extends UseCase {
    private String id;
    private String page;
    private String pageCount;

    public void setRequest(String id, String page, String pageCount)
    {
        this.id = id;
        this.page = page;
        this.pageCount = pageCount;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.getReviews(id, page, pageCount);
    }

}
