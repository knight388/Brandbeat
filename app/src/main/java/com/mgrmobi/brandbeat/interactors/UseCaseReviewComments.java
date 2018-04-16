package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseComment;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseReviewComments extends UseCase {
    private String reviewId;
    public void setReviewId(String reviewId)
    {
        this.reviewId = reviewId;
    }
    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse<ArrayList<ResponseComment>>> objectObservable = restApiBean.getComments(reviewId);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
