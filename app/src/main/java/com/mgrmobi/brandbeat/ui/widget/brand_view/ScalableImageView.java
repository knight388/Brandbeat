package com.mgrmobi.brandbeat.ui.widget.brand_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ScalableImageView  extends ImageView {
    public ScalableImageView(Context context) {
        super(context);
    }

    public ScalableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScalableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScalableImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private final Matrix scaleMatrix = new Matrix();
    private float currentScale;
    private int centerWidth;
    private int centerHeight;

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

    public float getCurrentScale() {
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

