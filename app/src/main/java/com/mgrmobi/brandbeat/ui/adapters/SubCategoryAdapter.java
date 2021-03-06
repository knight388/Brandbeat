package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.entity.Interests;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private Context context;
    private List<ResponseCategories> responseCategoriesList;

    public SubCategoryAdapter(Context context, ArrayList<ResponseCategories> responseCategories) {
        this.context = context;
        responseCategoriesList = responseCategories;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ResponseCategories inter = responseCategoriesList.get(position);
        if(inter.getResponseBrands().size() == 0) {
            holder.view.setVisibility(View.GONE);
            return;
        }
        else
            holder.view.setVisibility(View.VISIBLE);
        holder.name.setText(inter.getTitle());
        holder.recycle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        holder.recycle.setItemAnimator(itemAnimator);
        ArrayList<ResponseBrand> responseBrands = new ArrayList<>();
        responseBrands = (ArrayList<ResponseBrand>) responseCategoriesList.get(position).getResponseBrands();
        if(responseBrands.size() == 5)
        {
            responseBrands.add(new ResponseBrand());
        }
        SubCategoryBrandAdapter interestsAdapter = new SubCategoryBrandAdapter(context,
                responseBrands, responseCategoriesList.get(position).getId(),
                responseCategoriesList.get(position).getTitle());
        holder.recycle.setAdapter(interestsAdapter);
        interestsAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return responseCategoriesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title_sub_category)
        public TextView name;
        @Bind(R.id.recycle)
        public RecyclerView recycle;
        @Bind(R.id.root_view)
        public View view;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
