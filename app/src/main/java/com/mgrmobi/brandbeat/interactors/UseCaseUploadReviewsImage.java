package com.mgrmobi.brandbeat.interactors;

import android.graphics.Bitmap;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseUploadReviewsImage extends UseCase {

    private List<Bitmap> bitmapList;

    public void setReview(List<Bitmap> bitmaps) {
        bitmapList = bitmaps;
    }

    @Override
    protected Observable<BaseResponse<ResponsePhotoUrl>> buildUseCaseObservable() {
        return restApiBean.updateReviewsImage((ArrayList<Bitmap>) bitmapList);
    }
}
