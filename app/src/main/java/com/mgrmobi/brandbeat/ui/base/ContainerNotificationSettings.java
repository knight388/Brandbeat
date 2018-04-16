package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseNotificationSettings;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerNotificationSettings extends ContainerProgress {

    void setSettings(ResponseNotificationSettings settings);

    void updateSettingsComplete();
}
