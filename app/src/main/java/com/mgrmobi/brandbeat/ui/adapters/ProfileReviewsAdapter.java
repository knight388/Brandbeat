package com.mgrmobi.brandbeat.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.profile.ProfileActivity;
import com.mgrmobi.brandbeat.ui.activity.AchivmintsActivity;
import com.mgrmobi.brandbeat.ui.activity.EditProfileActivity;
import com.mgrmobi.brandbeat.ui.activity.ReviewActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.mgrmobi.brandbeat.ui.widget.RatingWidget;
import com.mgrmobi.brandbeat.ui.widget.hashTagTextView.HashTagTextView;
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
public class ProfileReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_FOOTER = 2;
    private ArrayList<ResponseReview> reviews = new ArrayList<>();
    private Context context;
    private ResponseProfile responseProfile;
    private Bitmap bitmap;
    private FooterViewHolder footerViewHolder;
    HeadeViewHolder holder1;

    public ProfileReviewsAdapter(Context context, ArrayList<ResponseReview> responseReviews) {
        this.context = context;
        reviews = responseReviews;
    }

    public ResponseProfile getResponseProfile() {
        return responseProfile;
    }

    public void setResponseProfile(final ResponseProfile responseProfile) {
        this.responseProfile = responseProfile;
    }

    public void setProfile(ResponseProfile profile) {
        responseProfile = profile;
        if (holder1 != null)
            holder1.setProfile(profile);
    }

    public void setPhoto(Bitmap photo) {
        bitmap = photo;
    }

    public void setReviews(final ArrayList<ResponseReview> reviews) {
        if (reviews.size() > 0) {
            this.reviews.addAll(reviews);
        }
        else {
            this.reviews = reviews;
        }
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
        View v;
        if (viewType == TYPE_HEADER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_view, parent, false);
            return new HeadeViewHolder(v);
        }
        if (viewType == TYPE_FOOTER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_progress_item, parent, false);
            return new FooterViewHolder(v);
        }
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == 0) {
            holder1 = (HeadeViewHolder) holder;
            holder1.setProfile(responseProfile);
            if (bitmap != null)
                holder1.setBitmap(bitmap);
        }
        else if (position == reviews.size() + 1) {
            footerViewHolder = (FooterViewHolder) holder;
        }
        else {
            ViewHolder holder1 = (ViewHolder) holder;
            holder1.setValue(reviews.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size() + 2;//header and footer
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        public TextView name;
        @Bind(R.id.user_picture)
        public ImageView icon;
        @Bind(R.id.rating_widjet)
        public RatingWidget ratingBar;
        @Bind(R.id.text)
        public HashTagTextView text;
        @Bind(R.id.time_ago)
        public TextView createAt;
        @Bind(R.id.root_view)
        public View rootView;
        @Bind(R.id.photo_view)
        public RecyclerView photoView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValue(ResponseReview value) {
            if (value.getResponseBrand() != null && value.getResponseBrand().getTitle() != null) {
                Spannable span = new SpannableString(value.getResponseBrand().getTitle() + " / " + value.getResponseBrand().getSubCategories().getTitle());
                span.setSpan(new RelativeSizeSpan(0.8f), (value.getResponseBrand().getTitle() + " / ").length(),
                        (value.getResponseBrand().getTitle() + " / " + value.getResponseBrand().getSubCategories().getTitle()).length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_primary)),
                        (value.getResponseBrand().getTitle() + " / ").length(),
                        (value.getResponseBrand().getTitle() + " / " + value.getResponseBrand().getSubCategories().getTitle()).length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                name.setText(span);
            }
            if (value.getResponseBrand() != null && value.getResponseBrand().getImage(PhotoSize.SIZE_MIDDLE, context) != null)
                Picasso.with(context).load(value.getResponseBrand().getImage(PhotoSize.SIZE_MIDDLE, context)).into(icon);

            if (value.getText() == null || value.getText().length() == 0)
                text.setVisibility(View.GONE);
            else {
                text.setVisibility(View.VISIBLE);
                text.setText(value.getText());
            }
            if (value.getCreateAt() != null && !value.getCreateAt().equals("null"))
                createAt.setText(Utils.getTimeAgo(Long.parseLong(value.getCreateAt())));
            ratingBar.setTextRating(Float.parseFloat(value.getRate()));
            name.setOnClickListener(v -> context.startActivity(NewBrandPagerActivtiy.createIntent(value.getResponseBrand().getId(), value.getResponseBrand().getSubCategoryId(), context)));
            icon.setOnClickListener(v -> context.startActivity(NewBrandPagerActivtiy.createIntent(value.getResponseBrand().getId(), value.getResponseBrand().getSubCategoryId(), context)));
            text.setOnClickListener(v -> context.startActivity(ReviewActivity.createIntent(value.getId(), value.getResponseBrand().getId(), context)));
            if (value.getPicturies(PhotoSize.SIZE_MIDDLE, context) != null && value.getPicturies(PhotoSize.SIZE_MIDDLE, context).size() > 0) {
                photoView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                photoView.setItemAnimator(itemAnimator);
                PhotoReviewAdapter photoReviewAdapter = new PhotoReviewAdapter(context, value.getPicturies(PhotoSize.SIZE_BIG, context), null, (position) -> {
                }, value);
                photoView.setAdapter(photoReviewAdapter);
                photoView.setVisibility(View.VISIBLE);
            }
            else {
                photoView.setVisibility(View.GONE);
            }
        }
    }

    public void showViewNextLoad() {
        footerViewHolder.rootView.setVisibility(View.VISIBLE);
    }

    public void dismissViewHolder() {
        if (footerViewHolder != null)
            footerViewHolder.rootView.setVisibility(View.VISIBLE);
    }

    class HeadeViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.photo_image)
        public SimpleDraweeView photo;
        @Bind(R.id.followers_count)
        public TextView followersCount;
        @Bind(R.id.reviews_count)
        public TextView reviewsCount;
        @Bind(R.id.achievments_list)
        public RecyclerView achievmentsList;
        @Bind(R.id.achievments)
        public TextView achievmentsCount;
        @Bind(R.id.add_picture)
        public TextView addPicture;
        @Bind(R.id.review_title)
        public View reviewTitle;
        @Bind(R.id.edit_profile)
        public FloatingActionButton editProfile;
        @Bind(R.id.show_profile)
        public FloatingActionButton showProfile;


        public HeadeViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            editProfile.setOnClickListener(v -> {
                context.startActivity(EditProfileActivity.createIntent(context, responseProfile));
            });
//            showProfile.setOnClickListener(v -> {
//                Intent intent = new Intent(ProfileReviewsAdapter.this, ProfileActivity.class);
//                context.startActivity(ProfileActivity.createIntent(context, responseProfile));
//            });

        }

        public void setProfile(ResponseProfile profile) {
            if (profile == null) return;
            context = itemView.getContext();
            followersCount.setText(profile.getFollowerSum());
            reviewsCount.setText(profile.getReviewSum());
            if (profile.getReviews() != null && profile.getReviews().size() == 0)
                reviewTitle.setVisibility(View.GONE);
            if (profile.getAchievements() != null) {
                if (profile.getAchievements().size() == 1) {
                    achievmentsCount.setText(
                            context.getResources().getString(R.string.achievement));
                }
                else {
                    achievmentsCount.setText(profile.getAchievements().size() + " " +
                            context.getResources().getString(R.string.achievements));
                }
            }
            else
                achievmentsCount.setText("");
            if (profile.getPhoto(PhotoSize.SIZE_BIG, context) == null) {
                bitmap = null;
                addPicture.setVisibility(View.VISIBLE);
                photo.setImageResource(R.drawable.profile_placeholder);
            }
            else {
                addPicture.setVisibility(View.GONE);
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                int height = (int) Utils.convertDpToPixel(192, context);
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(responseProfile.getPhoto(PhotoSize.SIZE_BIG, context)))
                        .setResizeOptions(new ResizeOptions(width, height))
                        .build();
                PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setOldController(photo.getController())
                        .setImageRequest(request)
                        .build();
                (photo).setController(controller);
            }
            addPicture.setOnClickListener(v ->
            {
                ((Activity) context).startActivityForResult(makePhotoIntent(context.getResources().getString(R.string.image_chooser_title)
                        , context), 1);
            });
            if (profile.getAchievements() != null && profile.getAchievements().size() > 0) {
                achievmentsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
                achievmentsList.setItemAnimator(itemAnimator);
                List<String> list = new ArrayList<>();
                for(int i = 0; i < profile.getAchievements().size(); i++) {
                    list.add(profile.getAchievements().get(i).getImage(PhotoSize.SIZE_MIDDLE, context));
                }
                AchievmentsAdapter achievmentsAdapter = new AchievmentsAdapter(context, list, null, (position) -> {
                    context.startActivity(AchivmintsActivity.createIntent(context, profile.getAchievements()));
                });
                achievmentsList.setAdapter(achievmentsAdapter);
                achievmentsList.setVisibility(View.VISIBLE);
            }
            else {
                achievmentsList.setVisibility(View.INVISIBLE);
            }

        }

        public void setBitmap(Bitmap bitmap) {
            photo.setImageBitmap(bitmap);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public View rootView;

        public FooterViewHolder(final View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.root_view);
        }
    }

    public Intent makePhotoIntent(String title, Context ctx) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        Intent chooser = Intent.createChooser(galleryIntent, title);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent[] extraIntents = {takePictureIntent};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        return chooser;
    }
}
