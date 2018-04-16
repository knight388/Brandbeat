package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseAddLikeReview;
import com.mgrmobi.brandbeat.interactors.UseCaseDeleteLike;
import com.mgrmobi.brandbeat.interactors.UseCaseLocalFeed;
import com.mgrmobi.brandbeat.interactors.UseCaseSuggestedBrand;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseLocalFeed;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerLocalFeed;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterLocalFeedView extends BasePresenter {

    private ContainerLocalFeed containerMyFeed;
    private ArrayList<ResponseLocalFeed> responseMyFeed;
    private ArrayList<ResponseBrand> suggestedBrands;
    private UseCaseLocalFeed useCaseMyFeed = new UseCaseLocalFeed();
    private UseCaseSuggestedBrand useCaseSuggestedBrand = new UseCaseSuggestedBrand();
    private UseCaseAddLikeReview useCaseAddLikeReview = new UseCaseAddLikeReview();
    private UseCaseDeleteLike useCaseDeleteLike = new UseCaseDeleteLike();
    private int page = 1;
    private String lat;
    private String lng;

    public void getSuggestedBrand() {
        useCaseSuggestedBrand.execute(this);
    }

    public void addLike(String reviewId) {
        useCaseAddLikeReview.setIdReview(reviewId);
        useCaseAddLikeReview.execute(this);
    }

    public void deleteLikeReview(String reviewId) {
        useCaseDeleteLike.setReviewId(reviewId);
        useCaseDeleteLike.execute(this);
    }

    public void getLocalFeed(String lat, String lng) {
        page = 1;
        this.lat = lat;
        this.lng = lat;
        useCaseMyFeed.setlocation(lat, lng, page + "");
        responseMyFeed = null;
        useCaseMyFeed.execute(this);
    }

    public void getNext() {
        page++;
        useCaseMyFeed.setlocation(lat, lng, page + "");
        useCaseMyFeed.execute(this);
    }

    public void setView(ContainerLocalFeed view) {
        containerMyFeed = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseAddLikeReview.unsubscribe();
        useCaseDeleteLike.unsubscribe();
        useCaseMyFeed.unsubscribe();
        useCaseSuggestedBrand.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseAddLikeReview.unsubscribe();
        useCaseDeleteLike.unsubscribe();
        useCaseMyFeed.unsubscribe();
        useCaseSuggestedBrand.unsubscribe();
    }


    @Override
    public void onCompleted() {
        containerMyFeed.hideProgress();
        if (responseMyFeed != null)
            containerMyFeed.getLocalFeed(responseMyFeed);
        if (suggestedBrands != null) {
            containerMyFeed.getSuggetedBrand(suggestedBrands);
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerMyFeed.hideProgress();
    }

    @Override
    public void onNext(Object t) {
        containerMyFeed.hideProgress();
        if (((BaseResponse) t).getData() instanceof ArrayList) {
            ArrayList list = (ArrayList) ((BaseResponse) t).getData();
            if (list.size() > 0) {
                if (list.size() > 0 && list.get(0) instanceof ResponseBrand) {
                    suggestedBrands = list;
                }
                else if (list.get(0) instanceof ResponseLocalFeed) {
                    if (responseMyFeed != null) {
                        responseMyFeed.addAll(list);
                    }
                    else
                        responseMyFeed = list;
                }
            }
        }
    }
}

