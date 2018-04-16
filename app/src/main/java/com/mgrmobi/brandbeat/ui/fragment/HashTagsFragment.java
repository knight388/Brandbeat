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
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.presenter.PresenterGetHashTag;
import com.mgrmobi.brandbeat.ui.activity.HashTagActivity;
import com.mgrmobi.brandbeat.ui.adapters.HashTagsAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerHashTag;
import com.mgrmobi.brandbeat.ui.callbacks.LikeDeslikeCallBack;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class HashTagsFragment extends Fragment {
    @Bind(R.id.grid_interests)
    public RecyclerView recyclerView;
    @Bind(R.id.progress_wheel)
    public View progress;
    @Bind(R.id.root_view)
    public View rootView;
    private HashTagsAdapter hashTagsAdapter;
    private PresenterGetHashTag presenterGetHashTag = new PresenterGetHashTag();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View rootView = inflater.inflate(R.layout.recycler_with_progress_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(getActivity() instanceof ContainerHashTag) {
            ContainerHashTag containerInterests = (ContainerHashTag) getActivity();
            presenterGetHashTag.setView(containerInterests);
        }
        ButterKnife.bind(this, view);
        presenterGetHashTag.getReviews(getActivity().getIntent().getStringExtra(HashTagActivity.HASH_TAG));
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        rootView.setBackgroundColor(getResources().getColor(R.color.hash_tag_background));
    }
    private void initRecycle(ArrayList<ResponseReview> reviews) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        hashTagsAdapter = new HashTagsAdapter(getActivity(), reviews, new LikeDeslikeCallBack() {
            @Override
            public void likeReview(final String reviewId) {
                presenterGetHashTag.addLike(reviewId);
            }

            @Override
            public void dislikeReview(final String dislekeReview) {
                presenterGetHashTag.addLike(dislekeReview);
            }
        });
        recyclerView.setAdapter(hashTagsAdapter);
        hashTagsAdapter.notifyDataSetChanged();
    }

    public void setReviews(ArrayList<ResponseReview> reviews) {
        progress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        initRecycle(reviews);
    }

}
