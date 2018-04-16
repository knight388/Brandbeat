package com.mgrmobi.brandbeat.presenter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.interactors.UseCaseLocation;
import com.mgrmobi.brandbeat.interactors.UseCaseLogin;
import com.mgrmobi.brandbeat.interactors.UseCaseProfile;
import com.mgrmobi.brandbeat.interactors.UseCaseSocialLogin;
import com.mgrmobi.brandbeat.interactors.base.DefaultSubscriber;
import com.mgrmobi.brandbeat.location.bean.LocationBean;
import com.mgrmobi.brandbeat.location.impl.RxLocationBeanImpl;
import com.mgrmobi.brandbeat.network.request.MetaRequest;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseLogin;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerAuthorization;
import com.mgrmobi.brandbeat.ui.presenter_views.ViewLogin;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;

import java.io.IOException;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterLoginView extends BasePresenter {

    @Inject
    UseCaseLogin useCaseLogin = new UseCaseLogin();
    @Inject
    UseCaseSocialLogin useCaseSocialLogin = new UseCaseSocialLogin();
    @Inject
    LocationBean locationBean = new RxLocationBeanImpl(BrandBeatApplication.getInstance().getApplicationContext());
    @Inject
    UseCaseLocation useCaseLocation = new UseCaseLocation();

    public boolean updateLocation;

    UseCaseProfile useCaseProfile = new UseCaseProfile();
    ResponseProfile responseProfile;

    private boolean isNew = false;
    private ContainerAuthorization containerAuthorization;
    private ViewLogin viewLogin;

    public void setContainerLogin(final ContainerAuthorization containerRegistration, ViewLogin viewLogin) {
        this.containerAuthorization = containerRegistration;
        this.viewLogin = viewLogin;
    }

    public void updateLocation(RequestLocation location) {
        updateLocation = true;
        useCaseLocation.setLocation(location);
        useCaseLocation.execute(this);
    }

    public void socialLogin(String social, String secret, String accessToken, RequestLocation location) {
        containerAuthorization.showProgress();
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
            useCaseSocialLogin.setRequest(social, accessToken, secret, location, metaRequest);
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

    public void login(String login, String password, RequestLocation location, MetaRequest metaRequest) {
        if (!validateRequestLogin(password, login)) return;
        containerAuthorization.showProgress();
        PackageInfo pInfo = null;
        try {
            pInfo = BrandBeatApplication.getInstance().getBaseContext().
                    getPackageManager().getPackageInfo(BrandBeatApplication.getInstance().getBaseContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;

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
            metaRequest.setVersionApp(version);
            useCaseLogin.setRequest(login, password, location, metaRequest);
            useCaseLogin.execute(this);

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

    public void getProfile(String id) {
        useCaseProfile.setRequest(id);
        useCaseProfile.execute(this);
    }

    @Override
    public void resume() {
        return;
    }

    @Override
    public void pause() {
        useCaseLocation.unsubscribe();
        useCaseLogin.unsubscribe();
        useCaseProfile.unsubscribe();
        useCaseSocialLogin.unsubscribe();
        return;
    }

    @Override
    public void destroy() {
        useCaseLocation.unsubscribe();
        useCaseLogin.unsubscribe();
        useCaseProfile.unsubscribe();
        useCaseSocialLogin.unsubscribe();
        return;
    }

    @Override
    public void onCompleted() {
        containerAuthorization.hideProgress();
        if (responseProfile != null) {
            if (responseProfile.getStatus().equals("2"))
                isNew = true;
            responseProfile = null;
            containerAuthorization.profileSuccess(isNew);
            return;
        }

        if (updateLocation) {
            containerAuthorization.completeUpdateLocation(isNew);
        }
        else
            containerAuthorization.loginSuccess(isNew);
    }

    @Override
    public void onError(Throwable e) {
        containerAuthorization.hideProgress();
        containerAuthorization.showError(e);
    }

    @Override
    public void onNext(Object t) {
        UserDataUtils response = new UserDataUtils(BrandBeatApplication.getInstance().getBaseContext());

        if (((BaseResponse) t).getData() instanceof ResponseLogin) {
            response.saveUserData((ResponseLogin) ((BaseResponse) t).getData());
            isNew = ((ResponseLogin) ((BaseResponse) t).getData()).isNew();
        }

        if (((BaseResponse) t).getData() instanceof ResponseProfile) {
            responseProfile = (ResponseProfile) ((BaseResponse) t).getData();
            UserDataUtils userDataUtils = new UserDataUtils(BrandBeatApplication.getInstance().getBaseContext());
            userDataUtils.saveUserProfile(responseProfile);
        }
        containerAuthorization.hideProgress();
    }

    public boolean validateRequestLogin(String password, String login) {
        boolean isValid = true;
        if (password.equals("")) {
            viewLogin.setPasswordError(BrandBeatApplication.getInstance().getApplicationContext().getString(R.string.null_password));
            isValid = false;
        }
        if (login.equals("")) {
            viewLogin.setLoginError(BrandBeatApplication.getInstance().getApplicationContext().getString(R.string.null_email));
            isValid = false;
        }
        if (!isValidEmail(login)) {
        }
        return isValid;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return true;
        }
        else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
