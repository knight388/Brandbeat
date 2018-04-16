package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.request.MetaRequest;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.network.request.RequestSocial;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseLogin;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseSocialLogin extends UseCase {
    private RequestSocial request;

    public void setRequest(String social, String acsessToken, String secret,RequestLocation location, MetaRequest metaRequest)
    {
        request = new RequestSocial(social, acsessToken, location, metaRequest);
        if(secret != null)
        request.setAccessTokenSecret(secret);
    }

    public void setRequest(RequestSocial request)
    {
        this.request = request;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        Observable<BaseResponse<ResponseLogin>> objectObservable = restApiBean.loginSocial(request);
        objectObservable.map(responseLogin -> responseLogin);
        return objectObservable;
    }
}
