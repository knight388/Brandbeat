package com.mgrmobi.brandbeat.interactors;

import android.graphics.Bitmap;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseUploadPhoto extends UseCase
{
    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse<ResponsePhotoUrl>> objectObservable = restApiBean.updatePhoto(bitmap);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
