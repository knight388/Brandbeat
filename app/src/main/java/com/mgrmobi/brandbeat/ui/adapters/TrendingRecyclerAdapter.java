package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseLocalFeed;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.AnotherUserProfileActivity;
import com.mgrmobi.brandbeat.ui.activity.ReviewActivity;
import com.mgrmobi.brandbeat.ui.activity.SeeAllBrandInSubCategoryActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.mgrmobi.brandbeat.ui.callbacks.LikeDeslikeCallBack;
import com.mgrmobi.brandbeat.ui.widget.hashTagTextView.HashTagTextView;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
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
public class TrendingRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_FOOTER = 2;
    private Context context;
    private List<ResponseLocalFeed> reviewItems;
    private List<ResponseBrand> brands = new ArrayList<>();
    private FooterViewHolder footerViewHolder;
    private LikeDeslikeCallBack callBack;

    public List<ResponseBrand> getBrands() {
        return brands;
    }

    public void setBrands(final List<ResponseBrand> brands) {
        this.brands = brands;
        notifyDataSetChanged();
    }

    public TrendingRecyclerAdapter(Context context, ArrayList<ResponseLocalFeed> reviewItems, LikeDeslikeCallBack callBack) {
        this.context = context;
        this.reviewItems = reviewItems;
        this.callBack = callBack;
    }

    public List<ResponseLocalFeed> getReviewItems() {
        return reviewItems;
    }

    public void setReviewItems(final List<ResponseLocalFeed> reviewItems) {
        this.reviewItems = reviewItems;
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_feed_item, parent, false);
            return new FooterViewHolder(v);
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
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
        else if (holder instanceof TrendingRecyclerAdapter.ViewHolder) {
            ((ViewHolder) holder).setValue(reviewItems.get(position - 1), context);
        }
    }

    @Override
    public int getItemCount() {
        return reviewItems.size() + 2;
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

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        TextView nameTextView;
        @Bind(R.id.beat_rating)
        RatingBar beats;
        @Bind(R.id.avatarImageView)
        SimpleDraweeView iconUser;
        @Bind(R.id.logo_brand)
        ImageView brandView;
        @Bind(R.id.raiting)
        TextView ratingText;
        @Bind(R.id.about)
        HashTagTextView reviewText;
        @Bind(R.id.count_like)
        TextView likeCount;
        @Bind(R.id.count_comment)
        TextView commentCount;
        @Bind(R.id.time_ago)
        TextView timeAgo;
        @Bind(R.id.name_brand)
        TextView nameBrand;
        @Bind(R.id.brand_view)
        View brandLayout;
        @Bind(R.id.like_click)
        View likeCommentView;
        @Bind(R.id.icon_like)
        public ImageView iconLike;
        @Bind(R.id.photo_view)
        public RecyclerView photoView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setValue(ResponseLocalFeed value, Context context) {
            if (value.getProfile() != null && value.getProfile().getUsername() != null) {
                String name = "";
                if (value.getProfile().getFirstName() != null)
                    name = value.getProfile().getFirstName() + " ";
                if (value.getProfile().getLastName() != null)
                    name += value.getProfile().getLastName();
                if (name.equals(""))
                    name = value.getProfile().getUsername();
                final SpannableString out = new SpannableString(name + " " +
                        context.getString(R.string.reviewed) + " " + value.getResponseBrand().getTitle());
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                out.setSpan(boldSpan, 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                StyleSpan boldSpan1 = new StyleSpan(Typeface.BOLD);
                out.setSpan(boldSpan1, (name +
                        context.getString(R.string.reviewed)).length() + 2, out.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                nameTextView.setText(out);
                nameTextView.setOnClickListener(v -> context.startActivity(AnotherUserProfileActivity.createIntent(context, value.getProfile().getId())));
            }
            if (checkLike(value)) {
                iconLike.setImageResource(R.drawable.ic_like_no_active);
            }
            else {
                iconLike.setImageResource(R.drawable.ic_like_active);
            }
            likeCommentView.setOnClickListener(v -> {
                if (checkLike(value)) {
                    callBack.likeReview(value.getId());
                    UserDataUtils userDataUtils = new UserDataUtils(context);
                    String id = userDataUtils.getUserData(UserDataUtils.KEY_USER_ID);
                    value.addLike(id);
                    likeCount.setText(value.getLikes().size() + "");
                    iconLike.setImageResource(R.drawable.ic_like_active);
                }
                else {
                    UserDataUtils userDataUtils = new UserDataUtils(context);
                    String id = userDataUtils.getUserData(UserDataUtils.KEY_USER_ID);
                    value.deslike(id);
                    likeCount.setText(value.getLikes().size() + "");
                    callBack.dislikeReview(value.getId());
                    iconLike.setImageResource(R.drawable.ic_like_no_active);
                }
            });
            beats.setRating(Float.parseFloat(value.getResponseBrand().getAvgRate()) / 5);
            brandView.setImageResource(0);
            brandView.setOnClickListener(v1 -> {
                context.startActivity(NewBrandPagerActivtiy.createIntent(value.getBrandId(), value.getResponseBrand().getSubCategoryId(), context));
            });
            if (value.getProfile() != null)
                if (value.getProfile().getPhoto(PhotoSize.SIZE_MIDDLE, context) != null) {
                    PipelineDraweeController controller = (PipelineDraweeController)
                            Fresco.newDraweeControllerBuilder()
                                    .setImageRequest(Utils.createImageRequest(50, 50, Uri.parse(value.getProfile().getPhoto(PhotoSize.SIZE_MIDDLE, context))))
                                    .setOldController((iconUser).getController())
                                    .build();
                    (iconUser).setController(controller);
                }
            if (value.getResponseBrand().getImage(PhotoSize.SIZE_MIDDLE, context) != null && !value.getResponseBrand().getImage(PhotoSize.SIZE_MIDDLE, context).equals("")) {
                Picasso.with(context).load(value.getResponseBrand().getImage(PhotoSize.SIZE_MIDDLE, context)).into(brandView);
            }
            iconUser.setOnClickListener(v -> {
                context.startActivity(AnotherUserProfileActivity.createIntent(context, value.getProfile().getId()));
            });
            ratingText.setText(value.getRate());
            reviewText.setText(value.getText());
            reviewText.setOnClickListener(v1 -> {
                context.startActivity(ReviewActivity.createIntent(value.getId(),value.getResponseBrand().getSubCategories().getId(), context));
            });
            likeCount.setText(value.getLikes().size() + "");
            commentCount.setText(value.getComments().size() + "");
            if (value.getCreatedAt() != null && !value.getCreatedAt().equals("null"))
                timeAgo.setText(Utils.getTimeAgo(Long.parseLong(value.getCreatedAt())));


            Spannable span = new SpannableString(value.getResponseBrand().getTitle() + " / "
                    + value.getResponseBrand().getSubCategories().getTitle());
            span.setSpan(new RelativeSizeSpan(0.8f), (value.getResponseBrand().getTitle() + " / ").length(),
                    (value.getResponseBrand().getTitle() + " / " + value.getResponseBrand().getSubCategories().getTitle()).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
     /*       ClickableSpan clickableSpan1 = new ClickableSpan() {
                @Override
                public void onClick(final View widget) {
                    context.startActivity(NewBrandPagerActivtiy.createIntent(value.getResponseBrand().getId(), value.getResponseBrand().getSubCategoryId(), context));
                }
            };
            span.setSpan(clickableSpan1, (value.getResponseBrand().getTitle() + " / ").length(),
                    (value.getResponseBrand().getTitle() + " / " + value.getResponseBrand().getSubCategories().getTitle()).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       */     ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(final View widget) {
                    context.startActivity(SeeAllBrandInSubCategoryActivity.createIntent(context,
                            value.getResponseBrand().getSubCategoryId(), value.getResponseBrand().getSubCategories().getTitle()));
                }
            };
            span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_primary)),
                    (value.getResponseBrand().getTitle() + " / ").length(), (value.getResponseBrand().getTitle()
                            + " / " + value.getResponseBrand().getSubCategories().getTitle()).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(clickableSpan, (value.getResponseBrand().getTitle() + " / ").length(), (value.getResponseBrand().getTitle()
                            + " / " + value.getResponseBrand().getSubCategories().getTitle()).length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            nameBrand.setText(span);
            nameBrand.setMovementMethod(LinkMovementMethod.getInstance());
            if (value.getImage(PhotoSize.SIZE_MIDDLE, context) != null && value.getImage(PhotoSize.SIZE_MIDDLE, context).size() > 0) {
                photoView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                photoView.setItemAnimator(itemAnimator);
                PhotoBrandsFeedAdapter photoReviewAdapter = new PhotoBrandsFeedAdapter(context, value.getImage(PhotoSize.SIZE_MIDDLE, context), (position) -> {
                });
                photoView.setAdapter(photoReviewAdapter);
                photoView.setVisibility(View.VISIBLE);
            }
            else {
                photoView.setVisibility(View.GONE);
            }
        }

        private boolean checkLike(ResponseLocalFeed responseLocalFeed) {
            UserDataUtils userDataUtils = new UserDataUtils(context);
            String id = userDataUtils.getUserData(UserDataUtils.KEY_USER_ID);
            for(int i = 0; i < responseLocalFeed.getLikes().size(); i++) {
                if (id.equals(responseLocalFeed.getLikes().get(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public View progress;

        public FooterViewHolder(final View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.root_view);
        }
    }
}
