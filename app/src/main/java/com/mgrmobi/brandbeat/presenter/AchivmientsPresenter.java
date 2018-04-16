package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.interactors.UseCaseGetAllAchivmients;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class AchivmientsPresenter extends BasePresenter {

    private ArrayList<ResponseAchievement> responseAchievements;
    private ContainerProfile containerProfile;

    private UseCaseGetAllAchivmients useCaseGetAllAchivmients = new UseCaseGetAllAchivmients();

    public void setView(ContainerProfile view) {
        containerProfile = view;
    }

    public void getAchivmients() {
        useCaseGetAllAchivmients.execute(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseGetAllAchivmients.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseGetAllAchivmients.unsubscribe();
    }


    @Override
    public void onCompleted() {
        if (responseAchievements != null) {
            containerProfile.setAchivmients(responseAchievements);
            responseAchievements = null;
        }

    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerProfile.hideProgress();
    }

    @Override
    public void onNext(Object t) {
        if (((BaseResponse) t).getData() instanceof List) {
            List list = (List) ((BaseResponse) t).getData();
            if (list.get(0) instanceof ResponseAchievement) {
                responseAchievements = (ArrayList<ResponseAchievement>) ((BaseResponse) t).getData();
            }
        }
        containerProfile.hideProgress();
    }

}
