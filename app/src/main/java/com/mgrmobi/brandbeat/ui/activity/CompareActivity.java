package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.fragment.ComparisonFragment;
import com.mgrmobi.brandbeat.utils.UserDataUtils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class CompareActivity extends BaseActivity {
    private Fragment compareFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }
    public static Intent callingIntent(Context callingContext, ArrayList<ResponseBrand> responseBrands)
    {
        UserDataUtils userDataUtils = new UserDataUtils(callingContext);
        userDataUtils.saveComparisonView(responseBrands);
        return new Intent(callingContext, CompareActivity.class);
    }

    @Override
    protected void onCreate(Bundle saveBundle)
    {
        super.onCreate(saveBundle);
        showCompare();
        initToolbar();
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.comparison));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void showCompare()
    {
        compareFragment = new ComparisonFragment();
        showFragment(compareFragment);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
