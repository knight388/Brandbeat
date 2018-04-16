package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.presenter.PresenterSubCategory;
import com.mgrmobi.brandbeat.ui.activity.CheckedSubCategoryActivity;
import com.mgrmobi.brandbeat.ui.adapters.CheckSubCategoryAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerSubCategory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class CheckSubCategoryFragment extends Fragment {
    @Bind(R.id.list)
    public ListView listView;
    private PresenterSubCategory presenterSubCategory = new PresenterSubCategory();
    private CheckSubCategoryAdapter checkSubCategoryAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.check_sub_category_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        if (getActivity() instanceof ContainerSubCategory) {
            presenterSubCategory.setView((ContainerSubCategory) getActivity());
            String id = getActivity().getIntent().getStringExtra(CheckedSubCategoryActivity.CATEGORY_ID);
            presenterSubCategory.getSubCategory(id);
        }
    }

    public void getSubcategories(ArrayList<ResponseCategories> responseCategories) {
        checkSubCategoryAdapter = new CheckSubCategoryAdapter(getActivity(), responseCategories);
        listView.setAdapter(checkSubCategoryAdapter);
        checkSubCategoryAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.submit)
    public void onClickSubbmit() {
        if (checkSubCategoryAdapter.getChecked() != null) {
            presenterSubCategory.setSetSubCategories(checkSubCategoryAdapter.getChecked());
        }
        else presenterSubCategory.enterAdd = true;
        if (checkSubCategoryAdapter.getDeleted() != null && checkSubCategoryAdapter.getDeleted().size() != 0) {
            presenterSubCategory.delete(checkSubCategoryAdapter.getDeleted());
        }
        else presenterSubCategory.enterDelete = true;
    }
}
