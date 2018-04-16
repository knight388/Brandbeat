package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.presenter.PresenterRestorePassword;
import com.mgrmobi.brandbeat.ui.base.ContainerRestorePassword;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RemindPasswordFragment extends Fragment {
    @Bind(R.id.submit_button)
    public View submit;

    @Bind(R.id.input_email)
    public EditText mInputEmail;

    private ContainerRestorePassword containerRestorePassword;
    private PresenterRestorePassword presenterRestorePassword = new PresenterRestorePassword();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.remind_password_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(getActivity() instanceof ContainerRestorePassword) {
            containerRestorePassword = (ContainerRestorePassword) getActivity();
            presenterRestorePassword.setView(containerRestorePassword);
        }
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.cancel_button)
    protected void onClickCancel() {
        getActivity().finish();
    }

    @OnClick(R.id.submit_button)
    protected  void onClickSubmit()
    {
        String email = mInputEmail.getText().toString();
        if(validate(email))
        presenterRestorePassword.registration(email);
    }
    private boolean validate(String s)
    {
        if(s.length()>5)
            return true;
        else
            return false;
    }
}
