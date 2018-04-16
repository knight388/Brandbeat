package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.callbacks.MoveViewCallback;
import com.mgrmobi.brandbeat.ui.widget.RatingWidget;
import com.mgrmobi.brandbeat.ui.widget.RatingWidgetWithoutBackground;
import com.mgrmobi.brandbeat.ui.widget.recycleViewSwipeButton.ItemTouchHelperAdapter;
import com.mgrmobi.brandbeat.ui.widget.recycleViewSwipeButton.DragListener;
import com.mgrmobi.brandbeat.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ChooseBrandsForCompareAdapter extends RecyclerView.Adapter<ChooseBrandsForCompareAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private List<ResponseBrand> brands = new ArrayList<>();

    private final DragListener dragStartListener;

    public Context context;

    public int dismissCount;

    public MoveViewCallback moveViewCallback;

    public ChooseBrandsForCompareAdapter(Context context, DragListener dragStartListener, ArrayList<ResponseBrand> brands, MoveViewCallback moveViewCallback) {
        this.dragStartListener = dragStartListener;
        this.brands.addAll(brands);
        this.context = context;
        this.moveViewCallback = moveViewCallback;
    }

    public List<ResponseBrand> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<ResponseBrand> brands) {
        this.brands = brands;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.compare_brand_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChooseBrandsForCompareAdapter.ItemViewHolder holder, final int position) {
        holder.setValue(brands.get(position), context);
        holder.handleView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                int[] ints = new int[2];
                v.getLocationOnScreen(ints);
                moveViewCallback.callback(ints, v, event, position, holder);
            }
            return true;
        });
    }

    public void addBrand(ResponseBrand responseBrand, int position) {
        if (position > getItemCount()) {
            brands.add(responseBrand);
        }
        else {
            brands.add(position, responseBrand);
        }
        notifyItemInserted(position);
    }

    @Override
    public void onItemDismiss(int position, int[] coordinats) {
        notifyItemChanged(position);
        dragStartListener.onDismiss(brands.get(position), coordinats);
        brands.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.brand_root)
        public View handleView;
        @Bind(R.id.brand_image)
        public ImageView brandImage;
        @Bind(R.id.rating_widjet)
        public RatingWidgetWithoutBackground beatRatingText;
        @Bind(R.id.name_brand)
        public TextView nameBrand;
        @Bind(R.id.review_count)
        public TextView mReviewCount;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValue(ResponseBrand value, Context context) {
            if (value == null) return;
            if (value.getImage(PhotoSize.SIZE_SMALL, context) == null) {
                brandImage.setImageResource(R.drawable.icon512);
            }
            else {
                Picasso.with(context).load(value.getImage(PhotoSize.SIZE_MIDDLE, context)).into(brandImage);
            }

            beatRatingText.setTextRating(Float.parseFloat(value.getAvgRate()));
            nameBrand.setText(value.getTitle());
            if (value.getReviewsSum() != null)
                mReviewCount.setText(value.getReviewsSum() + " " + context.getString(R.string.reviewed));
            else
                mReviewCount.setText("");
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) handleView.getLayoutParams();
            layoutParams.height = (int) (Utils.convertDpToPixel(260, BrandBeatApplication.getInstance().getApplicationContext()));
            layoutParams.width = (int) (Utils.convertDpToPixel(260, BrandBeatApplication.getInstance().getApplicationContext()));
            handleView.setLayoutParams(layoutParams);
        }
    }
}