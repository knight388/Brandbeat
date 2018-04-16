package com.mgrmobi.sdk.social.base;

/**
 * @author Valentin S. Bolkonsky.
 */
public interface OAuthListener<T> {

    void onSuccess(T session);

    void onFailed(Throwable throwable);
}
