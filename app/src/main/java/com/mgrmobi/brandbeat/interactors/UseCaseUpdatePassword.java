package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseUpdatePassword extends UseCase {

    private String oldPassword;
    private String newPssword;
    public void setPsswords(String oldPassword, String newPassword)
    {
        this.oldPassword = oldPassword;
        this.newPssword = newPassword;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.updatePassword(oldPassword, newPssword);
    }
}
