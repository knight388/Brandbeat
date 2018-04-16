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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.ResponseStatistics;
import com.mgrmobi.brandbeat.presenter.PresenterSeeAllReviews;
import com.mgrmobi.brandbeat.ui.activity.SeeAllReviewActivity;
import com.mgrmobi.brandbeat.ui.adapters.SeeAllReviewAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerReviewView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SeeAllReviewFragment extends Fragment {
    @Bind(R.id.recycle)
    public RecyclerView recyclerView;
    @Bind(R.id.progress_view)
    public View progressView;
    private LinearLayoutManager verticalLayoutManager;
    private ContainerReviewView containerReviewView;
    private ArrayList<ResponseReview> responseReviews;
    private SeeAllReviewAdapter seeAllReviewAdapter;
    private PresenterSeeAllReviews presenterSeeAllReviews = new PresenterSeeAllReviews();
    private ResponseStatistics responseStatistics;
    private String brandID;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View rootView = inflater.inflate(R.layout.see_all_review_frargment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerReviewView) {
            containerReviewView = (ContainerReviewView) getActivity();
            presenterSeeAllReviews.setView(containerReviewView);
        }

        ButterKnife.bind(this, view);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        verticalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(false);
        brandID = getActivity().getIntent().getStringExtra(SeeAllReviewActivity.BRAND_ID);

        seeAllReviewAdapter = new SeeAllReviewAdapter(getActivity(), new ArrayList<ResponseReview>(), new ResponseBrand(), presenterSeeAllReviews, brandID);
        recyclerView.setAdapter(seeAllReviewAdapter);
        presenterSeeAllReviews.getStatistics(brandID);
        presenterSeeAllReviews.getReviews(brandID, null, false, false);
    }

    public void setReviews(List<ResponseReview> reviews) {
        seeAllReviewAdapter.setReviews((ArrayList<ResponseReview>) reviews);
        seeAllReviewAdapter.notifyDataSetChanged();
    }

    public void setResponseStatistics(ResponseStatistics responseStatistics) {
        seeAllReviewAdapter.setResponseStatistics(responseStatistics);
    }

    public void showProgress() {
        progressView.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressView.setVisibility(View.GONE);
    }
}
