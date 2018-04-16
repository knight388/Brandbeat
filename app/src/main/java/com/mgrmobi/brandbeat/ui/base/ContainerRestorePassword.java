package com.mgrmobi.brandbeat.ui.base;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerRestorePassword
{
    public void showError(Throwable e);

    public void showProgress();

    public void hideProgress();

    public void success();

}
