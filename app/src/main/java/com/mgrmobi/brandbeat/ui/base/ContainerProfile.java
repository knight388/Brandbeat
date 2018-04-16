package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseAchievement;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerProfile extends ContainerPaginationProggress
{
    void getProfile(ResponseProfile responseProfile);

    void getUserReview(ArrayList<ResponseReview> responseReviews);

    void getPhotoUrl(String s);

    void setAchivmients(List<ResponseAchievement> achivmients);
}
