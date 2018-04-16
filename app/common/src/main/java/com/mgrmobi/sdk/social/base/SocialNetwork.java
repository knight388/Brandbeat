package com.mgrmobi.sdk.social.base;

import com.mgrmobi.sdk.social.base.logger.SocialLogger;

/**
 * @author Valentin S. Bolkonsky.
 */
public interface SocialNetwork {

    /**
     * Method of preparation for use
     */
    void prepare();

    /**
     * @return type of social type FACEBOOK, TWITTER, etc.
     */
    SocialType getSocialType();

    /**
     * Set a callback for the result of the authorization.
     *
     * @param listener
     */
    void setAuthorizationListener(final SocialAuthorizationListener listener);

    /**
     * Get a callback for the result of the authorization.
     *
     * @return
     */
    SocialAuthorizationListener getAuthorizationListener();

    void setProfileListener(final SocialProfileListener listener);

    SocialProfileListener getProfileListener();

    void setShareListener(SocialShareListener listener);

    SocialShareListener getShareListener();

    void setLogger(SocialLogger logger);

    /**
     * Clear access token.
     */
    void logout();

    /**
     * Send request for update profile
     */
    void profile();

    /**
     * @return access_token
     */
    String getAccessToken();

    /**
     * Return the user ID is registered in the network
     *
     * @return
     */
    String getUserId();
}
