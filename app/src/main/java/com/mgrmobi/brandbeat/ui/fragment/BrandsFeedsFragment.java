package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseFeed;
import com.mgrmobi.brandbeat.presenter.PresenterMyFeed;
import com.mgrmobi.brandbeat.ui.adapters.BrandsFeedRecyclerAdapter;
import com.mgrmobi.brandbeat.ui.base.BaseNavigationActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerMyFeed;
import com.mgrmobi.brandbeat.ui.callbacks.LikeDeslikeCallBack;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class BrandsFeedsFragment extends Fragment {
    @Bind(R.id.list_view)
    public RecyclerView listView;
    @Bind(R.id.swipe_refresh_layout)
    public SwipeRefreshLayout swipeRefreshLayout;
    private PresenterMyFeed presenterMyFeed = new PresenterMyFeed();
    private ContainerMyFeed containerMyFeed;
    private ArrayList<ResponseFeed> responseFeeds = new ArrayList<>();
    private BrandsFeedRecyclerAdapter myFeedAdapter;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean last = true;
    private int mScrollOffset = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_feed_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerMyFeed) {
            containerMyFeed = (ContainerMyFeed) getActivity();
            presenterMyFeed.setView(containerMyFeed);
        }
        ButterKnife.bind(this, view);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        listView.setItemAnimator(itemAnimator);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);
        myFeedAdapter = new BrandsFeedRecyclerAdapter(getActivity(), responseFeeds, new LikeDeslikeCallBack() {
            @Override
            public void likeReview(final String reviewId) {
                presenterMyFeed.addLike(reviewId);
            }

            @Override
            public void dislikeReview(final String dislikeReview) {
                presenterMyFeed.addDislike(dislikeReview);
            }
        });
        listView.setAdapter(myFeedAdapter);
        swipeRefreshLayout.setOnRefreshListener(presenterMyFeed::getNewMyFeed);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && last) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        presenterMyFeed.getNextMyFeed();
                    }
                }
                if (Math.abs(dy) > mScrollOffset) {
                    if (dy > 0) {
                        if(getActivity() instanceof BaseNavigationActivity)
                            ((BaseNavigationActivity) getActivity()).getMenu().hideMenu(true);
                    }
                    else {
                        if(getActivity() instanceof BaseNavigationActivity)
                            ((BaseNavigationActivity) getActivity()).getMenu().showMenu(true);
                    }
                }
            }
        });
    }

    public void setMyFeed(ArrayList<ResponseFeed> myFeed) {
        swipeRefreshLayout.setRefreshing(false);
        if (myFeed == null || myFeed.size() == 0) {
            myFeedAdapter.setReviewItems(myFeed);
            myFeedAdapter.setNullHolder(true);
            myFeedAdapter.notifyDataSetChanged();
        }
        else {
            myFeedAdapter.setReviewItems(myFeed);
            myFeedAdapter.setNullHolder(false);
            myFeedAdapter.notifyDataSetChanged();
        }
    }

    public void setSuggestedBrand(ArrayList<ResponseBrand> suggestedBrand) {
        myFeedAdapter.setBrands(suggestedBrand);
    }

    @Override
    public void onResume() {
        super.onResume();
        setMyFeed(new ArrayList<>());
        presenterMyFeed.setResponseMyFeed(new ArrayList<>());
        presenterMyFeed.getSuggestedBrands();
        presenterMyFeed.getNewMyFeed();
    }

}
