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
import com.mgrmobi.brandbeat.ui.callbacks.CompareBrandCallBack;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class CompareBottomAdapter extends RecyclerView.Adapter<CompareBottomAdapter.ViewHolder> {

    private List<ResponseBrand> brands;
    private Context context;
    private CompareBrandCallBack callBack;

    public CompareBottomAdapter(CompareBrandCallBack deleteFromButtom, List<ResponseBrand> records, Context context) {
        this.brands = records;
        this.context = context;
        this.callBack = deleteFromButtom;
    }

    public List<ResponseBrand> getBrands() {
        return brands;
    }

    public void setBrands(final List<ResponseBrand> brands) {
        this.brands = brands;
    }

    public void addBrand(ResponseBrand brand, int position) {
        if (position > getItemCount()) {
            brands.add(brand);
        }
        else {
            brands.add(position, brand);
        }
        notifyItemInserted(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.brand_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setValue(brands.get(i), context);
        viewHolder.icon.setOnClickListener(v -> {
            if (i >= brands.size())
                callBack.deleteFromButtom(brands.get(brands.size() - 1), brands.size() - 1);
            else
                callBack.deleteFromButtom(brands.get(i), i);
        });
    }

    @Override
    public int getItemCount() {
        return brands.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.brand_image)
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setValue(ResponseBrand value, Context context) {
            if (value == null) {
                icon.setImageResource(0);
                return;
            }
            Picasso.with(context).load(value.getImage(PhotoSize.SIZE_MIDDLE, context)).into(icon);
        }

    }
}