package com.mgrmobi.brandbeat.ui.fragment.best_ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseSubCategotry;
import com.mgrmobi.brandbeat.presenter.PresenterGetSubCategory;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.mgrmobi.brandbeat.ui.adapters.best_ui.NewBrandsPagerAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerGetBrandsInSubCategory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NewBrandPagerFragment extends Fragment implements ContainerGetBrandsInSubCategory {
    private NewBrandsPagerAdapter brandsPagerAdapter;

    protected Toolbar toolbar;

    public ArrayList<String> brandIds;
    @Bind(R.id.container)
    public ViewPager viewPager;
    private String brandFirstBrand;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_brand_pager_fragment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        brandIds = new ArrayList<>();
        brandFirstBrand = ((String) getActivity().getIntent().getSerializableExtra(NewBrandPagerActivtiy.BRAND_ID));
        brandsPagerAdapter = new NewBrandsPagerAdapter(getChildFragmentManager(), getActivity());
        PresenterGetSubCategory presenterGetSubCategory = new PresenterGetSubCategory();
        presenterGetSubCategory.setView(this);
        presenterGetSubCategory.getSubCategory(getActivity().getIntent().getStringExtra(NewBrandPagerActivtiy.SUBCATEGORY_ID));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(R.id.action_context).setVisible(true);
    }

    @Override
    public void setSubCategory(final ResponseSubCategotry subCategory) {
        brandsPagerAdapter.setIdsBrand(subCategory.getBrandsIds());
        brandsPagerAdapter.notifyDataSetChanged();
        brandIds = subCategory.getBrandsIds();
        int i = 0;
        for(; i < subCategory.getBrandsIds().size(); i++) {
            if (brandFirstBrand.equals(subCategory.getBrandsIds().get(i))) {
                break;
            }
        }
        viewPager.setAdapter(brandsPagerAdapter);
      //  viewPager.setCurrentItem(i+1);
        brandsPagerAdapter.notifyDataSetChanged();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

    }
}
