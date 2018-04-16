package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionMenu;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.ui.activity.AddBrandActivity;
import com.mgrmobi.brandbeat.ui.activity.ChooseCategoryForReviewActivity;
import com.mgrmobi.brandbeat.ui.activity.NavigationActivity;
import com.mgrmobi.brandbeat.ui.adapters.FeedPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class FeedFragment extends Fragment {

    private FeedPagerAdapter mSectionsPagerAdapter;

    @Bind(R.id.container)
    public ViewPager mViewPager;
    public TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        mSectionsPagerAdapter = new FeedPagerAdapter(getChildFragmentManager(), getActivity());
        if (getActivity() instanceof NavigationActivity) {
            tabLayout = ((NavigationActivity) getActivity()).getTabLayout();
        }
        else {
            return;
        }
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSectionsPagerAdapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(R.id.action_context).setVisible(true);
    }
}
