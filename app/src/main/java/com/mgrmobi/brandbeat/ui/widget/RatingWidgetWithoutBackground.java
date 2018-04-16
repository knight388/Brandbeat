package com.mgrmobi.brandbeat.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.utils.Utils;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RatingWidgetWithoutBackground extends LinearLayout {

    private TextView rating;
    private RatingBar ratingBar;

    public RatingWidgetWithoutBackground(final Context context) {
        super(context);
        init();
    }

    public RatingWidgetWithoutBackground(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatingWidgetWithoutBackground(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatingWidgetWithoutBackground(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        inflate(getContext(), R.layout.rating_widget_without_background, this);
        this.rating = (TextView) findViewById(R.id.raiting);
        this.ratingBar = (RatingBar) findViewById(R.id.beat_rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, final float rating, final boolean fromUser) {
                setTextRating(rating * Utils.RATING_BAR_DELETE_VALUE);
            }
        });
    }

    public void setTextRating(float rating) {
        this.rating.setText(rating + "");
        ratingBar.setRating(rating/Utils.RATING_BAR_DELETE_VALUE);
    }

    public int getProgress() {
        return (int) (ratingBar.getRating() * Utils.RATING_BAR_DELETE_VALUE);
    }
}