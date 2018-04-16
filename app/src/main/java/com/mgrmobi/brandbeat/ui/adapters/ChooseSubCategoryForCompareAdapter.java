package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.ui.activity.ChooseBradsForCompareActivity;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ChooseSubCategoryForCompareAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ResponseCategories> categories;

    public ChooseSubCategoryForCompareAdapter(Context context, ArrayList<ResponseCategories> subCategories) {
        this.context = context;
        categories = subCategories;
        for(int i = 0; i < categories.size(); i++) {
            if (categories.get(i).isSubscriber())
                categories.get(i).setIsChecked(true);
        }
    }

    public ArrayList<String> getChecked() {
        ArrayList<String> clickedCategories = new ArrayList<>();
        if (categories == null) return null;
        for(ResponseCategories responseCategories : categories) {
            if (responseCategories.isChecked())
                clickedCategories.add(responseCategories.getId());
        }
        return clickedCategories;
    }

    public ArrayList<String> getDeleted() {
        ArrayList<String> strings = new ArrayList<>();
        if (categories == null) return null;
        for(ResponseCategories responseCategories : categories) {
            if (!responseCategories.isChecked() && responseCategories.isSubscriber())
                strings.add(responseCategories.getId());
        }
        return strings;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(final int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sub_category_checked, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.initView(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setViewsValue(categories.get(position), context);
        convertView.setOnClickListener(v -> {
            context.startActivity(ChooseBradsForCompareActivity.createIntent(context, "", categories.get(position).getId()));
        });
        return convertView;
    }

    static class ViewHolder {
        TextView titleCategory;
        CheckBox checked;

        public void initView(View view) {
            titleCategory = (TextView) view.findViewById(R.id.subcategory_name);
            checked = (CheckBox) view.findViewById(R.id.checked);
            checked.setVisibility(View.GONE);
        }

        public void setViewsValue(ResponseCategories value, Context context) {
            titleCategory.setText(value.getTitle());
        }
    }
}
