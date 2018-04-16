package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerRestorePassword;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.RemindPasswordFragment;
import com.mgrmobi.brandbeat.utils.Utils;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RemindPasswordActivity extends BaseActivity implements ContainerRestorePassword
{
    private Fragment passwordFragment;
    private CustomProgressDialog progressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments;
    }

    public static Intent createIntent(Context callingContext) {
        return new Intent(callingContext, RemindPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showPassword();
    }

    private void showPassword()
    {
        passwordFragment = new RemindPasswordFragment();
        showFragment(passwordFragment);
    }

    @Override
    public void showError(final Throwable e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show(getSupportFragmentManager(), this.getClass().getName());
    }

    @Override
    public void hideProgress() {
        if(progressDialog == null)
        {
            return;
        }
        else if(progressDialog.isVisible())
            progressDialog.dismiss();
    }

    @Override
    public void success() {
        Utils.showAlertDialog(this, (dialog, which) -> {finish();},(dialog1, which1) -> {finish();}, getString(R.string.success),
                getString(R.string.remind_password_message), true, false);
    }

}
