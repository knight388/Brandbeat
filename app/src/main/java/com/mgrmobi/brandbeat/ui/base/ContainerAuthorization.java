package com.mgrmobi.brandbeat.ui.base;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerAuthorization {

    void connectWithFB();

    void connectWithLinkedIn();

    void connectWithTwitter();

    void connectWithGooglePlus();

    void connectWithEmail();

    void loginSuccess(boolean isNew);

    void profileSuccess(boolean isNew);

    void completeUpdateLocation(boolean isNew);

    public void showProgress();

    public void hideProgress();

    public void showError(Throwable e);

}
