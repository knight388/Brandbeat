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
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.presenter.PresenterAnotherProfileView;
import com.mgrmobi.brandbeat.ui.activity.AnotherUserProfileActivity;
import com.mgrmobi.brandbeat.ui.adapters.AnotherProfileReviewsAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerAnotherUserProfile;
import com.mgrmobi.brandbeat.ui.callbacks.FollowCallBack;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class AnotherUserProfileFragment extends Fragment {
    @Bind(R.id.recycle)
    public RecyclerView recyclerView;
    private LinearLayoutManager verticalLayoutManager;
    private PresenterAnotherProfileView presenter = new PresenterAnotherProfileView();
    private ContainerAnotherUserProfile containerProfile;
    private AnotherProfileReviewsAdapter profileReviewsAdapter;
    private ResponseProfile profile;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean last = true;

    public void setFollowButtion(ImageView view) {
        if(profile.isSubscriber()) {
            view.setImageResource(R.drawable.follow_unselect);
        }
        else {
            view.setImageResource(R.drawable.follow_select);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(profile.isSubscriber()) {
                    followCallBack.unFollow();
                    view.setImageResource(R.drawable.follow_select);
                }
                else {
                    followCallBack.follow();
                    view.setImageResource(R.drawable.follow_unselect);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(getActivity() instanceof ContainerAnotherUserProfile) {
            containerProfile = (ContainerAnotherUserProfile) getActivity();
            presenter.setView(containerProfile);
        }
        ButterKnife.bind(this, view);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        verticalLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(false);

        profileReviewsAdapter = new AnotherProfileReviewsAdapter(getActivity(), new ArrayList<ResponseReview>(), followCallBack);
        recyclerView.setAdapter(profileReviewsAdapter);
        String userId = getActivity().getIntent().getStringExtra(AnotherUserProfileActivity.USER_ID);
        presenter.getProfile(userId);
        presenter.getUserReviews(userId);

    }

    private FollowCallBack followCallBack = new FollowCallBack() {
        @Override
        public void follow() {
            presenter.follow(profile.getId());
        }

        @Override
        public void unFollow() {
            presenter.unFollow(profile.getId());
        }
    };

    public void setProfile(ResponseProfile profile) {
        this.profile = profile;
        profileReviewsAdapter.setProfile(profile);
        profileReviewsAdapter.notifyDataSetChanged();
    }

    public void setReviews(ArrayList<ResponseReview> reviews) {
        profileReviewsAdapter.dismissViewHolder();
        profileReviewsAdapter.setReviews(reviews);
        profileReviewsAdapter.notifyDataSetChanged();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0 && last) {
                    visibleItemCount = verticalLayoutManager.getChildCount();
                    totalItemCount = verticalLayoutManager.getItemCount();
                    pastVisiblesItems = verticalLayoutManager.findFirstVisibleItemPosition();
                    if((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        last = presenter.getNextUserReviews();
                    }
                }
            }

        });
    }

    public void subscrabe() {
        profile.setIsSubscriber(true);
        profile.setFollowerSum(Integer.parseInt(profile.getFollowerSum()) + 1 + "");
        profileReviewsAdapter.setProfile(profile);
        profileReviewsAdapter.notifyItemChanged(0);
    }

    public void unSubscrabe() {
        profile.setIsSubscriber(false);
        profile.setFollowerSum(Integer.parseInt(profile.getFollowerSum()) - 1 + "");
        profileReviewsAdapter.setProfile(profile);
        profileReviewsAdapter.notifyItemChanged(0);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadNextPage()
    {
        profileReviewsAdapter.showViewNextLoad();
    }
}
