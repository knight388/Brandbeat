package com.mgrmobi.brandbeat.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.ui.fragment.BrandsFeedsFragment;
import com.mgrmobi.brandbeat.ui.fragment.LocalFeedFragment;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class FeedPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public FeedPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments = (ArrayList<Fragment>) ((AppCompatActivity) context).getSupportFragmentManager().getFragments();
        if(position == 1) {
            for(int i = 0; i < fragments.size(); i++) {
                if(fragments.get(i) instanceof LocalFeedFragment) {
                    return fragments.get(i);
                }
            }
            return new LocalFeedFragment();
        }
        else if(position == 0) {
            for(int i = 0; i < fragments.size(); i++) {
                if(fragments.get(i) instanceof BrandsFeedsFragment) {
                    return fragments.get(i);
                }
            }
            return new BrandsFeedsFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.my_feed);
            case 1:
                return context.getResources().getString(R.string.local_feed);
        }
        return null;
    }
}