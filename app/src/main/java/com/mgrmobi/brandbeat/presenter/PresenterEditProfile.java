package com.mgrmobi.brandbeat.presenter;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.interactors.UseCaseDeleteUserFile;
import com.mgrmobi.brandbeat.interactors.UseCaseGetCurrentLocation;
import com.mgrmobi.brandbeat.interactors.UseCaseGetIncomeRange;
import com.mgrmobi.brandbeat.interactors.UseCaseUpdatePassword;
import com.mgrmobi.brandbeat.interactors.UseCaseUpdateProfile;
import com.mgrmobi.brandbeat.interactors.base.DefaultSubscriber;
import com.mgrmobi.brandbeat.network.request.MetaRequest;
import com.mgrmobi.brandbeat.network.request.RequestUpdateProfile;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseIncomeRange;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerEditProfile;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterEditProfile extends BasePresenter {
    private ContainerEditProfile containerEditProfile;
    private UseCaseUpdateProfile useCaseUpdateProfile = new UseCaseUpdateProfile();
    private UseCaseUpdatePassword useCaseUpdatePassword = new UseCaseUpdatePassword();
    private UseCaseDeleteUserFile useCaseDeleteUserFile = new UseCaseDeleteUserFile();
    private UseCaseGetIncomeRange useCaseGetIncomRange = new UseCaseGetIncomeRange();
    private UseCaseGetCurrentLocation useCaseGetCurrentLocation = new UseCaseGetCurrentLocation();
    private ResponseProfile responseProfile = new ResponseProfile();
    private ArrayList<ResponseIncomeRange> incomeRanges;

    public void setView(ContainerEditProfile view) {
        containerEditProfile = view;
    }

    public void deleteUserFile(String userPath) {
        useCaseDeleteUserFile.setRequest(userPath);
        useCaseDeleteUserFile.execute(this);
    }

    public void getIncomeRange() {
        useCaseGetIncomRange.execute(this);
    }

    public void getIncomeRangeCurrentLocation(String countryName) {
        useCaseGetCurrentLocation.setCountry(countryName);
        useCaseGetCurrentLocation.execute(this);
    }

    public void updateProfile(RequestUpdateProfile requestProfile) {
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
            requestProfile.setMetaRequest(metaRequest);
            useCaseUpdateProfile.setRequestUpdateProfile(requestProfile);
            useCaseUpdateProfile.execute(this);
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
        containerEditProfile.showProgress();
    }

    @Override
    public void onCompleted() {
        if(incomeRanges != null) {
            containerEditProfile.setIncomeRanges(incomeRanges);
            incomeRanges = null;
        }
        else {
            containerEditProfile.getUpdateProfile(null);
        }
    }

    public void updatePassword(String oldPassword, String newPssword) {
        useCaseUpdatePassword.setPsswords(oldPassword, newPssword);
        useCaseUpdatePassword.execute(this);
        containerEditProfile.showProgress();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseDeleteUserFile.unsubscribe();
        useCaseGetCurrentLocation.unsubscribe();
        useCaseGetIncomRange.unsubscribe();
        useCaseUpdatePassword.unsubscribe();
        useCaseUpdateProfile.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseDeleteUserFile.unsubscribe();
        useCaseGetCurrentLocation.unsubscribe();
        useCaseGetIncomRange.unsubscribe();
        useCaseUpdatePassword.unsubscribe();
        useCaseUpdateProfile.unsubscribe();
        containerEditProfile.hideProgress();
    }

    @Override
    public void onNext(Object t) {
        if(((BaseResponse) t).getData() instanceof List) {
            incomeRanges = (ArrayList<ResponseIncomeRange>) ((BaseResponse) t).getData();
        }
        if (((BaseResponse) t).getData() instanceof ResponseProfile) {
            UserDataUtils userDataUtils = new UserDataUtils(BrandBeatApplication.getInstance().getBaseContext());
            userDataUtils.saveUserProfile((ResponseProfile) (((BaseResponse) t).getData()));
        }
        containerEditProfile.hideProgress();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerEditProfile.hideProgress();
        containerEditProfile.showError(e);
    }

}
