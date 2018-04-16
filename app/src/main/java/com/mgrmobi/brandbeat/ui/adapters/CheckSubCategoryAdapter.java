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

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class CheckSubCategoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ResponseCategories> categories123;

    public CheckSubCategoryAdapter(Context context, ArrayList<ResponseCategories> subCategories) {
        this.context = context;
        categories123 = subCategories;
        for(int i = 0; i < categories123.size(); i++)
            if(categories123.get(i).isSubscriber())
                categories123.get(i).setIsChecked(true);
    }

    public ArrayList<String> getChecked() {
        ArrayList<String> clickedCategories = new ArrayList<>();
        if (categories123 == null) return null;
        for(ResponseCategories responseCategories : categories123) {
            if (responseCategories.isChecked())
                clickedCategories.add(responseCategories.getId());
        }
        return clickedCategories;
    }

    public ArrayList<String> getDeleted() {
        ArrayList<String> strings = new ArrayList<>();
        if (categories123 == null) return null;
        for(ResponseCategories responseCategories : categories123) {
            if (!responseCategories.isChecked() && responseCategories.isSubscriber())
                strings.add(responseCategories.getId());
        }
        return strings;
    }

    @Override
    public int getCount() {
        return categories123.size();
    }

    @Override
    public Object getItem(final int position) {
        return categories123.get(position);
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
        viewHolder.setViewsValue(categories123.get(position), context);
        if (categories123.get(position).isSubscriber())
            viewHolder.checked.setChecked(true);
        else
            viewHolder.checked.setChecked(false);
        convertView.setOnClickListener(v -> {
            if (viewHolder.checked.isChecked()) {
                viewHolder.checked.setChecked(false);
                categories123.get(position).setIsChecked(false);
            }
            else {
                viewHolder.checked.setChecked(true);
                categories123.get(position).setIsChecked(true);
            }

        });
        return convertView;
    }

    static class ViewHolder {
        TextView titleCategory;
        CheckBox checked;

        public void initView(View view) {
            titleCategory = (TextView) view.findViewById(R.id.subcategory_name);
            checked = (CheckBox) view.findViewById(R.id.checked);
        }

        public void setViewsValue(ResponseCategories value, Context context) {
            titleCategory.setText(value.getTitle());
        }
    }
}
