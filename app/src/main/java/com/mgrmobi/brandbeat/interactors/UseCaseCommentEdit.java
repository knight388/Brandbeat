package com.mgrmobi.brandbeat.interactors;

import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;

import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class UseCaseCommentEdit extends UseCase {

    private String id;
    private String text;
    public void setComment(String id, String text)
    {
        this.id = id;
        this.text = text;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return restApiBean.editComment(id, text);
    }
}

