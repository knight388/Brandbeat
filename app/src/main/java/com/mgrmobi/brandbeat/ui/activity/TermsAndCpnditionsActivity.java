package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.fragment.TermsAndConditionsFragment;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class TermsAndCpnditionsActivity extends BaseActivity {
    private Fragment termsAndConditionsFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    public static Intent createIntent(Context callingContext) {
        Intent intent = new Intent(callingContext, TermsAndCpnditionsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTermsAndConditionsFragment();
        setFinishOnTouchOutside(false);
        initToolbar();
    }

    private void showTermsAndConditionsFragment() {
        termsAndConditionsFragment = new TermsAndConditionsFragment();
        showFragment(termsAndConditionsFragment);
    }

    public void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.terms_and_conditions));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

}
