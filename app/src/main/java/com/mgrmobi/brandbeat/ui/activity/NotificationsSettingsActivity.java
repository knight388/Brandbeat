package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.network.responce.ResponseNotificationSettings;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerNotificationSettings;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.NotificationsSettingsFragment;
import com.mgrmobi.brandbeat.utils.Utils;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NotificationsSettingsActivity extends BaseActivity implements ContainerNotificationSettings {

    private Fragment fragment;
    private CustomProgressDialog progressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        fragment = new NotificationsSettingsFragment();
        showFragment(fragment);
        initToolbar();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, NotificationsSettingsActivity.class);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.notification));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void setSettings(ResponseNotificationSettings settings) {
        if (fragment instanceof NotificationsSettingsFragment) {
            ((NotificationsSettingsFragment) fragment).setSettings(settings);
        }
    }

    @Override
    public void updateSettingsComplete() {
        finish();
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
        int id = item.getItemId();
        if (id == R.id.action_ok) {
            if (fragment instanceof NotificationsSettingsFragment) {
                ((NotificationsSettingsFragment) fragment).saveSettings();
            }
            return true;
        }
        if (id == android.R.id.home) {
            close();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.okay_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        close();
    }

    private void close() {
        if (fragment instanceof NotificationsSettingsFragment) {
            if (!((NotificationsSettingsFragment) fragment).isChange()) {
                finish();
            }
            else {
                Utils.showAlertDialog(NotificationsSettingsActivity.this, (dialog, which) -> finish(), (dialog, which) -> {
                    dialog.dismiss();
                }, getString(R.string.warning), getString(R.string.save_message), true, true);
            }
        }

    }
}
