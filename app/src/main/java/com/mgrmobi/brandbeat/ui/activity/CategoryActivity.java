package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerInterests;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.InterestsFragment;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class CategoryActivity extends BaseActivity implements ContainerInterests {
    private Fragment fragment;
    private CustomProgressDialog progressDialog;

    private void showFragment() {
        fragment = new InterestsFragment();
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

    @Override
    public void getInterest(final ArrayList<ResponseCategories> interests) {
        if (fragment != null && fragment instanceof InterestsFragment) {
            ((InterestsFragment) fragment).setInterests(interests);
        }
    }

    @Override
    public void onComplete() {
        finish();
        startActivity(NavigationActivity.createIntent(this));
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    public void hideProgress() {
        if (progressDialog == null) {
            return;
        }
        else if (progressDialog.isVisible())
            progressDialog.dismiss();
    }

    @Override
    public void showProgress() {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.okay_menu, menu);
        return true;
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.interests_title_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Utils.showAlertDialog(this, (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        }, (dialog, which) -> dialog.dismiss(),
                        getString(R.string.title_alert_message_interests),
                        getString(R.string.alert_message_interests), true, true);
                return true;
            case R.id.action_ok:
                if (fragment instanceof InterestsFragment) {
                    if (((InterestsFragment) fragment).haveCheckedItems())
                        ((InterestsFragment) fragment).uploadNewInterest();
                    else
                        Utils.showAlertDialog(this, (dialog, which) -> {
                                    dialog.dismiss();
                                }, (dialog, which) -> dialog.dismiss(),
                                getString(R.string.title_alert_message_interests),
                                getString(R.string.please_choose_interest), true, false);
                }
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }


    @Override
    public void onBackPressed() {
        Utils.showAlertDialog(this, (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }, (dialog, which) -> dialog.dismiss(),
                getString(R.string.title_alert_message_interests),
                getString(R.string.alert_message_interests), true, true);
    }

}
