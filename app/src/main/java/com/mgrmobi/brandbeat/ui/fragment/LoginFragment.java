package com.mgrmobi.brandbeat.ui.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.maps.model.LatLng;
import com.mgrmobi.brandbeat.interactors.base.DefaultSubscriber;
import com.mgrmobi.sdk.social.android.AndroidBaseSocialNetwork;
import com.mgrmobi.sdk.social.base.SocialAuthorizationListener;
import com.mgrmobi.sdk.social.base.SocialCancelReason;
import com.mgrmobi.sdk.social.base.SocialNetwork;
import com.mgrmobi.sdk.social.base.SocialNetworkManager;
import com.mgrmobi.sdk.social.base.SocialType;
import com.mgrmobi.sdk.social.facebook.FacebookSocialNetwork;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.location.bean.LocationBean;
import com.mgrmobi.brandbeat.location.impl.RxLocationBeanImpl;
import com.mgrmobi.brandbeat.network.request.MetaRequest;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.presenter.PresenterLoginView;
import com.mgrmobi.brandbeat.ui.activity.RegistrationActivity;
import com.mgrmobi.brandbeat.ui.activity.RemindPasswordActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerAuthorization;
import com.mgrmobi.brandbeat.ui.presenter_views.ViewLogin;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;
import com.mgrmobi.sdk.social.googleplus.GooglePlusSocialNetwork;
import com.mgrmobi.sdk.social.googleplus.GoogleTokenChangedListener;
import com.mgrmobi.sdk.social.linkedin.LinkedinSocialNetwork;
import com.mgrmobi.sdk.social.twitter.TwitterSocialNetwork;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class LoginFragment extends Fragment {
    private ContainerAuthorization containerAuthorization;
    @Bind(R.id.input_login)
    public EditText loginText;
    @Bind(R.id.input_password)
    public EditText password;
    @Bind(R.id.login_button)
    public View view;
    @Bind(R.id.registration)
    public View registrationButton;
    @Bind(R.id.til_login)
    public TextInputLayout inputLogin;
    @Bind(R.id.til_password)
    public TextInputLayout inputPassword;
    @Inject
    LocationBean locationBean = new RxLocationBeanImpl(BrandBeatApplication.getInstance().getApplicationContext());
    private RequestLocation requestLocation = new RequestLocation();
    private PresenterLoginView presenter = new PresenterLoginView();
    private String gogleToken = "";
    private AndroidBaseSocialNetwork socialNetwork;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        if (getActivity() instanceof ContainerAuthorization) {
            containerAuthorization = (ContainerAuthorization) getActivity();
            presenter.setContainerLogin(containerAuthorization, new ViewLogin(inputLogin, inputPassword));
        }
        initLocation();
    }

    @OnTextChanged(R.id.input_login)
    public void loginChanged() {
        inputLogin.setError("");
    }

    @OnTextChanged(R.id.input_password)
    public void passwordChanged() {
        inputPassword.setError("");
    }


    @OnClick(R.id.login_button)
    protected void onButtonLogInClicked() {


        Observable.create(subscriber -> {
            MetaRequest metaRequest = new MetaRequest(Utils.getHardwareId(), "132", "1");
            InstanceID instanceID = InstanceID.getInstance(BrandBeatApplication.getInstance().getBaseContext());
            String token = null;
            try {
                token = instanceID.getToken(BrandBeatApplication.getInstance().getBaseContext()
                        .getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            metaRequest.setPushId(token);
            metaRequest.setVersionApp(Utils.getVersion());
            presenter.login(loginText.getText().toString(), password.getText().toString(), requestLocation, metaRequest);
            containerAuthorization.connectWithEmail();
            return;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber() {
                    @Override
                    public void onCompleted() {
                        return;
                    }

                    @Override
                    public void onError(Throwable e) {
                        return;
                    }

                    @Override
                    public void onNext(Object o) {
                        return;
                    }
                });



    }

    @OnClick(R.id.registration)
    protected void onRegistrationClick() {
        startActivity(RegistrationActivity.callingIntent(getActivity()));
    }

    @OnClick(R.id.remind_password)
    protected void onRemindClick() {
        startActivity(RemindPasswordActivity.createIntent(getActivity()));
    }

    @OnClick(R.id.google_plus_login)
    protected void onGooglePlusClick() {
        if (SocialNetworkManager.isRegistered(SocialType.GOOGLE_PLUS)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.GOOGLE_PLUS);
        }
        else {
            socialNetwork = new GooglePlusSocialNetwork.Builder()
                    .setContext(getActivity())
                    .create();
        }
        //((GooglePlusSocialNetwork) socialNetwork).setGoogleTokenChangedListener(googleTokenChangedListener);
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
        socialNetwork.login(getActivity());
    }

    @OnClick(R.id.twitter_login)
    protected void onTwitterClick() {
        if (SocialNetworkManager.isRegistered(SocialType.TWITTER)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.TWITTER);
        }
        else {
            socialNetwork = new TwitterSocialNetwork.Builder()
                    .setContext(getActivity())
                    .create();
        }
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
        socialNetwork.login(getActivity());
    }

    @OnClick(R.id.linkedIn_login)
    protected void onLinkedInClick() {
        if (SocialNetworkManager.isRegistered(SocialType.LINKEDIN_WEB)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.LINKEDIN_WEB);
        }
        else {
            socialNetwork = new LinkedinSocialNetwork.Builder()
                    .setContext(getContext())
                    .setConfig(getString(R.string.linked_in_client_id), getString(R.string.linked_in_client_secret),
                            getString(R.string.linked_in_redirect_url),getResources().getStringArray(R.array.linked_in_params))
                    .create();
        }
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
        socialNetwork.login(getActivity());
    }

    @OnClick(R.id.facebook_login)
    protected void onFacebookClick() {
        if (SocialNetworkManager.isRegistered(SocialType.FACEBOOK)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.FACEBOOK);
        }
        else {
            socialNetwork = new FacebookSocialNetwork.Builder()
                    .setContext(getActivity())
                    .setPermission(new String[]{"public_profile", "email", "user_friends"})
                    .setPublishPermission(new String[]{"publish_actions"})
                    .create();
        }
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
        socialNetwork.login(getActivity());
    }


    private GoogleTokenChangedListener googleTokenChangedListener = new GoogleTokenChangedListener() {
        @Override
        public void onGoogleTokenChanged(final SocialNetwork socialNetwork, final String s) {
            gogleToken = s;
            String socialType = "google";
            presenter.socialLogin(socialType, null, gogleToken, requestLocation);
        }
    };
    private SocialAuthorizationListener socialAuthorizationListener = new SocialAuthorizationListener() {
        @Override
        public void onAuthorizationSuccess(SocialNetwork network) {
            String socialType = "";
            if (SocialType.FACEBOOK == network.getSocialType()) {
                ((FacebookSocialNetwork) network).profile(true);
                socialType = "facebook";
            }
            String secret = null;
            if (SocialType.TWITTER == network.getSocialType()) {
                socialType = "twitter";
                TwitterSession var1 = Twitter.getSessionManager().getActiveSession();
                if (var1 != null) {
                    TwitterAuthToken var2 = var1.getAuthToken();
                    secret = var2.secret;
                }
            }
            if (SocialType.LINKEDIN_WEB == network.getSocialType()) {
                socialType = "linkedin";
            }
            if (SocialType.GOOGLE_PLUS == network.getSocialType()) {
                return;
            }
            network.profile();

            presenter.socialLogin(socialType, secret, network.getAccessToken(), requestLocation);
        }

        @Override
        public void onAuthorizationFail(SocialNetwork network, Throwable throwable) {
            Utils.showAlertDialog(getContext(), (dialog, which) -> {
            }, (dialog1, which1) -> {
            },
                    getString(R.string.error), throwable.getMessage(), true, false);
            return;
        }
        @Override
        public void onAuthorizationCancel(SocialNetwork network, SocialCancelReason reason) {
            return;
        }
    };

    public void callOnActivityResult(int resultCode, int requestCode, Intent data) {

        socialNetwork.onActivityResult(requestCode, resultCode, data);
    }

    public void setLocationBean() {
       presenter.updateLocation(requestLocation);
    }

    private void initLocation() {
        Observable<Location> brandBeatAddressObservable = locationBean.getCurrentLocation();
        brandBeatAddressObservable.map(loc -> new LatLng(loc.getLatitude(), loc.getLongitude()))
                .subscribe(loc ->
                {
                    locationBean.getAddressByLocation(loc)
                            .subscribe(address ->
                            {
                                initRequestLocationFromAddress(address);
                            }, error -> {
                                return;
                            });
                }, error -> {
                    return;
                });
    }

    public void initRequestLocationFromAddress(Address address) {
        requestLocation.setCountry(address.getCountryName());
        if (address.getLocality() != null) {
            requestLocation.setCity(address.getLocality());
        }
        else {
            if (address.getAdminArea() != null) {
                requestLocation.setCity(address.getAdminArea());
            }
        }
        requestLocation.setCountryCode(address.getCountryCode());
        requestLocation.setLat(address.getLatitude());
        requestLocation.setLng(address.getLongitude());
    }

    public void getProfile() {
        UserDataUtils userDataUtils = new UserDataUtils(getActivity());
        presenter.getProfile(userDataUtils.getUserId());
    }
}