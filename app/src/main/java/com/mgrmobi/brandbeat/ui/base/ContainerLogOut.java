package com.mgrmobi.brandbeat.ui.base;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerLogOut extends ContainerProgress
{
    public void logOutSuccess();

    public void onError(String message);
}
