package com.mgrmobi.brandbeat.entity;

import com.mgrmobi.brandbeat.R;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public enum TypeNatifications {

        FOLLOW(R.string.is_following_you, 1), LIKE_REVIEW(R.string.likes_your_review, 2),
    COMMENT_REVIEW(R.string.comment_your_review, 3), LIKE_COMMENT(R.string.like_your_comment, 4),
    REPLIED(R.string.replied_your_comment, 5), ACHIEVEMENT(R.string.your_have_new_achievement, 6),
    ANNOUNCEMENTS(R.string.message_type_7, 7), ADVERTSING(R.string.message_type_8, 8),
    ACCEPTED_BRAND(R.string.yout_brand_accepted, 9), REJECTED_BRAND(R.string.you_brand_rejected, 10);

    private TypeNatifications(int resourceText, int numberType) {
        textResource = resourceText;
        this.numberType = numberType;
    }

    public final int textResource;
    public final int numberType;

    public static TypeNatifications valueByType(int type)
    {   switch (type) {
            case 1:
                return TypeNatifications.FOLLOW;
            case 2:
                return TypeNatifications.LIKE_REVIEW;
            case 3:
                return TypeNatifications.COMMENT_REVIEW;
            case 4:
                return TypeNatifications.LIKE_COMMENT;
            case 5:
                return TypeNatifications.REPLIED;
            case 6:
                return TypeNatifications.ACHIEVEMENT;
            case 7:
                return TypeNatifications.ANNOUNCEMENTS;
            case 8:
                return TypeNatifications.ADVERTSING;
            case 9:
                return TypeNatifications.ACCEPTED_BRAND;
            case 10:
                return TypeNatifications.REJECTED_BRAND;
        }
        return null;
    }
}
