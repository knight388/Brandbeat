package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseUserReview extends UseCase {

    private String userId;
    private String page;

    public void setUserId(String setUserId)
    {
        userId = setUserId;
    }

    public void setPage(String page)
    {
        this.page = page;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if(page == null)
        {
            page = "1";
        }
        Observable<BaseResponse<ArrayList<ResponseReview>>> objectObservable = restApiBean.getUserReviews(userId, page);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
