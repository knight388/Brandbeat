package com.mgrmobi.brandbeat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseComment;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.presenter.PresenterReview;
import com.mgrmobi.brandbeat.ui.activity.AddCommentActivity;
import com.mgrmobi.brandbeat.ui.activity.ReviewActivity;
import com.mgrmobi.brandbeat.ui.activity.WriteReviewActivity;
import com.mgrmobi.brandbeat.ui.adapters.CommentReviewAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerReview;
import com.mgrmobi.brandbeat.ui.callbacks.CommentAdapterCallback;
import com.mgrmobi.brandbeat.ui.dialog.ShareDialog;
import com.mgrmobi.brandbeat.ui.widget.slidinPanel.SlidingUpPanelLayout;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ReviewFragment extends Fragment {
    private ContainerReview containerReview;
    private PresenterReview presenterReview = new PresenterReview();
    private CommentReviewAdapter commentReviewAdapter;
    private RecyclerView listView;
    private LinearLayout menuRootLayout;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private View share;
    private View edit;
    private View delete;
    private String id;
    private int deletePosition = -1;
    private ResponseReview responseReview;
    private ArrayList<ResponseComment> responseComments = new ArrayList<>();
    private String subCategoryId;
    private CommentAdapterCallback commentAdapterCallback = new CommentAdapterCallback() {
        @Override
        public void deleteComment(final String idComment, int position) {

            UserDataUtils userDataUtils = new UserDataUtils(getActivity());

            share.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            edit.setOnClickListener(v -> {
                startActivity(AddCommentActivity.createEditIntent(getActivity(), responseComments.get(position - 1)));
                initSlidePanel();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            });
            delete.setOnClickListener(v -> {
                Utils.showAlertDialog(getActivity(), (dialog, which) -> {
                            presenterReview.deleteComment(idComment, position);
                            deletePosition = -1;
                        },
                        (dialog, which) -> dialog.dismiss(), getString(R.string.are_you_sure), "", true, true);
                initSlidePanel();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            });

            if (responseComments.get(position - 1).getResponseUser().getId().equals(userDataUtils.getUserId())) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) menuRootLayout.getLayoutParams();
                layoutParams.height = (int) Utils.convertDpToPixel(172, getActivity());

                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
            else {
                if (responseReview.getUser().getId().equals(userDataUtils.getUserId())) {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) menuRootLayout.getLayoutParams();
                    layoutParams.height = (int) Utils.convertDpToPixel(86, getActivity());
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    edit.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void editComment(final ResponseComment responseComment) {

        }

        @Override
        public void likeReview() {
            presenterReview.addLike(responseReview.getId());
        }

        @Override
        public void deleteLike() {
            presenterReview.deleteLike(responseReview.getId());
        }
    };

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.review_fragment, container, false);
        return rootView;
    }

    public void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.review));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    ShareDialog shareDialog;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerReview) {
            containerReview = (ContainerReview) getActivity();
            presenterReview.setView(containerReview);
        }
        listView = (RecyclerView) view.findViewById(R.id.list_view);
        slidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        shareDialog = new ShareDialog(getActivity());
        ButterKnife.bind(this, view);
        share = view.findViewById(R.id.share);
        edit = view.findViewById(R.id.edit);
        delete = view.findViewById(R.id.delete);
        menuRootLayout = (LinearLayout) view.findViewById(R.id.menu_root);
        id = getActivity().getIntent().getStringExtra(ReviewActivity.REVIEW_ID);
        subCategoryId = getActivity().getIntent().getStringExtra(ReviewActivity.SUBCATEGORY_ID);
        initToolbar(view);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(final View panel, final float slideOffset) {
                return;
            }

            @Override
            public void onPanelCollapsed(final View panel) {
                initSlidePanel();
            }

            @Override
            public void onPanelExpanded(final View panel) {
                return;
            }

            @Override
            public void onPanelAnchored(final View panel) {
                return;
            }

            @Override
            public void onPanelHidden(final View panel) {
                return;
            }
        });
    }

    public void initComment(ArrayList<ResponseComment> responseComments) {
        this.responseComments = responseComments;
        if (responseReview != null) {
            initReview(responseReview);
        }
        if (responseComments.size() == 0) {
            //noComment.setText(getResources().getString(R.string.no_comment));
        }
    }

    public void initReview(ResponseReview requestReview) {
        responseReview = requestReview;
        requestReview.getResponseBrand().setSubCategoryId(subCategoryId);
        commentReviewAdapter = new CommentReviewAdapter(getActivity(), new ArrayList<ResponseComment>(), commentAdapterCallback);
        listView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        listView.setItemAnimator(itemAnimator);
        listView.setAdapter(commentReviewAdapter);
        commentReviewAdapter.setResponseComments(responseComments);
        commentReviewAdapter.notifyDataSetChanged();

        commentReviewAdapter.setReview(requestReview);

        initSlidePanel();
    }

    public void openSlidingPanel() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) menuRootLayout.getLayoutParams();
        layoutParams.height = (int) Utils.convertDpToPixel(172, getActivity());
        menuRootLayout.setLayoutParams(layoutParams);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterReview.getReview(id);
        presenterReview.getReviewComments(id);
    }

    public void deleteComment(int deletePosition) {
        responseComments.remove(deletePosition - 1);
        commentReviewAdapter.setResponseComments(responseComments);
        commentReviewAdapter.notifyItemRemoved(deletePosition - 1);
        commentReviewAdapter.notifyDataSetChanged();
    }


    public void initSlidePanel() {
        share.setVisibility(View.VISIBLE);
        share.setOnClickListener(v -> {


            shareDialog.initDialog((AppCompatActivity) getActivity(),responseReview.getUser().getUsername() + " " +
                    getResources().getString(R.string.reviewed_share) + " "
                    + responseReview.getResponseBrand().getTitle() + " " + getResources().getString(R.string.via_brand_beat) + " " +
                    responseReview.getText(), responseReview.getResponseBrand().getImage(PhotoSize.SIZE_BIG , getContext()), "");
            shareDialog.show();
        });
        edit.setOnClickListener(v -> {
            startActivity(WriteReviewActivity.createIntentForEdit(responseReview, getActivity()));
        });
        delete.setOnClickListener(v -> {
            Utils.showAlertDialog(getActivity(), (dialog, which) ->
                            presenterReview.deleteReview(responseReview.getId()),
                    (dialog, which) -> dialog.dismiss(), getString(R.string.are_you_sure), "", true, true);
        });
        UserDataUtils userDataUtils = new UserDataUtils(getActivity());
        if (!userDataUtils.getUserId().equals(responseReview.getUser().getId())) {
            edit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) menuRootLayout.getLayoutParams();
            layoutParams.height = (int) Utils.convertDpToPixel(86, getActivity());
        }
        else {
            edit.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        }
    }

    public void likeAction() {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(shareDialog.isShowing())
        {
            shareDialog.onResult(requestCode, resultCode, data);
        }
    }

}
