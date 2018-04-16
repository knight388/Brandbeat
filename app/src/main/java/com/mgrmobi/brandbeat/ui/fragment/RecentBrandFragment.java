package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.presenter.RecentBrandPresenter;
import com.mgrmobi.brandbeat.ui.adapters.RecentBrandAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerRecentBrands;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RecentBrandFragment extends Fragment {
    @Bind(R.id.progress_wheel)
    public View view;
    @Bind(R.id.grid_interests)
    public RecyclerView recyclerView;
    @Bind(R.id.message_text)
    public TextView messageText;
    private ContainerRecentBrands containerRecentBrands;
    private RecentBrandAdapter brandAdapter;
    private RecentBrandPresenter recentBrandPresenter = new RecentBrandPresenter();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View rootView = inflater.inflate(R.layout.recycler_with_progress_layout, container, false);
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(R.id.action_context).setVisible(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerRecentBrands) {
            containerRecentBrands = (ContainerRecentBrands) getActivity();
            recentBrandPresenter.setView(containerRecentBrands);
        }
        brandAdapter = new RecentBrandAdapter(getActivity());
        ButterKnife.bind(this, view);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        LinearLayoutManager verticalManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(brandAdapter);
        recentBrandPresenter.getRecentBrands();
        this.view.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    public void setBrands(ArrayList<ResponseBrand> brands) {
        if (brands.size() == 0) {
            messageText.setVisibility(View.VISIBLE);
            messageText.setText(getString(R.string.no_recently_brand));
        }
        else {
            brandAdapter.setBrands(brands);
            recyclerView.setVisibility(View.VISIBLE);
        }
        view.setVisibility(View.GONE);
    }

    public void showError() {
        view.setVisibility(View.GONE);
    }
}
