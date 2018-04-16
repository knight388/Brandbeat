package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.ui.callbacks.ClickImageRecyclerPhoto;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class AchievmentsAdapter extends RecyclerView.Adapter<AchievmentsAdapter.ViewHolder> {

    private List<Bitmap> bitmaps;
    private ClickImageRecyclerPhoto clickImageRecyclerPhoto;
    private List<String> responsePhotoUrls;

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(final List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public AchievmentsAdapter(Context context, List<String> responsePhotoUrls, List<Bitmap> bitmaps, ClickImageRecyclerPhoto clickImageRecyclerPhoto) {
        this.bitmaps = bitmaps;
        this.clickImageRecyclerPhoto = clickImageRecyclerPhoto;
        this.responsePhotoUrls = responsePhotoUrls;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_achievnts_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (bitmaps != null)
            holder.setValue(bitmaps.get(position));
        else
            holder.setValue(responsePhotoUrls.get(position));
    }

    @Override
    public int getItemCount() {
        if (bitmaps == null) {
            return responsePhotoUrls.size();
        }
        else {
            return bitmaps.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image_view)
        public ImageView image_view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValue(Bitmap bitmap) {
            image_view.setImageBitmap(bitmap);
            image_view.setOnClickListener(v -> clickImageRecyclerPhoto.click(0));
        }

        public void setValue(String value) {
            Picasso.with(itemView.getContext()).load(value)
                    .placeholder(R.drawable.icon512).into(image_view);
            image_view.setOnClickListener(v -> clickImageRecyclerPhoto.click(0));
        }
    }
}
