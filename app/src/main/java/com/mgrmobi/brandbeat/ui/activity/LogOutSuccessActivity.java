package com.mgrmobi.brandbeat.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.fragment.LogOutFragment;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class LogOutSuccessActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLogout();
    }
    private void showLogout()
    {
        Fragment logOutFragment = new LogOutFragment();
        showFragment(logOutFragment);
    }
}
