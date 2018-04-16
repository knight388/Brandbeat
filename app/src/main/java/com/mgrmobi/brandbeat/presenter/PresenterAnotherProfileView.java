package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.interactors.UseCaseFollowUser;
import com.mgrmobi.brandbeat.interactors.UseCaseProfile;
import com.mgrmobi.brandbeat.interactors.UseCaseUnFollowBrand;
import com.mgrmobi.brandbeat.interactors.UseCaseUnFollowUser;
import com.mgrmobi.brandbeat.interactors.UseCaseUserReview;
import com.mgrmobi.brandbeat.network.request.RequestUpdateProfile;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerAnotherUserProfile;
import com.mgrmobi.brandbeat.ui.base.ContainerProfile;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterAnotherProfileView extends BasePresenter {
    private UseCaseProfile useCaseProfile = new UseCaseProfile();
    private UseCaseUserReview useCaseUserReview = new UseCaseUserReview();
    private UseCaseFollowUser useCaseFollowUser = new UseCaseFollowUser();
    private UseCaseUnFollowUser useCaseUnFollowUser = new UseCaseUnFollowUser();
    private ContainerAnotherUserProfile containerAnotherUserProfile;
    private ResponseProfile responseProfile = null;
    private ArrayList<ResponseReview> reviews = null;
    private boolean subscribe = false;
    private boolean unSubscribe = false;
    private int page = 1;
    private boolean isLast;

    public void setView(ContainerAnotherUserProfile view) {
        this.containerAnotherUserProfile = view;
    }

    public void getProfile(String userId) {
        useCaseProfile.setRequest(userId);
        useCaseProfile.execute(this);
        containerAnotherUserProfile.showProgress();
    }

    public void getUserReviews(String userId) {
        useCaseUserReview.setUserId(userId);
        useCaseUserReview.execute(this);
        containerAnotherUserProfile.showProgress();
    }

    public boolean getNextUserReviews() {
        if(isLast) return false;
        page++;
        useCaseUserReview.setPage(page + "");
        useCaseUserReview.execute(this);
        containerAnotherUserProfile.showPaginationProgress();
        return true;
    }

    public void follow(String userId) {
        useCaseFollowUser.setId(userId);
        useCaseFollowUser.execute(this);
        containerAnotherUserProfile.showProgress();
        subscribe = true;
    }

    public void unFollow(String userId) {
        useCaseUnFollowUser.setId(userId);
        useCaseUnFollowUser.execute(this);
        containerAnotherUserProfile.showProgress();
        unSubscribe = true;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseFollowUser.unsubscribe();
        useCaseProfile.unsubscribe();
        useCaseUnFollowUser.unsubscribe();
        useCaseUserReview.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseFollowUser.unsubscribe();
        useCaseProfile.unsubscribe();
        useCaseUnFollowUser.unsubscribe();
        useCaseUserReview.unsubscribe();
    }

    @Override
    public void onCompleted() {
        containerAnotherUserProfile.hideProgress();
        if(responseProfile != null) {
            containerAnotherUserProfile.getProfile(responseProfile);
            responseProfile = null;
        }
        if(reviews != null) {
            containerAnotherUserProfile.getReviews(reviews);
            reviews = null;
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerAnotherUserProfile.hideProgress();
        if(unSubscribe) {
            containerAnotherUserProfile.unSubscribe();
            unSubscribe = false;
        }
    }

    @Override
    public void onNext(Object t) {
        if(subscribe) {
            containerAnotherUserProfile.subscribe();
            subscribe = false;
        }
        if(unSubscribe) {
            containerAnotherUserProfile.unSubscribe();
            unSubscribe = false;
        }

        if(((BaseResponse) t).getData() instanceof ResponseProfile) {
            responseProfile = (ResponseProfile) ((BaseResponse) t).getData();
        }
        else {
            reviews = (ArrayList<ResponseReview>) ((BaseResponse) t).getData();
            if(reviews.size() < Utils.PAGE_SIZE) {
                isLast = true;
            }
            if(reviews.size() == 0) {
                reviews = null;
            }
        }
        containerAnotherUserProfile.hideProgress();
    }
}
