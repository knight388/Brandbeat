package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseNotification;

import java.util.List;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseNotifications extends UseCase
{
    String page;
    public void setPage(String page)
    {
        this.page = page;
    }

    @Override
    protected Observable<BaseResponse<List<ResponseNotification>>> buildUseCaseObservable() {
        return restApiBean.getNotifications(page);
    }
}
