package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.network.responce.ResponseIncomeRange;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerEditProfile;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.EditProfileFragment;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class EditProfileActivity extends BaseActivity implements ContainerEditProfile {
    private Fragment editProfileFragment;
    public static final String PROFILE = "profile";
    private CustomProgressDialog progressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    public static Intent createIntent(Context context, ResponseProfile responseProfile) {
        Intent intent;
        if (context == null) {
            intent = new Intent(BrandBeatApplication.getInstance().getBaseContext(), EditProfileActivity.class);
        }
        else {
            intent = new Intent(context, EditProfileActivity.class);
        }

        intent.putExtra(PROFILE, responseProfile);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showEditProfileFragment();
        initToolbar();
    }

    private void showEditProfileFragment() {
        editProfileFragment = new EditProfileFragment();
        showFragment(editProfileFragment);
    }

    @Override
    public void getUpdateProfile(final ResponseProfile responseProfile) {

        if (editProfileFragment != null && editProfileFragment instanceof EditProfileFragment)
            ((EditProfileFragment) editProfileFragment).setProfile();
    }

    @Override
    public void setIncomeRanges(final List<ResponseIncomeRange> incomeRanges) {
        if (editProfileFragment != null && editProfileFragment instanceof EditProfileFragment)
            ((EditProfileFragment) editProfileFragment).setIncomeRange(incomeRanges);
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
    public void showProgress() {

        progressDialog = new CustomProgressDialog(this);
        progressDialog.show(getSupportFragmentManager(), this.getClass().getName());
    }

    @Override
    public void showError(final Throwable e) {
        Utils.showAlertDialog(this, (dialog, which) -> dialog.dismiss(), (dialog1, which1) -> dialog1.dismiss(),
                getString(R.string.error), e.getMessage(), true, false);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getIntent().getSerializableExtra(PROFILE) != null) {
            String name = ((ResponseProfile) getIntent().getSerializableExtra(PROFILE)).getUsername();
            if (name != null)
                toolbar.setTitle(name);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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

    private void exit() {
        if (editProfileFragment instanceof EditProfileFragment && ((EditProfileFragment) editProfileFragment).isChange())
            Utils.showAlertDialog(this, (dialog, which) -> {
                finish();
            }, (dialog1, which1) -> {
                dialog1.dismiss();
            }, getString(R.string.warning), getString(R.string.save_message), true, true);
        else finish();
    }

    @Override
    public void onBackPressed() {
        exit();
    }
}
