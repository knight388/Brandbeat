package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.BaseResponse;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerFollowBrand extends ContainerProgress
{
    void followReview(BaseResponse responce);
}
