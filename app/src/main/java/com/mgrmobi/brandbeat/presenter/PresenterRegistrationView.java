package com.mgrmobi.brandbeat.presenter;

import android.util.Patterns;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.sdk.social.android.model.SocialUser;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.interactors.UseCaseRegistration;
import com.mgrmobi.brandbeat.interactors.UseCaseSocialLogin;
import com.mgrmobi.brandbeat.interactors.base.DefaultSubscriber;
import com.mgrmobi.brandbeat.network.request.MetaRequest;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.network.request.RequestSocial;
import com.mgrmobi.brandbeat.network.responce.ResponseLogin;
import com.mgrmobi.brandbeat.network.responce.ResponseRegistration;
import com.mgrmobi.brandbeat.ui.base.ContainerRegistration;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;

import java.io.IOException;
import java.util.Calendar;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.mgrmobi.brandbeat.R.drawable.gender;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterRegistrationView extends DefaultSubscriber<Object> {

    private ContainerRegistration containerRegistration;
    ResponseRegistration registration;
    private ResponseProfile responseProfile = new ResponseProfile();
    private ResponseLogin responseLogin;

    public void setContainerRegistration(final ContainerRegistration containerRegistration) {
        this.containerRegistration = containerRegistration;
    }

    @Inject
    UseCaseRegistration useCaseRegistration = new UseCaseRegistration();
    @Inject
    UseCaseSocialLogin useCaseSocialLogin = new UseCaseSocialLogin();

    public void register(String name, String email, String password, RequestLocation location, int gender, Calendar dob) {
        if (!validate(name, email, password, location, gender, dob))
            return;
        containerRegistration.showProgress();
        String dobString;
        if (dob == null) {
            dobString = "";
        }
        else {
            dobString = dob.getTime().getTime() + "";
        }

        MetaRequest metaRequest = new MetaRequest(Utils.getHardwareId(), "" , "");
        Observable.create(subscriber -> {
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
            useCaseRegistration.setRequest(name, email, password, location, gender, dobString, metaRequest);
            useCaseRegistration.execute(this);
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



        responseProfile.setUsername(name);
        responseProfile.setEmail(email);
        responseProfile.setGender(gender + "");
    }

    public void socialLogin(String social, String accessToken, RequestLocation location, MetaRequest metaRequest) {
        containerRegistration.showProgress();
        metaRequest = new MetaRequest(Utils.getHardwareId(), "" , "");
        final MetaRequest finalMetaRequest = metaRequest;
        Observable.create(subscriber -> {
            InstanceID instanceID = InstanceID.getInstance(BrandBeatApplication.getInstance().getBaseContext());
            String token = null;
            try {
                token = instanceID.getToken(BrandBeatApplication.getInstance().getBaseContext()
                        .getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            finalMetaRequest.setPushId(token);
            finalMetaRequest.setVersionApp(Utils.getVersion());
            useCaseSocialLogin.setRequest(social, accessToken, "", location, finalMetaRequest);
            useCaseSocialLogin.execute(this);
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

    public void socialLogin(SocialUser socialUser) {
        containerRegistration.initViewAfterGetProfile(socialUser);
    }

    public void socialLogin(String name, String email, String password, RequestLocation location,
                            int gender, Calendar dob, String social, String accessToken,
                            SocialUser socialUser, String secretKey) {
        containerRegistration.showProgress();
        if (!validate(name, email, password, location, gender, dob))
            return;
        RequestSocial requestSocial = new RequestSocial(name, email, password, location, gender, dob, social, accessToken);
        if (social.equals("twitter")) {
            requestSocial.setAccessTokenSecret(secretKey);
        }
        if (social.equals("linkedin_web")) {
            social = "linkedin";
        }
        if (socialUser.getPicture() != null)
            requestSocial.setPhoto(socialUser.getPicture());
        if (socialUser.getFirstName() != null)
            requestSocial.setFirstName(socialUser.getFirstName());
        if (socialUser.getLastName() != null)
            requestSocial.setLastName(socialUser.getLastName());
        MetaRequest metaRequest = new MetaRequest(Utils.getHardwareId(), "", "");
        Observable.create(subscriber -> {
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
            requestSocial.setMetaRequest(metaRequest);
            useCaseSocialLogin.setRequest(requestSocial);
            useCaseSocialLogin.execute(this);
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

    @Override
    public void onCompleted() {
        containerRegistration.hideProgress();
        if (registration != null) {
            containerRegistration.loginSuccess();
        }
        if(responseLogin != null)
            containerRegistration.registrationWihFaceBook();

    }

    @Override
    public void onError(Throwable e) {
        containerRegistration.hideProgress();
        containerRegistration.showError(e);
    }

    @Override
    public void onNext(Object t) {
        containerRegistration.hideProgress();
        ResponseLogin responseLogin = new ResponseLogin();
        if (t instanceof ResponseRegistration) {
            registration = ((ResponseRegistration) t).getRegistration();

            responseLogin.setAccessToken(registration.getAccessToken());
            responseLogin.setRefreshToken(registration.getRefreshToken());
            responseLogin.setUserId(registration.getResponseAuthInfo().getId());
        }
        else {
            if (t instanceof ResponseLogin) {
                responseLogin = ((ResponseLogin) t).getResponseLogin();
                this.responseLogin = responseLogin;
            }
        }
        if(t instanceof BaseResponse) {
            responseLogin = (ResponseLogin) ((BaseResponse) t).getData();
            this.responseLogin = responseLogin;
        }
        UserDataUtils response = new UserDataUtils(BrandBeatApplication.getInstance().getBaseContext());
        response.clear();
        response.saveUserDataRegistration(responseLogin);
        response.saveUserProfile(responseProfile);
    }

    private boolean validate(String name, String email, String password, RequestLocation location, int gender, Calendar dob) {
        if (name.replaceAll(" ", "").equals("")) {
            onError(new Throwable(BrandBeatApplication.getInstance().getString(R.string.null_name_error)));
            return false;
        }
        else if (email.replaceAll(" ", "").equals("")) {
            onError(new Throwable(BrandBeatApplication.getInstance().getString(R.string.null_email_error)));
            return false;
        }
        else if (password.replaceAll(" ", "").equals("")) {
            onError(new Throwable(BrandBeatApplication.getInstance().getString(R.string.null_password_error)));
            return false;
        }
        else if (name.length() > 35) {
            onError(new Throwable(BrandBeatApplication.getInstance().getString(R.string.length_name_error)));
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onError(new Throwable(BrandBeatApplication.getInstance().getString(R.string.inccorect_email)));
            return false;
        }
        return true;
    }
}
