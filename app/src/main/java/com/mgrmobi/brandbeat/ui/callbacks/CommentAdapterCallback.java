package com.mgrmobi.brandbeat.ui.callbacks;

import com.mgrmobi.brandbeat.network.responce.ResponseComment;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface CommentAdapterCallback
{
    public void deleteComment(String idComment, int position);

    public void editComment(ResponseComment responseComment);

    public void likeReview();

    public void deleteLike();
}
