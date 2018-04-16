package com.mgrmobi.brandbeat.application.dagger;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.location.impl.RxLocationBeanImpl;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.BaseNavigationActivity;
import com.mgrmobi.brandbeat.utils.UserDataUtils;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */

@Singleton
@Component(modules = {BrandBeatModule.class})

public interface BrandBeatGraph {

    void inject(UseCase u);

    void inject(BaseActivity a);

    void inject(BaseNavigationActivity baseNavigationActivity);

    void inject(RxLocationBeanImpl rxLocationBean);

    void inject(UserDataUtils userDataUtils);
}
