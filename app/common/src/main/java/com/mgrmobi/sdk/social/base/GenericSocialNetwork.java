package com.mgrmobi.sdk.social.base;

import com.mgrmobi.sdk.social.base.logger.SocialLogger;

/**
 * @author Valentin S. Bolkonsky.
 */
public abstract class GenericSocialNetwork implements SocialNetwork {

    private SocialLogger logger;
    private SocialAuthorizationListener authorizationListener;
    private SocialProfileListener profileListener;
    private SocialShareListener shareListener;

    public void setAuthorizationListener(SocialAuthorizationListener listener) {
        authorizationListener = listener;
    }

    @Override
    public SocialAuthorizationListener getAuthorizationListener() {
        return authorizationListener;
    }

    @Override
    public SocialProfileListener getProfileListener() {
        return profileListener;
    }

    @Override
    public void setProfileListener(SocialProfileListener profileListener) {
        this.profileListener = profileListener;
    }

    @Override
    public void setShareListener(SocialShareListener listener) {
        this.shareListener = listener;
    }

    @Override
    public SocialShareListener getShareListener() {
        return this.shareListener;
    }

    @Override
    public void setLogger(SocialLogger logger) {
        this.logger = logger;
    }

    @Override
    public void logout() {
        log(getSocialType() + ": logout");
    }

    @Override
    public void prepare() {
        SocialNetworkManager.registrationNetwork(this);
    }

    protected void log(final String message) {
        if (logger != null) {
            logger.log(message);
        }
    }

    public abstract static class Builder<T extends SocialNetwork> {

        public abstract T create();

    }

}
