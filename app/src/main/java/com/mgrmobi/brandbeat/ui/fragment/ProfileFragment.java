package com.mgrmobi.brandbeat.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseAchievement;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.presenter.PresenterProfileView;
import com.mgrmobi.brandbeat.ui.adapters.ProfileReviewsAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerProfile;
import com.mgrmobi.brandbeat.utils.UserDataUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ProfileFragment extends Fragment {
    @Bind(R.id.recycle)
    public RecyclerView recyclerView;

    private PresenterProfileView presenter = new PresenterProfileView();
    private ContainerProfile containerProfile;
    private ProfileReviewsAdapter profileReviewsAdapter;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean last = true;
    private LinearLayoutManager verticalManager;
    private ResponseProfile profile;
    private List<ResponseReview> reviews;
    private ArrayList<ResponseAchievement> achievements;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(R.id.action_context).setVisible(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerProfile) {
            containerProfile = (ContainerProfile) getActivity();
            presenter.setView(containerProfile);
        }
        ButterKnife.bind(this, view);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        verticalManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(false);
        if (profileReviewsAdapter == null)
            profileReviewsAdapter = new ProfileReviewsAdapter(getActivity(), new ArrayList<>());
        recyclerView.setAdapter(profileReviewsAdapter);
        if (profile != null)
            profileReviewsAdapter.setProfile(profile);
        if (reviews != null)
            profileReviewsAdapter.setReviews((ArrayList<ResponseReview>) reviews);

    }

    public void setProfile(ResponseProfile profile) {
        if (profile == null) return;
        if (achievements != null) {
            profile.setAchievements(achievements);
        }

        this.profile = profile;
        if (this.profile.getAchievements() == null) {
            Log.e("daspifhjdasof", "null this.profile");
        }
        if (profile.getAchievements() == null) {
            Log.e("daspifhjdasof", "null get profile");
        }
        if (achievements == null) {
            Log.e("daspifhjdasof", "null achivmients");
        }
        if (profile.getPhoto(PhotoSize.SIZE_SMALL , getContext()) != null &&
                profile.getPhoto(PhotoSize.SIZE_SMALL , getContext()).equals(""))
            profile.setPhoto(null);
        profileReviewsAdapter.setProfile(profile);
        profileReviewsAdapter.notifyDataSetChanged();
    }

    public void setPhoto(String photoUrl) {
        ResponseProfile profile = profileReviewsAdapter.getResponseProfile();
        profile.setPhoto(photoUrl);
        profileReviewsAdapter.setProfile(profile);
        profileReviewsAdapter.notifyDataSetChanged();
    }

    public void setAchvimients(List<ResponseAchievement> achvimients) {
        this.achievements = (ArrayList<ResponseAchievement>) achvimients;
        if (this.achievements == null)
            Log.e("daspifhjdasof", "null get achivmients ");
        if (profile == null) {
            return;
        }
        profile.setAchievements((ArrayList<ResponseAchievement>) achvimients);
        if (profile.getPhoto(PhotoSize.SIZE_SMALL , getContext()) != null && profile.getPhoto(PhotoSize.SIZE_SMALL , getContext()).equals(""))
            profile.setPhoto(null);
        profileReviewsAdapter.setProfile(profile);
        profileReviewsAdapter.notifyDataSetChanged();
    }

    public void setReviews(ArrayList<ResponseReview> reviews) {
        this.reviews = reviews;
        profileReviewsAdapter.dismissViewHolder();
        profileReviewsAdapter.setReviews(reviews);
        profileReviewsAdapter.notifyDataSetChanged();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && last) {
                    visibleItemCount = verticalManager.getChildCount();
                    totalItemCount = verticalManager.getItemCount();
                    pastVisiblesItems = verticalManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        last = presenter.getNextUserReviews();
                    }
                }
            }
        });
    }

    public void setBitmap(Bitmap bitmap) {
        if (profileReviewsAdapter == null)
            profileReviewsAdapter = new ProfileReviewsAdapter(getContext(), new ArrayList<>());
        profileReviewsAdapter.setPhoto(bitmap);
        profileReviewsAdapter.notifyDataSetChanged();
        presenter.uploadPhoto(bitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        UserDataUtils userDataUtils = new UserDataUtils(getActivity());
        presenter.getUser(userDataUtils.getUserId());
        containerProfile.showProgress();
        profileReviewsAdapter.setReviews(new ArrayList<>());
        presenter.getReview();
        presenter.getAchivmient();
    }

    public void loadNextPage() {
        profileReviewsAdapter.showViewNextLoad();
    }

    public void setReviews(final List<ResponseReview> reviews) {
        this.reviews = reviews;
    }

    public void setProfileResponse(ResponseProfile profileResponse) {
        profile = profileResponse;
    }

    public ResponseProfile getProfile() {
        return profile;
    }

    public List<ResponseReview> getReviews() {
        return reviews;
    }
}
