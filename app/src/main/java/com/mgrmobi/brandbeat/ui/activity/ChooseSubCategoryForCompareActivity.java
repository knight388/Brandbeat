package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerSubCategory;
import com.mgrmobi.brandbeat.ui.fragment.CheckSubCategoryFragment;
import com.mgrmobi.brandbeat.ui.fragment.ChooseSubCategoryForCompareFragment;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ChooseSubCategoryForCompareActivity extends BaseActivity implements ContainerSubCategory
{
    private Fragment subCategoryFragment;
    public static final String CATEGORY_ID = "category_id";
    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments;
    }

    public static Intent createIntent(Context callingContext, String categoryId) {
        Intent intent = new Intent(callingContext, ChooseSubCategoryForCompareActivity.class);
        intent.putExtra(CATEGORY_ID, categoryId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSubCategoryFragment();
    }

    private void showSubCategoryFragment()
    {
        subCategoryFragment = new ChooseSubCategoryForCompareFragment();
        showFragment(subCategoryFragment);
    }

    @Override
    public void getSubCategory(final ArrayList<ResponseCategories> responseCategories) {
        if(subCategoryFragment != null && subCategoryFragment instanceof ChooseSubCategoryForCompareFragment)
        {
            ((ChooseSubCategoryForCompareFragment) subCategoryFragment).getSubcategories(responseCategories);
        }
    }

    @Override
    public void setCategories() {
        finish();
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
}