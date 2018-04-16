package com.mgrmobi.brandbeat.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class RatingBrandView extends RelativeLayout {
    private RatingBar ratingBar;
    private TextView rating;
    private TextView rated;
    private TextView reviews;

    public RatingBrandView(final Context context) {
        super(context);
        init();
    }

    public RatingBrandView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatingBrandView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatingBrandView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.rating_brand_view, this);
        ratingBar = (RatingBar) findViewById(R.id.beat_rating);
        rating = (TextView) findViewById(R.id.rating_text);
        rated = (TextView) findViewById(R.id.rate_count);
        reviews = (TextView) findViewById(R.id.reviews_count);
    }

    public void setRating(double rating) {
        ratingBar.setRating((float) rating / 5);
        this.rating.setText(rating + "");
    }

    public void setReviewSumm(String reviewSumm) {
        reviews.setText(reviewSumm + " " + getContext().getString(R.string.review));
    }

    public void setRatedSum(String rated) {
      //  this.rated.setText(rated + " " + getContext().getString(R.string.rated));
    }
}