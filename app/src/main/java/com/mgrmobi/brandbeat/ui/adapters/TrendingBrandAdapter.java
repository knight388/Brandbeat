package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.NewBrandActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.mgrmobi.brandbeat.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class TrendingBrandAdapter extends RecyclerView.Adapter<TrendingBrandAdapter.ViewHolder> {

    private Context context;
    private List<ResponseBrand> responseReviews;

    public TrendingBrandAdapter(Context context, List<ResponseBrand> responseReviews) {
        this.responseReviews = responseReviews;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_trending_brand, parent, false);//trending_brand_item
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setValue(responseReviews.get(position), position);
        holder.itemView.setOnClickListener(v -> context.startActivity(NewBrandPagerActivtiy.createIntent(responseReviews.get(position).getId(), responseReviews.get(position).getSubCategoryId(), context)));

    }

    @Override
    public int getItemCount() {
        return responseReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.position_number)
        public TextView position;
        @Bind(R.id.beat_rating)
        public RatingBar brandRating;
        @Bind(R.id.arrow_icon)
        public ImageView arrowIcon;
        @Bind(R.id.change_position)
        public TextView changePosition;
        @Bind(R.id.icon_brand)
        public ImageView iconBrend;
        @Bind(R.id.name_brand)
        public TextView nameBrand;
        @Bind(R.id.rating_text)
        public TextView ratingText;
        @Bind(R.id.root_view)
        public View view;
        @Bind(R.id.review_count)
        public TextView reviewCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValue(ResponseBrand value, int position) {
            Context context = itemView.getContext();
            ratingText.setText(value.getAvgRate());
            brandRating.setRating(Float.parseFloat(value.getAvgRate()) / Utils.RATING_BAR_DELETE_VALUE);
            nameBrand.setText(value.getTitle());
            Picasso.with(context).load(value.getImage(PhotoSize.SIZE_MIDDLE, context)).placeholder(R.drawable.icon512).into(iconBrend);
            this.position.setText(position + 1 + " ");

            if (value.getResponseTranding() == null) return;
            if (value.getResponseTranding().getChangeIndex() == null) {
                changePosition.setText(context.getString(R.string.new_trending));
            }
            else if (value.getResponseTranding().getChangeIndex().equals("0"))
                changePosition.setText("");
            else {
                changePosition.setText(value.getResponseTranding().getChangeIndex());
            }
            if (value.getResponseTranding().getChangeIndex() != null && !value.getResponseTranding().getChangeIndex().equals("0")) {
                if (Integer.parseInt(value.getResponseTranding().getChangeIndex()) < 0) {
                    arrowIcon.setImageResource(R.drawable.ic_trending_dawn_image);
                }
                else if (Integer.parseInt(value.getResponseTranding().getChangeIndex()) > 0) {
                    arrowIcon.setImageResource(R.drawable.ic_trending_up_image);
                }
                else {
                    arrowIcon.setImageResource(0);
                }
            }
            else {
                arrowIcon.setImageResource(0);
            }

            if (value.getReviewsSum() != null)
                reviewCount.setText(value.getReviewsSum() + " " + context.getString(R.string.reviewed));
            else
                reviewCount.setText(value.getRatedSum() + " " + context.getString(R.string.reviewed));
        }
    }
}
