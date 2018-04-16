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
import android.text.style.StyleSpan;
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
import com.mgrmobi.brandbeat.network.responce.ResponseComment;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.AddCommentActivity;
import com.mgrmobi.brandbeat.ui.activity.AnotherUserProfileActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.mgrmobi.brandbeat.ui.callbacks.CommentAdapterCallback;
import com.mgrmobi.brandbeat.ui.widget.hashTagTextView.HashTagTextView;
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
public class CommentReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private Context context;
    private ArrayList<ResponseComment> responseComments;
    private CommentAdapterCallback commentAdapterCallback;
    private ResponseReview responseReview;

    public CommentReviewAdapter(Context context, ArrayList<ResponseComment> commentArrayList, CommentAdapterCallback callback) {
        this.context = context;
        responseComments = commentArrayList;
        commentAdapterCallback = callback;
    }

    public ArrayList<ResponseComment> getResponseComments() {
        return responseComments;
    }

    public void setResponseComments(final ArrayList<ResponseComment> responseComments) {
        this.responseComments = responseComments;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        if (i == 0) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_header, viewGroup, false);
            return new ViewHolder(v);
        }
        else {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_in_review_item, viewGroup, false);
            return new CommentViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (position == 0) {
            ((ViewHolder) holder).setHeaderValue(responseReview, context);

            ((ViewHolder) holder).likeButton.setOnClickListener(v -> {
                if (checkLike()) {
                    commentAdapterCallback.likeReview();
                    UserDataUtils userDataUtils = new UserDataUtils(context);
                    String id = userDataUtils.getUserData(UserDataUtils.KEY_USER_ID);
                    responseReview.getLikes().add(id);
                    responseReview.addNewLike();
                    ((ViewHolder) holder).iconLike.setImageResource(R.drawable.ic_like_active);
                    ((ViewHolder) holder).likeCount.setText(responseReview.getLikesCount() + "");
                }
                else {
                    UserDataUtils userDataUtils = new UserDataUtils(context);
                    String id = userDataUtils.getUserData(UserDataUtils.KEY_USER_ID);
                    responseReview.getLikes().remove(id);
                    responseReview.deslike();
                    ((ViewHolder) holder).iconLike.setImageResource(R.drawable.ic_like_no_active);
                    ((ViewHolder) holder).likeCount.setText(responseReview.getLikesCount() + "");
                    commentAdapterCallback.deleteLike();
                }
            });
            if (checkLike()) {
                ((ViewHolder) holder).iconLike.setImageResource(R.drawable.ic_like_no_active);
            }
            else {
                ((ViewHolder) holder).iconLike.setImageResource(R.drawable.ic_like_active);
            }
        }
        else {
            final int deletePosition = position - 1;
            ((CommentViewHolder) holder).setViewValue(responseComments.get(deletePosition), context);
            ((CommentViewHolder) holder).view.setOnLongClickListener(v -> {
                commentAdapterCallback.deleteComment(responseComments.get(deletePosition).getId(), position);
                return true;
            });

        }
    }

    public void setReview(ResponseReview review) {
        responseReview = review;
    }

    @Override
    public int getItemCount() {
        return responseComments.size() + 1;
    }


    @Override
    public long getItemId(final int position) {
        return position;
    }


    private boolean checkLike() {
        UserDataUtils userDataUtils = new UserDataUtils(context);
        String id = userDataUtils.getUserData(UserDataUtils.KEY_USER_ID);
        for(int i = 0; i < responseReview.getLikes().size(); i++) {
            if (id.equals(responseReview.getLikes().get(i))) {
                return false;
            }
        }
        return true;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_picture)
        public SimpleDraweeView photoUser;
        @Bind(R.id.name)
        public TextView nameUser;
        @Bind(R.id.time_ago)
        public TextView timeAgo;
        @Bind(R.id.text)
        public TextView text;
        @Bind(R.id.root_view)
        public View view;

        public CommentViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setViewValue(ResponseComment value, Context context) {
            if (value.getResponseUser() != null && value.getResponseUser().getPhoto(PhotoSize.SIZE_MIDDLE, context) != null) {
                PipelineDraweeController controller = (PipelineDraweeController)
                        Fresco.newDraweeControllerBuilder()
                                .setImageRequest(Utils.createImageRequest((int) context.getResources().getDimension(R.dimen.icon_size),
                                        (int) context.getResources().getDimension(R.dimen.icon_size),
                                        Uri.parse(value.getResponseUser().getPhoto(PhotoSize.SIZE_MIDDLE, context))))
                                .setOldController(photoUser.getController()).build();
                photoUser.setController(controller);
            }
            String nameUserText = "";
            if (value.getResponseUser().getFirstName() != null && !value.getResponseUser().getFirstName().equals("")) {
                nameUserText += value.getResponseUser().getFirstName() + " ";
            }
            if (value.getResponseUser().getLastName() != null && !value.getResponseUser().getLastName().equals("")) {
                nameUserText += value.getResponseUser().getLastName();
            }
            if (nameUserText.equals("") && value.getResponseUser() != null && value.getResponseUser().getUsername() != null)
                nameUser.setText(value.getResponseUser().getUsername());
            else
                nameUser.setText(nameUserText);

            timeAgo.setText(Utils.getTimeAgo(Long.parseLong(value.getCreateAt())));
            text.setText(value.getText());
            itemView.setOnClickListener(v -> context.startActivity(AnotherUserProfileActivity.createIntent(context, value.getResponseUser().getId())));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        public TextView titleText;
        @Bind(R.id.avatarImageView)
        public ImageView photoUserHeader;
        @Bind(R.id.logo_brand)
        public ImageView logoBrand;
        @Bind(R.id.about)
        public HashTagTextView aboutBrand;
        @Bind(R.id.beat_rating)
        public RatingBar ratingBar;
        @Bind(R.id.count_comment)
        public TextView commentCount;
        @Bind(R.id.count_like)
        public TextView likeCount;
        @Bind(R.id.add_comment)
        public Button addComment;
        @Bind(R.id.name_brand)
        public TextView nameBrand;
        @Bind(R.id.raiting)
        public TextView rating;
        @Bind(R.id.comment_title)
        public TextView noComment;
        @Bind(R.id.time_ago)
        public TextView timaAgoHeader;
        @Bind(R.id.like_layout)
        public View likeButton;
        @Bind(R.id.icon_like)
        public ImageView iconLike;
        @Bind(R.id.photo_recycler)
        public RecyclerView photoView;
        @Bind(R.id.brand_view)
        public View brandView;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setHeaderValue(ResponseReview requestReview, Context context) {
            String nameText = "";
            if (requestReview.getUser().getFirstName() != null && !requestReview.getUser().getFirstName().equals("null"))
                nameText = requestReview.getUser().getFirstName();
            if (requestReview.getUser().getLastName() != null && !requestReview.getUser().getLastName().equals("null"))
                nameText += " " + requestReview.getUser().getLastName();
            if (requestReview.getUser() != null && requestReview.getUser().getUsername() != null && nameText.equals(""))
                nameText = requestReview.getUser().getUsername();
            final SpannableString out = new SpannableString(nameText + " " + context.getString(R.string.reviewed)
                    + " " + requestReview.getResponseBrand().getTitle());
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            out.setSpan(boldSpan, 0, nameText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(final View widget) {
                    if (requestReview.getResponseBrand().getSubCategoryId() != null) {
                        context.startActivity(NewBrandPagerActivtiy.createIntent(requestReview.getResponseBrand().getId(),
                                requestReview.getResponseBrand().getSubCategoryId(), context));
                    }
                }
            };
            out.setSpan(clickableSpan, (nameText +
                    context.getString(R.string.reviewed)).length() + 2, out.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            StyleSpan boldSpan1 = new StyleSpan(Typeface.BOLD);
            out.setSpan(boldSpan1, (nameText +
                    context.getString(R.string.reviewed)).length() + 2, out.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleText.setText(out);
            titleText.setMovementMethod(LinkMovementMethod.getInstance());
            if (requestReview.getUser() != null && requestReview.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, context) != null) {
                PipelineDraweeController controller = (PipelineDraweeController)
                        Fresco.newDraweeControllerBuilder()
                                .setImageRequest(Utils.createImageRequest((int) context.getResources().getDimension(R.dimen.icon_size),
                                        (int) context.getResources().getDimension(R.dimen.icon_size), Uri.parse(requestReview.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, context))))
                                .setOldController(((SimpleDraweeView) photoUserHeader).getController())
                                .build();
                ((SimpleDraweeView) photoUserHeader).setController(controller);
            }
            photoUserHeader.setOnClickListener(v -> context.startActivity(AnotherUserProfileActivity.createIntent(context, requestReview.getUser().getId())));
            titleText.setOnClickListener(v -> context.startActivity(AnotherUserProfileActivity.createIntent(context, requestReview.getUser().getId())));
            timaAgoHeader.setText(Utils.getTimeAgo(Long.parseLong(requestReview.getCreateAt())));
            Picasso.with(context).load(requestReview.getResponseBrand().getImage(PhotoSize.SIZE_MIDDLE, context)).into(logoBrand);
            aboutBrand.setText(requestReview.getText());
            ratingBar.setRating(Float.parseFloat(requestReview.getRate()) / Utils.RATING_BAR_DELETE_VALUE);
            commentCount.setText(requestReview.getCommentsCount());
            likeCount.setText(requestReview.getLikesCount() + "");
            addComment.setOnClickListener(v -> {
                context.startActivity(AddCommentActivity.createIntent(context, requestReview.getId()));
            });
            nameBrand.setText(requestReview.getResponseBrand().getTitle());
            rating.setText(requestReview.getRate());
            if (requestReview.getPicturies(PhotoSize.SIZE_MIDDLE, context) != null && requestReview.getPicturies(PhotoSize.SIZE_MIDDLE, context).size() > 0) {
                photoView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                photoView.setItemAnimator(itemAnimator);
                PhotoReviewAdapter photoReviewAdapter = new PhotoReviewAdapter(context, requestReview.getPicturies(PhotoSize.SIZE_MIDDLE, context), null, (position) -> {
                }, requestReview);
                photoView.setAdapter(photoReviewAdapter);
                photoView.setVisibility(View.VISIBLE);
            }
            brandView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (requestReview.getResponseBrand().getSubCategoryId() != null) {
                        context.startActivity(NewBrandPagerActivtiy.createIntent(requestReview.getResponseBrand().getId(),
                                requestReview.getResponseBrand().getSubCategoryId(), context));
                    }
                }
            });
        }
    }
}
