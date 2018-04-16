package com.mgrmobi.brandbeat.ui.activity.best_ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerRecentBrands;
import com.mgrmobi.brandbeat.ui.fragment.SeeAllBrandInSubCategoryFragment;
import com.mgrmobi.brandbeat.ui.fragment.best_ui.NewSeeAllBrandInSubCategoryFragment;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NewSeeAllBrandInSubCategoryActivity  extends BaseActivity implements ContainerRecentBrands {
    public static final String SUBCATEGORY_ID = "subcategory_id";
    public static final String SUB_CATEGORY_NAME = "subcategory_name";
    private Fragment fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.check_brands_layout;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        showFragment();
        initToolbar();
    }

    public static Intent createIntent(Context context, String subcat, String name) {
        Intent intent = new Intent(context, NewSeeAllBrandInSubCategoryActivity.class);
        intent.putExtra(SUBCATEGORY_ID, subcat);
        intent.putExtra(SUB_CATEGORY_NAME, name);
        return intent;
    }

    private void showFragment() {
        fragment = new NewSeeAllBrandInSubCategoryFragment();
        super.showFragment(fragment);
    }

    @Override
    public void getBrands(final ArrayList<ResponseBrand> responseBrands) {
        if (fragment instanceof NewSeeAllBrandInSubCategoryFragment) {
            ((NewSeeAllBrandInSubCategoryFragment) fragment).setBrands(responseBrands);
        }
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }

    private boolean isShowNetworkError = false;

    @Override
    public void showError(final Throwable e) {
        if (e instanceof NetworkErrorException) {
            if (!isShowNetworkError) {
                isShowNetworkError = true;
                Utils.showAlertDialog(this, (dialog, which) -> dialog.dismiss(), (dialog1, which1) -> dialog1.dismiss(),
                        getString(R.string.error), e.getMessage(), true, false);
            }
        }
        else {
            Utils.showAlertDialog(this, (dialog, which) -> dialog.dismiss(), (dialog1, which1) -> dialog1.dismiss(),
                    getString(R.string.error), e.getMessage(), true, false);
        }
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(SUB_CATEGORY_NAME));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
