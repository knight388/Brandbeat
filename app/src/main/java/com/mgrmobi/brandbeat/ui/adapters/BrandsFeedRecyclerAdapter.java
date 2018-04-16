package com.mgrmobi.brandbeat.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseFeed;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.EditInterestsFromMenuActivity;
import com.mgrmobi.brandbeat.ui.activity.ReviewActivity;
import com.mgrmobi.brandbeat.ui.activity.SeeAllBrandInSubCategoryActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.mgrmobi.brandbeat.ui.callbacks.LikeDeslikeCallBack;
import com.mgrmobi.brandbeat.ui.view_holder.MyFeedViewHolder;
import com.mgrmobi.brandbeat.ui.widget.RatingWidget;
import com.mgrmobi.brandbeat.ui.widget.RatingWidgetWithoutBackground;
import com.mgrmobi.brandbeat.utils.UriBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class BrandsFeedRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_FOOTER = 2;
    private Context context;
    private List<ResponseFeed> responseFeeds;
    private List<ResponseBrand> brands = new ArrayList<>();
    private FooterViewHolder footerViewHolder;
    private LikeDeslikeCallBack likeDeslikeCallBack;

    private boolean nullHolder = false;

    public void setNullHolder(final boolean setNullHolder) {
        this.nullHolder = setNullHolder;
    }

    public List<ResponseBrand> getBrands() {
        return brands;
    }

    public void setBrands(final List<ResponseBrand> brands) {
        this.brands = brands;
        notifyDataSetChanged();
    }

    public BrandsFeedRecyclerAdapter(Context context, ArrayList<ResponseFeed> reviewItems, LikeDeslikeCallBack likeDeslikeCallBack) {
        this.context = context;
        this.responseFeeds = reviewItems;
        this.likeDeslikeCallBack = likeDeslikeCallBack;
    }

    public List<ResponseFeed> getReviewItems() {
        return responseFeeds;
    }

    public void setReviewItems(final List<ResponseFeed> reviewItems) {
        this.responseFeeds = reviewItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_feed_view, parent, false);
            return new HeadeViewHolder(v);
        }
        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_my_feed, parent, false);
            return new FooterViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_feed_root_item, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position == 0) {
            HeadeViewHolder holder1 = (HeadeViewHolder) holder;
            holder1.setSuggestedBrands((ArrayList<ResponseBrand>) brands, context);
        }
        else if (position == getItemCount() - 1) {
            footerViewHolder = (FooterViewHolder) holder;
            if (getItemCount() == 2) {
                footerViewHolder.rootView.setVisibility(View.VISIBLE);
            }
            else
                footerViewHolder.rootView.setVisibility(View.GONE);
        }
        else if (holder instanceof BrandsFeedRecyclerAdapter.ViewHolder) {
            ((ViewHolder) holder).setValue(responseFeeds.get(position - 1), context);

            ((ViewHolder) holder).frameLayout.removeAllViews();
            for(int i = 0; i < responseFeeds.get(position - 1).getReviews().size(); i++) {
                if (i == 2) break;
                ResponseReview responseReview = responseFeeds.get(position - 1).getReviews().get(i);
                View v = LayoutInflater.from(context).inflate(R.layout.my_feed_child_item, null);
                new MyFeedViewHolder(v, responseReview, context, likeDeslikeCallBack);
                final int finalI = i;
                v.setOnClickListener(v1 -> context.startActivity(ReviewActivity.createIntent(responseFeeds.get(position - 1).getReviews().get(finalI).getId(),
                        responseFeeds.get(position -1).getSubCategories().getId(), context)));

                ((ViewHolder) holder).frameLayout.addView(v);
            }

            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //   context.startActivity(NewBrandPagerActivtiy.createIntent(responseFeeds.get(position - 1).getId(), responseFeeds.get(position -1).getSubcategoryId(), context));
                }
            });
        }
    }

    class HeadeViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView brandsList;

        public HeadeViewHolder(final View itemView) {
            super(itemView);
            brandsList = (RecyclerView) itemView.findViewById(R.id.sugessted_view);

        }

        public void setSuggestedBrands(ArrayList<ResponseBrand> brands, Context context) {
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            brandsList.setItemAnimator(itemAnimator);
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            brandsList.setLayoutManager(layoutManager);
            brandsList.setAdapter(new SuggestsAdapter(brands, context));

            if (brands.size() == 0) {
                ((TextView) itemView.findViewById(R.id.suggest_brand)).setText("");
            }
            else
                ((TextView) itemView.findViewById(R.id.suggest_brand)).setText(context.getString(R.string.suggested_brend));
        }
    }

    @Override
    public int getItemCount() {
        return responseFeeds.size() + 2;
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public View progress;

        public FooterViewHolder(final View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.root_view);
            rootView.findViewById(R.id.add_brand).setOnClickListener(v ->
                    itemView.getContext().startActivity(EditInterestsFromMenuActivity
                            .createIntent(itemView.getContext())));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.title)
        public TextView nameUser;
        @Bind(R.id.second_title)
        public TextView secondTitle;
        @Bind(R.id.avatarImageView)
        public ImageView photo;
        @Bind(R.id.time_ago)
        public TextView timeAgo;
        @Bind(R.id.frame_view)
        public LinearLayout frameLayout;
        @Bind(R.id.rating_widjet)
        public RatingWidgetWithoutBackground beatView;
        @Bind(R.id.review_summ)
        public TextView reviewSum;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @TargetApi(Build.VERSION_CODES.M)
        public void setValue(ResponseFeed value, Context context) {
            if (value.getSubCategories() != null) {
                nameUser.setText(value.getTitle() + " / ");
                secondTitle.setText(value.getSubCategories().getTitle());

                nameUser.setOnClickListener(v ->
                        context.startActivity(NewBrandPagerActivtiy.createIntent(value.getId(), value.getSubCategories().getId(), itemView.getContext())));
                secondTitle.setOnClickListener(v ->
                        context.startActivity(SeeAllBrandInSubCategoryActivity.createIntent(context, value.getSubCategories().getId(), value.getSubCategories().getTitle())));
            }
            else {
                nameUser.setText(value.getTitle());
            }
            if(value != null && value.getSimpleImage() != null)
                photo.setImageURI(Uri.parse(value.getImage(PhotoSize.SIZE_MIDDLE, context)));
            photo.setOnClickListener(v ->
                    context.startActivity(NewBrandPagerActivtiy.createIntent(value.getId(), value.getSubCategories().getId(), itemView.getContext())));
            photo.setScaleType(ImageView.ScaleType.FIT_CENTER);
            beatView.setTextRating((float) (value.getAvgRate()));
            //    timeAgo.setText(value.getAvgRate() + "");
            if (value.getReviewSum() != null)
                reviewSum.setText(value.getReviewSum() + " " + context.getString(R.string.reviewed));
            else
                reviewSum.setText("0 " + context.getString(R.string.reviewed));
        }
    }

}