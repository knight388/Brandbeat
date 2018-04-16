package com.mgrmobi.brandbeat.ui.adapters;

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
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
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
public class RecentBrandAdapter extends RecyclerView.Adapter<RecentBrandAdapter.ViewHolder> {

    private List<ResponseBrand> brands;
    private Context context;

    public void setBrands(final List<ResponseBrand> brands) {
        this.brands = brands;
        notifyDataSetChanged();
    }

    public RecentBrandAdapter(Context context) {
        this.context = context;
        brands = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recent_brand, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setBrand(brands.get(i));
        viewHolder.view.setOnClickListener(v -> context.startActivity(NewBrandPagerActivtiy.createIntent(brands.get(i).getId(), brands.get(i).getSubCategoryId(),context)));
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
        //     @Bind(R.id.rating_text)
        //   TextView ratingText;
        @Bind(R.id.rating_widjet)
        RatingWidgetWithoutBackground ratingBar;
        @Bind(R.id.reviewed_count)
        TextView reviewedCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setBrand(ResponseBrand brand) {
            Picasso.with(context).load(brand.getImage(PhotoSize.SIZE_MIDDLE, context)).into(logo);
            nameBrand.setText(brand.getTitle());
            //ratingText.setText(brand.getAvgRate());

            ratingBar.setTextRating(Float.parseFloat(brand.getAvgRate()));
            if (brand.getReviewsSum() != null)
                reviewedCount.setText(brand.getReviewsSum() + " " + context.getString(R.string.reviewed));
            else
                reviewedCount.setText(brand.getRatedSum() + " " + context.getString(R.string.reviewed));
        }
    }
}
