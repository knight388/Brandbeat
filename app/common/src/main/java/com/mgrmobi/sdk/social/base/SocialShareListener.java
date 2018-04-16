package com.mgrmobi.sdk.social.base;

/**
 * @author Valentin S. Bolkonsky.
 */
public interface SocialShareListener {

    void onShareSuccess(SocialNetwork network, String postId);

    void onShareCancel(SocialNetwork network, SocialCancelReason reason);

    void onShareFail(SocialNetwork network, Throwable e);
}
