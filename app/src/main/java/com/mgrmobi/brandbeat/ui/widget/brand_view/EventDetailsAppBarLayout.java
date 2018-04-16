package com.mgrmobi.brandbeat.ui.widget.brand_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.view.View;

import com.mgrmobi.brandbeat.R;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class EventDetailsAppBarLayout extends AppBarLayout {
    private final int bottomOffset;

    public EventDetailsAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EventDetailsAppBarLayout, 0, 0);
        int offset = array.getDimensionPixelOffset(R.styleable.EventDetailsAppBarLayout_edabl_bottomOffset, 0);
        if(array.getBoolean(R.styleable.EventDetailsAppBarLayout_edabl_adjustStatusBarSize, false)){
            offset += getStatusBarHeight(context);
        }
        bottomOffset = offset;
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int currentHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        int newHeight = currentHeight - bottomOffset;
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(newHeight, View.MeasureSpec.EXACTLY));
    }

    private static int getStatusBarHeight(Context appContext) {
        int result = 0;
        int resourceId = appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
