package com.mgrmobi.brandbeat.ui.callbacks;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface MoveViewCallback
{
    public boolean callback(int[] ints, View moveView, MotionEvent event, int position, RecyclerView.ViewHolder viewHolder);
}
