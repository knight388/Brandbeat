package com.mgrmobi.brandbeat.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.SeeAllBrandInSubCategoryActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.mgrmobi.brandbeat.ui.fragment.BrandsFeedsFragment;
import com.mgrmobi.brandbeat.ui.fragment.LocalFeedFragment;
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
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<ResponseBrand> brands;
    private Context context;

    public void setBrands(final List<ResponseBrand> brands) {
        this.brands = brands;
        notifyDataSetChanged();
    }

    public SearchAdapter(Context context) {
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
        viewHolder.view.setOnClickListener(v -> context.startActivity(NewBrandPagerActivtiy.createIntent(brands.get(i).getId(), brands.get(i).getSubCategoryId(), context)));
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        public void setBrand(ResponseBrand brand) {
            Picasso.with(context).load(brand.getImage(PhotoSize.SIZE_MIDDLE, context)).into(logo);

            Spannable span = new SpannableString(brand.getTitle() + " / " + brand.getSubCategories().getTitle());
            span.setSpan(new RelativeSizeSpan(0.8f), (brand.getTitle() + " / ").length(),
                    (brand.getTitle() + " / " + brand.getSubCategories().getTitle()).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_primary)),
                    (brand.getTitle() + " / ").length(),
                    (brand.getTitle() + " / " + brand.getSubCategories().getTitle()).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new ClickableSpan() {
                             @Override
                             public void onClick(final View widget) {
                                 context.startActivity(SeeAllBrandInSubCategoryActivity.createIntent
                                         (context, brand.getSubCategories().getId(), brand.getSubCategories().getTitle()));
                             }
                         }, (brand.getTitle() + " / ").length(),
                    (brand.getTitle() + " / " + brand.getSubCategories().getTitle()).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nameBrand.setText(span);
            nameBrand.setMovementMethod(LinkMovementMethod.getInstance());
            //         ratingText.setText(brand.getAvgRate());
            if (brand.getAvgRate() != null) {
                ratingBar.setTextRating(Float.parseFloat(brand.getAvgRate()));
            }
            if (brand.getReviewsSum() != null) {
                reviewedCount.setText(brand.getReviewsSum() + " " + context.getString(R.string.reviewed));
            }
        }
    }
}
