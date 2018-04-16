package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.NewBrandActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SuggestsAdapter extends RecyclerView.Adapter<SuggestsAdapter.ViewHolder> {

    private List<ResponseBrand> brands;
    private Context context;

    public SuggestsAdapter(List<ResponseBrand> brands, Context context) {
        this.brands = brands;
        this.context = context;
    }

    public List<ResponseBrand> getBrands() {
        return brands;
    }

    public void setBrands(final List<ResponseBrand> brands) {
        this.brands = brands;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.brand_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (brands.size() != 0 && context != null && viewHolder.icon != null) {
            Picasso.with(context).load(brands.get(i).getImage(PhotoSize.SIZE_MIDDLE, context)).placeholder(R.drawable.icon512)
                    .into(viewHolder.icon);
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    context.startActivity(NewBrandPagerActivtiy.createIntent(brands.get(i).getId(), brands.get(i).getSubCategoryId(),context));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.brand_image);
        }
    }
}