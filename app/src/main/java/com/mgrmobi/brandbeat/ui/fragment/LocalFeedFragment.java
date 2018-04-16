package com.mgrmobi.brandbeat.ui.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PermissionInfo;
import android.location.Address;
import android.location.Location;
import android.os.Build;
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

import com.google.android.gms.maps.model.LatLng;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.location.bean.LocationBean;
import com.mgrmobi.brandbeat.location.impl.RxLocationBeanImpl;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseLocalFeed;
import com.mgrmobi.brandbeat.presenter.PresenterLocalFeedView;
import com.mgrmobi.brandbeat.ui.adapters.TrendingRecyclerAdapter;
import com.mgrmobi.brandbeat.ui.base.BaseNavigationActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerLocalFeed;
import com.mgrmobi.brandbeat.ui.callbacks.LikeDeslikeCallBack;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class LocalFeedFragment extends Fragment {
    @Bind(R.id.list_view)
    public RecyclerView listView;
    @Bind(R.id.swipe_refresh_layout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private int mScrollOffset = 1;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean last = true;
    private ContainerLocalFeed containerMyFeed;
    private PresenterLocalFeedView presenterLocalFeedView = new PresenterLocalFeedView();
    private TrendingRecyclerAdapter localFeedAdapter;
    private boolean isLoad = false;
    @Inject
    LocationBean locationBean = new RxLocationBeanImpl(BrandBeatApplication.getInstance().getApplicationContext());

    private LatLng latLng = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.local_feed_fragment, container, false);
        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerLocalFeed) {
            containerMyFeed = (ContainerLocalFeed) getActivity();
            presenterLocalFeedView.setView(containerMyFeed);
        }
        initLocation();
        ButterKnife.bind(this, view);
        presenterLocalFeedView.getSuggestedBrand();
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        listView.setItemAnimator(itemAnimator);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(layoutManager);
        localFeedAdapter = new TrendingRecyclerAdapter(getActivity(), new ArrayList<ResponseLocalFeed>(), new LikeDeslikeCallBack() {
            @Override
            public void likeReview(final String reviewId) {
                presenterLocalFeedView.addLike(reviewId);
            }

            @Override
            public void dislikeReview(final String dislekeReview) {
                presenterLocalFeedView.deleteLikeReview(dislekeReview);
            }
        });
        listView.setAdapter(localFeedAdapter);
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && last) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        presenterLocalFeedView.getNext();
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
        requestAllPermissions();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (latLng != null) {
                presenterLocalFeedView.getLocalFeed(latLng.latitude + "", latLng.longitude + "");
            }
        });
    }

    private void requestAllPermissions() {
        if (isLoad) return;
        isLoad = true;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < Build.VERSION_CODES.M) {
            Utils.checkLocationOlderVersion(getActivity(), () -> {
                initLocation();
            });
        }
        else {
//dragon            RxResult.checkPermissions(getActivity(),
//                    new PermissionInfo(Manifest.permission.ACCESS_COARSE_LOCATION,
//                            getString(R.string.warning), getString(R.string.use_location),
//                            getString(R.string.ok), getString(R.string.cancel)))
//                    .subscribe(permission -> initLocation());
        }
    }

    public void setLocalFeed(ArrayList<ResponseLocalFeed> myFeed) {
        swipeRefreshLayout.setRefreshing(false);
        if (localFeedAdapter.getItemCount() - 2 == myFeed.size())
            return;
        localFeedAdapter.setReviewItems(myFeed);
        isLoad = false;
        localFeedAdapter.notifyDataSetChanged();
    }

    public void setSuggestedBrand(ArrayList<ResponseBrand> suggestedBrand) {
        localFeedAdapter.setBrands((List) suggestedBrand);
        localFeedAdapter.notifyItemChanged(0);
    }

    private void initLocation() {
        isLoad = false;
        Observable<Location> brandBeatAddressObservable = locationBean.getCurrentLocation();
        brandBeatAddressObservable.map(loc -> {
            latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            return latLng;
        }).subscribe(loc -> {
            locationBean.getAddressByLocation(loc)
                    .subscribe(address ->
                    {
                        initRequestLocationFromAddress(address);

                    }, error -> {
                        return;
                    });
        }, error -> {
            return;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        requestAllPermissions();
    }

    public void initRequestLocationFromAddress(Address address) {
        presenterLocalFeedView.getLocalFeed(String.valueOf(address.getLatitude()), String.valueOf(address.getLongitude()));
    }
}
