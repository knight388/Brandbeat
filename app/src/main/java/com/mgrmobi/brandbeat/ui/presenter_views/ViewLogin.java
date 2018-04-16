package com.mgrmobi.brandbeat.ui.presenter_views;

import android.support.design.widget.TextInputLayout;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ViewLogin {
    TextInputLayout login;
    TextInputLayout password;

    public ViewLogin(TextInputLayout login, TextInputLayout password) {
        this.login = login;
        this.password = password;
        this.login.setErrorEnabled(false);
        this.password.setErrorEnabled(false);
    }

    public void setLoginError(String error) {
        login.setError(error);
    }

    public void setPasswordError(String error)
    {
        password.setError(error);
    }
}
