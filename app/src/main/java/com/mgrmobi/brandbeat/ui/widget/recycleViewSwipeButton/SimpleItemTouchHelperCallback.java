package com.mgrmobi.brandbeat.ui.widget.recycleViewSwipeButton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.ui.base.BaseNavigationActivity;
import com.mgrmobi.brandbeat.utils.Utils;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP |
                    ItemTouchHelper.START | ItemTouchHelper.END;
            return makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, dragFlags);
        }
        else {
            final int dragFlags = ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.UP;
            return makeMovementFlags(ItemTouchHelper.ACTION_STATE_DRAG, dragFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.itemView.findViewById(R.id.brand_root).getLayoutParams();
        layoutParams.height = (int) (Utils.convertDpToPixel(260, BrandBeatApplication.getInstance().getApplicationContext()));
        layoutParams.width = (int) (Utils.convertDpToPixel(260, BrandBeatApplication.getInstance().getApplicationContext()));
        viewHolder.itemView.findViewById(R.id.brand_root).setLayoutParams(layoutParams);
        int[] position = new int[2];
        viewHolder.itemView.findViewById(R.id.brand_image).getLocationOnScreen(position);
        return true;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.itemView.findViewById(R.id.brand_root).getLayoutParams();
        layoutParams.height = (int) (Utils.convertDpToPixel(260, BrandBeatApplication.getInstance().getApplicationContext()));
        layoutParams.width = (int) (Utils.convertDpToPixel(260, BrandBeatApplication.getInstance().getApplicationContext()));
        viewHolder.itemView.findViewById(R.id.brand_root).setLayoutParams(layoutParams);
        int[] position = new int[2];
        viewHolder.itemView.findViewById(R.id.brand_image).getLocationOnScreen(position);
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition(), position);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.itemView.findViewById(R.id.brand_root).getLayoutParams();
        layoutParams.height = (int) (Utils.convertDpToPixel(260, BrandBeatApplication.getInstance().getApplicationContext()) - dY / 2);
        layoutParams.width = (int) (Utils.convertDpToPixel(280, BrandBeatApplication.getInstance().getApplicationContext()) - dY / 2);
        if (layoutParams.width > 0 && layoutParams.height > 0)
            viewHolder.itemView.findViewById(R.id.brand_root).setLayoutParams(layoutParams);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}