package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerAnotherUserProfile extends ContainerPaginationProggress
{
    void getProfile(ResponseProfile responseProfile);

    void getReviews(ArrayList<ResponseReview> responseReviews);

    void subscribe();

    void unSubscribe();

}
