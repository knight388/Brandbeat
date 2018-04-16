package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mgrmobi.sdk.social.android.model.SocialUser;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerRegistration;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.RegistrationFragment;
import com.mgrmobi.brandbeat.utils.Utils;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RegistrationActivity extends BaseActivity implements ContainerRegistration {
    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    private Fragment registrationFragment;
    private CustomProgressDialog progressDialog;

    public static Intent callingIntent(Context callingContext) {
        return new Intent(callingContext, RegistrationActivity.class);
    }

    @Override
    protected void onCreate(Bundle saveBundle) {
        super.onCreate(saveBundle);
        showRegistration();
        initToolbar();
    }

    @Override
    public void initViewAfterGetProfile(SocialUser socialUser) {
        if(registrationFragment instanceof RegistrationFragment) {
            ((RegistrationFragment) registrationFragment).nameEditText.setText(socialUser.getName());
            ((RegistrationFragment) registrationFragment).emailEditText.setText(socialUser.getEmail());
            if(socialUser.getBirthday() != null)
                ((RegistrationFragment) registrationFragment).dateOfBirth.setText(Utils.formatDate.format(socialUser.getBirthday()));
            if(socialUser.getLocation() != null)
            ((RegistrationFragment) registrationFragment).locationEditText.setText(socialUser.getLocation());
        }
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.register_button_title));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void registrationWithEmail() {

    }

    @Override
    public void registrationWihFaceBook() {
        hideProgress();
        finish();
        startActivity(CategoryActivity.createIntent(getBaseContext()));
    }

    @Override
    public void registrationWithTwitter() {

    }

    @Override
    public void registrationWithLinkedIn() {

    }

    @Override
    public void registrationWihGooglePlus() {

    }

    @Override
    public void showError(final Throwable e) {
        hideProgress();
        Snackbar.make(getParentView(), e.getMessage(), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.ok), v -> {
                })
                .show();

    }

    @Override
    public void showProgress() {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void hideProgress() {
        if(progressDialog == null) {
            return;
        }
        progressDialog.dismiss();
    }

    @Override
    public void loginSuccess() {
        hideProgress();
        finish();
        startActivity(CategoryActivity.createIntent(getBaseContext()));
    }

    private void showRegistration() {
        registrationFragment = new RegistrationFragment();
        showFragment(registrationFragment);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(registrationFragment instanceof RegistrationFragment) {
            ((RegistrationFragment) registrationFragment).callOnActivityResult(resultCode, requestCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void exit()
    {
        if(registrationFragment instanceof RegistrationFragment)
        {
            if(((RegistrationFragment) registrationFragment).isDataEnter())
            {
                Utils.showAlertDialog(this, (dialog, which) -> {
                    finish();
                }, (dialog1, which1) -> {
                    dialog1.dismiss();
                }, getString(R.string.warning), getString(R.string.save_message), true, true);
            }
            else{
                finish();
            }
        }
        else finish();
    }
}
