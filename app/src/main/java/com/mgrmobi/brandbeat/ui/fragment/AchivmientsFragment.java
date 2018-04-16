package com.mgrmobi.brandbeat.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.entity.Interests;
import com.mgrmobi.brandbeat.network.responce.ResponseAchievement;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.presenter.AchivmientsPresenter;
import com.mgrmobi.brandbeat.presenter.PresenterInterestsView;
import com.mgrmobi.brandbeat.presenter.PresenterProfileView;
import com.mgrmobi.brandbeat.ui.activity.AchivmintsActivity;
import com.mgrmobi.brandbeat.ui.adapters.AchivmientsAdapter;
import com.mgrmobi.brandbeat.ui.adapters.InterestsAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerInterests;
import com.mgrmobi.brandbeat.ui.base.ContainerProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
@SuppressLint("ValidFragment")
public class AchivmientsFragment extends Fragment {

    private RecyclerView gridView;
    private AchivmientsAdapter achivmientsAdapter;
    private ArrayList<ResponseAchievement> userAchvmients;
    private AchivmientsPresenter achivmientsPresenter = new AchivmientsPresenter();

    @Bind(R.id.progress_wheel)
    public View progressView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View rootView = inflater.inflate(R.layout.recycler_with_progress_layout, container, false);
        gridView = (RecyclerView) rootView.findViewById(R.id.grid_interests);
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(R.id.action_context).setVisible(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(getActivity() instanceof ContainerProfile)
        {
            achivmientsPresenter.setView((ContainerProfile) getActivity());
        }
        achivmientsPresenter.getAchivmients();
        ButterKnife.bind(this, view);
        gridView.setVisibility(View.VISIBLE);
        userAchvmients = (ArrayList<ResponseAchievement>) getActivity().getIntent().getSerializableExtra(AchivmintsActivity.ACHIVMIENTS);
        initGrid(userAchvmients);
    }

    private void initGrid(ArrayList<ResponseAchievement> achievements) {
        gridView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        gridView.setItemAnimator(itemAnimator);
        achivmientsAdapter = new AchivmientsAdapter(achievements, userAchvmients, getActivity());
        gridView.setAdapter(achivmientsAdapter);
        achivmientsAdapter.notifyDataSetChanged();
    }

    public void setAchivmients(List<ResponseAchievement> achivmients)
    {
        initGrid((ArrayList<ResponseAchievement>) achivmients);
    }
}
