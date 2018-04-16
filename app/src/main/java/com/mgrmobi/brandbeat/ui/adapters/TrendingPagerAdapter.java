package com.mgrmobi.brandbeat.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.ui.fragment.TrendingPagerFragment;
import com.mgrmobi.brandbeat.ui.fragment.TrendingPagerItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class TrendingPagerAdapter extends FragmentPagerAdapter {

    private List<ResponseBrand> responseBrands;
    private Object currentObject;

    public TrendingPagerAdapter(FragmentManager manager, List<ResponseBrand> responseBrands) {
        super(manager);
        this.responseBrands = responseBrands;
    }

    @Override
    public int getCount() {
        return responseBrands.size();
    }

    @Override
    public Fragment getItem(int position) {
        return TrendingPagerItemFragment.newInstance(responseBrands.get(position), position);
    }

    public void clearAdapter() {
        responseBrands = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object)
    {
        if(object instanceof TrendingPagerItemFragment)
        {
            currentObject = object;
        }
        super.setPrimaryItem(container, position, object);
    }

    public Object getCurrentObject() {
        return currentObject;
    }

    public void setCurrentObject(final Object currentObject) {
        this.currentObject = currentObject;
    }
}
