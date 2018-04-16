package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.presenter.PresenterSubCategory;
import com.mgrmobi.brandbeat.ui.activity.SubCategoryActivity;
import com.mgrmobi.brandbeat.ui.adapters.SubCategoryAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerSubCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SubCategoryFragment extends Fragment {
    private RecyclerView gridView;
    private PresenterSubCategory presenter = new PresenterSubCategory();
    private ContainerSubCategory containerSubCategory;
    private SubCategoryAdapter interestsAdapter;

    @Bind(R.id.progress_wheel)
    public View progressView;
    @Bind(R.id.message_text)
    public TextView message;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View rootView = inflater.inflate(R.layout.recycler_with_progress_layout, container, false);
        gridView = (RecyclerView) rootView.findViewById(R.id.grid_interests);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerSubCategory) {
            containerSubCategory = (ContainerSubCategory) getActivity();
            presenter.setView(containerSubCategory);
        }
        ButterKnife.bind(this, view);
        containerSubCategory.showProgress();
        presenter.getSubCategory((String) getActivity().getIntent().getSerializableExtra(SubCategoryActivity.CATEGORY_ID));
        progressView.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
    }

    private void initGrid(ArrayList<ResponseCategories> interestses) {
        LinearLayoutManager verticalManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        gridView.setLayoutManager(verticalManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        gridView.setItemAnimator(itemAnimator);
        List<ResponseCategories> responseCategoriesList = new ArrayList<>();
        for(int i = 0; i < interestses.size(); i++) {
            if (interestses.get(i).getResponseBrands() != null && interestses.get(i).getResponseBrands().size() != 0) {
                responseCategoriesList.add(interestses.get(i));
            }
        }
        interestsAdapter = new SubCategoryAdapter(getActivity(), (ArrayList<ResponseCategories>) responseCategoriesList);
        gridView.setAdapter(interestsAdapter);
        interestsAdapter.notifyDataSetChanged();
    }

    public void setSubIntersest(ArrayList<ResponseCategories> intersest) {
        progressView.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        initGrid(intersest);
        boolean haveBrands = false;
        for(ResponseCategories interes : intersest) {
            if (interes.getResponseBrands().size() > 0) {
                haveBrands = true;
                break;
            }
        }
        if (!haveBrands) {
            message.setText(getString(R.string.not_brands));
            message.setVisibility(View.VISIBLE);
        }
    }
}
