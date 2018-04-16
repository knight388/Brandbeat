package com.mgrmobi.brandbeat.ui.adapters.best_ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.callbacks.FollowInbrandCallBack;
import com.mgrmobi.brandbeat.ui.widget.RatingWidget;
import com.mgrmobi.brandbeat.ui.widget.RatingWidgetWithoutBackground;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NewSeeAllBrandsAdapter extends RecyclerView.Adapter<NewSeeAllBrandsAdapter.ViewHolder> {

    private List<ResponseBrand> brands;
    private Context context;
    private FollowInbrandCallBack followInbrandCallBack;

    public void setBrands(final List<ResponseBrand> brands) {
        this.brands = brands;
        notifyDataSetChanged();
    }

    public NewSeeAllBrandsAdapter(Context context, FollowInbrandCallBack followInbrandCallBack) {
        this.context = context;
        brands = new ArrayList<>();
        this.followInbrandCallBack = followInbrandCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recent_brand, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setBrand(brands.get(i));
        viewHolder.view.setOnClickListener(v -> {
            if(brands.get(i).iSubscriber()) {
                followInbrandCallBack.unFollowBrand(brands.get(i).getId());
                viewHolder.checkedView.setVisibility(View.GONE);
                brands.get(i).setiSubscriber(false);
            }
            else {
                followInbrandCallBack.followBrand(brands.get(i).getId());
                viewHolder.checkedView.setVisibility(View.VISIBLE);
                brands.get(i).setiSubscriber(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.root_view)
        View view;
        @Bind(R.id.logo_brand)
        ImageView logo;
        @Bind(R.id.name_brand)
        TextView nameBrand;
        @Bind(R.id.rating_widjet)
        RatingWidgetWithoutBackground ratingBar;
        @Bind(R.id.reviewed_count)
        TextView reviewedCount;
        @Bind(R.id.checked)
        View checkedView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setBrand(ResponseBrand brand) {
            Picasso.with(context).load(brand.getImage(PhotoSize.SIZE_MIDDLE, context)).into(logo);
            nameBrand.setText(brand.getTitle());
            ratingBar.setTextRating(Float.parseFloat(brand.getAvgRate()));
            if (brand.getAvgRate() != null && !brand.getAvgRate().equals("null"))
                ratingBar.setTextRating(Float.parseFloat(brand.getAvgRate()));
            if (brand.getReviewsSum() != null)
                reviewedCount.setText(brand.getReviewsSum() + " " + context.getString(R.string.reviewed));
            else
                reviewedCount.setText(brand.getRatedSum() + " " + context.getString(R.string.reviewed));
            if (brand.iSubscriber()) {
                checkedView.setVisibility(View.VISIBLE);
            }
            else checkedView.setVisibility(View.GONE);
        }
    }
}
