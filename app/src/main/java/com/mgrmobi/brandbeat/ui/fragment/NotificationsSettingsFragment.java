package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseNotificationSettings;
import com.mgrmobi.brandbeat.network.responce.ResponsePushEmailEntity;
import com.mgrmobi.brandbeat.presenter.PresenterSettingsNotifications;
import com.mgrmobi.brandbeat.ui.base.ContainerNotificationSettings;
import com.mgrmobi.brandbeat.ui.base.ContainerTrandingBrands;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NotificationsSettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    @Bind(R.id.comment_phone)
    public CheckBox commentPhone;
    @Bind(R.id.replies_phone)
    public CheckBox repliesPhone;
    @Bind(R.id.new_followers_phone)
    public CheckBox newFollowersPhone;
    @Bind(R.id.new_achievements_phone)
    public CheckBox newAchievementsPhone;
    @Bind(R.id.administrator_phone)
    public CheckBox administrationPhone;
    @Bind(R.id.news_phone)
    public CheckBox newsPhone;
    @Bind(R.id.comment_email)
    public CheckBox commentEmail;
    @Bind(R.id.replies_email)
    public CheckBox repliesEmail;
    @Bind(R.id.new_followers_email)
    public CheckBox newFollowersEmail;
    @Bind(R.id.new_achievements_email)
    public CheckBox newAchievementsEmail;
    @Bind(R.id.administration_email)
    public CheckBox administrationEmail;
    @Bind(R.id.news_email)
    public CheckBox newsEmail;

    private boolean isChange = false;
    private ResponseNotificationSettings responseNotificationSettings;

    private PresenterSettingsNotifications presenterSettingsNotifications = new PresenterSettingsNotifications();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_navigation_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerNotificationSettings) {
            final ContainerNotificationSettings containerNotificationSettings = (ContainerNotificationSettings) getActivity();
            presenterSettingsNotifications.setView(containerNotificationSettings);
        }
        ButterKnife.bind(this, view);
        presenterSettingsNotifications.getSettings();
    }

    public void setSettings(ResponseNotificationSettings settings) {
        responseNotificationSettings = settings;
        commentPhone.setChecked(settings.getComments().isPush());
        repliesPhone.setChecked(settings.getReplies().isPush());
        newFollowersPhone.setChecked(settings.getFollowers().isPush());
        newAchievementsPhone.setChecked(settings.getAchievements().isPush());
        administrationPhone.setChecked(settings.getSystem().isPush());
        commentEmail.setChecked(settings.getComments().isEmail());
        repliesEmail.setChecked(settings.getReplies().isEmail());
        newFollowersEmail.setChecked(settings.getFollowers().isEmail());
        newAchievementsEmail.setChecked(settings.getAchievements().isEmail());
        administrationEmail.setChecked(settings.getSystem().isEmail());
        commentPhone.setOnCheckedChangeListener(this);
        repliesPhone.setOnCheckedChangeListener(this);
        newFollowersPhone.setOnCheckedChangeListener(this);
        newAchievementsPhone.setOnCheckedChangeListener(this);
        administrationPhone.setOnCheckedChangeListener(this);
        commentEmail.setOnCheckedChangeListener(this);
        repliesEmail.setOnCheckedChangeListener(this);
        newFollowersEmail.setOnCheckedChangeListener(this);
        newAchievementsEmail.setOnCheckedChangeListener(this);
        administrationEmail.setOnCheckedChangeListener(this);
    }

    public void saveSettings() {
        if(responseNotificationSettings == null) {
            responseNotificationSettings = new ResponseNotificationSettings();
            responseNotificationSettings.setAchievements(new ResponsePushEmailEntity());
            responseNotificationSettings.setComments(new ResponsePushEmailEntity());
            responseNotificationSettings.setFollowers(new ResponsePushEmailEntity());
            responseNotificationSettings.setReplies(new ResponsePushEmailEntity());
            responseNotificationSettings.setSystem(new ResponsePushEmailEntity());

        }
        responseNotificationSettings.getComments().setPush(commentPhone.isChecked());
        responseNotificationSettings.getReplies().setPush(repliesPhone.isChecked());
        responseNotificationSettings.getFollowers().setPush(newFollowersPhone.isChecked());
        responseNotificationSettings.getAchievements().setPush(newAchievementsPhone.isChecked());
        responseNotificationSettings.getSystem().setPush(administrationPhone.isChecked());
        responseNotificationSettings.getComments().setEmail(commentEmail.isChecked());
        responseNotificationSettings.getReplies().setEmail(repliesEmail.isChecked());
        responseNotificationSettings.getFollowers().setEmail(newFollowersEmail.isChecked());
        responseNotificationSettings.getAchievements().setEmail(newAchievementsEmail.isChecked());
        responseNotificationSettings.getSystem().setEmail(administrationEmail.isChecked());
        presenterSettingsNotifications.setSettings(responseNotificationSettings);
        isChange = false;
    }

    public boolean isChange() {
        return isChange;
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        isChange = true;
    }
}
