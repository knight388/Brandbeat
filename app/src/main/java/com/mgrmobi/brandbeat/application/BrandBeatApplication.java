package com.mgrmobi.brandbeat.application;

import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.util.SparseIntArray;

import com.crashlytics.android.Crashlytics;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpNetworkFetcher;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.memory.PoolConfig;
import com.facebook.imagepipeline.memory.PoolFactory;
import com.facebook.imagepipeline.memory.PoolParams;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.dagger.BrandBeatGraph;
import com.mgrmobi.brandbeat.application.dagger.BrandBeatModule;
import com.mgrmobi.brandbeat.application.dagger.DaggerBrandBeatGraph;
import com.mgrmobi.brandbeat.interactors.UseCaseUpdateToken;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseLogin;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import rx.Subscriber;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;


/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */

public class BrandBeatApplication extends MultiDexApplication {

    private static BrandBeatApplication instance;
    public BrandBeatGraph brandBeatGraph;

    @Override
    public void onCreate() {
        super.onCreate();
//dragon        RxResult.register(this);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setNetworkFetcher(new OkHttpNetworkFetcher(okHttpClient))
                .setResizeAndRotateEnabledForNetwork(true)
                .build();

        Fresco.initialize(this, config);
        Fabric.with(this, new Crashlytics(), new Twitter(new TwitterAuthConfig(getString(R.string.twitter_api_key), getString(R.string.twitter_api_consumer_secret))));
        instance = this;
        initGraph();
        UserDataUtils userDataUtils = new UserDataUtils(this);
        String string = userDataUtils.getUserData(UserDataUtils.EXPIRED_DATA);

        if (string != null && !string.equals("")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(string));
            long dayCount = (Calendar.getInstance().getTimeInMillis() - calendar.getTime().getTime()) / (24 * 3600 * 1000);
            if (dayCount > -4) {
                UseCaseUpdateToken useCaseUpdateToken = new UseCaseUpdateToken();
                useCaseUpdateToken.execute(new Subscriber() {
                    @Override
                    public void onCompleted() {
                        return;
                    }

                    @Override
                    public void onError(final Throwable e) {
                        return;
                    }

                    @Override
                    public void onNext(final Object o) {
                        UserDataUtils response = new UserDataUtils(BrandBeatApplication.getInstance().getBaseContext());
                        if (((BaseResponse) o).getData() instanceof ResponseLogin)
                            response.saveUserData((ResponseLogin) ((BaseResponse) o).getData());
                    }
                });
            }
        }
    }

    private void initGraph() {
        brandBeatGraph = DaggerBrandBeatGraph.builder()
                .brandBeatModule(new BrandBeatModule(this))
                .build();
    }

    public static BrandBeatApplication getInstance() {
        return instance;
    }

    public BrandBeatGraph getGraph() {
        return brandBeatGraph;
    }

}
