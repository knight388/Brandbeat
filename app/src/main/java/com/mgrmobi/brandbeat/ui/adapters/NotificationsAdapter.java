package com.mgrmobi.brandbeat.ui.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.entity.TypeNatifications;
import com.mgrmobi.brandbeat.network.responce.ResponseNotification;
import com.mgrmobi.brandbeat.ui.activity.AnotherUserProfileActivity;
import com.mgrmobi.brandbeat.ui.activity.NewBrandActivity;
import com.mgrmobi.brandbeat.ui.activity.ReviewActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_FOOTER = 2;
    private List<ResponseNotification> natifications;
    private Context context;
    private FooterViewHolder footerViewHolder;
    private boolean needFooter = true;

    public NotificationsAdapter(List natifications, Context context) {
        this.natifications = natifications;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_progress_item, parent, false);
            return new FooterViewHolder(v);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.natification_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (position == natifications.size()) {
            footerViewHolder = (FooterViewHolder) holder;
            if (!needFooter) {
                footerViewHolder.rootView.setVisibility(View.VISIBLE);
                footerViewHolder.rootView.findViewById(R.id.progress_wheel).setVisibility(View.GONE);
            }
        }
        else {
            ((ViewHolder) holder).initValue(natifications.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return natifications.size() + 1;
    }

    public void showFooter(boolean show) {
        needFooter = show;
        if (!needFooter) {
            footerViewHolder.rootView.setVisibility(View.VISIBLE);
            footerViewHolder.rootView.findViewById(R.id.progress_wheel).setVisibility(View.GONE);
        }
        if (show) {
            footerViewHolder.rootView.setVisibility(View.VISIBLE);
            notifyItemRemoved(getItemCount());
        }
        else {
            footerViewHolder.rootView.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.user_picture)
        public SimpleDraweeView icon;
        @Bind(R.id.name_user)
        public TextView name;
        @Bind(R.id.text)
        public TextView text;
        @Bind(R.id.time_ago)
        public TextView timeAgo;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public void initValue(ResponseNotification responseNatification) {
            timeAgo.setText(Utils.getTimeAgo(Long.parseLong(responseNatification.getCreatedAt())));
            text.setText(responseNatification.createText());
            name.setText(responseNatification.getTitleText());

            if (responseNatification.getPhotoUrl(context) != null) {
                icon.setImageDrawable(context.getDrawable(R.drawable.icon512));
                PipelineDraweeController controller = (PipelineDraweeController)
                        Fresco.newDraweeControllerBuilder()
                                .setImageRequest(Utils.createImageRequest((int) context.getResources().getDimension(R.dimen.icon_size),
                                        (int) context.getResources().getDimension(R.dimen.icon_size),
                                        Uri.parse(responseNatification.getPhotoUrl(context))))
                                .setOldController(icon.getController()).build();
                icon.setController(controller);
            }
            itemView.setOnClickListener(v -> {
                TypeNatifications typeNatification = responseNatification.getEnum();
                switch (typeNatification) {
                    case FOLLOW:
                        itemView.getContext().startActivity(AnotherUserProfileActivity.createIntent(itemView.getContext(), responseNatification.getUserId()));
                        break;
                    case LIKE_REVIEW:
                        itemView.getContext().startActivity(ReviewActivity.createIntent(responseNatification.getReviewId(), "", itemView.getContext()));
                        break;
                    case COMMENT_REVIEW:
                        itemView.getContext().startActivity(ReviewActivity.createIntent(responseNatification.getReviewId(), "", itemView.getContext()));
                        break;
                    case LIKE_COMMENT:
                        break;
                    case REPLIED:
                        break;
                    case ACHIEVEMENT:
                        break;
                    case ANNOUNCEMENTS:
                        break;
                    case ADVERTSING:
                        break;
                    case ACCEPTED_BRAND:
                        itemView.getContext().startActivity(NewBrandActivity.createIntent(responseNatification.getBrand().getId(), itemView.getContext()));
                        break;
                    case REJECTED_BRAND:
                        break;
                }
            });
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public View rootView;

        public FooterViewHolder(final View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.root_view);
        }
    }

    public void addNotifications(List<ResponseNotification> natifications) {
        this.natifications = natifications;
    }
}
