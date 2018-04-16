package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.AnotherUserProfileActivity;
import com.mgrmobi.brandbeat.ui.activity.ChooseBradsForCompareActivity;
import com.mgrmobi.brandbeat.ui.activity.ReviewActivity;
import com.mgrmobi.brandbeat.ui.activity.SeeAllReviewActivity;
import com.mgrmobi.brandbeat.ui.activity.WriteReviewActivity;
import com.mgrmobi.brandbeat.ui.callbacks.FollowCallBack;
import com.mgrmobi.brandbeat.ui.widget.RatingBrandView;
import com.mgrmobi.brandbeat.ui.widget.RatingWidget;
import com.mgrmobi.brandbeat.ui.widget.hashTagTextView.HashTagTextView;
import com.mgrmobi.brandbeat.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ReviewInBrandRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private List<ResponseReview> responseReviewList;
    private HeaderViewHolder headerViewHolder;
    private FollowCallBack followCallBack;
    private ResponseBrand responseBrand;
    public ReviewInBrandRecyclerAdapter(List<ResponseReview> responseReviews, FollowCallBack followCallBack) {
        responseReviewList = responseReviews;
        this.followCallBack = followCallBack;
    }

    public void setResponseReviewList(List<ResponseReview> responseReviewList) {
        this.responseReviewList = responseReviewList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v;
        if (viewType == TYPE_HEADER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_header, parent, false);
            headerViewHolder = new HeaderViewHolder(v);
            return headerViewHolder;
        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
            return new ReviewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position == 0) {
            ((HeaderViewHolder)holder).setValue(responseBrand, followCallBack);
        }
        else if (holder instanceof ReviewHolder) {
            ((ReviewHolder) holder).setValue(responseReviewList.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return responseReviewList.size() + 1;
    }

    public void setHeader(ResponseBrand brand) {
        responseBrand = brand;
        if (headerViewHolder != null)
            headerViewHolder.setValue(brand, followCallBack);
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image)
        public ImageView imageView;
        @Bind(R.id.about)
        public TextView about;
        @Bind(R.id.rating_view)
        public RatingBrandView ratingBrandView;
        @Bind(R.id.follow)
        public Button follow;
        @Bind(R.id.write_review)
        public Button writeReview;
        @Bind(R.id.compare)
        public View compare;
        @Bind(R.id.see_all)
        public View seeAll;

        public HeaderViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValue(ResponseBrand value, FollowCallBack followCallBack) {
            Context context = itemView.getContext();
            if(value == null) return;
            if (value.getAvgRate() != null)
                ratingBrandView.setRating(Double.parseDouble(value.getAvgRate()));
            ratingBrandView.setReviewSumm(value.getReviewsSum());
            Picasso.with(context).load(value.getImage(PhotoSize.SIZE_BIG, context)).into(imageView);
            about.setText(value.getText());
            ratingBrandView.setRatedSum(value.getAvgRateSumm());
            if (!value.iSubscriber()) {
                follow.setText(context.getString(R.string.follow));
                follow.setOnClickListener(v -> {
                    followCallBack.follow();
                });
            }
            else {
                follow.setText(context.getString(R.string.un_follow));
                follow.setOnClickListener(v -> {
                    followCallBack.unFollow();
                });
            }
            writeReview.setOnClickListener(v -> {
                context.startActivity(WriteReviewActivity.createIntent(value.getId(), context));
            });
            compare.setOnClickListener(v1 ->
                    context.startActivity(ChooseBradsForCompareActivity.createIntent(context, value.getId(), value.getSubCategoryId())));
            imageView.setVisibility(View.GONE);
            seeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    context.startActivity(SeeAllReviewActivity.createIntent(context, value.getId()));
                }
            });
        }
    }


    static class ReviewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        TextView nameUser;
        @Bind(R.id.user_picture)
        ImageView photo;
        @Bind(R.id.time_ago)
        TextView timeAgo;
        @Bind(R.id.rating_widjet)
        RatingWidget rating;
        @Bind(R.id.text)
        HashTagTextView about;
        @Bind(R.id.beat_rating)
        RatingBar beatView;
        @Bind(R.id.root_view)
        View rootView;
        @Bind(R.id.photo_view)
        public RecyclerView photoView;

        public ReviewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValue(ResponseReview value) {
            String nameString = "";
            if (value.getUser() != null) {
                if (value.getUser().getFirstName() != null) {
                    nameString = value.getUser().getFirstName() + " ";
                }
                if (value.getUser().getLastName() != null) {
                    nameString += value.getUser().getLastName();
                }
                if (nameString.equals("")) {
                    nameString = value.getUser().getUsername();
                }
            }
            nameUser.setText(nameString);
            photo.setImageResource(R.drawable.icon512);
            if (value.getUser() != null)
                if (value.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, itemView.getContext()) != null) {
                    PipelineDraweeController controller = (PipelineDraweeController)
                            Fresco.newDraweeControllerBuilder()
                                    .setImageRequest(Utils.createImageRequest(40, 40, Uri.parse(value.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, itemView.getContext()))))
                                    .setOldController(((SimpleDraweeView) photo).getController())
                                    .build();
                    ((SimpleDraweeView) photo).setController(controller);
                }
            if (value.getCreateAt() != null && !value.getCreateAt().equals("null")) {
                try {
                    timeAgo.setText(Utils.getTimeAgo(Long.parseLong(value.getCreateAt())));
                } catch (NumberFormatException e) {
                }
            }
            rating.setTextRating(Float.parseFloat(value.getRate()));
            about.setText(value.getText());
            Context context = itemView.getContext();
            beatView.setRating((float) ((Double.parseDouble(value.getRate())) / Utils.RATING_BAR_DELETE_VALUE));

            about.setOnClickListener(v1 ->
                    context.startActivity(ReviewActivity.createIntent(value.getId(), value.getSubCategoryId(), context)));
            photo.setOnClickListener(v ->
                    context.startActivity(AnotherUserProfileActivity.createIntent(context, value.getUser().getId())));
            nameUser.setOnClickListener(v ->
                    context.startActivity(AnotherUserProfileActivity.createIntent(context, value.getUser().getId())));
            beatView.setOnClickListener(v ->
                    context.startActivity(ReviewActivity.createIntent(value.getId(), value.getSubCategoryId(), context)));
            rating.setOnClickListener(v ->
                    context.startActivity(ReviewActivity.createIntent(value.getId(), value.getResponseBrand().getSubCategories().getId(), context)));

            if (value.getPicturies(PhotoSize.SIZE_MIDDLE, context) != null && value.getPicturies(PhotoSize.SIZE_MIDDLE, context).size() > 0) {
                photoView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                photoView.setItemAnimator(itemAnimator);
                PhotoReviewAdapter photoReviewAdapter = new PhotoReviewAdapter(context, value.getPicturies(PhotoSize.SIZE_MIDDLE, context), null, (position) -> {
                }, value);
                photoView.setAdapter(photoReviewAdapter);
                photoView.setVisibility(View.VISIBLE);
            }
            else {
                photoView.setVisibility(View.GONE);
            }
        }
    }

}
