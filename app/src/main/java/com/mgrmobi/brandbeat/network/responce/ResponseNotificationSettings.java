package com.mgrmobi.brandbeat.network.responce;

import com.google.gson.annotations.SerializedName;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ResponseNotificationSettings
{
    @SerializedName("comments")
    private ResponsePushEmailEntity comments;
    @SerializedName("replies")
    private ResponsePushEmailEntity replies;
    @SerializedName("followers")
    private ResponsePushEmailEntity followers;
    @SerializedName("achievements")
    private ResponsePushEmailEntity achievements;
    @SerializedName("system")
    private ResponsePushEmailEntity system;

    public ResponsePushEmailEntity getComments() {
        return comments;
    }

    public void setComments(final ResponsePushEmailEntity comments) {
        this.comments = comments;
    }

    public ResponsePushEmailEntity getReplies() {
        return replies;
    }

    public void setReplies(final ResponsePushEmailEntity replies) {
        this.replies = replies;
    }

    public ResponsePushEmailEntity getFollowers() {
        return followers;
    }

    public void setFollowers(final ResponsePushEmailEntity followers) {
        this.followers = followers;
    }

    public ResponsePushEmailEntity getAchievements() {
        return achievements;
    }

    public void setAchievements(final ResponsePushEmailEntity achievements) {
        this.achievements = achievements;
    }

    public ResponsePushEmailEntity getSystem() {
        return system;
    }

    public void setSystem(final ResponsePushEmailEntity system) {
        this.system = system;
    }
}
