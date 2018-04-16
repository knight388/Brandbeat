package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.network.responce.ResponseComment;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerReview;
import com.mgrmobi.brandbeat.ui.fragment.ReviewFragment;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ReviewActivity extends BaseActivity implements ContainerReview, NavigationView.OnNavigationItemSelectedListener
{
    public static final String REVIEW_ID = "review_id";
    public static final String BRAND_ID = "brand_id";
    public static final String SUBCATEGORY_ID = "subcategory_id";
    private Fragment reviewFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.review_activity_layout;
    }
    public static Intent createIntent(String reviewId, String subcatgoryId,Context callingContext) {
        Intent intent = new Intent(callingContext, ReviewActivity.class);
        intent.putExtra(REVIEW_ID, reviewId);
        intent.putExtra(SUBCATEGORY_ID, subcatgoryId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showReview();
    }


    private void showReview()
    {
        reviewFragment = new ReviewFragment();
        showFragment(reviewFragment);
    }

    @Override
    public void getReview(ResponseReview requestReview) {
        if(reviewFragment instanceof ReviewFragment)
        {
            ((ReviewFragment) reviewFragment).initReview(requestReview);
        }
    }

    @Override
    public void deleteReview(final String idReview) {
        finish();
    }

    @Override
    public void getComments(final ArrayList<ResponseComment> responseComments) {
        if(reviewFragment instanceof ReviewFragment)
        {
            ((ReviewFragment) reviewFragment).initComment(responseComments);
        }
    }

    @Override
    public void deleteComment(int i) {
        if(reviewFragment instanceof ReviewFragment)
        {
            ((ReviewFragment) reviewFragment).deleteComment(i);
        }
    }

    @Override
    public void likeAction() {
        if(reviewFragment instanceof ReviewFragment)
        {
            ((ReviewFragment) reviewFragment).likeAction();
        }
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }


    private boolean isShowNetworkError = false;

    @Override
    public void showError(final Throwable e) {
        if (e instanceof NetworkErrorException) {
            if (!isShowNetworkError) {
                isShowNetworkError = true;
                Utils.showAlertDialog(this, (dialog, which) -> dialog.dismiss(), (dialog1, which1) -> dialog1.dismiss(),
                        getString(R.string.error), e.getMessage(), true, false);
            }
        }
        else {
            Utils.showAlertDialog(this, (dialog, which) -> dialog.dismiss(), (dialog1, which1) -> dialog1.dismiss(),
                    getString(R.string.error), e.getMessage(), true, false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.review_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_context) {
            if(reviewFragment instanceof ReviewFragment) {
                ((ReviewFragment) reviewFragment).initSlidePanel();
                ((ReviewFragment) reviewFragment).openSlidingPanel();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        reviewFragment.onActivityResult(requestCode, resultCode, data);
    }
}
