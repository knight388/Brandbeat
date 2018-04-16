package com.mgrmobi.brandbeat.ui.widget.brand_view.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class WidgetUtils {
    public static final float NO_SCALE = 1.0f;

    public static Bitmap blurBitmap(Context appContext, Bitmap origBitmap, int scaleFactor, @IntRange(from = 1, to = 25) int blurFactor) {
        Bitmap toBlur;
        if (scaleFactor != NO_SCALE) {
            toBlur = Bitmap.createBitmap(origBitmap.getWidth() / scaleFactor, origBitmap.getHeight() / scaleFactor, Bitmap.Config.ARGB_8888);
            Matrix m = new Matrix();
            float scale = 1f / scaleFactor;
            m.preScale(scale, scale, 0, 0);
            Canvas canvas = new Canvas(toBlur);
            canvas.setMatrix(m);
            canvas.drawBitmap(origBitmap, 0, 0, null);
        } else {
            toBlur = origBitmap;
        }
        Bitmap blurredBitmap = Bitmap.createBitmap(toBlur.getWidth(), toBlur.getHeight(), Bitmap.Config.ARGB_8888);
        return blurredBitmap;
    }

    public static void gone(@Nullable View... views) {
        if (views != null) {
            for (View v : views) {
                if (v != null && v.getVisibility() == View.VISIBLE) {
                    v.setVisibility(View.GONE);
                }
            }
        }
    }
}

