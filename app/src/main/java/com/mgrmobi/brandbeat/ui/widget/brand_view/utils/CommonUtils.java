package com.mgrmobi.brandbeat.ui.widget.brand_view.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class CommonUtils {
    @Nullable
    public static Uri parseUri(String toParse) {
        if (TextUtils.isEmpty(toParse)) {
            return null;
        }
        try {
            return Uri.parse(toParse);
        } catch (Exception e) {
            return null;
        }
    }
    public static boolean isRevealSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static int getStatusBarHeight(Context appContext) {
        int result = 0;
        int resourceId = appContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = appContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
