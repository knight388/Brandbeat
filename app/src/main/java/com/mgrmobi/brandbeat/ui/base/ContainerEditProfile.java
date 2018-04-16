package com.mgrmobi.brandbeat.ui.base;

import com.mgrmobi.brandbeat.network.responce.ResponseIncomeRange;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;

import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public interface ContainerEditProfile extends ContainerProgress {

    void getUpdateProfile(ResponseProfile responseProfile);

    void setIncomeRanges(List<ResponseIncomeRange> incomeRanges);
}
