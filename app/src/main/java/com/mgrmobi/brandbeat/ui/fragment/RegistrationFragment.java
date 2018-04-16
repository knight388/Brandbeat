package com.mgrmobi.brandbeat.ui.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mgrmobi.brandbeat.ui.activity.TermsAndCpnditionsActivity;
import com.mgrmobi.sdk.social.android.AndroidBaseSocialNetwork;
import com.mgrmobi.sdk.social.android.model.SocialUser;
import com.mgrmobi.sdk.social.base.SocialAuthorizationListener;
import com.mgrmobi.sdk.social.base.SocialCancelReason;
import com.mgrmobi.sdk.social.base.SocialNetwork;
import com.mgrmobi.sdk.social.base.SocialNetworkManager;
import com.mgrmobi.sdk.social.base.SocialProfileListener;
import com.mgrmobi.sdk.social.base.SocialType;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.location.bean.LocationBean;
import com.mgrmobi.brandbeat.location.impl.RxLocationBeanImpl;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.presenter.PresenterRegistrationView;
import com.mgrmobi.brandbeat.ui.base.ContainerRegistration;
import com.mgrmobi.brandbeat.ui.dialog.ChooseDate;
import com.mgrmobi.brandbeat.ui.dialog.DateDialog;
import com.mgrmobi.brandbeat.utils.Utils;
import com.mgrmobi.sdk.social.facebook.MyFacebookSocial;
import com.mgrmobi.sdk.social.googleplus.GooglePlusSocialNetwork;
import com.mgrmobi.sdk.social.googleplus.GoogleTokenChangedListener;
import com.mgrmobi.sdk.social.linkedin.LinkedinSocialNetwork;
import com.mgrmobi.sdk.social.twitter.TwitterSocialNetwork;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RegistrationFragment extends Fragment {

    private ContainerRegistration containerRegistration;
    private RequestLocation requestLocation = new RequestLocation();

    @Bind(R.id.input_dob)
    public TextView dateOfBirth;
    @Bind(R.id.register_button)
    public View mRegisterButton;
    @Bind(R.id.input_name)
    public EditText nameEditText;
    @Bind(R.id.input_email)
    public EditText emailEditText;
    @Bind(R.id.input_password)
    public EditText passwordEditText;
    @Bind(R.id.location)
    public AutoCompleteTextView locationEditText;
    @Bind(R.id.input_gender)
    public Spinner gender;


    private boolean genderChange = false;
    private Calendar dateInMill;
    private String secret = null;
    private String gogleToken = "";
    @Inject
    LocationBean locationBean = new RxLocationBeanImpl(BrandBeatApplication.getInstance().getApplicationContext());
    private AndroidBaseSocialNetwork socialNetwork;
    private SocialUser socialUser;
    private PresenterRegistrationView presenter = new PresenterRegistrationView();

    @OnCheckedChanged(R.id.checkbox)
    public void onChecked(final CompoundButton buttonView, final boolean isChecked) {
        if (!isChecked) {
            mRegisterButton.setEnabled(false);
        }
        else
            mRegisterButton.setEnabled(true);
    }

    @OnClick(R.id.facebook_login)
    protected void onFacebookClick() {
        if (SocialNetworkManager.isRegistered(SocialType.FACEBOOK)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.FACEBOOK);
        }
        else {
            socialNetwork = new MyFacebookSocial.Builder()
                    .setContext(getActivity())
                    .setPermission(new String[]{"public_profile", "email", "user_friends"})
                    .setPublishPermission(new String[]{"publish_actions"})
                    .create();
        }
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
        socialNetwork.login(getActivity());
        socialNetwork.setProfileListener(socialProfileListener);
    }

    @OnClick(R.id.text_click)
    public void onClick() {
        startActivity(new Intent(getActivity(), TermsAndCpnditionsActivity.class));
    }

    @OnClick(R.id.registration_twitter)
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
        socialNetwork.setProfileListener(socialProfileListener);
    }

    @OnClick(R.id.linked_in)
    protected void onLinkedIn() {
        if (SocialNetworkManager.isRegistered(SocialType.LINKEDIN_WEB)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.LINKEDIN_WEB);
        }
        else {
            socialNetwork = new LinkedinSocialNetwork.Builder()
                    .setContext(getContext())
                    .setConfig(getString(R.string.linked_in_client_id), getString(R.string.linked_in_client_secret),
                            getString(R.string.linked_in_redirect_url), getResources().getStringArray(R.array.linked_in_params))
                    .create();
        }
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
        socialNetwork.login(getActivity());
        socialNetwork.setProfileListener(socialProfileListener);
    }

    @OnClick(R.id.google_plus)
    protected void onGooglePlusClick() {
        if (SocialNetworkManager.isRegistered(SocialType.GOOGLE_PLUS)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.GOOGLE_PLUS);
        }
        else {
            socialNetwork = new GooglePlusSocialNetwork.Builder()
                    .setContext(getActivity())
                    .create();
        }
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
        socialNetwork.login(getActivity());
        socialNetwork.setProfileListener(socialProfileListener);
    }

    SocialProfileListener socialProfileListener = new SocialProfileListener() {
        @Override
        public void onProfileSuccess(final SocialNetwork network) {
            socialUser = socialNetwork.getProfile();
            presenter.socialLogin(socialUser);
        }

        @Override
        public void onProfileFailed(final SocialNetwork network, final Throwable throwable) {

        }
    };
    private SocialAuthorizationListener socialAuthorizationListener = new SocialAuthorizationListener() {
        @Override
        public void onAuthorizationSuccess(SocialNetwork network) {
            if (SocialType.FACEBOOK == network.getSocialType()) {
                if (!((MyFacebookSocial) network).checkPermissions()) {
                    // ((FacebookSocialNetwork) network).login(getActivity());
                }
                ((MyFacebookSocial) network).profile(true);
                ((MyFacebookSocial) socialNetwork).getFacebookUserProfileInformation();
            }

            if (SocialType.TWITTER == network.getSocialType()) {
                TwitterSession var1 = Twitter.getSessionManager().getActiveSession();
                if (var1 != null) {
                    TwitterAuthToken var2 = var1.getAuthToken();
                    secret = var2.secret;
                }
            }
            network.profile();
        }

        @Override
        public void onAuthorizationFail(SocialNetwork network, Throwable throwable) {
            return;
        }

        @Override
        public void onAuthorizationCancel(SocialNetwork network, SocialCancelReason reason) {
            return;
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerRegistration) {
            containerRegistration = (ContainerRegistration) getActivity();
            presenter.setContainerRegistration(containerRegistration);
        }

        ButterKnife.bind(this, view);
        mRegisterButton.setEnabled(false);
        passwordEditText.setTypeface(Typeface.DEFAULT);
        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
        initLocation();
        String[] data = getActivity().getResources().getStringArray(R.array.genders);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, data) {
            @Override
            public int getCount() {
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        gender.setAdapter(adapter);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (position + 1 != data.length)
                    genderChange = true;
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                genderChange = false;
            }
        });
        this.gender.setSelection(data.length - 1);
        return;
    }

    @OnTouch(R.id.input_dob)
    protected boolean onInputDateClick(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            chooseDate();
        return false;
    }

    public Address address;
    boolean isStartRegistration = false;

    @OnClick(R.id.register_button)
    protected void onClickRegister() {
        if (!locationEditText.getText().equals("") && !locationEditText.getText().toString().equals(requestLocation.getAddressString())) {
            locationBean.getAddressByName(locationEditText.getText().toString())
                    .subscribe(address ->
                    {
                        isStartRegistration = true;
                        initRequestLocationFromAddress(address);
                        startRegistration();

                    }, error -> {
                        if (address == null) {
                            presenter.onError(new Throwable(getResources().getString(R.string.please_enter_current_location)));
                        }
                    }, () ->
                    {
                        if (isStartRegistration) return;
                        if (address == null) {
                            presenter.onError(new Throwable(getResources().getString(R.string.please_enter_current_location)));
                        }
                        else {
                            startRegistration();
                        }
                    });
        }
        else {
            startRegistration();
        }

    }

    private GoogleTokenChangedListener googleTokenChangedListener = new GoogleTokenChangedListener() {
        @Override
        public void onGoogleTokenChanged(final SocialNetwork socialNetwork, final String s) {
            gogleToken = s;
        }
    };

    private void startRegistration() {
        int gender = this.gender.getSelectedItemPosition() + 1;
        String token = null;
        if (socialNetwork != null && socialNetwork.getSocialType() == SocialType.GOOGLE_PLUS) {
            token = gogleToken;
            if (gogleToken.equals(""))
                token = socialNetwork.getAccessToken();

            presenter.socialLogin(nameEditText.getText().toString(), emailEditText.getText().toString(),
                    passwordEditText.getText().toString(), requestLocation, gender, dateInMill,
                    "google", token, socialUser, secret);
            return;
        }
        else {
            if (socialNetwork != null) {
                token = socialNetwork.getAccessToken();
            }
        }
        if (socialUser != null) {
            presenter.socialLogin(nameEditText.getText().toString(), emailEditText.getText().toString(),
                    passwordEditText.getText().toString(), requestLocation, gender, dateInMill,
                    socialNetwork.getSocialType().name().toLowerCase(), token, socialUser, secret);
        }
        else {
            presenter.register(nameEditText.getText().toString(), emailEditText.getText().toString(),
                    passwordEditText.getText().toString(), requestLocation, gender, dateInMill);
        }
    }

    private void chooseDate() {
        DateDialog dateDialog = new DateDialog(new ChooseDate() {
            @Override
            public void dateChoose(final Calendar date) {
                dateOfBirth.setText(Utils.formatDate.format(date.getTime()));
                dateInMill = date;
            }
        });
        dateDialog.show(getFragmentManager(), dateDialog.getClass().getName());
    }

    public void callOnActivityResult(int resultCode, int requestCode, Intent data) {
        socialNetwork.onActivityResult(requestCode, resultCode, data);
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
                                locationEditText.setText(requestLocation.getAddressString());
                            }, error -> {
                                return;
                            });
                }, error -> {
                    return;
                });
    }

    public void initRequestLocationFromAddress(Address address) {
        this.address = address;
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
        requestLocation.setCountry(address.getCountryName());
    }

    public boolean isDataEnter() {
        if (nameEditText.getText().length() > 0) {
            return true;
        }
        if (emailEditText.getText().length() > 0) {
            return true;
        }
        if (passwordEditText.getText().length() > 0) {
            return true;
        }
        if (dateInMill != null) {
            return true;
        }
        if (locationEditText.getText().length() > 0 && !locationEditText.getText().toString().equals(requestLocation.getAddressString())) {
            return true;
        }
        if (genderChange) {
            return true;
        }
        return false;
    }

    @OnTextChanged(R.id.location)
    public void locationChange() {
        String locationName = locationEditText.getText().toString();
        LatLngBounds latLngBounds = new LatLngBounds(new LatLng(0, 0), new LatLng(0, 0));
        List<Integer> integers = new ArrayList<>();
        integers.add(AutocompleteFilter.TYPE_FILTER_CITIES);
        locationBean.getAutocompletePredictions(locationName, latLngBounds, AutocompleteFilter.create(integers)).subscribe(address ->
        {
            List<String> strings = new ArrayList<String>();
            for(int i = 0; i < address.getCount(); i++) {
                strings.add(address.get(i).getDescription());
            }
            ArrayAdapter adapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_dropdown_item_1line, strings);

            locationEditText.setAdapter(adapter);
            return;
        }, error -> {
            return;
        }, () -> {
            return;
        });

    }
}
