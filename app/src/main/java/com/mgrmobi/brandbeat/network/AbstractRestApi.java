package com.mgrmobi.brandbeat.network;

import android.content.Context;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public abstract class AbstractRestApi {

    protected final Context appContext;

    public AbstractRestApi(Context context) {
        appContext = context;
    }
}
