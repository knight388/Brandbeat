package com.mgrmobi.brandbeat.ui.widget.brand_view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class LinearLayoutManagerWithPredictiveAnimationSupport extends LinearLayoutManager {
    private boolean supportPredictiveItemAnimation;

    public LinearLayoutManagerWithPredictiveAnimationSupport(Context context) {
        super(context);
    }

    public LinearLayoutManagerWithPredictiveAnimationSupport(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerWithPredictiveAnimationSupport(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setSupportPredictiveItemAnimation(boolean value){
        supportPredictiveItemAnimation = value;
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return supportPredictiveItemAnimation;
    }
}
