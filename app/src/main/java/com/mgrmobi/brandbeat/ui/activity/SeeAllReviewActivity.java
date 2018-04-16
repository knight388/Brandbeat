package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.ResponseStatistics;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerReviewView;
import com.mgrmobi.brandbeat.ui.fragment.SeeAllReviewFragment;

import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SeeAllReviewActivity extends BaseActivity implements ContainerReviewView {
    private Fragment fragment;
    public static String BRAND_ID = "brand_id";

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        fragment = new SeeAllReviewFragment();
        showFragment(fragment);
        initToolBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.reviews));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static Intent createIntent(Context context, String brandId) {
        Intent intent = new Intent(context, SeeAllReviewActivity.class);
        intent.putExtra(BRAND_ID, brandId);
        return intent;
    }

    @Override
    public void setReview(final List<ResponseReview> review) {
        if (fragment instanceof SeeAllReviewFragment) {
            ((SeeAllReviewFragment) fragment).setReviews(review);
            ((SeeAllReviewFragment) fragment).hideProgress();
        }
    }

    @Override
    public void setStatistics(final ResponseStatistics statistics) {
        if (fragment instanceof SeeAllReviewFragment) {
            ((SeeAllReviewFragment) fragment).setResponseStatistics(statistics);
            ((SeeAllReviewFragment) fragment).hideProgress();
        }
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {
        if (fragment instanceof SeeAllReviewFragment)
            ((SeeAllReviewFragment) fragment).showProgress();
    }

    @Override
    public void showError(final Throwable e) {

    }
}
