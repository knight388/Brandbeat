package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.presenter.PresenterEditProfile;
import com.mgrmobi.brandbeat.ui.base.ContainerEditProfile;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ChangePasswordFragment extends Fragment {
    @Bind(R.id.submit_button)
    public View submit;
    @Bind(R.id.til_password)
    public TextInputLayout tilPassword;
    @Bind(R.id.til_confirm_password)
    public TextInputLayout tilOldPassword;
    @Bind(R.id.input_password)
    public EditText password;
    @Bind(R.id.input_confirm_password)
    public EditText newPassword;
    @Bind(R.id.til_confirm_old_password)
    public TextInputLayout oldConfirm;
    @Bind(R.id.input__confirm_password)
    public EditText editTextConfirmOldPassword;

    private ContainerEditProfile containerRestorePassword;
    private PresenterEditProfile presenterRestorePassword = new PresenterEditProfile();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_password_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerEditProfile) {
            containerRestorePassword = (ContainerEditProfile) getActivity();
            presenterRestorePassword.setView(containerRestorePassword);
        }
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.cancel_button)
    protected void onClickCancel() {
        getActivity().finish();
    }

    @OnClick(R.id.submit_button)
    protected void onClickSubmit() {
        boolean isError = false;
        if (password.getText().toString().length() == 0) {
            tilOldPassword.setError(getString(R.string.required));
            isError = true;
        }
        if (editTextConfirmOldPassword.getText().toString().length() == 0) {
            oldConfirm.setError(getString(R.string.required));
            isError = true;
        }
        if (!isError && !newPassword.getText().toString().equals(editTextConfirmOldPassword.getText().toString())) {
            oldConfirm.setError(getString(R.string.passwords_do_not_match));
            isError = true;
        }
        if (newPassword.getText().length() == 0) {
            tilPassword.setError(getString(R.string.required));
            isError = true;
        }
        if (isError) return;
        presenterRestorePassword.updatePassword(password.getText().toString(), newPassword.getText().toString());
    }
}
