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
public class UseCaseGetReviewsByHashTag extends UseCase {
    private String hastag;

    public void setRequest(String hashTag) {
        hastag = hashTag;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.getReviewsByHashTag(hastag);
    }
}
