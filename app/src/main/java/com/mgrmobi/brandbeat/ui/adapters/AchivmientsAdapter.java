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
import com.mgrmobi.brandbeat.network.responce.ResponseAchievement;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.NavigationActivity;
import com.mgrmobi.brandbeat.ui.fragment.InterestsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class AchivmientsAdapter  extends RecyclerView.Adapter<AchivmientsAdapter.ViewHolder> {
    private List<ResponseAchievement> achievements;
    private Context context;
    private List<ResponseAchievement> userAchvmients;

    public AchivmientsAdapter(ArrayList<ResponseAchievement> achievements, ArrayList<ResponseAchievement> userAchvmients, Context context) {
        this.achievements = new ArrayList<>();
        this.achievements = achievements;
        this.context = context;
        this.userAchvmients = userAchvmients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.interest_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        for(int j = 0; j < userAchvmients.size(); j++)
        {
            if(achievements.get(i).getId().equals(userAchvmients.get(j).getId()))
            {
                viewHolder.blurImage.setVisibility(View.GONE);
                break;
            }
            else
                viewHolder.blurImage.setVisibility(View.VISIBLE);
        }
        viewHolder.selected.setVisibility(View.GONE);
        viewHolder.name.setText(achievements.get(i).getDescription());
        Picasso.with(context).load(achievements.get(i).getImage(PhotoSize.SIZE_BIG, context)).into(viewHolder.icon);
        viewHolder.icon.setOnClickListener(v -> {
        });
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.name)
        public TextView name;
        @Bind(R.id.icon)
        public ImageView icon;
        @Bind(R.id.select_flag)
        public ImageView selected;
        @Bind(R.id.blurred_image)
        public View blurImage;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
