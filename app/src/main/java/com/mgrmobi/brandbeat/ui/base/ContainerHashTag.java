package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseReview;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerHashTag extends ContainerProgress
{
    void getReviews(ArrayList<ResponseReview> responseReviews);
}
