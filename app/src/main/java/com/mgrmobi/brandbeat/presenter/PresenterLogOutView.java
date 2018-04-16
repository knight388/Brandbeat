package com.mgrmobi.brandbeat.presenter;

import android.support.annotation.NonNull;

import com.mgrmobi.brandbeat.interactors.UseCaseLogOut;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerLogOut;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterLogOutView extends BasePresenter {
    private UseCaseLogOut useCaseLogOut = new UseCaseLogOut();
    private ContainerLogOut containerLogOut;
    public void setView(@NonNull final ContainerLogOut containerLogOut) {
         this.containerLogOut = containerLogOut;
    }

    public void logOut()
    {
        containerLogOut.showProgress();
        useCaseLogOut.execute(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseLogOut.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseLogOut.unsubscribe();
    }

    @Override
    public void onCompleted() {
        containerLogOut.hideProgress();
        containerLogOut.logOutSuccess();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerLogOut.hideProgress();
        containerLogOut.onError(e.getMessage());
    }

    @Override
    public void onNext(Object t) {
        containerLogOut.hideProgress();
    }
}
