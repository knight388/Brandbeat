package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerWriteReview;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.WriteReviewFragment;
import com.mgrmobi.brandbeat.utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class WriteReviewActivity extends BaseActivity implements ContainerWriteReview {
    public static final String BRANDID = "brand_id";
    public static final String REVIEW = "review";
    private Bitmap bitmap;
    private Fragment writeReviewFragment;
    private CustomProgressDialog progressDialog;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments;
    }

    public static Intent createIntent(String brandId, Context callingContext) {
        Intent intent = new Intent(callingContext, WriteReviewActivity.class);
        intent.putExtra(BRANDID, brandId);
        return intent;
    }

    public static Intent createIntentForEdit(ResponseReview responseReview, Context context) {
        Intent intent = new Intent(context, WriteReviewActivity.class);
        intent.putExtra(REVIEW, responseReview);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showWriteReview();
        setFinishOnTouchOutside(false);
    }

    private void showWriteReview() {
        writeReviewFragment = new WriteReviewFragment();
        showFragment(writeReviewFragment);
    }

    @Override
    public void showProgress() {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show(getSupportFragmentManager(), this.getClass().getName());
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BITMAP_STORAGE_KEY, bitmap);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        showWriteReview();
    }

    @Override
    public void hideProgress() {
        if (progressDialog == null) {
            return;
        }
        else if (progressDialog.isVisible())
            progressDialog.dismiss();
    }

    @Override
    public void success() {
        this.finish();
    }

    @Override
    public void success(final ArrayList<ResponsePhotoUrl> responsePhotoUrls) {
        if (writeReviewFragment instanceof WriteReviewFragment) {
            ((WriteReviewFragment) writeReviewFragment).uploadReview(responsePhotoUrls);
        }
    }

    @Override
    public void showError(final String s) {
        Utils.showAlertDialog(this, (dialog, which) -> {
            dialog.dismiss();
        }, null, getString(R.string.error), s, true, false);
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (writeReviewFragment instanceof WriteReviewFragment) {
                    ((WriteReviewFragment) writeReviewFragment).onCancelClick();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (writeReviewFragment instanceof WriteReviewFragment) {
            ((WriteReviewFragment) writeReviewFragment).onCancelClick();
        }
        else finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmap = null;
        if (data == null) return;
        if(data.getExtras() == null && data.getData() == null) return;
        if (data.getData() != null || data.getExtras().get("data") instanceof Bitmap)
            try {
                bitmap = Utils.loadImageFromResurce(data, this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (bitmap != null) {
            if (writeReviewFragment instanceof WriteReviewFragment) {
                ((WriteReviewFragment) writeReviewFragment).setImage(bitmap);
            }
        }
    }

}
