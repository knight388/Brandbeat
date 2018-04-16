package com.mgrmobi.brandbeat.presenter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.mgrmobi.brandbeat.interactors.UseCaseUpdateReview;
import com.mgrmobi.brandbeat.interactors.UseCaseUploadReviewsImage;
import com.mgrmobi.brandbeat.interactors.UseCaseWriteReview;
import com.mgrmobi.brandbeat.network.request.RequestReview;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerWriteReview;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterWriteReview extends BasePresenter {
    private ContainerWriteReview containerWriteReview;
    private UseCaseUpdateReview useCaseUpdateReview = new UseCaseUpdateReview();
    private UseCaseWriteReview useCaseWriteReview = new UseCaseWriteReview();
    private UseCaseUploadReviewsImage useCaseUploadReviewsImage = new UseCaseUploadReviewsImage();

    private boolean uploadPhoto = false;
    private ArrayList<ResponsePhotoUrl> responsePhotoUrls = new ArrayList<>();

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        useCaseUpdateReview.unsubscribe();
        useCaseUploadReviewsImage.unsubscribe();
        useCaseWriteReview.unsubscribe();
    }

    public void uploadPhotoUrls(ArrayList<Bitmap> bitmaps) {
        useCaseUploadReviewsImage.setReview(bitmaps);
        containerWriteReview.showProgress();
        useCaseUploadReviewsImage.execute(this);
    }

    public void setView(@NonNull final ContainerWriteReview containerWriteReview) {
        this.containerWriteReview = containerWriteReview;
    }

    public void getReview(RequestReview requestReview) {
        useCaseWriteReview.setReview(requestReview);
        containerWriteReview.showProgress();
        useCaseWriteReview.execute(this);
    }

    public void updateReview(RequestReview requestReview) {
        useCaseUpdateReview.setRequestReview(requestReview);
        containerWriteReview.showProgress();
        useCaseUpdateReview.execute(this);
    }

    @Override
    public void onCompleted() {
        if (uploadPhoto) {
            containerWriteReview.hideProgress();
            containerWriteReview.success(responsePhotoUrls);
            uploadPhoto = false;
        }
        else {
            containerWriteReview.hideProgress();
            containerWriteReview.success();
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerWriteReview.hideProgress();
        containerWriteReview.showError(e.getMessage());
    }

    @Override
    public void onNext(Object t) {
        if (((BaseResponse) t).getData() instanceof ResponsePhotoUrl) {
            if (((ResponsePhotoUrl) ((BaseResponse) t).getData()).getResponsePhotoUrls().size() > 0) {
                responsePhotoUrls = ((ResponsePhotoUrl) ((BaseResponse) t).getData()).getResponsePhotoUrls();
                uploadPhoto = true;
            }
        }
    }

}
