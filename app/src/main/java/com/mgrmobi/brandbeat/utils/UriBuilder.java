package com.mgrmobi.brandbeat.utils;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UriBuilder  {
    @Nullable
    public static Uri createUri(Uri orig, String width, @Nullable String height){
        if(orig == null){
            return null;
        }
        if(height != null) {
            return orig.buildUpon().appendQueryParameter("width", width).appendQueryParameter("height", height).build();
        }else{
            return orig.buildUpon().appendQueryParameter("width", width).build();
        }
    }
}
