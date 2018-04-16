package com.mgrmobi.sdk.social.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

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
import com.mgrmobi.sdk.social.base.GenericSocialNetwork;
import com.mgrmobi.sdk.social.base.SocialCancelReason;
import com.mgrmobi.sdk.social.base.SocialShareListener;
import com.mgrmobi.sdk.social.base.SocialType;
import com.mgrmobi.sdk.social.base.model.ShareMediaFile;
import com.mgrmobi.sdk.social.facebook.utils.FacebookUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Valentin S. Bolkonsky.
 */
public class FacebookSocialNetwork extends SimpleAndroidSocialNetwork {

    private String[] permissions;
    private String[] publishPermissions;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    private FacebookSocialNetwork() {
        super();

    }

    public boolean checkPermissions() {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.isExpired()) {
            return false;
        }
        Set<String> approvedPermissions = accessToken.getPermissions();
        if (publishPermissions != null) {
            for (String permission : publishPermissions) {
                if (!approvedPermissions.contains(permission)) {
                    return false;
                }
            }
        }
        for (String permission : permissions) {
            if (!approvedPermissions.contains(permission)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void prepare() {
        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();
        super.prepare();
    }

    @Override
    public String getAccessToken() {
        final AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            return token.getToken();
        }
        return null;
    }

    @Override
    public String getUserId() {
        final AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            return token.getUserId();
        }
        return null;
    }

    @Override
    public void logout() {
        super.logout();
        if (profileTracker != null && profileTracker.isTracking()) {
            profileTracker.stopTracking();
        }
        setProfile((SocialUser) null);
        LoginManager.getInstance().logOut();
    }

    @Override
    public void login(final Activity activity) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            Set<String> permissions = accessToken.getPermissions();
            if (publishPermissions == null) {
                if (getAuthorizationListener() != null) {
                    getAuthorizationListener().onAuthorizationSuccess(FacebookSocialNetwork.this);
                }
                Profile.fetchProfileForCurrentAccessToken();
                return;
            }
            final List<String> need = new ArrayList<>();
            for (String permission : publishPermissions) {
                if (!permissions.contains(permission)) {
                    need.add(permission);
                }
            }
            if (!need.isEmpty()) {
                registrationAuthorizationCallback();
                LoginManager.getInstance().logInWithPublishPermissions(activity, need);
            } else {
                if (getAuthorizationListener() != null) {
                    getAuthorizationListener().onAuthorizationSuccess(FacebookSocialNetwork.this);
                }
                Profile.fetchProfileForCurrentAccessToken();
            }
            return;
        }
        registrationAuthorizationCallback();
        //logout();
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(permissions));
    }

    @Override
    public void share(Activity activity, String title, String message, String subscribe,
                      ShareMediaFile mediaFile, final SocialShareListener listener) {
        ShareContent shareContent = null;
        if (mediaFile != null) {
            if (ShareDialog.canShow(SharePhotoContent.class)) {
                if (ShareMediaFile.ShareMediaType.IMAGE == mediaFile.getType()) {
                    final Bitmap bitmap = BitmapFactory.decodeFile(mediaFile.getMediaPath());
                    final SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(bitmap)
                            .build();
                    shareContent = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                }
            }
        } else {
            shareContent = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setContentDescription(message)
                    .setContentUrl(subscribe != null ? Uri.parse(subscribe) : null)
                    .build();
        }
        final ShareDialog shareDialog = new ShareDialog(activity);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if (listener != null) {
                    listener.onShareSuccess(FacebookSocialNetwork.this, result.getPostId());
                }
            }

            @Override
            public void onCancel() {
                if (listener != null) {
                    listener.onShareCancel(FacebookSocialNetwork.this, SocialCancelReason.USER_CANCEL);
                }
            }

            @Override
            public void onError(FacebookException e) {
                if (listener != null) {
                    listener.onShareFail(FacebookSocialNetwork.this, e);
                }
            }
        });
        shareDialog.show(shareContent);
        setShareListener(listener);
    }

    public void profile(boolean isManual) {
        if (!isManual) {
            profile();
            return;
        }
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null || accessToken.isExpired()) {
            if (getProfileListener() != null) {
                getProfileListener().onProfileFailed(this, new FacebookException("no access token"));
            }
            return;
        }
        GraphRequestBatch batch = new GraphRequestBatch(GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        log("jsonObject: " + jsonObject + " response: " + graphResponse);
                        setProfile(FacebookUtils.fromFacebook(jsonObject, Profile.getCurrentProfile()));
                    }

                }
        ));
        batch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch graphRequestBatch) {
                log(" onBatchCompleted: " + graphRequestBatch);
                if (getProfileListener() != null) {
                    getProfileListener().onProfileSuccess(FacebookSocialNetwork.this);
                }
            }

        });
        batch.executeAsync();
    }

    @Override
    public void profile() {
        if (profileTracker == null) {
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                    setProfile(newProfile);
                }
            };
        } else {
            if (!profileTracker.isTracking()) {
                profileTracker.startTracking();
            }
        }
        Profile.fetchProfileForCurrentAccessToken();
        setProfile(Profile.getCurrentProfile());
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.FACEBOOK;
    }

    private void setProfile(Profile profile) {
        if (profile != null) {
            log("currentProfile: " + profile.getProfilePictureUri(640, 640));
            setProfile(FacebookUtils.convert(profile));
            if (getProfileListener() != null) {
                getProfileListener().onProfileSuccess(this);
            }
        } else {
            if (getProfileListener() != null) {
                getProfileListener().onProfileFailed(this, new FacebookException("profile is null"));
            }
        }
    }

    private void registrationAuthorizationCallback() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                log("onSuccess: " + loginResult);
                if (getAuthorizationListener() != null) {
                    getAuthorizationListener().onAuthorizationSuccess(FacebookSocialNetwork.this);
                }
                Profile.fetchProfileForCurrentAccessToken();
            }

            @Override
            public void onCancel() {
                log("onCancel");
                if (getAuthorizationListener() != null) {
                    getAuthorizationListener().onAuthorizationCancel(FacebookSocialNetwork.this, SocialCancelReason.USER_CANCEL);
                }
            }

            @Override
            public void onError(FacebookException e) {
                log("onError");
                if (getAuthorizationListener() != null) {
                    getAuthorizationListener().onAuthorizationFail(FacebookSocialNetwork.this, e);
                }
            }
        });
    }


    public static class Builder extends GenericSocialNetwork.Builder<AndroidBaseSocialNetwork> {

        private Context context;
        private String[] permission;
        private String[] publishPermissions;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setPermission(final String[] permission) {
            if (permission == null || permission.length < 1) {
                throw new IllegalArgumentException("permissions can't be empty");
            }
            this.permission = permission;
            return this;
        }

        public Builder setPublishPermission(final String[] permission) {
            if (permission == null || permission.length < 1) {
                throw new IllegalArgumentException("permissions can't be empty");
            }
            this.publishPermissions = permission;
            return this;
        }


        @Override
        public FacebookSocialNetwork create() {
            if (permission == null || permission.length < 1) {
                throw new IllegalArgumentException("permissions can't be empty");
            }
            final FacebookSocialNetwork socialNetwork = new FacebookSocialNetwork();
            socialNetwork.setContext(context);
            socialNetwork.permissions = permission;
            socialNetwork.publishPermissions = publishPermissions;
            socialNetwork.prepare();
            return socialNetwork;
        }
    }
}
