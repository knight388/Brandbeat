package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.request.MetaRequest;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.network.request.RequestLogin;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseLogin;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseLogin extends UseCase {

    private RequestLogin request;

    public void setRequest(String login, String password, RequestLocation location, MetaRequest metaRequest)
    {
        login = login.replaceAll(" ", "");
        request = new RequestLogin(login, password, null, metaRequest);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.login(request);
    }
}
