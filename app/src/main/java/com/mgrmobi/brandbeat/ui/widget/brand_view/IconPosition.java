package com.mgrmobi.brandbeat.ui.widget.brand_view;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
@IntDef({
        IconPosition.START,
        IconPosition.TOP,
        IconPosition.END,
        IconPosition.BOTTOM,
        IconPosition.ALL
})
@Retention(RetentionPolicy.SOURCE)
public @interface IconPosition {
    int START = 0;
    int TOP = 1;
    int END = 2;
    int BOTTOM = 3;
    int ALL = 4;
}
