package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.WriteReviewActivity;
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
public class CompareBrandsAdapter extends RecyclerView.Adapter<CompareBrandsAdapter.ViewHolder> {

    private List<ResponseBrand> brands = new ArrayList<>();
    private Context context;

    public CompareBrandsAdapter(ArrayList<ResponseBrand> brands, Context context) {
        this.brands = brands;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_compare_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setValue(brands.get(i), context);
        if(i%2 == 0)
        {
            viewHolder.rootView.setBackgroundColor(context.getResources().getColor(R.color.compare_color_background));
        }
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name_brand)
        public TextView nameBrand;
        @Bind(R.id.about)
        public TextView about;
        @Bind(R.id.brand_logo)
        public ImageView brandLogo;
        @Bind(R.id.rate_text)
        public TextView rateText;
        @Bind(R.id.beat_rating)
        public RatingBar ratingBar;
        @Bind(R.id.rated_count)
        public TextView ratedCount;
        @Bind(R.id.review_count)
        public TextView reviewCount;
        @Bind(R.id.review)
        public Button reviewButton;
        @Bind(R.id.root_view)
        public View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValue(ResponseBrand value, Context context) {
            int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            rootView.getLayoutParams().width = widthPixels/2;
            if(value == null) return;
            nameBrand.setText(value.getTitle());
            if(value.getCategories() != null)
                about.setText(value.getCategories().getTitle());

            Picasso.with(context).load(value.getImage(PhotoSize.SIZE_MIDDLE, context)).into(brandLogo);
            rateText.setText(value.getAvgRate());
            if(value.getAvgRate() != null) {
                ratingBar.setRating(Float.parseFloat(value.getAvgRate()) / Utils.RATING_BAR_DELETE_VALUE);
            }
          //  ratedCount.setText(value.getRatedSum() + " " + context.getString(R.string.rated));
            reviewCount.setText(value.getRatedSum() + " " + context.getString(R.string.reviewed));
            reviewButton.setOnClickListener(v -> context.startActivity(WriteReviewActivity.createIntent(value.getId(), context)));
        }
    }
}
