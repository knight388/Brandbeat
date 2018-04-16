package com.mgrmobi.brandbeat.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.interactors.base.UseCase;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerAddBrend;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.AddBrandFragment;
import com.mgrmobi.brandbeat.ui.widget.RevealLinearLayout;
import com.mgrmobi.brandbeat.ui.widget.brand_view.utils.CommonUtils;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;
import com.twitter.sdk.android.core.models.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class AddBrandActivity extends BaseActivity implements ContainerAddBrend {
    private Fragment addBrandFragment;
    private CustomProgressDialog progressDialog;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private Bitmap bitmap;
    private RevealLinearLayout rootView;

    private static final long REVEAL_ANIMATION_DURATION_MILLIS = 500;
    private static final String EXTRA_X = "extra_x";
    private static final String EXTRA_Y = "extra_y";
    private boolean revealInProgress;

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments_with_toolbar;
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.submit_product));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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

    public static Intent createIntent(Context context, int x, int y) {
        Intent intent = new Intent(context, AddBrandActivity.class);
        intent.putExtra(EXTRA_X, x);
        intent.putExtra(EXTRA_Y, y);
        return intent;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        showBrand();
        initToolbar();
    }

    public void showBrand() {
        addBrandFragment = new AddBrandFragment();
        showFragment(addBrandFragment);
    }

    @Override
    public void getCategory(ArrayList<ResponseCategories> responseCategories) {
        if (addBrandFragment instanceof AddBrandFragment) {
            ((AddBrandFragment) addBrandFragment).setCategory(responseCategories);
        }
    }

    @Override
    public void getSubCategory(ArrayList<ResponseCategories> responseCategories) {
        if (addBrandFragment instanceof AddBrandFragment) {
            ((AddBrandFragment) addBrandFragment).setSubCategory(responseCategories);
        }
    }

    @Override
    public void getPhoto(final ResponsePhotoUrl responsePhotoUrl) {
        if (addBrandFragment instanceof AddBrandFragment) {
            ((AddBrandFragment) addBrandFragment).setResponseImage(responsePhotoUrl);
        }
    }

    @Override
    public void createBrand() {
        UserDataUtils userDataUtils = new UserDataUtils(this);
        Utils.showAlertDialog(this, (dialog, which) -> {
            dialog.dismiss();
            finish();
            userDataUtils.clearBrand();
        }, (dialog1, which) -> {
            dialog1.dismiss();
            finish();
            userDataUtils.clearBrand();
        }, getString(R.string.success), getString(R.string.brand_send), true, false);
    }

    @Override
    public void hideProgress() {

        if (progressDialog == null) {
            return;
        }

        Log.e("asdasdf", "dismiss");
        progressDialog.dismissAllowingStateLoss();
    }

    @Override
    public void showProgress() {
        Log.e("asdasdf", "show");
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(this);
            progressDialog.show(getSupportFragmentManager(), this.getClass().getName());
        }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        if (data == null) return;
        if (data.getData() != null || data.getExtras().get("data") instanceof Bitmap)
            try {
                bitmap = Utils.loadImageFromResurce(data, this);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (bitmap != null) {
            if (addBrandFragment instanceof AddBrandFragment) {
                ((AddBrandFragment) addBrandFragment).setImage(bitmap);
            }
        }
    }

    private void exit() {
        UserDataUtils userDataUtils = new UserDataUtils(this);
        if (addBrandFragment instanceof AddBrandFragment && ((AddBrandFragment) addBrandFragment).isChange())
            Utils.showAlertDialog(this, (dialog, which) -> {
                finish();
                userDataUtils.clearBrand();
            }, (dialog1, which1) -> {
                dialog1.dismiss();
            }, getString(R.string.warning), getString(R.string.save_message), true, true);
        else {
            finish();
            userDataUtils.clearBrand();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BITMAP_STORAGE_KEY, bitmap);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        showBrand();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}