package com.mgrmobi.sdk.social.base;

/**
 * @author Valentin S. Bolkonsky.
 */
public interface SocialAuthorizationListener {

    /**
     * Authorization successful.
     * @param network
     */
    void onAuthorizationSuccess(final SocialNetwork network);

    /**
     * Authorisation Error
     * @param network
     * @param throwable
     */
    void onAuthorizationFail(final SocialNetwork network, final Throwable throwable);

    /**
     * Authorization cancel
     * @param network
     * @param reason
     */
    void onAuthorizationCancel(final SocialNetwork network, final SocialCancelReason reason);
}
