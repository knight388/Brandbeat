package com.mgrmobi.brandbeat.ui.base;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerProgress
{
    void hideProgress();

    void showProgress();

    void showError(Throwable e);

}
