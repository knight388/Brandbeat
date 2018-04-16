package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseLocalFeed;

import java.util.List;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseSuggestedBrand extends UseCase {

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.getSuggestedBrand();
    }
}
