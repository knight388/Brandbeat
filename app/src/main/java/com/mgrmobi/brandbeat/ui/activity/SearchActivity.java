package com.mgrmobi.brandbeat.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerSearchView;
import com.mgrmobi.brandbeat.ui.fragment.SearchFragment;
import com.mgrmobi.brandbeat.ui.widget.RevealLinearLayout;
import com.mgrmobi.brandbeat.ui.widget.brand_view.utils.CommonUtils;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SearchActivity extends BaseActivity implements ContainerSearchView {
    private Fragment fragment;
    private SearchView searchView;
    private RevealLinearLayout rootView;
    private static final long REVEAL_ANIMATION_DURATION_MILLIS = 500;
    private static final String EXTRA_X = "extra_x";
    private static final String EXTRA_Y = "extra_y";

    private boolean revealInProgress;

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setWindowAnimations(R.style.NoAnimation);

        initToolbar();
        fragment = new SearchFragment();
        showFragment(fragment);
        rootView = (RevealLinearLayout) findViewById(R.id.root_view);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                showContentWithReveal();
            }
        });
    }

    private void showContentWithReveal() {
        if (CommonUtils.isRevealSupported()) {
            showContentWithNativeReveal();
        }
        else {
            showContentWithCustomReveal();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showContentWithNativeReveal() {
        Intent intent = getIntent();
        int startX = intent.getIntExtra(EXTRA_X, 0);
        int startY = intent.getIntExtra(EXTRA_Y, 0) - CommonUtils.getStatusBarHeight(this);
        float finalRadius = (float) Math.hypot(rootView.getMeasuredWidth(), rootView.getMeasuredHeight());
        Animator animator = ViewAnimationUtils.createCircularReveal(rootView, startX, startY, 0, finalRadius);
        rootView.setVisibility(View.VISIBLE);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                revealInProgress = false;
            }
        });
        animator.setInterpolator(new AccelerateInterpolator(2.0f));
        animator.setDuration(REVEAL_ANIMATION_DURATION_MILLIS);
        animator.start();
        revealInProgress = true;
    }

    private void showContentWithCustomReveal() {
        Intent intent = getIntent();
        int startX = intent.getIntExtra(EXTRA_X, 0);
        int startY = intent.getIntExtra(EXTRA_Y, 0) - CommonUtils.getStatusBarHeight(this);
        float finalRadius = (float) Math.hypot(rootView.getMeasuredWidth(), rootView.getMeasuredHeight());
        rootView.setClipOutLines(true);
        rootView.setClipCenter(startX, startY);
        rootView.setClipRadius(0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(rootView, "clipRadius", 0, finalRadius);
        rootView.setVisibility(View.VISIBLE);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (rootView != null) {
                    rootView.setClipOutLines(false);
                }
                revealInProgress = false;
            }
        });
        animator.setInterpolator(new AccelerateInterpolator(2.0f));
        animator.setDuration(REVEAL_ANIMATION_DURATION_MILLIS);
        animator.start();
        revealInProgress = true;
    }

    public static Intent createIntent(Context context, int extraX, int extraY) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(EXTRA_X, extraX);
        intent.putExtra(EXTRA_Y, extraY);
        return intent;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_text_view, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);

        searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchActivity.this.getComponentName()));
        }
        searchView.setFocusable(true);
        searchView.setIconified(false);
        AppCompatImageView closeButton = (AppCompatImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.search, null));

        // Set on click listener
        closeButton.setOnClickListener(v -> {
            if (searchView.getQuery().toString().length() > 0) {
                if (((SearchFragment) fragment).currentSearchItem == 1) {
                    ((SearchFragment) fragment).setSearchString1(searchView.getQuery().toString());
                } else if (((SearchFragment) fragment).currentSearchItem == 2) {
                    ((SearchFragment) fragment).setSearchString2(searchView.getQuery().toString());
                } else {
                    ((SearchFragment) fragment).setSearchString3(searchView.getQuery().toString());
                }
            }
        });
        searchView.setQueryHint(getString(R.string.search));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    if (((SearchFragment) fragment).currentSearchItem == 1) {
                        ((SearchFragment) fragment).setSearchString1(query);
                    } else if (((SearchFragment) fragment).currentSearchItem == 2) {
                        ((SearchFragment) fragment).setSearchString2(query);
                    } else {
                        ((SearchFragment) fragment).setSearchString3(query);
                    }
                }

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void searchResult(final ArrayList<ResponseBrand> responseBrands) {
        if (fragment instanceof SearchFragment) {
            if (((SearchFragment) fragment).currentSearchItem == 1) {
                ((SearchFragment) fragment).setSearchResult1(responseBrands);
            } else if (((SearchFragment) fragment).currentSearchItem == 2) {
                ((SearchFragment) fragment).setSearchResult2(responseBrands);
            } else {
                ((SearchFragment) fragment).setSearchResult3(responseBrands);
            }
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
}
