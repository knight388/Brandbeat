package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseNotifications;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseNotification;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerNotifications;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NotificationsPresenter extends BasePresenter {

    private UseCaseNotifications useCaseNotifications = new UseCaseNotifications();
    private ContainerNotifications view;
    private int page;
    private List<ResponseNotification> resonseNatifications;
    private boolean haveNext = true;

    public void getNotifications() {
        resonseNatifications = new ArrayList<>();
        page = 1;
        haveNext = true;
        useCaseNotifications.setPage(page + "");
        useCaseNotifications.execute(this);
    }

    public void setView(ContainerNotifications view) {
        this.view = view;
    }

    public boolean getNextNatifications() {
        if (!haveNext) return haveNext;
        page++;
        useCaseNotifications.setPage(page + "");
        useCaseNotifications.execute(this);
        return true;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseNotifications.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseNotifications.unsubscribe();
    }

    @Override
    public void onCompleted() {
        view.getNotifications(resonseNatifications);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        view.hideProgress();
    }

    @Override
    public void onNext(Object t) {
        Object value = ((BaseResponse) t).getData();
        if (value instanceof Collection) {
            resonseNatifications.addAll((Collection<? extends ResponseNotification>) value);
            if (((Collection<? extends ResponseNotification>) value).size() < Utils.PAGE_SIZE) {
                haveNext = false;
            }
        }
    }
}
