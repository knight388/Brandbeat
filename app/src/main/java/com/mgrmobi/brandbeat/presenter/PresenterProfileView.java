package com.mgrmobi.brandbeat.presenter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.interactors.UseCaseGetAchivmients;
import com.mgrmobi.brandbeat.interactors.UseCaseProfile;
import com.mgrmobi.brandbeat.interactors.UseCaseUpdateProfile;
import com.mgrmobi.brandbeat.interactors.UseCaseUploadPhoto;
import com.mgrmobi.brandbeat.interactors.UseCaseUserReview;
import com.mgrmobi.brandbeat.network.request.MetaRequest;
import com.mgrmobi.brandbeat.network.request.RequestUpdateProfile;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseAchievement;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerProfile;
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
public class PresenterProfileView extends BasePresenter {
    private UseCaseProfile useCaseProfile = new UseCaseProfile();
    private UseCaseUserReview useCaseUserReview = new UseCaseUserReview();
    private UseCaseUploadPhoto useCaseUploadPhoto = new UseCaseUploadPhoto();
    private UseCaseUpdateProfile useCaseUpdateProfile = new UseCaseUpdateProfile();
    private UseCaseGetAchivmients useCaseGetAchivmients = new UseCaseGetAchivmients();
    private ArrayList<ResponseReview> reviews;
    private ResponsePhotoUrl responsePhotoUrl;
    private ArrayList<ResponseAchievement> responseAchievements;
    private ContainerProfile containerProfile;
    private boolean isLoadProfile;
    private ResponseProfile responseProfile;
    private int page = 1;
    private boolean isLast;

    public void setView(@NonNull final ContainerProfile containerProfile) {
        this.containerProfile = containerProfile;
    }

    public void getUser(String userId) {
        useCaseProfile.setRequest(userId);
        useCaseProfile.execute(this);
    }

    public void getAchivmient() {
        useCaseGetAchivmients.execute(this);
    }

    public void getReview() {
        if (isLoadProfile) return;
        UserDataUtils userDataUtils = new UserDataUtils(BrandBeatApplication.getInstance().getBaseContext());
        useCaseUserReview.setUserId(userDataUtils.getUserId());
        useCaseUserReview.execute(this);
        isLoadProfile = true;
    }

    public void uploadPhoto(Bitmap bitmap) {
        useCaseUploadPhoto.setBitmap(bitmap);
        if (containerProfile != null)
            containerProfile.showProgress();
        useCaseUploadPhoto.execute(this);
    }

    public boolean getNextUserReviews() {
        if (isLast) return false;
        page++;
        useCaseUserReview.setPage(page + "");
        useCaseUserReview.execute(this);
        containerProfile.showPaginationProgress();
        return true;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseGetAchivmients.unsubscribe();
        useCaseProfile.unsubscribe();
        useCaseUpdateProfile.unsubscribe();
        useCaseUploadPhoto.unsubscribe();
        useCaseUserReview.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseGetAchivmients.unsubscribe();
        useCaseProfile.unsubscribe();
        useCaseUpdateProfile.unsubscribe();
        useCaseUploadPhoto.unsubscribe();
        useCaseUserReview.unsubscribe();
    }

    @Override
    public void onCompleted() {
        if (containerProfile != null)
            containerProfile.hideProgress();
        if (responseProfile != null) {
            containerProfile.getProfile(responseProfile);
            UserDataUtils userDataUtils = new UserDataUtils(BrandBeatApplication.getInstance().getBaseContext());
            userDataUtils.saveUserProfile(responseProfile);
            isLoadProfile = false;
            responseProfile = null;
        }
        if (reviews != null) {
            containerProfile.getUserReview(reviews);
            reviews = null;
        }
        if (responseAchievements != null) {
            containerProfile.setAchivmients(responseAchievements);
            responseAchievements = null;
        }
        if (responsePhotoUrl != null) {
            RequestUpdateProfile requestUpdateProfile = new RequestUpdateProfile();
            requestUpdateProfile.setPhoto(responsePhotoUrl.getPath());

            Observable.create(subscriber -> {
                InstanceID instanceID = InstanceID.getInstance(BrandBeatApplication.getInstance().getBaseContext());
                String token = null;
                try {
                    token = instanceID.getToken(BrandBeatApplication.getInstance().getBaseContext()
                            .getString(R.string.gcm_sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MetaRequest metaRequest = new MetaRequest(Utils.getHardwareId(), Utils.getVersion(), token);
                requestUpdateProfile.setMetaRequest(metaRequest);
                useCaseUpdateProfile.setRequestUpdateProfile(requestUpdateProfile);
                useCaseUpdateProfile.execute(this);

                return;
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new com.mgrmobi.brandbeat.interactors.base.DefaultSubscriber() {
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
;
            if (containerProfile != null)
                containerProfile.getPhotoUrl(responsePhotoUrl.getPath());
            responsePhotoUrl = null;
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerProfile.hideProgress();
    }

    @Override
    public void onNext(Object t) {
        if (((BaseResponse) t).getData() instanceof ResponseProfile) {
            responseProfile = (ResponseProfile) ((BaseResponse) t).getData();
            UserDataUtils userDataUtils = new UserDataUtils(BrandBeatApplication.getInstance().getBaseContext());
            userDataUtils.saveUserProfile(responseProfile);
            if (responsePhotoUrl != null) responseProfile.setPhoto(responsePhotoUrl.getPath());
        }
        if (((BaseResponse) t).getData() instanceof ResponsePhotoUrl) {
            responsePhotoUrl = ((ResponsePhotoUrl) ((BaseResponse) t).getData()).getResponsePhotoUrls().get(0);
        }
        else {
            if (((BaseResponse) t).getData() instanceof List) {
                List list = (List) ((BaseResponse) t).getData();
                if (list.get(0) instanceof ResponseAchievement) {
                    responseAchievements = (ArrayList<ResponseAchievement>) ((BaseResponse) t).getData();
                }
                else {
                    reviews = (ArrayList<ResponseReview>) ((BaseResponse) t).getData();
                    if (reviews != null && reviews.size() < Utils.PAGE_SIZE) {
                        isLast = true;
                    }
                    if (reviews != null && reviews.size() == 0) {
                        reviews = null;
                    }
                }
            }
        }
        containerProfile.hideProgress();
    }
}
