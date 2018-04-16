package com.mgrmobi.sdk.social.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.mgrmobi.sdk.social.android.AndroidBaseSocialNetwork;
import com.mgrmobi.sdk.social.android.SimpleAndroidSocialNetwork;
import com.mgrmobi.sdk.social.android.model.SocialUser;
import com.mgrmobi.sdk.social.base.SocialCancelReason;
import com.mgrmobi.sdk.social.base.SocialNetwork;
import com.mgrmobi.sdk.social.base.SocialShareListener;
import com.mgrmobi.sdk.social.base.SocialType;
import com.mgrmobi.sdk.social.base.model.ShareMediaFile;
import com.mgrmobi.sdk.social.facebook.utils.FacebookUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class MyFacebookSocial extends SimpleAndroidSocialNetwork {

    private String[] permissions;
    private String[] publishPermissions;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    private MyFacebookSocial() {
    }

    public boolean checkPermissions() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            Set approvedPermissions = accessToken.getPermissions();
            String[] var3;
            int var4;
            int var5;
            String permission;
            if (this.publishPermissions != null) {
                var3 = this.publishPermissions;
                var4 = var3.length;

                for(var5 = 0; var5 < var4; ++var5) {
                    permission = var3[var5];
                    if (!approvedPermissions.contains(permission)) {
                        return false;
                    }
                }
            }

            var3 = this.permissions;
            var4 = var3.length;

            for(var5 = 0; var5 < var4; ++var5) {
                permission = var3[var5];
                if (!approvedPermissions.contains(permission)) {
                    return false;
                }
            }

            return true;
        }
        else {
            return false;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (this.callbackManager != null) {
            this.callbackManager.onActivityResult(requestCode, resultCode, intent);
        }

    }

    public void prepare() {
        FacebookSdk.sdkInitialize(this.getContext());
        this.callbackManager = CallbackManager.Factory.create();
        super.prepare();
    }

    public String getAccessToken() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token != null ? token.getToken() : null;
    }

    public String getUserId() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token != null ? token.getUserId() : null;
    }

    public void logout() {
        super.logout();
        if (this.profileTracker != null && this.profileTracker.isTracking()) {
            this.profileTracker.stopTracking();
        }

        this.setProfile((SocialUser) null);
        LoginManager.getInstance().logOut();
    }

    public void login(Activity activity) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            Set permissions = accessToken.getPermissions();
            if (this.publishPermissions == null) {
                if (this.getAuthorizationListener() != null) {
                    this.getAuthorizationListener().onAuthorizationSuccess(this);
                }

                Profile.fetchProfileForCurrentAccessToken();
            }
            else {
                ArrayList need = new ArrayList();
                String[] var5 = this.publishPermissions;
                int var6 = var5.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    String permission = var5[var7];
                    if (!permissions.contains(permission)) {
                        need.add(permission);
                    }
                }

                if (!need.isEmpty()) {
                    this.registrationAuthorizationCallback();
                    LoginManager.getInstance().logInWithPublishPermissions(activity, need);
                }
                else {
                    if (this.getAuthorizationListener() != null) {
                        this.getAuthorizationListener().onAuthorizationSuccess(this);
                    }

                    Profile.fetchProfileForCurrentAccessToken();
                }

            }
        }
        else {
            this.registrationAuthorizationCallback();
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(this.permissions));
        }
    }

    public void share(Activity activity, String title, String message, String subscribe, ShareMediaFile mediaFile, final SocialShareListener listener) {
        ShareLinkContent shareContent = null;
        if (mediaFile != null) {
            if (ShareDialog.canShow(SharePhotoContent.class) && ShareMediaFile.ShareMediaType.IMAGE == mediaFile.getType()) {
                Bitmap shareDialog = BitmapFactory.decodeFile(mediaFile.getMediaPath());
                SharePhoto photo = (new com.facebook.share.model.SharePhoto.Builder()).setBitmap(shareDialog).build();
                //shareContent = (new com.facebook.share.model.SharePhotoContent.Builder()).addPhoto(photo).build();
            }
        }
        else {

        }
        if(subscribe == null) subscribe = "http://brand-beat.com";
        shareContent = new com.facebook.share.model.ShareLinkContent.Builder()
                .setContentTitle(title).setContentDescription(message).setImageUrl(Uri.parse(subscribe)).build();
        shareContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(subscribe))
                .setContentDescription(message)
                .setContentTitle(title)
                .build();
        ShareDialog shareDialog1 = new ShareDialog(activity);
        shareDialog1.registerCallback(this.callbackManager, new FacebookCallback() {

            @Override
            public void onSuccess(final Object o) {
                if (listener != null) {
                    listener.onShareSuccess(MyFacebookSocial.this, ((Sharer.Result) o).getPostId());
                }
            }

            public void onCancel() {
                if (listener != null) {
                    listener.onShareCancel(MyFacebookSocial.this, SocialCancelReason.USER_CANCEL);
                }

            }

            public void onError(FacebookException e) {
                if (listener != null) {
                    listener.onShareFail(MyFacebookSocial.this, e);
                }

            }
        });
        shareDialog1.show(shareContent, ShareDialog.Mode.AUTOMATIC);
   //     this.setShareListener(listener);
    }

    public void profile(boolean isManual) {
        if (!isManual) {
            this.profile();
        }
        else {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken != null && !accessToken.isExpired()) {
                GraphRequestBatch batch = new GraphRequestBatch(new GraphRequest[]{GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        MyFacebookSocial.this.log("jsonObject: " + jsonObject + " response: " + graphResponse);
                        MyFacebookSocial.this.setProfile(FacebookUtils.fromFacebook(jsonObject, Profile.getCurrentProfile()));
                    }
                })});
                batch.addCallback(new GraphRequestBatch.Callback() {
                    public void onBatchCompleted(GraphRequestBatch graphRequestBatch) {
                        MyFacebookSocial.this.log(" onBatchCompleted: " + graphRequestBatch);
                        if (MyFacebookSocial.this.getProfileListener() != null) {
                            MyFacebookSocial.this.getProfileListener().onProfileSuccess(MyFacebookSocial.this);
                        }

                    }
                });
                batch.executeAsync();
            }
            else {
                if (this.getProfileListener() != null) {
                    this.getProfileListener().onProfileFailed(this, new FacebookException("no access token"));
                }

            }
        }
    }

    public void profile() {
        if (this.profileTracker == null) {
            this.profileTracker = new ProfileTracker() {
                protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                    MyFacebookSocial.this.setProfile(newProfile);
                }
            };
        }
        else if (!this.profileTracker.isTracking()) {
            this.profileTracker.startTracking();
        }

        Profile.fetchProfileForCurrentAccessToken();
        this.setProfile(Profile.getCurrentProfile());
    }

    public SocialType getSocialType() {
        return SocialType.FACEBOOK;
    }

    private void setProfile(Profile profile) {
        if (profile != null) {
            this.log("currentProfile: " + profile.getProfilePictureUri(640, 640));
            this.setProfile(FacebookUtils.convert(profile));
            if (this.getProfileListener() != null) {
                this.getProfileListener().onProfileSuccess(this);
            }
        }
        else if (this.getProfileListener() != null) {
            this.getProfileListener().onProfileFailed(this, new FacebookException("profile is null"));
        }

    }

    private void registrationAuthorizationCallback() {
        LoginManager.getInstance().registerCallback(this.callbackManager, new FacebookCallback() {

            @Override
            public void onSuccess(final Object o) {
                MyFacebookSocial.this.log("onSuccess: " + o);
                if (MyFacebookSocial.this.getAuthorizationListener() != null) {
                    MyFacebookSocial.this.getAuthorizationListener().onAuthorizationSuccess(MyFacebookSocial.this);
                }

                Profile.fetchProfileForCurrentAccessToken();
            }

            public void onCancel() {
                MyFacebookSocial.this.log("onCancel");
                if (MyFacebookSocial.this.getAuthorizationListener() != null) {
                    MyFacebookSocial.this.getAuthorizationListener().onAuthorizationCancel(MyFacebookSocial.this, SocialCancelReason.USER_CANCEL);
                }

            }

            public void onError(FacebookException e) {
                MyFacebookSocial.this.log("onError");
                if (MyFacebookSocial.this.getAuthorizationListener() != null) {
                    MyFacebookSocial.this.getAuthorizationListener().onAuthorizationFail(MyFacebookSocial.this, e);
                }

            }
        });
    }

    public static class Builder extends com.mgrmobi.sdk.social.base.GenericSocialNetwork.Builder<AndroidBaseSocialNetwork> {
        private Context context;
        private String[] permission;
        private String[] publishPermissions;

        public Builder() {
        }

        public MyFacebookSocial.Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public MyFacebookSocial.Builder setPermission(String[] permission) {
            if (permission != null && permission.length >= 1) {
                this.permission = permission;
                return this;
            }
            else {
                throw new IllegalArgumentException("permissions can\'t be empty");
            }
        }

        public MyFacebookSocial.Builder setPublishPermission(String[] permission) {
            if (permission != null && permission.length >= 1) {
                this.publishPermissions = permission;
                return this;
            }
            else {
                throw new IllegalArgumentException("permissions can\'t be empty");
            }
        }

        public MyFacebookSocial create() {
            if (this.permission != null && this.permission.length >= 1) {
                MyFacebookSocial socialNetwork = new MyFacebookSocial();
                socialNetwork.setContext(this.context);
                socialNetwork.permissions = this.permission;
                socialNetwork.publishPermissions = this.publishPermissions;
                socialNetwork.prepare();
                return socialNetwork;
            }
            else {
                throw new IllegalArgumentException("permissions can\'t be empty");
            }
        }
    }
    public void getFacebookUserProfileInformation() {

        Bundle bundle = new Bundle();
        bundle.putSerializable("fields", "email,bio,first_name,last_name,middle_name,name");

        GraphRequestBatch batch = new GraphRequestBatch(new GraphRequest(AccessToken.getCurrentAccessToken(),
                "me", bundle, null, new GraphRequest.Callback() {
            @Override
            public void onCompleted(final GraphResponse response) {
                MyFacebookSocial.this.setProfile(FacebookUtils.fromFacebook(response.getJSONObject(), Profile.getCurrentProfile()));
            }
        }));
        batch.addCallback(new GraphRequestBatch.Callback() {
            public void onBatchCompleted(GraphRequestBatch graphRequestBatch) {
                MyFacebookSocial.this.log(" onBatchCompleted: " + graphRequestBatch);
                if (MyFacebookSocial.this.getProfileListener() != null) {
                    MyFacebookSocial.this.getProfileListener().onProfileSuccess(MyFacebookSocial.this);
                }

            }
        });
        batch.executeAsync();
    }
}
