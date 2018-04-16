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

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.ui.adapters.CompareBrandsAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerRegistration;
import com.mgrmobi.brandbeat.utils.UserDataUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ComparisonFragment extends Fragment {
    @Bind(R.id.grid_interests)
    public RecyclerView recyclerView;
    private CompareBrandsAdapter compareBrandsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_with_progress_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        UserDataUtils userDataUtils = new UserDataUtils(getActivity());
        ArrayList<ResponseBrand> responseBrands = userDataUtils.getBrandsToCompare();
        initLayput(responseBrands);
    }

    private void initLayput(ArrayList<ResponseBrand> interestses) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        compareBrandsAdapter = new CompareBrandsAdapter(interestses, getActivity());
        recyclerView.setAdapter(compareBrandsAdapter);
        compareBrandsAdapter.notifyDataSetChanged();
    }
}
