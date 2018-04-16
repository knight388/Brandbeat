package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseNotification;
import com.mgrmobi.brandbeat.presenter.NotificationsPresenter;
import com.mgrmobi.brandbeat.ui.activity.NotificationsSettingsActivity;
import com.mgrmobi.brandbeat.ui.adapters.NotificationsAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerNotifications;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NotificationsFragment extends Fragment {

    @Bind(R.id.grid_interests)
    public RecyclerView recyclerView;
    @Bind(R.id.progress_wheel)
    public View progressView;
    @Bind(R.id.swipe_refresh_layout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.no_found_layout)
    public View noFoundLayout;

    private LinearLayoutManager verticalManager;
    private NotificationsAdapter natificationsAdapter;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private NotificationsPresenter notificationsPresenter = new NotificationsPresenter();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View rootView = inflater.inflate(R.layout.recycler_with_progress_and_pull_to_refresh, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerNotifications) {
            notificationsPresenter.setView((ContainerNotifications) getActivity());
        }
        ButterKnife.bind(this, view);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        verticalManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(false);
        notificationsPresenter.getNotifications();
        natificationsAdapter = new NotificationsAdapter(new ArrayList<>(), getActivity());
        progressView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setOnRefreshListener(notificationsPresenter::getNotifications);
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = verticalManager.getChildCount();
                    totalItemCount = verticalManager.getItemCount();
                    pastVisiblesItems = verticalManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        natificationsAdapter.showFooter(notificationsPresenter.getNextNatifications());
                    }
                }
            }
        });
        recyclerView.setAdapter(natificationsAdapter);

    }

    @OnClick(R.id.action_button)
    public void onClickAction() {
        startActivity(NotificationsSettingsActivity.createIntent(getActivity()));
    }

    public void setNatifications(List<ResponseNotification> natifications) {
        progressView.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        natificationsAdapter.addNotifications(natifications);
        natificationsAdapter.notifyDataSetChanged();
        if (natifications.size() == 0) {
            noFoundLayout.setVisibility(View.VISIBLE);
        }
    }

}
