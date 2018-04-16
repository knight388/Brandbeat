package com.mgrmobi.brandbeat.network.responce.enums;

import com.mgrmobi.brandbeat.R;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public enum PhotoSize
{
    SIZE_SMALL(R.string.size_small), SIZE_MIDDLE(R.string.size_middle), SIZE_BIG(R.string.size_large);
    public int value;
    private PhotoSize(int value)    {
        this.value = value;
    }
}
