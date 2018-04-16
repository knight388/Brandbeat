package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseFeed;

import java.util.List;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseMyFeed  extends UseCase {

    private int page;

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.getMyFeed(page);
    }
}
