package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.AnotherUserProfileActivity;
import com.mgrmobi.brandbeat.ui.activity.ReviewActivity;
import com.mgrmobi.brandbeat.ui.callbacks.LikeDeslikeCallBack;
import com.mgrmobi.brandbeat.ui.widget.hashTagTextView.HashTagTextView;
import com.mgrmobi.brandbeat.utils.UriBuilder;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class HashTagsAdapter extends RecyclerView.Adapter<HashTagsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ResponseReview> responseReviews;
    private LikeDeslikeCallBack likeCallBack;

    public HashTagsAdapter(Context context, ArrayList<ResponseReview> responseReviews, LikeDeslikeCallBack callBack) {
        this.responseReviews = responseReviews;
        this.context = context;
        likeCallBack = callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setValue(responseReviews.get(position));
    }

    @Override
    public int getItemCount() {
        return responseReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        public TextView nameTextView;
        @Bind(R.id.beat_rating)
        public RatingBar beats;
        @Bind(R.id.avatarImageView)
        public SimpleDraweeView icon;
        @Bind(R.id.logo_brand)
        public ImageView brandView;
        @Bind(R.id.raiting)
        public TextView ratingText;
        @Bind(R.id.about)
        public HashTagTextView reviewText;
        @Bind(R.id.count_like)
        public TextView likeCount;
        @Bind(R.id.count_comment)
        public TextView commentCount;
        @Bind(R.id.time_ago)
        public TextView timeAgo;
        @Bind(R.id.name_brand)
        public TextView nameBrand;
        @Bind(R.id.brand_view)
        public View brandLayout;
        @Bind(R.id.like_comment_view)
        public View likeCommentView;
        @Bind(R.id.icon_like)
        public ImageView iconLike;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValue(ResponseReview value) {
            context = itemView.getContext();
            if (value.getUser() != null && value.getUser().getUsername() != null) {
                final SpannableString out = new SpannableString(value.getUser().getUsername() + " " +
                        itemView.getContext().getString(R.string.reviewed) + " " + value.getResponseBrand().getTitle());
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                out.setSpan(boldSpan, 0, value.getUser().getUsername().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                StyleSpan boldSpan1 = new StyleSpan(Typeface.BOLD);
                out.setSpan(boldSpan1, (value.getUser().getUsername() +
                        context.getString(R.string.reviewed)).length() + 2, out.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                nameTextView.setText(out);
            }
            likeCommentView.setOnClickListener(v -> {

            });
            beats.setRating(Float.parseFloat(value.getResponseBrand().getAvgRate()) / 5);
            brandView.setImageResource(0);
            icon.setImageResource(0);
            if (value.getResponseBrand().getImage(PhotoSize.SIZE_MIDDLE, context) != null && !value.getResponseBrand().getImage(PhotoSize.SIZE_MIDDLE, context).equals("")) {
                Picasso.with(itemView.getContext()).load(value.getResponseBrand().getImage(PhotoSize.SIZE_MIDDLE, context)).into(brandView);
            }
            if (value.getUser() != null)
                if (value.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, context) != null && !value.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, context).equals("")) {
                    icon.setImageURI(UriBuilder.createUri(Uri.parse(value.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, context)), 50 + "", 50 + ""));
                }
            ratingText.setText(value.getResponseBrand().getAvgRate());
            reviewText.setText(value.getText());
            likeCount.setText(value.getLikes().size() + "");
            commentCount.setText(value.getComments().size() + "");
            if (value.getCreateAt() != null && !value.getCreateAt().equals("null"))
                timeAgo.setText(Utils.getTimeAgo(Long.parseLong(value.getCreateAt())));
            nameBrand.setText(value.getResponseBrand().getTitle());

            if (checkLike(value)) {
                iconLike.setImageResource(R.drawable.ic_like_no_active);
            }
            else {
                iconLike.setImageResource(R.drawable.ic_like_active);
            }
            likeCommentView.setOnClickListener(v -> {
                if (checkLike(value)) {
                    likeCallBack.likeReview(value.getId());
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
                    likeCallBack.dislikeReview(value.getId());
                    iconLike.setImageResource(R.drawable.ic_like_no_active);
                }
            });
            brandLayout.setOnClickListener(v -> context.startActivity(ReviewActivity.createIntent(value.getId(), value.getResponseBrand().getSubCategories().getId(),context)));
            icon.setOnClickListener(v -> context.startActivity(AnotherUserProfileActivity.createIntent(context, value.getUser().getId())));
        }

        private boolean checkLike(ResponseReview responseLocalFeed) {
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
}
