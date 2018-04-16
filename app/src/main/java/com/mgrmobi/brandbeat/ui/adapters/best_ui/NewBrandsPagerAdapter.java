package com.mgrmobi.brandbeat.ui.adapters.best_ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;

import com.mgrmobi.brandbeat.ui.fragment.best_ui.BrandPagerItemFragment;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NewBrandsPagerAdapter  extends FragmentStatePagerAdapter {

    protected Toolbar toolbar;
    private ArrayList<String> idsBrands;

    public ArrayList<String> getIdsBrand() {
        return idsBrands;
    }

    public void setIdsBrand(final ArrayList<String> strings) {
        this.idsBrands = strings;
    }


    public NewBrandsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        BrandPagerItemFragment f = new BrandPagerItemFragment();
        Bundle args = new Bundle();
        args.putString("num", idsBrands.get(position));
        f.setArguments(args);
        return f;
        //return BrandPagerItemFragment.newInstance();
    }

    @Override
    public int getCount() {
        return idsBrands.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

        }
        return null;
    }
}