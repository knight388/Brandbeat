package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseAchievement;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerProfile;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.AchivmientsFragment;
import com.mgrmobi.brandbeat.ui.fragment.InterestsFragment;
import com.mgrmobi.brandbeat.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class AchivmintsActivity extends BaseActivity implements ContainerProfile {
    public static final String ACHIVMIENTS = "achivmients";
    private Fragment fragment;
    private CustomProgressDialog progressDialog;

    private void showFragment() {
        fragment = new AchivmientsFragment();
        showFragment(fragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        showFragment();
        initToolbar();
    }

    public static Intent createIntent(Context context, ArrayList<ResponseAchievement> responseAchievements) {
        Intent intent = new Intent(context, AchivmintsActivity.class);
        intent.putExtra(ACHIVMIENTS, responseAchievements);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.achievements));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void getProfile(final ResponseProfile responseProfile) {

    }

    @Override
    public void getUserReview(final ArrayList<ResponseReview> responseReviews) {

    }

    @Override
    public void getPhotoUrl(final String s) {

    }

    @Override
    public void setAchivmients(final List<ResponseAchievement> achivmients) {
        if (achivmients != null && fragment instanceof AchivmientsFragment) {
            ((AchivmientsFragment) fragment).setAchivmients(achivmients);
        }
    }

    @Override
    public void showPaginationProgress() {

    }

    @Override
    public void dismissPaginationProggress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void showError(final Throwable e) {

    }
}
