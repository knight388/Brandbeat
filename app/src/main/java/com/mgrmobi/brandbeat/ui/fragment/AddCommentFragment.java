package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseComment;
import com.mgrmobi.brandbeat.presenter.PresenterAddComment;
import com.mgrmobi.brandbeat.ui.activity.AddCommentActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerAddComment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class AddCommentFragment extends Fragment {
    private ContainerAddComment containerAddComment;
    private PresenterAddComment presenterAddComment = new PresenterAddComment();
    private String reviewId;
    private ResponseComment responseComment;
    @Bind(R.id.input_comment)
    public EditText commentText;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(getActivity() instanceof ContainerAddComment) {
            containerAddComment = (ContainerAddComment) getActivity();
            presenterAddComment.setView(containerAddComment);
        }
        reviewId = getActivity().getIntent().getStringExtra(AddCommentActivity.REVIEW_ID);
        ButterKnife.bind(this, view);
        if(reviewId == null || reviewId.equals("null")) {
            responseComment = (ResponseComment) getActivity().getIntent().getSerializableExtra(AddCommentActivity.COMMENT);
            commentText.setText(responseComment.getText());
        }

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.comment_fragment, container, false);
        return rootView;
    }

    @OnClick(R.id.submit_button)
    public void onClick() {
        if(commentText.getText().toString().length() == 0) {
            containerAddComment.showError(new Throwable(getString(R.string.null_comment_message)));
            return;
        }
        else {
            if(responseComment != null) {
                presenterAddComment.editComment(responseComment.getId(), commentText.getText().toString());
            }
            else {
                presenterAddComment.addComment(reviewId, commentText.getText().toString());
            }
        }

    }

    @OnClick(R.id.cancel_button)
    public void onCancelClick() {
        getActivity().finish();
    }
}
