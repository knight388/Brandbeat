package com.mgrmobi.brandbeat.presenter;

import com.google.gson.internal.LinkedTreeMap;
import com.mgrmobi.brandbeat.interactors.UseCaseGetNotificationSettings;
import com.mgrmobi.brandbeat.interactors.UseCaseSetNotificationsSettings;
import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseNotificationSettings;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerAddBrend;
import com.mgrmobi.brandbeat.ui.base.ContainerNotificationSettings;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterSettingsNotifications extends BasePresenter {
    private ContainerNotificationSettings view;
    private UseCaseGetNotificationSettings useCaseGetNotificationSettings = new UseCaseGetNotificationSettings();
    private UseCaseSetNotificationsSettings useCaseSetNotificationsSettings = new UseCaseSetNotificationsSettings();

    public void getSettings() {
        useCaseGetNotificationSettings.execute(this);
    }

    public void setSettings(ResponseNotificationSettings settings) {
        useCaseSetNotificationsSettings.setNotificationSettings(settings);
        useCaseSetNotificationsSettings.execute(this);
        view.showProgress();
    }

    public void setView(final ContainerNotificationSettings view) {
        this.view = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseGetNotificationSettings.unsubscribe();
        useCaseSetNotificationsSettings.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseGetNotificationSettings.unsubscribe();
        useCaseSetNotificationsSettings.unsubscribe();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);

    }

    @Override
    public void onNext(Object t) {
        if (((BaseResponse) t).getData() instanceof ResponseNotificationSettings) {
            view.setSettings((ResponseNotificationSettings) ((BaseResponse) t).getData());
        }
        if (((BaseResponse) t).getData() == null) {
            view.updateSettingsComplete();
        }
        view.hideProgress();
    }
}
