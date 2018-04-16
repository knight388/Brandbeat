package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.sdk.social.android.model.SocialUser;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerRegistration
{
    public void initViewAfterGetProfile(SocialUser socialUser);

    public void registrationWithEmail();

    public void registrationWihFaceBook();

    public void registrationWithTwitter();

    public void registrationWithLinkedIn();

    public void registrationWihGooglePlus();

    public void showError(Throwable e);

    public void showProgress();

    public void hideProgress();

    public void loginSuccess();

}
