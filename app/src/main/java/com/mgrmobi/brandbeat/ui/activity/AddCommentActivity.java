package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.exeption.NetworkErrorException;
import com.mgrmobi.brandbeat.network.responce.ResponseComment;
import com.mgrmobi.brandbeat.ui.base.BaseActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerAddComment;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.AddCommentFragment;
import com.mgrmobi.brandbeat.utils.Utils;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class AddCommentActivity extends BaseActivity implements ContainerAddComment{
    public static final String REVIEW_ID = "review_id";
    public static final String COMMENT = "comment";
    private Fragment commentFragment;
    private CustomProgressDialog progressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragments;
    }
    public static Intent createIntent(Context callingContext, String reviewID) {
        Intent intent = new Intent(callingContext, AddCommentActivity.class);
        intent.putExtra(REVIEW_ID, reviewID);
        return intent;
    }

    public static Intent createEditIntent(Context context, ResponseComment responseComment)
    {
        Intent intent = new Intent(context, AddCommentActivity.class);
        intent.putExtra(COMMENT, responseComment);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showFragmentAddComment();
    }

    private void showFragmentAddComment()
    {
        commentFragment = new AddCommentFragment();
        showFragment(commentFragment);
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
    public void showProgress() {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.show(getSupportFragmentManager(), this.getClass().getName());
    }

    @Override
    public void hideProgress() {
        if(progressDialog == null)
        {
            return;
        }
        else if(progressDialog.isVisible())
            progressDialog.dismiss();
    }

    @Override
    public void onSuccess() {
        finish();
    }

    @Override
    public void editSuccess() {
        Utils.showAlertDialog(this, (dialog, which) -> {
                    finish();
                }, (dialog1, which1) -> {
                    finish();
                }, getString(R.string.success),
                getString(R.string.edit_comment_success), true, false);
    }
}
