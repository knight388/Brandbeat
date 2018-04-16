package com.mgrmobi.brandbeat.presenter;

import android.support.annotation.NonNull;

import com.mgrmobi.brandbeat.entity.Interests;
import com.mgrmobi.brandbeat.interactors.UseCaseDeleteInterests;
import com.mgrmobi.brandbeat.interactors.UseCaseInterests;
import com.mgrmobi.brandbeat.interactors.UseCaseSetCategory;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.presenter.base.BasePresenter;
import com.mgrmobi.brandbeat.ui.base.ContainerInterests;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class PresenterInterestsView extends BasePresenter {

    private ContainerInterests containerIntersts;
    private UseCaseInterests useCaseInterests = new UseCaseInterests();
    private UseCaseSetCategory useCaseSetCategory = new UseCaseSetCategory();
    private UseCaseDeleteInterests useCaseDeleteInterests = new UseCaseDeleteInterests();
    private ArrayList<ResponseCategories> interestses;
    private boolean isSetCategoryes = false;

    public void setView(@NonNull final ContainerInterests containerProfile) {
        this.containerIntersts = containerProfile;
    }

    public void getInterests() {
        useCaseInterests.execute(this);
    }

    public void setCategories(ArrayList<Interests> categories) {
        useCaseSetCategory.setCategoriesIds(categories);
        useCaseSetCategory.execute(this);
        isSetCategoryes = true;
    }

    public void deleteInterest(List<String> id)
    {
        useCaseDeleteInterests.setDeleteId(id);
        useCaseDeleteInterests.execute(this);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {
        useCaseDeleteInterests.unsubscribe();
        useCaseInterests.unsubscribe();
        useCaseSetCategory.unsubscribe();
    }

    @Override
    public void destroy() {
        useCaseDeleteInterests.unsubscribe();
        useCaseInterests.unsubscribe();
        useCaseSetCategory.unsubscribe();
    }

    @Override
    public void onCompleted() {
        if(interestses == null) return;
        containerIntersts.hideProgress();
        if(interestses != null && interestses.size() > 0) {
            containerIntersts.getInterest(interestses);
            interestses = null;
            return;
        }
        if(interestses.size() == 0)
        {
            if(isSetCategoryes)
            {
                isSetCategoryes = false;
                containerIntersts.onComplete();
            }
        }
        if(isSetCategoryes)
        {
            isSetCategoryes = false;
            containerIntersts.onComplete();
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        containerIntersts.hideProgress();
    }

    @Override
    public void onNext(Object t) {
        interestses = (ArrayList) ((BaseResponse) t).getData();
        containerIntersts.hideProgress();
    }
}
