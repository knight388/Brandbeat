package com.mgrmobi.brandbeat.ui.activity.best_ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseSubCategotry;
import com.mgrmobi.brandbeat.presenter.PresenterGetSubCategory;
import com.mgrmobi.brandbeat.ui.activity.NewBrandActivity;
import com.mgrmobi.brandbeat.ui.adapters.best_ui.NewBrandsPagerAdapter;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerGetBrandsInSubCategory;
import com.mgrmobi.brandbeat.ui.fragment.best_ui.NewBrandPagerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NewBrandPagerActivtiy extends AppCompatActivity implements ContainerGetBrandsInSubCategory {
    public NewBrandsPagerAdapter brandsPagerAdapter;
    public static String BRAND_ID = "brand_id";
    public static String SUBCATEGORY_ID = "subcategory_id";
    private boolean dispatchTouch;
    public ArrayList<String> brandIds;
    @Bind(R.id.view_pager_brand)
    public ViewPager viewPager;
    private String brandFirstBrand;

    public void setDispatchTouch(boolean touch) {
        dispatchTouch = touch;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.new_brand_pager_fragment);
        ButterKnife.bind(this, this);
        brandIds = new ArrayList<>();
        brandFirstBrand = ((String) getIntent().getSerializableExtra(NewBrandPagerActivtiy.BRAND_ID));
        brandsPagerAdapter = new NewBrandsPagerAdapter(getSupportFragmentManager(), this);
        PresenterGetSubCategory presenterGetSubCategory = new PresenterGetSubCategory();
        presenterGetSubCategory.setView(this);
        String subCategoryId = getIntent().getStringExtra(NewBrandPagerActivtiy.SUBCATEGORY_ID);
        if(subCategoryId == null) {
            brandIds.add((String) getIntent().getSerializableExtra(NewBrandPagerActivtiy.BRAND_ID));
            brandsPagerAdapter.setIdsBrand(brandIds);
            brandsPagerAdapter.notifyDataSetChanged();
            int i = 0;
            viewPager.setAdapter(brandsPagerAdapter);
            viewPager.setCurrentItem(i);
            brandsPagerAdapter.notifyDataSetChanged();
        }
        presenterGetSubCategory.getSubCategory(subCategoryId);
    }

    public static Intent createIntent(String brandId, String subcategoryID, Context context) {
        Intent intent = new Intent(context, NewBrandPagerActivtiy.class);
        intent.putExtra(BRAND_ID, brandId);
        intent.putExtra(SUBCATEGORY_ID, subcategoryID);
        return intent;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return dispatchTouch && super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_brand, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_next:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                return true;
            case R.id.action_previus:
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void setSubCategory(final ResponseSubCategotry subCategory) {
        if(subCategory == null) return;
        if(subCategory.getBrandsIds() != null) {
            brandsPagerAdapter.setIdsBrand(subCategory.getBrandsIds());
            brandsPagerAdapter.notifyDataSetChanged();
        }
        brandIds = subCategory.getBrandsIds();
        int i = 0;
        for(; i < subCategory.getBrandsIds().size(); i++) {
            if (brandFirstBrand.equals(subCategory.getBrandsIds().get(i))) {
                break;
            }
        }
        viewPager.setAdapter(brandsPagerAdapter);
        viewPager.setCurrentItem(i);
        brandsPagerAdapter.notifyDataSetChanged();

    }

}
