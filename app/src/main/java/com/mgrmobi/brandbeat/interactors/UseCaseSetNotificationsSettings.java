package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.ResponseNotificationSettings;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseSetNotificationsSettings extends UseCase {

    private ResponseNotificationSettings responseNotificationSettings;
    public void setNotificationSettings(ResponseNotificationSettings notificationSettings)
    {
        responseNotificationSettings = notificationSettings;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.setNotificationsSettings(responseNotificationSettings);
    }
}
