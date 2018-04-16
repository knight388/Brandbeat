package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseFilterReview;
import com.mgrmobi.brandbeat.interactors.UseCaseGetStatisticsReviewBrand;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.ResponseStatistics;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerReviewView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterSeeAllReviews extends BasePresenter {
    private UseCaseGetStatisticsReviewBrand useCaseGetStatisticsReviewBrand = new UseCaseGetStatisticsReviewBrand();
    private ContainerReviewView containerReviewView;
    private UseCaseFilterReview useCaseFilterReview = new UseCaseFilterReview();
    private int page = 1;
    private ResponseStatistics responseStatistics;
    private ArrayList<ResponseReview> responseReviews = new ArrayList<>();

    public void setView(ContainerReviewView view) {
        containerReviewView = view;
    }

    public void getReviews(String brandId, String rate, boolean withImage, boolean withComment) {
        useCaseFilterReview.setParams(brandId, rate, String.valueOf(page), withImage, withComment);
        useCaseFilterReview.execute(this);
        containerReviewView.showProgress();
    }

    public void getStatistics(String brandId) {
        useCaseGetStatisticsReviewBrand.setBrandId(brandId);
        useCaseGetStatisticsReviewBrand.execute(this);
        containerReviewView.showProgress();
    }

    public void getNext() {
        page++;
        useCaseFilterReview.nextPage(String.valueOf(page));
        useCaseFilterReview.execute(this);
        containerReviewView.showProgress();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onCompleted() {
        if (responseStatistics != null) {
            containerReviewView.setStatistics(responseStatistics);
            responseStatistics = null;
            return;
        }
        containerReviewView.setReview(responseReviews);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
    }

    @Override
    public void onNext(Object t) {
        if (((BaseResponse) t).getData() instanceof ResponseStatistics) {
            responseStatistics = (ResponseStatistics) ((BaseResponse) t).getData();
        }
        if (((BaseResponse) t).getData() instanceof List) {
            responseReviews = (ArrayList<ResponseReview>) ((BaseResponse) t).getData();
        }
    }
}
