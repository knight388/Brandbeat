package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.mgrmobi.sdk.social.android.AndroidBaseSocialNetwork;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerAuthorization;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.LoginFragment;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */

public class LoginActivity extends BaseActivity implements ContainerAuthorization {

    private Fragment loginFragment;
    private CustomProgressDialog progressDialog;
    private AndroidBaseSocialNetwork socialNetwork;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLogin();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments;
    }

    public static Intent createIntent(Context callingContext) {
        return new Intent(callingContext, LoginActivity.class);
    }

    @Override
    public void connectWithFB() {

    }

    @Override
    public void connectWithLinkedIn() {

    }

    @Override
    public void connectWithTwitter() {

    }

    @Override
    public void connectWithGooglePlus() {

    }

    @Override
    public void connectWithEmail() {

    }

    @Override
    public void loginSuccess(boolean isNew) {
        if (loginFragment instanceof LoginFragment) {
            ((LoginFragment) loginFragment).getProfile();
        }
    }

    @Override
    public void profileSuccess(final boolean isNew) {
        if (loginFragment instanceof LoginFragment)
            ((LoginFragment) loginFragment).setLocationBean();

    }

    @Override
    public void completeUpdateLocation(final boolean isNew) {
        if (isNew)
            startActivity(CategoryActivity.createIntent(this));
        else
            startActivity(NavigationActivity.createIntent(this));
        finish();
    }

    @Override
    public void showProgress() {
        progressDialog = new CustomProgressDialog(this);
        try {
            progressDialog.show(getSupportFragmentManager(), this.getClass().getName());
        } catch (IllegalStateException e) {
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog == null) {
            return;
        }
        else if (progressDialog.isVisible())
            progressDialog.dismissAllowingStateLoss();
    }

    @Override
    public void showError(Throwable e) {
        Snackbar.make(getParentView(), e.getLocalizedMessage(), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.ok), v -> {
                })
                .show();
    }

    private void showLogin() {
        loginFragment = new LoginFragment();
        showFragment(loginFragment);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (loginFragment instanceof LoginFragment) {
            ((LoginFragment) loginFragment).callOnActivityResult(resultCode, requestCode, data);
        }
    }

}
