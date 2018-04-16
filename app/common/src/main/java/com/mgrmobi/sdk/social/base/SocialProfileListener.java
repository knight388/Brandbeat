package com.mgrmobi.sdk.social.base;

/**
 * @author Valentin S. Bolkonsky.
 */
public interface SocialProfileListener {

    void onProfileSuccess(final SocialNetwork network);

    void onProfileFailed(final SocialNetwork network, final Throwable throwable);
}
