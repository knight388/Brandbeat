package com.mgrmobi.brandbeat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.ui.activity.LoginActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class LogOutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.logout_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
     }

    @OnClick(R.id.login_again)
    public void onClick()
    {
        getActivity().finish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
