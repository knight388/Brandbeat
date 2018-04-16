package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseComment;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerReview extends ContainerProgress
{
    public void getReview(ResponseReview requestReview);

    public void deleteReview(String idReview);

    public void getComments(ArrayList<ResponseComment> responseComments);

    public void deleteComment(int i);

    public void likeAction();
}
