package com.mgrmobi.sdk.social.base;

/**
 * @author Valentin S. Bolkonsky.
 */
public interface SocialCallback<T> {

    void onSuccess(T result);

    void onCancel();

    void onFailed(Throwable throwable);
}
