package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseFilterReview extends UseCase {

    private String brandId;
    private String rate;
    private String page;
    private boolean withText;
    private boolean withComments;

    public void setParams(String brandId, String rate, String page,
                          boolean withText, boolean withComments) {
        this.brandId = brandId;
        this.rate = rate;
        this.page = page;
        this.withText = withText;
        this.withComments = withComments;
    }

    public void nextPage(String page) {
        this.page = page;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.getReviewsFromParamentrs(brandId, rate, page, withText, withComments);
    }
}
