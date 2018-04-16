package com.mgrmobi.brandbeat.ui.activity;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerRecentBrands;
import com.mgrmobi.brandbeat.ui.fragment.ChooseBrandForCompareFragment;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ChooseBradsForCompareActivity extends BaseActivity implements ContainerRecentBrands {
    public static final String SUBCATEGORY_ID = "subCategoryId";
    public static final String BRAND_ID = "brand_id";

    private Fragment fragment;
    private SearchView searchView;

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        showFragment();
        initToolbar();
    }

    private void showFragment() {
        fragment = new ChooseBrandForCompareFragment();
        showFragment(fragment);
    }

    public static Intent createIntent(Context context, String brandId, String subCategoryId) {
        Intent intent = new Intent(context, ChooseBradsForCompareActivity.class);
        intent.putExtra(SUBCATEGORY_ID, subCategoryId);
        intent.putExtra(BRAND_ID, brandId);
        return intent;
    }

    @Override
    public void getBrands(final ArrayList<ResponseBrand> responseBrands) {
        if(fragment instanceof ChooseBrandForCompareFragment) {
            if(responseBrands.size() > 0 && responseBrands.get(0)!= null &&
                    responseBrands.get(0).getSubCategories() != null &&
                    responseBrands.get(0).getSubCategories().getTitle() != null)
                ((Toolbar) findViewById(R.id.toolbar)).setTitle(responseBrands.get(0).getSubCategories().getTitle());
            ((ChooseBrandForCompareFragment) fragment).setBrands(responseBrands);
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
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.compare_brands));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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



    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_text_view, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) ChooseBradsForCompareActivity.this.getSystemService(Context.SEARCH_SERVICE);

        searchView = null;
        if(searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if(searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ChooseBradsForCompareActivity.this.getComponentName()));
        }
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);

        // Set on click listener
        closeButton.setOnClickListener(v -> {
            EditText et = (EditText) findViewById(R.id.search_src_text);
            et.setText("");
            searchView.setQuery("", false);
            if(fragment instanceof ChooseBrandForCompareFragment)
            {
                ((ChooseBrandForCompareFragment) fragment).setCancelClick();
            }
        });
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                ((ChooseBrandForCompareFragment) fragment).setSearchString(query);
                return true;
            }
        };
        LinearLayout searchBar = (LinearLayout) searchView.findViewById(R.id.search_bar);
//Give the Linearlayout a transition animation.
        searchBar.setLayoutTransition(new LayoutTransition());
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }
}
