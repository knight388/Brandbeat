package com.mgrmobi.brandbeat.ui.widget.brand_view.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.mgrmobi.brandbeat.R;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ColorUtils  {

    @ColorInt
    public static int getColor(int baseColor, float alphaPercent) {
        int alpha = Math.round(Color.alpha(baseColor) * alphaPercent);

        return (baseColor & 0x00FFFFFF) | (alpha << 24);
    }

    @ColorInt
    public static int getColorFromResources(Context context, @ColorRes int colorId) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getResources().getColor(colorId, context.getTheme());
        } else {
            return context.getResources().getColor(colorId);
        }
    }
    @ColorInt
    public static int colorPrimary(Context context) {
        return getColorFromResources(context, R.color.color_primary);
    }
}
