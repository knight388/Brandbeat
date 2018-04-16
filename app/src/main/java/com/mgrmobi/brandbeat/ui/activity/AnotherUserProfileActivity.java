package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerAnotherUserProfile;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.AnotherUserProfileFragment;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class AnotherUserProfileActivity extends BaseActivity implements ContainerAnotherUserProfile {

    private CustomProgressDialog progressDialog;
    private Fragment fragment;
    public static final String USER_ID = "user_id";

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    public static Intent createIntent(Context context, String userId) {
        Intent intent = new Intent(context, AnotherUserProfileActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    public void initToolbar(String name) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        fragment = new AnotherUserProfileFragment();
        showFragment(fragment);
        initToolbar("");
    }

    @Override
    public void getProfile(final ResponseProfile responseProfile) {
        if(fragment instanceof AnotherUserProfileFragment) {
            ((AnotherUserProfileFragment) fragment).setProfile(responseProfile);
           String resultName = "";
            if(responseProfile.getFirstName() != null)
                resultName = responseProfile.getFirstName() + " ";
            if(responseProfile.getLastName() != null)
            {
                resultName += responseProfile.getLastName();
            }
            if(resultName.equals(""))
                resultName = responseProfile.getUsername();
            initToolbar(resultName);
        }
    }

    @Override
    public void getReviews(final ArrayList<ResponseReview> responseReviews) {
        if(fragment instanceof AnotherUserProfileFragment) {
            ((AnotherUserProfileFragment) fragment).setReviews(responseReviews);
        }
    }

    @Override
    public void subscribe() {
        if(fragment instanceof AnotherUserProfileFragment) {
            ((AnotherUserProfileFragment) fragment).subscrabe();
        }
    }

    @Override
    public void unSubscribe() {
        if(fragment instanceof AnotherUserProfileFragment) {
            ((AnotherUserProfileFragment) fragment).unSubscrabe();
        }
    }

    @Override
    public void hideProgress() {
        if(progressDialog == null) {
            return;
        }
        else if(progressDialog.isVisible()) {
            progressDialog.dismissAllowingStateLoss();
        }
        if(progressDialog != null) {
            progressDialog = null;
        }
    }

    @Override
    public void showProgress() {
        if(progressDialog != null) {
            return;
        }
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show(getSupportFragmentManager(), this.getClass().getName());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showPaginationProgress() {
        if(fragment instanceof AnotherUserProfileFragment)
        {
            ((AnotherUserProfileFragment) fragment).loadNextPage();
        }
    }

    @Override
    public void dismissPaginationProggress() {

    }
}
