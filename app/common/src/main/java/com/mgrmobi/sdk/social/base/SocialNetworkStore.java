package com.mgrmobi.sdk.social.base;

/**
 * @author Valentin S. Bolkonsky.
 */
public interface SocialNetworkStore<T> {

    /**
     * @param session
     */
    void storeSession(T session);

    /**
     * @return
     */
    T restoreSession();


    /**
     *
     */
    void resetSession();
}
