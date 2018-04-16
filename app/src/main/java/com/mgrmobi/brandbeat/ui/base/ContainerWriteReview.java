package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerWriteReview extends ContainerProgress
{
    void success();

    void success(ArrayList<ResponsePhotoUrl> responsePhotoUrls);

    void showError(String s);
}
