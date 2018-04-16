package com.mgrmobi.brandbeat.ui.widget.brand_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ScalableDraweeView  extends SimpleDraweeView {
    private final Matrix scaleMatrix = new Matrix();
    private float currentScale;
    private int centerWidth;
    private int centerHeight;

    public ScalableDraweeView(Context context) {
        super(context);
    }

    public ScalableDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public ScalableDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScalableDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScalableDraweeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerWidth = w >> 1;
        centerHeight = h >> 1;
    }

    public void setCurrentScale(float value) {
        if (Float.compare(currentScale, value) != 0) {
            scaleMatrix.reset();
            scaleMatrix.preScale(value, value, centerWidth, centerHeight);
            currentScale = value;
            invalidate();
        }
    }

    public float getCurrentScale(){
        return currentScale;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.concat(scaleMatrix);
        super.onDraw(canvas);
        canvas.restore();
    }
}

