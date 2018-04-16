package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseAddLikeReview;
import com.mgrmobi.brandbeat.interactors.UseCaseDeleteLike;
import com.mgrmobi.brandbeat.interactors.UseCaseMyFeed;
import com.mgrmobi.brandbeat.interactors.UseCaseSuggestedBrand;
import com.mgrmobi.brandbeat.interactors.UseCaseUpdateToken;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseFeed;
import com.mgrmobi.brandbeat.network.responce.ResponseLocalFeed;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerMyFeed;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterMyFeed extends BasePresenter {

    private ContainerMyFeed containerMyFeed;
    private ArrayList<ResponseFeed> responseMyFeed;
    private UseCaseMyFeed useCaseMyFeed = new UseCaseMyFeed();
    private UseCaseSuggestedBrand useCaseSuggestedBrand = new UseCaseSuggestedBrand();
    private ArrayList<ResponseBrand> suggestedBrands = new ArrayList<>();
    private UseCaseAddLikeReview useCaseAddLikeReview = new UseCaseAddLikeReview();
    private UseCaseDeleteLike useCaseDeleteLike = new UseCaseDeleteLike();
    private int page = 1;
    private boolean isLoad;

    public void setResponseMyFeed(final ArrayList<ResponseFeed> responseMyFeed) {
        this.responseMyFeed = responseMyFeed;
    }

    public void addLike(String idReview) {
        useCaseAddLikeReview.setIdReview(idReview);
        useCaseAddLikeReview.execute(this);
    }

    public void addDislike(String idReview) {
        useCaseDeleteLike.setReviewId(idReview);
        useCaseDeleteLike.execute(this);
    }

    public void getNewMyFeed() {
        responseMyFeed = null;
        isLoad = true;
        page = 1;
        useCaseMyFeed.setPage(page);
        useCaseMyFeed.execute(this);
    }

    public void getNextMyFeed() {
        if(responseMyFeed.size()% Utils.PAGE_SIZE != 0)
            return;
        if(isLoad == false)
            isLoad = true;
        else
            return;
        page++;
        useCaseMyFeed.setPage(page);
        useCaseMyFeed.execute(this);
    }

    public void setView(ContainerMyFeed view) {
        containerMyFeed = view;
    }

    public void getSuggestedBrands() {
        useCaseSuggestedBrand.execute(this);
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
            containerMyFeed.getMyFeed(responseMyFeed);
        if (suggestedBrands != null) {
            containerMyFeed.suggestedBrand(suggestedBrands);
        }
    }

    @Override
    public void onError(Throwable e) {
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
                else if (list.get(0) instanceof ResponseFeed) {
                    if (responseMyFeed != null) {
                        responseMyFeed.addAll(list);
                        isLoad = false;
                    }
                    else
                        responseMyFeed = list;
                }
            }
        }

    }
}
