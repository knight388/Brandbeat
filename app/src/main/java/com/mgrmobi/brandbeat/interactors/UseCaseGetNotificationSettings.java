package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseNotificationSettings;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseGetNotificationSettings extends UseCase {

    @Override
    protected Observable<BaseResponse<ResponseNotificationSettings>> buildUseCaseObservable() {
        return restApiBean.getNotificationsSettings();
    }
}
