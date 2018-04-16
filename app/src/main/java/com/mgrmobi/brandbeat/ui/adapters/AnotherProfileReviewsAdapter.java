package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mgrmobi.brandbeat.R;

import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.ReviewActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.mgrmobi.brandbeat.ui.callbacks.FollowCallBack;
import com.mgrmobi.brandbeat.ui.widget.RatingWidget;
import com.mgrmobi.brandbeat.ui.widget.hashTagTextView.HashTagTextView;
import com.mgrmobi.brandbeat.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class AnotherProfileReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private static final int FOOTER_ITEM = 2;
    private ArrayList<ResponseReview> reviews = new ArrayList<>();
    private Context context;
    private ResponseProfile responseProfile;
    private FollowCallBack followCallBack;
    private FooterViewHolder footerViewHolder;

    public AnotherProfileReviewsAdapter(Context context, ArrayList<ResponseReview> responseReviews, FollowCallBack followCallBack) {
        this.context = context;
        reviews = responseReviews;
        this.followCallBack = followCallBack;
    }

    public ResponseProfile getResponseProfile() {
        return responseProfile;
    }

    public void setResponseProfile(final ResponseProfile responseProfile) {
        this.responseProfile = responseProfile;
    }

    public void setProfile(ResponseProfile profile) {
        responseProfile = profile;
    }

    public void setReviews(final ArrayList<ResponseReview> reviews) {
        if(reviews.size() > 0) {
            this.reviews.addAll(reviews);
        }
        else {
            this.reviews = reviews;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return TYPE_HEADER;
        }
        if(position == reviews.size() + 1) {
            return FOOTER_ITEM;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = null;
        if(viewType == TYPE_HEADER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_another_user_adapter, parent, false);
            return new HeadeViewHolder(v);
        }
        if(viewType == TYPE_ITEM) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        }
        if(viewType == FOOTER_ITEM) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_progress_item, parent, false);
            return new FooterViewHolder(v);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(position == 0) {
            HeadeViewHolder holder1 = (HeadeViewHolder) holder;
            holder1.setProfile(responseProfile);
        }
        else if(position == reviews.size()+1){
            footerViewHolder = (FooterViewHolder) holder;
        }
        else
        {
            ViewHolder holder1 = (ViewHolder) holder;
           holder1.setValue(reviews.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size() + 2;//header and footer
    }

    public void showViewNextLoad()
    {
        footerViewHolder.rootView.setVisibility(View.VISIBLE);
    }

    public void dismissViewHolder()
    {
        footerViewHolder.rootView.setVisibility(View.GONE);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        public TextView name;
        @Bind(R.id.user_picture)
        public ImageView icon;
        @Bind(R.id.beat_rating)
        public RatingBar ratingBar;
        @Bind(R.id.text)
        public HashTagTextView text;
        @Bind(R.id.time_ago)
        public TextView createAt;
        @Bind(R.id.rating_widjet)
        public RatingWidget rating;
        @Bind(R.id.root_view)
        public View rootView;
        @Bind(R.id.photo_view)
        public RecyclerView photoView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValue(ResponseReview value)
        {
            if(value.getResponseBrand() != null && value.getResponseBrand().getTitle() != null)
                name.setText(value.getResponseBrand().getTitle());
            if(value.getResponseBrand() != null && value.getResponseBrand().getImage(PhotoSize.SIZE_SMALL, context) != null)
                Picasso.with(context).load(value.getResponseBrand().getImage(PhotoSize.SIZE_SMALL, context)).into(icon);
            ratingBar.setRating(Float.parseFloat(value.getRate()) / Utils.RATING_BAR_DELETE_VALUE);
            text.setText(value.getText());
            text.setOnClickListener(v1 -> {context.startActivity(ReviewActivity.createIntent(value.getId(), value.getResponseBrand().getSubCategories().getId(), context));});
            if(value.getCreateAt() != null && !value.getCreateAt().equals("null"))
                createAt.setText(Utils.getTimeAgo(Long.parseLong(value.getCreateAt())));
            rating.setTextRating(Float.parseFloat(value.getRate()));
            rootView.setOnClickListener(v -> context.startActivity(ReviewActivity.createIntent(value.getId(), value.getResponseBrand().getSubCategories().getId(), context)));
            icon.setOnClickListener(v -> context.startActivity(NewBrandPagerActivtiy.createIntent(value.getResponseBrand().getId(), value.getResponseBrand().getSubCategoryId(), context)));
            if (value.getPicturies(PhotoSize.SIZE_MIDDLE , context) != null && value.getPicturies(PhotoSize.SIZE_MIDDLE , context).size() > 0) {
                photoView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                photoView.setItemAnimator(itemAnimator);
                PhotoReviewAdapter photoReviewAdapter = new PhotoReviewAdapter(context, value.getPicturies(PhotoSize.SIZE_SMALL , context), null, (position) -> {
                }, value);
                photoView.setAdapter(photoReviewAdapter);
                photoView.setVisibility(View.VISIBLE);
            }
            else {
                photoView.setVisibility(View.GONE);
            }
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public View rootView;

        public FooterViewHolder(final View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.root_view);
        }
    }

    class HeadeViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.photo_image)
        public SimpleDraweeView photo;
        @Bind(R.id.location)
        public TextView location;
        @Bind(R.id.followers_count)
        public TextView followersCount;
        @Bind(R.id.reviews_count)
        public TextView reviewsCount;
        @Bind(R.id.photos_count)
        public TextView photosCount;
        @Bind(R.id.review_title)
        public View reviewTitle;
        @Bind(R.id.follow_user)
        public FloatingActionButton followUser;

        public HeadeViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setProfile(ResponseProfile profile) {
            if(profile == null)
                return;
            if(profile.getPhoto(PhotoSize.SIZE_BIG , context) != null && !profile.getPhoto(PhotoSize.SIZE_BIG, context).equals(""))
                photo.setImageURI(Uri.parse(profile.getPhoto(PhotoSize.SIZE_BIG, context)));
            if(profile.getResponseAddress() != null) {
                location.setText(profile.getResponseAddress().getString());
            }
            followersCount.setText(profile.getFollowerSum());
            reviewsCount.setText(profile.getReviewSum());
            photosCount.setText(profile.getReviewImageSum());
            if(profile.isSubscriber()) {
                followUser.setImageResource(R.drawable.follow_unselect);
            }
            else {
                followUser.setImageResource(R.drawable.follow_select);
            }
            followUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if(profile.isSubscriber()) {
                        followCallBack.unFollow();
                    }
                    else {
                        followCallBack.follow();
                    }
                }
            });
        }
    }
}

