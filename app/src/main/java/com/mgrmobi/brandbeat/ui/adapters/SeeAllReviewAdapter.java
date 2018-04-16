package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.ResponseStatistics;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.presenter.PresenterSeeAllReviews;
import com.mgrmobi.brandbeat.ui.activity.AnotherUserProfileActivity;
import com.mgrmobi.brandbeat.ui.activity.ReviewActivity;
import com.mgrmobi.brandbeat.ui.widget.RatingWidget;
import com.mgrmobi.brandbeat.ui.widget.hashTagTextView.HashTagTextView;
import com.mgrmobi.brandbeat.utils.Utils;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SeeAllReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private static final int FOOTER_ITEM = 2;
    private ArrayList<ResponseReview> reviews = new ArrayList<>();
    private ResponseBrand responseBrand;
    private ResponseStatistics responseStatistics;
    private PresenterSeeAllReviews presenterSeeAllReviews;
    private String brandId;

    public ResponseStatistics getResponseStatistics() {
        return responseStatistics;
    }

    public void setResponseStatistics(final ResponseStatistics responseStatistics) {
        this.responseStatistics = responseStatistics;
        notifyDataSetChanged();
    }

    public SeeAllReviewAdapter(Context context, ArrayList<ResponseReview> responseReviews, ResponseBrand responseBrand, PresenterSeeAllReviews presenterSeeAllReviews, String brandId) {
        reviews = responseReviews;
        this.responseBrand = responseBrand;
        this.presenterSeeAllReviews = presenterSeeAllReviews;
        this.brandId = brandId;
    }

    public void setReviews(final ArrayList<ResponseReview> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        if (position == reviews.size() + 1) {
            return FOOTER_ITEM;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = null;
        if (viewType == TYPE_HEADER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_all_review_item, parent, false);
            return new HeaderViewHolder(v);
        }
        if (viewType == TYPE_ITEM) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == 0) {
            HeaderViewHolder holder1 = (HeaderViewHolder) holder;
            holder1.setStatistics(responseStatistics);
        }
        else {
            ViewHolder holder1 = (ViewHolder) holder;
            holder1.setValue(reviews.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size() + 1;
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

        public void setValue(ResponseReview value) {
            Context context = itemView.getContext();
            String title = "";
            if (value.getUser().getFirstName() != null) {
                title = value.getUser().getFirstName() + " ";
            }
            if (value.getUser().getLastName() != null) {
                title += value.getUser().getLastName();
            }
            if (title.equals("") && value.getUser().getUsername() != null) {
                title = value.getUser().getUsername();
            }
            name.setText(title);

            ratingBar.setRating(Float.parseFloat(value.getRate()) / Utils.RATING_BAR_DELETE_VALUE);
            text.setText(value.getText());
            text.setOnClickListener(v1 -> {
                context.startActivity(ReviewActivity.createIntent(value.getId(), value.getResponseBrand().getSubCategories().getId(), context));
            });
            if (value.getCreateAt() != null && !value.getCreateAt().equals("null"))
                createAt.setText(Utils.getTimeAgo(Long.parseLong(value.getCreateAt())));
            rating.setTextRating(Float.parseFloat(value.getRate()));
            if (value.getUser() != null && value.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, itemView.getContext()) != null) {
                PipelineDraweeController controller = (PipelineDraweeController)
                        Fresco.newDraweeControllerBuilder()
                                .setImageRequest(Utils.createImageRequest(40, 40, Uri.parse(value.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, itemView.getContext()))))
                                .setOldController(((SimpleDraweeView) icon).getController())
                                .build();
                ((SimpleDraweeView) icon).setController(controller);
            }
            rootView.setOnClickListener(v -> context.startActivity(ReviewActivity.createIntent(value.getId(), value.getResponseBrand().getSubCategories().getId(),context)));
            icon.setOnClickListener(v -> context.startActivity(AnotherUserProfileActivity.createIntent(context, value.getUser().getId())));
            if (value.getPicturies(PhotoSize.SIZE_MIDDLE, context) != null && value.getPicturies(PhotoSize.SIZE_MIDDLE, context).size() > 0) {
                photoView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                photoView.setItemAnimator(itemAnimator);
                PhotoReviewAdapter photoReviewAdapter = new PhotoReviewAdapter(context, value.getPicturies(PhotoSize.SIZE_SMALL, context), null, (position) -> {
                }, value);
                photoView.setAdapter(photoReviewAdapter);
                photoView.setVisibility(View.VISIBLE);
            }
            else {
                photoView.setVisibility(View.GONE);
            }
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.rate_brand)
        TextView rateText;
        @Bind(R.id.beat_rating)
        RatingBar ratingBar;
        @Bind(R.id.rate_count)
        TextView rateCount;
        @Bind(R.id.review_count)
        TextView reviewCount;
        @Bind(R.id.count_five)
        TextView countFive;
        @Bind(R.id.count_four)
        TextView countFour;
        @Bind(R.id.count_three)
        TextView countThree;
        @Bind(R.id.count_two)
        TextView countTwo;
        @Bind(R.id.count_one)
        TextView countOne;
        @Bind(R.id.seek_five)
        ProgressBar ratingFive;
        @Bind(R.id.seek_four)
        ProgressBar ratingFour;
        @Bind(R.id.seek_three)
        ProgressBar ratingThree;
        @Bind(R.id.seek_two)
        ProgressBar ratingTwo;
        @Bind(R.id.seek_one)
        ProgressBar ratingOne;


        @Bind(R.id.all_layout)
        View allLayout;
        @Bind(R.id.all_text)
        TextView allText;
        @Bind(R.id.photo_text)
        TextView photoText;
        @Bind(R.id.photo_layout)
        View photoLayout;
       // @Bind(R.id.text_layout)
     //   View commentLayout;
    //    @Bind(R.id.comment_text)
      //  TextView comment;
        @Bind(R.id.one_layout)
        View oneLayout;
        @Bind(R.id.two_layout)
        View twoLayout;
        @Bind(R.id.three_layout)
        View threeLayout;
        @Bind(R.id.four_layout)
        View fourLayout;
        @Bind(R.id.five_layout)
        View fiveLayout;

        public HeaderViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setStatistics(ResponseStatistics statistics) {
            if (statistics == null) return;
            Context context = itemView.getContext();
            ratingFive.setProgress((int) ((statistics.getRates().get("5") / statistics.getReviewsSum()) * 100));
            ratingFour.setProgress((int) ((statistics.getRates().get("4") / statistics.getReviewsSum()) * 100));
            ratingThree.setProgress((int) ((statistics.getRates().get("3") / statistics.getReviewsSum()) * 100));
            ratingTwo.setProgress((int) ((statistics.getRates().get("2") / statistics.getReviewsSum()) * 100));
            ratingOne.setProgress((int) ((statistics.getRates().get("1") / statistics.getReviewsSum()) * 100));
            countFive.setText(statistics.getRates().get("5").toString().replace(".0", ""));
            countFour.setText(statistics.getRates().get("4").toString().replace(".0", ""));
            countThree.setText(statistics.getRates().get("3").toString().replace(".0", ""));
            countTwo.setText(statistics.getRates().get("2").toString().replace(".0", ""));
            countOne.setText(statistics.getRates().get("1").toString().replace(".0", ""));
            if(statistics == null || statistics.getAvgDubleValue() == null || statistics.getAvgDubleValue().equals("null"))
                rateText.setText("0.0");
            else
                rateText.setText(statistics.getAvgDubleValue() + "");
            if(statistics.getAvgDubleValue() != null)
                ratingBar.setRating((float) (statistics.getAvgDubleValue() / Utils.RATING_BAR_DELETE_VALUE));
          //  rateCount.setText(statistics.getReviewsSum().toString().replace(".0", "") + " " + itemView.getContext().getString(R.string.rated));
            reviewCount.setText(statistics.getReviewsWithTextSum().replace(".0", "") + " " + itemView.getContext().getString(R.string.reviews));
            allText.setText(context.getString(R.string.all) + " (" + statistics.getReviewsSum().toString().replace(".0", "") + ")");
            photoText.setText("(" + statistics.getReviewsImageSum() + ")");
      //    comment.setText("(" + statistics.getReviewsWithCommentsSum() + ")");
            allLayout.setOnClickListener(v -> {
                allNullView();
                presenterSeeAllReviews.getReviews(brandId, null, false, false);
                allLayout.setBackgroundResource(R.drawable.roundrect);
                allText.setTextColor(context.getResources().getColor(android.R.color.white));
            });
            photoLayout.setOnClickListener(v -> {
                allNullView();
                presenterSeeAllReviews.getReviews(brandId, null, true, false);
                photoLayout.setBackgroundResource(R.drawable.roundrect);
                photoText.setTextColor(context.getResources().getColor(android.R.color.white));
            });
  /*          commentLayout.setOnClickListener(v -> {
                allNullView();
                presenterSeeAllReviews.getReviews(brandId, null, false, true);
                commentLayout.setBackgroundResource(R.drawable.roundrect);
//                comment.setTextColor(context.getResources().getColor(android.R.color.white));
            });*/
            oneLayout.setOnClickListener(v -> {
                allNullView();
                presenterSeeAllReviews.getReviews(brandId, "1", false, false);
                oneLayout.setBackgroundResource(R.drawable.roundrect);
            });
            twoLayout.setOnClickListener(v -> {
                allNullView();
                presenterSeeAllReviews.getReviews(brandId, "2", false, false);
                twoLayout.setBackgroundResource(R.drawable.roundrect);
            });
            threeLayout.setOnClickListener(v -> {
                allNullView();
                presenterSeeAllReviews.getReviews(brandId, "3", false, false);
                threeLayout.setBackgroundResource(R.drawable.roundrect);
            });
            fourLayout.setOnClickListener(v -> {
                allNullView();
                presenterSeeAllReviews.getReviews(brandId, "4", false, false);
                fourLayout.setBackgroundResource(R.drawable.roundrect);
            });
            fiveLayout.setOnClickListener(v -> {
                allNullView();
                presenterSeeAllReviews.getReviews(brandId, "5", false, false);
                fiveLayout.setBackgroundResource(R.drawable.roundrect);
            });
        }

        private void allNullView() {
            Context context = itemView.getContext();
            allText.setTextColor(context.getResources().getColor(R.color.text_color_menu));
            photoText.setTextColor(context.getResources().getColor(R.color.text_color_menu));
      //      comment.setTextColor(context.getResources().getColor(R.color.text_color_menu));
            allLayout.setBackgroundResource(R.drawable.round_rect);
            photoLayout.setBackgroundResource(R.drawable.round_rect);
           // commentLayout.setBackgroundResource(R.drawable.round_rect);
            oneLayout.setBackgroundResource(R.drawable.round_rect);
            twoLayout.setBackgroundResource(R.drawable.round_rect);
            threeLayout.setBackgroundResource(R.drawable.round_rect);
            fourLayout.setBackgroundResource(R.drawable.round_rect);
            fiveLayout.setBackgroundResource(R.drawable.round_rect);
        }
    }
}
