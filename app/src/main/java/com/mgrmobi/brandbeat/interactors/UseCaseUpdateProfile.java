package com.mgrmobi.brandbeat.interactors;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.request.MetaRequest;
import com.mgrmobi.brandbeat.network.request.RequestUpdateProfile;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.utils.Utils;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseUpdateProfile extends UseCase {
    private RequestUpdateProfile requestUpdateProfile;
    public void setRequestUpdateProfile(RequestUpdateProfile requestUpdateProfile)
    {
        this.requestUpdateProfile = requestUpdateProfile;

    }
    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.updateUser(requestUpdateProfile);
    }
}
