package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerHashTag;
import com.mgrmobi.brandbeat.ui.fragment.HashTagsFragment;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class HashTagActivity extends BaseActivity implements ContainerHashTag {
    public static final String HASH_TAG = "hash_tag";
    private Fragment fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    public static Intent createIntent(Context context, String hashTag) {
        Intent intent = new Intent(context, HashTagActivity.class);
        intent.putExtra(HASH_TAG, hashTag);
        return intent;
    }

    @Override
    public void getReviews(final ArrayList<ResponseReview> responseReviews) {
        if (fragment instanceof HashTagsFragment) {
            ((HashTagsFragment) fragment).setReviews(responseReviews);
        }
    }

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        fragment = new HashTagsFragment();
        showFragment(fragment);
        initToolbar();
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
        toolbar.setTitle("#" + getIntent().getStringExtra(HASH_TAG));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
