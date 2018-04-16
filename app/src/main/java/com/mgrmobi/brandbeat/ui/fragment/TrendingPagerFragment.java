package com.mgrmobi.brandbeat.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.ui.adapters.TrendingPagerAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
@SuppressLint("ValidFragment")
public class TrendingPagerFragment extends Fragment {

    @Bind(R.id.pager)
    public ViewPager pager;

    private TrendingPagerAdapter pagerAdapter;
    private List<ResponseBrand> brands;

    public TrendingPagerFragment(List<ResponseBrand> brands) {
        this.brands = brands;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trending_pager, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (brands == null) return;
        pagerAdapter = new TrendingPagerAdapter(getActivity().getSupportFragmentManager(), brands);
        pager.setAdapter(pagerAdapter);
    }

    @Override
    public void onDestroyView() {
        pagerAdapter.clearAdapter();
        super.onDestroyView();
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        if((pagerAdapter).getCurrentObject() instanceof TrendingPagerItemFragment)
        {
            ((TrendingPagerItemFragment) pagerAdapter.getCurrentObject()).onResult(requestCode, resultCode, data);
        }
    }
}
