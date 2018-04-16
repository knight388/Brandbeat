package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.ResponseStatistics;

import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerReviewView extends ContainerProgress {
    public void setReview(List<ResponseReview> review);

    public void setStatistics(ResponseStatistics statistics);
}
