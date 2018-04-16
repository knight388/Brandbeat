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
public class RatingWidget extends LinearLayout {

    private TextView rating;
    private RatingBar ratingBar;

    public RatingWidget(final Context context) {
        super(context);
        init();
    }

    public RatingWidget(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatingWidget(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatingWidget(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        inflate(getContext(), R.layout.rating_for_widjet, this);
        this.rating = (TextView) findViewById(R.id.raiting);
        this.ratingBar = (RatingBar) findViewById(R.id.beat_rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, final float rating, final boolean fromUser) {
                setTextRating(rating * Utils.RATING_BAR_DELETE_VALUE);
            }
        });
    }

    public  void setTextRating(float rating) {

        if (rating == 0) {
            this.rating.setText(0 + "");
            this.rating.setBackgroundDrawable(getResources().getDrawable(R.drawable.rate_text_1));
            ratingBar.setRating(rating / Utils.RATING_BAR_DELETE_VALUE);
        }
        else if (0 <= rating && rating < 1.5) {
            this.rating.setText(1 + "");
            this.rating.setBackgroundDrawable(getResources().getDrawable(R.drawable.rate_text_1));
            ratingBar.setRating(rating / Utils.RATING_BAR_DELETE_VALUE);
        }
        else if (1.5 <= rating && rating < 2.5) {
            this.rating.setText(2 + "");
            this.rating.setBackgroundDrawable(getResources().getDrawable(R.drawable.rate_text_2));
            ratingBar.setRating(rating / Utils.RATING_BAR_DELETE_VALUE);
        }
        else if (2.5 <= rating && rating < 3.5) {
            this.rating.setText(3 + "");
            this.rating.setBackgroundDrawable(getResources().getDrawable(R.drawable.rate_text_3));
            ratingBar.setRating(rating / Utils.RATING_BAR_DELETE_VALUE);
        }
        else if (3.5 <= rating && rating < 4.5) {
            this.rating.setText(4 + "");
            this.rating.setBackgroundDrawable(getResources().getDrawable(R.drawable.rate_text_4));
            ratingBar.setRating(rating / Utils.RATING_BAR_DELETE_VALUE);
        }
        else if (4.5 <= rating && rating <= 5) {
            this.rating.setText(5 + "");
            this.rating.setBackgroundDrawable(getResources().getDrawable(R.drawable.rate_text_5));
            ratingBar.setRating(rating / Utils.RATING_BAR_DELETE_VALUE);
        }

    }

    public int getProgress()
    {
        return (int) (ratingBar.getRating()* Utils.RATING_BAR_DELETE_VALUE);
    }
}