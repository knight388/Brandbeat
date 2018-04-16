package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseStatistics;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseGetStatisticsReviewBrand extends UseCase
{
    private String brandId;
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }


    @Override
    protected Observable<BaseResponse<ResponseStatistics>> buildUseCaseObservable() {
        return restApiBean.getBrandReviewStatistics(brandId);
    }
}
