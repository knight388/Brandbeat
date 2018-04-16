package com.mgrmobi.brandbeat.ui.widget.brand_view;

import android.animation.Animator;
import android.support.annotation.NonNull;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class AnimatorHelper {

    public static void cancelAllAnimators(@NonNull Animator... animator) {
        for (Animator v : animator) {
            if (v != null && v.isRunning()) {
                v.cancel();
            }
        }
    }
}