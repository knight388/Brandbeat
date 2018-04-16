package com.mgrmobi.brandbeat.interactors.base;

import android.content.Intent;

import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.ui.activity.LoginActivity;

import rx.android.schedulers.AndroidSchedulers;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class DefaultSubscriber<T> extends rx.Subscriber<T> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        if (e != null && e.getMessage() != null)
            if (e.getMessage().equals("001-004")) {
                if (AndroidSchedulers.mainThread() != null)
                    AndroidSchedulers.mainThread().createWorker().schedule(() -> {
                        Intent intent = LoginActivity.createIntent(BrandBeatApplication.getInstance().getBaseContext());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        BrandBeatApplication.getInstance().startActivity(intent);
                    });
            }
    }

    @Override
    public void onNext(T t) {
        // no-op by default.
    }
}