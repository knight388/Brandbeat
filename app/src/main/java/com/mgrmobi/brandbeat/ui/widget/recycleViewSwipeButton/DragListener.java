package com.mgrmobi.brandbeat.ui.widget.recycleViewSwipeButton;

import android.support.v7.widget.RecyclerView;

import com.mgrmobi.brandbeat.network.responce.ResponseBrand;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface DragListener {
    void onDismiss(ResponseBrand responseBrand, int[] cordinats);
}
