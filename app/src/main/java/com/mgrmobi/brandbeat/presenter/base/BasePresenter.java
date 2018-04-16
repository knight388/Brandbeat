package com.mgrmobi.brandbeat.presenter.base;

import com.mgrmobi.brandbeat.interactors.base.DefaultSubscriber;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public abstract class BasePresenter extends DefaultSubscriber {

    public abstract void resume();

    public abstract void pause();

    public abstract void destroy();

}
