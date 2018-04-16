package com.mgrmobi.brandbeat.ui.widget.brand_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.mgrmobi.brandbeat.R;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class EventDetailsRecyclerView  extends RecyclerView {
    private LinearLayoutManagerWithPredictiveAnimationSupport recyclerViewManager;
    private Subscription scrollObservableSubscription;
    private final int topOffset;

    public EventDetailsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EventDetailsRecyclerView, 0, 0);
        topOffset = array.getDimensionPixelOffset(R.styleable.EventDetailsRecyclerView_edrv_topOffset, 0);
        array.recycle();
    }

    public EventDetailsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EventDetailsRecyclerView, defStyle, 0);
        topOffset = array.getDimensionPixelOffset(R.styleable.EventDetailsRecyclerView_edrv_topOffset, 0);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int currentHeight = View.MeasureSpec.getSize(heightSpec);
        int newHeight = currentHeight - topOffset;
        super.onMeasure(widthSpec, View.MeasureSpec.makeMeasureSpec(newHeight, View.MeasureSpec.EXACTLY));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            init();
            scrollObservableSubscription = createScrollObservable()
                    .distinctUntilChanged()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(value -> {}, Throwable::printStackTrace);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isInEditMode()) {
            if (scrollObservableSubscription != null) {
                scrollObservableSubscription.unsubscribe();
                scrollObservableSubscription = null;
            }
        }
    }

    public void init() {
        recyclerViewManager = new LinearLayoutManagerWithPredictiveAnimationSupport(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewManager.setSupportPredictiveItemAnimation(true);
        setLayoutManager(recyclerViewManager);
    }

    public Observable<Integer> createScrollObservable() {
        return Observable.create(subscriber -> {
            RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
                private int pastVisibleItem, visibleItemCount, totalItemCount;

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    visibleItemCount = recyclerViewManager.getChildCount();
                    totalItemCount = recyclerViewManager.getItemCount();
                    pastVisibleItem = recyclerViewManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisibleItem) >= totalItemCount) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(totalItemCount);
                        }
                    }
                }
            };
            addOnScrollListener(scrollListener);
            subscriber.add(Subscriptions.create(() -> removeOnScrollListener(scrollListener)));
        });
    }

}
