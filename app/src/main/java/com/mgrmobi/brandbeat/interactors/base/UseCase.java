package com.mgrmobi.brandbeat.interactors.base;

import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.interactors.DefaultSubscriber;
import com.mgrmobi.brandbeat.network.RestApiBean;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public abstract class UseCase {

    private Subscription subscription = Subscriptions.empty();

    @Inject
    protected RestApiBean restApiBean;

    public UseCase() {
        BrandBeatApplication.getInstance().getGraph().inject(this);
    }

    protected abstract Observable buildUseCaseObservable();

    /**
     * Executes the current use case.
     *
     * @param useCaseSubscriber The guy who will be listen to the observable build with {@link #buildUseCaseObservable()}.
     */
    @SuppressWarnings("unchecked")
    public void execute(Subscriber useCaseSubscriber) {
        execute(useCaseSubscriber, 0);
    }

    /**
     * Executes the current use case.
     *
     * @param useCaseSubscriber The guy who will be listen to the observable build with {@link #buildUseCaseObservable()}.
     */
    @SuppressWarnings("unchecked")
    public void execute(final Subscriber useCaseSubscriber, int times) {
        subscription = buildUseCaseObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(times)
                .subscribe(new DefaultSubscriber() {
                    @Override
                    public void onCompleted() {
                        useCaseSubscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null)
                            useCaseSubscriber.onError(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        useCaseSubscriber.onNext(o);
                    }
                });
    }

    /**
     * Unsubscribes from current {@link rx.Subscription}.
     */
    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
