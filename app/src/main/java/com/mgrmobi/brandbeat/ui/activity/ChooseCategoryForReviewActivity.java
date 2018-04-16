package com.mgrmobi.brandbeat.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerInterests;
import com.mgrmobi.brandbeat.ui.fragment.ChooseCategoryForReviewFragment;
import com.mgrmobi.brandbeat.ui.widget.RevealLinearLayout;
import com.mgrmobi.brandbeat.ui.widget.brand_view.utils.CommonUtils;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ChooseCategoryForReviewActivity extends BaseActivity implements ContainerInterests {
    private Fragment fragment;
    private static final long REVEAL_ANIMATION_DURATION_MILLIS = 500;
    private static final String EXTRA_X = "extra_x";
    private static final String EXTRA_Y = "extra_y";
    private boolean revealInProgress;
    private RevealLinearLayout rootView;


    private void showFragment() {
        fragment = new ChooseCategoryForReviewFragment();
        showFragment(fragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        showFragment();
        initToolbar();
        getWindow().setWindowAnimations(R.style.NoAnimation);
        rootView = (RevealLinearLayout) findViewById(R.id.root_view);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                showContentWithReveal();
            }
        });

    }


    private void showContentWithReveal() {
        if (CommonUtils.isRevealSupported()) {
            showContentWithNativeReveal();
        } else {
            showContentWithCustomReveal();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showContentWithNativeReveal() {
        Intent intent = getIntent();
        int startX = intent.getIntExtra(EXTRA_X, 0);
        int startY = intent.getIntExtra(EXTRA_Y, 0) - CommonUtils.getStatusBarHeight(this);
        float finalRadius = (float) Math.hypot(rootView.getMeasuredWidth(), rootView.getMeasuredHeight());
        Animator animator = ViewAnimationUtils.createCircularReveal(rootView, startX, startY, 0, finalRadius);
        rootView.setVisibility(View.VISIBLE);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                revealInProgress = false;
            }
        });
        animator.setInterpolator(new AccelerateInterpolator(2.0f));
        animator.setDuration(REVEAL_ANIMATION_DURATION_MILLIS);
        animator.start();
        revealInProgress = true;
    }

    private void showContentWithCustomReveal() {
        Intent intent = getIntent();
        int startX = intent.getIntExtra(EXTRA_X, 0);
        int startY = intent.getIntExtra(EXTRA_Y, 0) - CommonUtils.getStatusBarHeight(this);
        float finalRadius = (float) Math.hypot(rootView.getMeasuredWidth(), rootView.getMeasuredHeight());
        rootView.setClipOutLines(true);
        rootView.setClipCenter(startX, startY);
        rootView.setClipRadius(0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(rootView, "clipRadius", 0, finalRadius);
        rootView.setVisibility(View.VISIBLE);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (rootView != null) {
                    rootView.setClipOutLines(false);
                }
                revealInProgress = false;
            }
        });
        animator.setInterpolator(new AccelerateInterpolator(2.0f));
        animator.setDuration(REVEAL_ANIMATION_DURATION_MILLIS);
        animator.start();
        revealInProgress = true;
    }

    @Override
    public void getInterest(final ArrayList<ResponseCategories> interests) {
        if(fragment != null && fragment instanceof ChooseCategoryForReviewFragment) {
            ((ChooseCategoryForReviewFragment) fragment).setIntersest(interests);
        }
    }

    @Override
    public void onComplete() {
        finish();
        startActivity(NavigationActivity.createIntent(this));
    }

    public static Intent createIntent(Context context, int x, int y) {
        Intent intent = new Intent(context, ChooseCategoryForReviewActivity.class);
        intent.putExtra(EXTRA_X, x);
        intent.putExtra(EXTRA_Y, y);
        return intent;
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showProgress() {

    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.interests_title_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if(((ChooseCategoryForReviewFragment) fragment).haveCheckedItems())
                    Utils.showAlertDialog(this, (dialog, which) -> {
                                dialog.dismiss();
                                finish();
                            }, (dialog, which) -> dialog.dismiss(),
                            getString(R.string.title_alert_message_interests),
                            getString(R.string.alert_message_interests), true, true);
                else
                    finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
