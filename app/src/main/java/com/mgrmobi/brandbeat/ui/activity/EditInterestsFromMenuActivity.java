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
import com.mgrmobi.brandbeat.ui.fragment.EditInterestsFragment;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class EditInterestsFromMenuActivity extends BaseActivity implements ContainerInterests {

    public static Intent createIntent(Context context) {
        return new Intent(context, EditInterestsFromMenuActivity.class);
    }

    private Fragment fragment;

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showEditProfileFragment();
        initToolbar();
    }

    private void showEditProfileFragment() {
        fragment = new EditInterestsFragment();
        showFragment(fragment);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.interests);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void getInterest(final ArrayList<ResponseCategories> interests) {
        if (fragment != null && fragment instanceof EditInterestsFragment) {
            ((EditInterestsFragment) fragment).setInterests(interests);
        }
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    //    getMenuInflater().inflate(R.menu.okay_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_ok:

                if (((EditInterestsFragment) fragment).haveCheckedInterests()) {
                    ((EditInterestsFragment) fragment).uploadInterests();
                }
                else {
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
        if (((EditInterestsFragment) fragment).isChange())
            Utils.showAlertDialog(this, (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }, (dialog, which) -> dialog.dismiss(),
                getString(R.string.title_alert_message_interests),
                getString(R.string.alert_message_interests), true, true);
        else finish();
    }
}
