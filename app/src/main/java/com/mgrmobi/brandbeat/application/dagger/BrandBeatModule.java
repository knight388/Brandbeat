package com.mgrmobi.brandbeat.application.dagger;

import android.content.Context;

import com.mgrmobi.brandbeat.location.bean.LocationBean;
import com.mgrmobi.brandbeat.location.impl.RxLocationBeanImpl;
import com.mgrmobi.brandbeat.network.RestApiBean;
import com.mgrmobi.brandbeat.network.RestApiTestBeanImp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */

@Module
public class BrandBeatModule {
    private final Context mAppContext;

    public BrandBeatModule(final Context mAppContext) {
        this.mAppContext = mAppContext;
    }

    @Provides
    @Singleton
    public final RestApiBean provideContext() {
        return new RestApiTestBeanImp(mAppContext);
    }

    @Provides
    @Singleton
    public final LocationBean provideLocation() {
        return new RxLocationBeanImpl(mAppContext);
    }

}
