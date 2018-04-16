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
import com.mgrmobi.brandbeat.ui.activity.CheckedSubCategoryActivity;
import com.mgrmobi.brandbeat.ui.fragment.EditInterestsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class EditInterestsAdapter extends RecyclerView.Adapter<EditInterestsAdapter.ViewHolder> {
    private List<Interests> intersts;
    private List<Interests> checkedInterst;
    private Context context;
    private Fragment fragment;

    private boolean isChange = false;

    public boolean isChange() {
        return isChange;
    }

    public EditInterestsAdapter(ArrayList<ResponseCategories> intersts, Context context, Fragment fragment) {
        this.intersts = initInterests(intersts);
        this.context = context;
        this.fragment = fragment;
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
        if (intersts.get(i).isSubscriber()) {
            viewHolder.selected.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.selected.setVisibility(View.GONE);
        }
        if (intersts.get(i).isSubscriber()) {
            viewHolder.selected.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.selected.setVisibility(View.GONE);
        }
        viewHolder.icon.setOnClickListener(v -> {
            if (intersts.get(i).isSubscriber()) {
                viewHolder.selected.setVisibility(View.GONE);
                intersts.get(i).setIsSubscriber(false);
                ((EditInterestsFragment) fragment).uploadInterests();
                isChange = true;
            }
            else {
                viewHolder.selected.setVisibility(View.VISIBLE);
                intersts.get(i).setIsSubscriber(true);
                ((EditInterestsFragment) fragment).uploadInterests();
                context.startActivity(CheckedSubCategoryActivity.createIntent(context, intersts.get(i).getId()));
                isChange = true;
            }
        });
    }

    public boolean isChecked() {
        int count = 0;
        for(int i = 0; i < intersts.size(); i++) {
            if (intersts.get(i).isSubscriber())
                count++;
        }
        if (count >= 1) return true;
        return false;
    }

    public List<Interests> getCheckedInterst() {
        checkedInterst = new ArrayList<>();
        List<String> deleteIds = new ArrayList<>();
        for(Interests interests : intersts) {
            if (interests.isSubscriber()) {
                checkedInterst.add(interests);
                ((EditInterestsFragment) fragment).uploadNewInterest(interests);
            }
            else {
                deleteIds.add(interests.getId());
                ((EditInterestsFragment) fragment).deleteInterests(deleteIds);
            }
        }
        ((EditInterestsFragment) fragment).uploadNewInterest((ArrayList) checkedInterst);
        ((EditInterestsFragment) fragment).deleteInterests(deleteIds);
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
            interests.setIsSubscriber(categories.isSubscriber());
            interestses.add(interests);
            if (categories.isSubscriber())
                if(checkedInterst != null)
                checkedInterst.add(interests);
        }
        return interestses;
    }
}