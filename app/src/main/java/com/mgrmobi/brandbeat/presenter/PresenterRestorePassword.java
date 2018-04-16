package com.mgrmobi.brandbeat.presenter;

import com.mgrmobi.brandbeat.interactors.UseCaseRestorePssword;
import com.mgrmobi.brandbeat.interactors.base.DefaultSubscriber;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.ui.base.ContainerRestorePassword;

import javax.inject.Inject;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterRestorePassword extends DefaultSubscriber<BaseResponse> {
    private ContainerRestorePassword containerRestorePassword;
    private static final int SUCCESS_CODE = 0;
    private int code;
    public void setView(ContainerRestorePassword view)
    {
        containerRestorePassword = view;
    }
    @Inject
    UseCaseRestorePssword useCasePassword = new UseCaseRestorePssword();

    public void registration(String email)
    {
        containerRestorePassword.showProgress();
        useCasePassword.setEmail(email);
        useCasePassword.execute(this);
    }

    @Override
    public void onCompleted() {
        if(code == SUCCESS_CODE) {
            containerRestorePassword.success();
        }
        containerRestorePassword.hideProgress();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerRestorePassword.hideProgress();
        containerRestorePassword.showError(e);
    }

    @Override
    public void onNext(BaseResponse t)
     {
        code = (int) t.getCode();
        return;
    }

}
