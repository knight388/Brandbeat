package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.entity.Interests;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.ChooseSubCategoryForCompareActivity;
import com.mgrmobi.brandbeat.ui.activity.SubCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ChooseCategoryForCompareAdapter  extends RecyclerView.Adapter<ChooseCategoryForCompareAdapter.ViewHolder> {

    private List<Interests> intersts;
    private List<Interests> checkedInterst = new ArrayList<>();
    private Context context;

    public ChooseCategoryForCompareAdapter(ArrayList<ResponseCategories> intersts, Context context) {
        this.intersts = initInterests(intersts);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.interest_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Interests inter = intersts.get(i);
        viewHolder.name.setText(inter.getName());

        Picasso.with(context).load(inter.getIcon()).into(viewHolder.icon);
        viewHolder.selected.setVisibility(View.GONE);

        viewHolder.icon.setOnClickListener(v -> {
            int[] mass = new int[2];
            viewHolder.icon.getLocationOnScreen(mass);
            context.startActivity(ChooseSubCategoryForCompareActivity.createIntent(context, inter.getId()));
        });
    }

    public List<Interests> getCheckedInterst() {
        return checkedInterst;
    }

    @Override
    public int getItemCount() {
        return intersts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        public TextView name;
        @Bind(R.id.icon)
        public ImageView icon;
        @Bind(R.id.select_flag)
        public ImageView selected;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public ArrayList<Interests> initInterests(ArrayList<ResponseCategories> responseCategories) {
        ArrayList<Interests> interestses = new ArrayList<>();
        for(ResponseCategories categories : responseCategories) {
            Interests interests = new Interests();
            interests.setIcon(categories.getImage(PhotoSize.SIZE_SMALL , context));
            interests.setName(categories.getTitle());
            interests.setId(categories.getId());
            interestses.add(interests);
        }
        return interestses;
    }
}
