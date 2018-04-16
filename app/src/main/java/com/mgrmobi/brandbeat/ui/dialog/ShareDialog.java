package com.mgrmobi.brandbeat.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.utils.Utils;
import com.mgrmobi.sdk.social.android.AndroidBaseSocialNetwork;
import com.mgrmobi.sdk.social.base.SocialAuthorizationListener;
import com.mgrmobi.sdk.social.base.SocialCancelReason;
import com.mgrmobi.sdk.social.base.SocialNetwork;
import com.mgrmobi.sdk.social.base.SocialNetworkManager;
import com.mgrmobi.sdk.social.base.SocialShareListener;
import com.mgrmobi.sdk.social.base.SocialType;
import com.mgrmobi.sdk.social.facebook.MyFacebookSocial;
import com.mgrmobi.sdk.social.googleplus.GooglePlusSocialNetwork;
import com.mgrmobi.sdk.social.googleplus.utils.MGooglePlusUtils;
import com.mgrmobi.sdk.social.linkedin.LinkedinSocialNetwork;
import com.mgrmobi.sdk.social.twitter.TwitterSocialNetwork;
import com.mgrmobi.sdk.social.twitter.utils.TwitterUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ShareDialog  extends Dialog {
    public ShareDialog(final Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_dialog);
        ButterKnife.bind(this, this);
    }
    AppCompatActivity activity;
    String text;
    String imageUrl;
    String url;
    public void initDialog(AppCompatActivity activity, String text, String imageUrl, String url)
    {
        this.activity = activity;
        this.text = text;
        this.imageUrl = imageUrl;
        this.url = url;
    }
    CustomProgressDialog progressDialog;
    AndroidBaseSocialNetwork socialNetwork;
    public ShareDialog(final Context context, final int themeResId) {
        super(context, themeResId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_dialog);
        ButterKnife.bind(this, this);
    }

    protected ShareDialog(final Context context, final boolean cancelable, final OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_dialog);
        ButterKnife.bind(this, this);
    }
    @OnClick(R.id.facebook_button)
    protected void onFacebookClick() {
        if (SocialNetworkManager.isRegistered(SocialType.FACEBOOK)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.FACEBOOK);
            share();
        }
        else {
            socialNetwork = new MyFacebookSocial.Builder()
                    .setContext(getContext())
                    .setPermission(new String[]{"public_profile", "email", "user_friends"})
                    .setPublishPermission(new String[]{"publish_actions"})
                    .create();
        }
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
    }

    @OnClick(R.id.twitter_button)
    protected void onTwitterClick() {
        if (SocialNetworkManager.isRegistered(SocialType.TWITTER)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.TWITTER);
        }
        else {
            socialNetwork = new TwitterSocialNetwork.Builder()
                    .setContext(getContext()).create();
        }
        share();
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
        //    socialNetwork.login(getActivity());
    }

    @OnClick(R.id.google_plus_button)
    protected void onGooglePlusClick() {
        if (SocialNetworkManager.isRegistered(SocialType.GOOGLE_PLUS)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.GOOGLE_PLUS);
        }
        socialNetwork = new GooglePlusSocialNetwork.Builder()
                .setContext(getContext())
                .create();
        share();
        //    socialNetwork.setAuthorizationListener(socialAuthorizationListener);
//        socialNetwork.login(getActivity());
    }

    @OnClick(R.id.linked_in_button)
    protected void onLinkedInClick() {
        if (SocialNetworkManager.isRegistered(SocialType.LINKEDIN_WEB)) {
            socialNetwork = (LinkedinSocialNetwork) SocialNetworkManager.getNetwork(SocialType.LINKEDIN_WEB);
        }
        socialNetwork = new LinkedinSocialNetwork.Builder()
                .setContext(getContext())
                .setConfig(getContext().getString(R.string.linked_in_client_id), getContext().getString(R.string.linked_in_client_secret),
                        getContext().getString(R.string.linked_in_redirect_url), getContext().getResources().getStringArray(R.array.linked_in_params))
                .create();
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
        socialNetwork.login((Activity) getContext());
    }

    private SocialAuthorizationListener socialAuthorizationListener = new SocialAuthorizationListener() {
        @Override
        public void onAuthorizationSuccess(SocialNetwork network) {
            if (SocialType.FACEBOOK == network.getSocialType()) {
                if (!((MyFacebookSocial) network).checkPermissions()) {
                    ((MyFacebookSocial) network).login(activity);
                }
                ((MyFacebookSocial) network).profile(true);
            }
            share();
        }

        @Override
        public void onAuthorizationFail(SocialNetwork network, Throwable throwable) {
            if (socialNetwork.getSocialType() == SocialType.LINKEDIN_WEB) {
            }
            return;
        }

        @Override
        public void onAuthorizationCancel(SocialNetwork network, SocialCancelReason reason) {
            return;
        }
    };

    private void share() {
        if (socialNetwork.getSocialType() == SocialType.LINKEDIN_WEB) {
            progressDialog = new CustomProgressDialog(getContext());
            try {
                progressDialog.show(activity.getSupportFragmentManager(), this.getClass().getName());
            }
            catch (IllegalStateException e)
            {}
        }
        if (socialNetwork == null) {
            return;
        }
        if (socialNetwork.getSocialType() == SocialType.TWITTER) {
            if (!TwitterUtils.isTwitterInstalled(getContext())) {
                TwitterUtils.openMarket(getContext());
                return;
            }
        }

        socialNetwork.share(activity, getContext().getString(R.string.app_name), text, imageUrl, null, new SocialShareListener() {
            @Override
            public void onShareSuccess(SocialNetwork network, String postId) {
                if (socialNetwork.getSocialType() == SocialType.LINKEDIN_WEB) {
                    progressDialog.dismiss();
                }
                Utils.showAlertDialog(getContext(), (dialog, which) -> {}, (dialog1, which1) -> {}, "", getContext().getString(R.string.success), true, false);
                return;
            }

            @Override
            public void onShareCancel(SocialNetwork network, SocialCancelReason reason) {
                if (network.getSocialType() == SocialType.GOOGLE_PLUS) {
                    if (!MGooglePlusUtils.isGooglePlusAppInstalled(activity)) {
                        MGooglePlusUtils.openMarket(getContext());
                    }
                }
                if (socialNetwork.getSocialType() == SocialType.LINKEDIN_WEB) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onShareFail(SocialNetwork network, Throwable e) {
                socialNetwork.logout();
                socialNetwork.login(activity);
                if (socialNetwork.getSocialType() == SocialType.LINKEDIN_WEB) {
                    progressDialog.dismiss();
                }

                return;
            }
        });
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        socialNetwork.onActivityResult(requestCode, resultCode, data);
    }
}
