package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseLocalFeed;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.callbacks.ClickImageRecyclerPhoto;
import com.mgrmobi.brandbeat.ui.widget.view_pager_photo.PhotoViewPagerActivity;
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
public class PhotoBrandsFeedAdapter extends RecyclerView.Adapter<PhotoBrandsFeedAdapter.ViewHolder> {

    private List<String> responsePhotoUrls;
    private ClickImageRecyclerPhoto clickImageRecyclerPhoto;

    public PhotoBrandsFeedAdapter(Context context, List<String> responsePhotoUrls, ClickImageRecyclerPhoto clickImageRecyclerPhoto) {
        this.responsePhotoUrls = responsePhotoUrls;
        this.clickImageRecyclerPhoto = clickImageRecyclerPhoto;
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_brands_feed_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setValue(responsePhotoUrls.get(position));
    }

    @Override
    public int getItemCount() {
        return responsePhotoUrls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.logo_brand)
        public ImageView image_view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValue(String value) {
            PipelineDraweeController controller = (PipelineDraweeController)
                    Fresco.newDraweeControllerBuilder()
                            .setImageRequest(Utils.createImageRequest(60, 60, Uri.parse(value)))
                            .setOldController(((SimpleDraweeView) image_view).getController())
                            .build();
            ((SimpleDraweeView) image_view).setController(controller);
            image_view.setOnClickListener(v -> {
                Context context = itemView.getContext();
                    context.startActivity(PhotoViewPagerActivity.createIntent
                            ((ArrayList<String>) responsePhotoUrls, context));

            });
        }
    }
}
