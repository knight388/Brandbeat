package com.mgrmobi.brandbeat.ui.view_holder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.adapters.PhotoBrandsFeedAdapter;
import com.mgrmobi.brandbeat.ui.callbacks.ClickImageRecyclerPhoto;
import com.mgrmobi.brandbeat.ui.callbacks.LikeDeslikeCallBack;
import com.mgrmobi.brandbeat.ui.widget.RatingWidget;
import com.mgrmobi.brandbeat.ui.widget.hashTagTextView.HashTagTextView;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class MyFeedViewHolder {
    @Bind(R.id.logo_brand)
    public SimpleDraweeView logoBrand;
    @Bind(R.id.name_brand)
    public TextView nameBrand;
//    @Bind(R.id.raiting)
  //  public TextView ratingText;

    @Bind(R.id.time_ago)
    public TextView timeText;

    @Bind(R.id.about)
    public HashTagTextView about;

    @Bind(R.id.rating_widjet)
    public RatingWidget ratingWidget;

    @Bind(R.id.count_like)
    public TextView countView;
    @Bind(R.id.like_icon)
    public ImageView likeView;
    @Bind(R.id.count_comment)
    public TextView countCommentView;
    @Bind(R.id.like_click)
    public View likeButton;
    @Bind(R.id.photo_recycler)
    public RecyclerView recyclerView;

    private ResponseReview responseReview;
    private Context context;
    private LikeDeslikeCallBack likeDeslikeCallBack;

    public MyFeedViewHolder(View view, ResponseReview responseReview, Context context, LikeDeslikeCallBack callBack) {
        ButterKnife.bind(this, view);
        this.responseReview = responseReview;
        this.context = context;
        initView();
        likeDeslikeCallBack = callBack;
    }

    private void initView() {

        if (responseReview.getUser() != null && responseReview.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, context) != null &&
                !responseReview.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, context).equals("")) {
            try {
                PipelineDraweeController controller = (PipelineDraweeController)
                        Fresco.newDraweeControllerBuilder()
                                .setImageRequest(Utils.createImageRequest(40, 40,
                                        Uri.parse(responseReview.getUser().getPhoto(PhotoSize.SIZE_MIDDLE, context))))
                                .setOldController((logoBrand).getController())
                                .build();
                (logoBrand).setController(controller);
            } catch (Exception e) {
            }
        }

        PhotoBrandsFeedAdapter photoReviewAdapter = new PhotoBrandsFeedAdapter(context, responseReview.getPicturies(PhotoSize.SIZE_MIDDLE, context), new ClickImageRecyclerPhoto() {
            @Override
            public void click(final int position) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(photoReviewAdapter);
        photoReviewAdapter.notifyDataSetChanged();

        String name = "";
        if (responseReview.getUser().getFirstName() != null && !responseReview.getUser().getFirstName().equals("")) {
            name = responseReview.getUser().getFirstName() + " ";
        }
        if (responseReview.getUser().getLastName() != null && !responseReview.getUser().getLastName().equals("")) {
            name += responseReview.getUser().getLastName();
        }
        if (name.equals("")) {
            if (responseReview.getUser() != null && responseReview.getUser().getUsername() != null)
                nameBrand.setText(responseReview.getUser().getUsername());
        }
        else
            nameBrand.setText(name);
        if (responseReview.getCreateAt() != null && !responseReview.getCreateAt().equals("null"))
            timeText.setText(Utils.getTimeAgo(Long.parseLong(responseReview.getCreateAt())));
        about.setText(responseReview.getText());


        //ratingBar.setVisibility(View.VISIBLE);
        //ratingBar.setRating(Float.parseFloat(responseReview.getRate()) / Utils.RATING_BAR_DELETE_VALUE);

        ratingWidget.setTextRating(Float.parseFloat(responseReview.getRate()));
        //ratingText.setText(responseReview.getRate());
        if (responseReview.getLikes() != null && responseReview.getLikes().size() != 0) {
            countView.setText(responseReview.getLikes().size() + "");
        }
        if (responseReview.getComments() != null && responseReview.getComments().size() != 0) {
            countCommentView.setText(responseReview.getComments().size() + "");
        }
        if (checkLike(responseReview)) {
            likeView.setImageResource(R.drawable.ic_like_no_active);
        }
        else {
            likeView.setImageResource(R.drawable.ic_like_active);
        }
        likeButton.setOnClickListener(v -> {
            if (checkLike(responseReview)) {
                likeDeslikeCallBack.likeReview(responseReview.getId());
                UserDataUtils userDataUtils = new UserDataUtils(context);
                String id = userDataUtils.getUserData(UserDataUtils.KEY_USER_ID);
                responseReview.addLike(id);
                countView.setText(responseReview.getLikes().size() + "");
                likeView.setImageResource(R.drawable.ic_like_active);
            }
            else {
                UserDataUtils userDataUtils = new UserDataUtils(context);
                String id = userDataUtils.getUserData(UserDataUtils.KEY_USER_ID);
                responseReview.deslike(id);
                countView.setText(responseReview.getLikes().size() + "");
                likeDeslikeCallBack.dislikeReview(responseReview.getId());
                likeView.setImageResource(R.drawable.ic_like_no_active);
            }
        });
    }

    private boolean checkLike(ResponseReview responseLocalFeed) {
        UserDataUtils userDataUtils = new UserDataUtils(context);
        String id = userDataUtils.getUserData(UserDataUtils.KEY_USER_ID);
        for(int i = 0; i < responseLocalFeed.getLikes().size(); i++) {
            if (id.equals(responseLocalFeed.getLikes().get(i))) {
                return false;
            }
        }
        return true;
    }
}
