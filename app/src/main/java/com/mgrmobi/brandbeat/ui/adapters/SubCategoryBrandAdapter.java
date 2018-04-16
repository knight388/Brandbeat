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
import com.mgrmobi.brandbeat.ui.activity.SeeAllBrandInSubCategoryActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SubCategoryBrandAdapter extends RecyclerView.Adapter<SubCategoryBrandAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ResponseBrand> responseBrands;
    private String subCatId;
    private String nameSubCategory;

    public SubCategoryBrandAdapter(Context context, ArrayList<ResponseBrand> brands, String subCatId,
                                   String nameSubCategory)
    {
        this.context = context;
        responseBrands = brands;
        this.subCatId = subCatId;
        this.nameSubCategory = nameSubCategory;
    }

    @Override
    public SubCategoryBrandAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_subcategiry_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SubCategoryBrandAdapter.ViewHolder holder, final int position) {
        if(position != 5) {
            Picasso.with(context).load(responseBrands.get(position).getImage(PhotoSize.SIZE_MIDDLE, context)).placeholder(R.drawable.icon512).into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    context.startActivity(NewBrandPagerActivtiy.createIntent(responseBrands.get(position).getId(), responseBrands.get(position).getSubCategoryId(),context));
                }
            });
        }
        else
        {
            holder.seeAll.setVisibility(View.VISIBLE);
            holder.seeAll.setOnClickListener(v ->
                    context.startActivity(SeeAllBrandInSubCategoryActivity.createIntent(context, subCatId, nameSubCategory)));
        }
    }

    @Override
    public int getItemCount() {
        if(responseBrands.size()>6)
            return 6;
        return responseBrands.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image_view)
        public ImageView image;
        @Bind(R.id.text_layout)
        public View seeAll;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
