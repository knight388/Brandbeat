package com.mgrmobi.brandbeat.ui.fragment.best_ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.app.Fragment;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.Postprocessor;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseAchievement;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.presenter.PresenterProfileView;
import com.mgrmobi.brandbeat.ui.adapters.ProfileReviewsAdapter;
import com.mgrmobi.brandbeat.ui.adapters.ReviewInBrandRecyclerAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerProfile;
import com.mgrmobi.brandbeat.ui.callbacks.FollowCallBack;
import com.mgrmobi.brandbeat.ui.widget.brand_view.AnimatorHelper;
import com.mgrmobi.brandbeat.ui.widget.brand_view.EventDetailsBottomSheetBehavior;
import com.mgrmobi.brandbeat.ui.widget.brand_view.FrescoControllerListener;
import com.mgrmobi.brandbeat.ui.widget.brand_view.ScalableDraweeView;
import com.mgrmobi.brandbeat.ui.widget.brand_view.ScalableImageView;
import com.mgrmobi.brandbeat.ui.widget.brand_view.WidgetCoverSwapper;
import com.mgrmobi.brandbeat.ui.widget.brand_view.utils.ColorUtils;
import com.mgrmobi.brandbeat.ui.widget.brand_view.utils.WidgetUtils;
import com.mgrmobi.brandbeat.utils.UserDataUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NewProfileFragment extends android.support.v4.app.Fragment {
    @Bind(R.id.recycle)
    public RecyclerView recyclerView;
    @Bind(R.id.id_widget_event_cover_swapper)
    protected WidgetCoverSwapper widgetEventCoverSwapper;
    @Bind(R.id.bottom_sheet)
    protected LinearLayout bottomSheet;
    @Bind(R.id.blurred_image)
    protected ScalableImageView draweeBlurredEventImage;
    private Bitmap blurredBitmap;

    private static final float MIN_HEADER_SCALE = 0.2f;
    private static final TimeInterpolator INTERPOLATOR_FAST_OUT_SLOW_IN = new FastOutSlowInInterpolator();

    private static final long DURATION_DEFAULT = 350;
    private static final long DURATION_OPEN_TRANSLATION = DURATION_DEFAULT;
    private static final long DURATION_COMMON_ALPHA = DURATION_DEFAULT;
    private static final long APPEARING_START_DELAY = DURATION_DEFAULT;
    private int colorPrimary;
    private boolean shouldUseImageScaleAndBlur;
    private EventDetailsBottomSheetBehavior bottomSheetBehavior;
    private boolean shouldDispatchTouches;
    private float middleOffsetPercent;

    @FloatRange(from = 0f, to = 1f)
    private static final float MAX_AVATAR_OVERSCALE = 0.4f;


    private PresenterProfileView presenter = new PresenterProfileView();
    private ContainerProfile containerProfile;
    private ProfileReviewsAdapter profileReviewsAdapter;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean last = true;
    private LinearLayoutManager verticalManager;
    private ResponseProfile profile;
    private List<ResponseReview> reviews;
    private ArrayList<ResponseAchievement> achievements;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View rootView = inflater.inflate(R.layout.new_profile_fragment, container, false);
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(R.id.action_context).setVisible(false);
    }

    private boolean useImageScalingAndBlurring() {
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        getActivity().getWindow().setWindowAnimations(R.style.NoAnimation);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this, view);
        widgetEventCoverSwapper.setUseSaturationWhenSwapping(false);
        shouldUseImageScaleAndBlur = useImageScalingAndBlurring();
        colorPrimary = ColorUtils.colorPrimary(getActivity());

        if (getActivity() instanceof ContainerProfile) {
            containerProfile = (ContainerProfile) getActivity();
            presenter.setView(containerProfile);
        }
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        verticalManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(false);
        if (profileReviewsAdapter == null)
            profileReviewsAdapter = new ProfileReviewsAdapter(getActivity(), new ArrayList<>());
        recyclerView.setAdapter(profileReviewsAdapter);
        if (profile != null)
            profileReviewsAdapter.setProfile(profile);
        if (reviews != null)
            profileReviewsAdapter.setReviews((ArrayList<ResponseReview>) reviews);
        showBrand();
    }

    private void showBrand() {
        initRequiredBehaviorParams();
        profileReviewsAdapter = new ProfileReviewsAdapter(getActivity(), new ArrayList<>());
        recyclerView.setAdapter(profileReviewsAdapter);
        appearBottomSheet();
    }

    private void initRequiredBehaviorParams() {
        bottomSheet.post(() -> {
            bottomSheetBehavior = (EventDetailsBottomSheetBehavior) EventDetailsBottomSheetBehavior.from(bottomSheet);
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            bottomSheetBehavior.setPeekHeight((int) (displayMetrics.heightPixels - 300 * displayMetrics.density));
            bottomSheetBehavior.setBottomSheetCallback(new EventDetailsBottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, @EventDetailsBottomSheetBehavior.State int newState) {
                    shouldUseImageScaleAndBlur = useImageScalingAndBlurring();
                    if (newState == EventDetailsBottomSheetBehavior.STATE_SETTLING) {
                        showTranslationIfRequired();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    onOffsetChanged(bottomSheet, slideOffset);
                }
            });
            middleOffsetPercent = (float) bottomSheetBehavior.getMiddleOffset() / bottomSheetBehavior.getMaxOffset();
        });
    }

    public void onOffsetChanged(@SuppressWarnings("UnusedParameters") View bottomSheet, float offset) {
        float tmpMiddleOffsetPercent = 1 - middleOffsetPercent;
        ScalableImageView tmpScalableBlurringImageView = draweeBlurredEventImage;
        ScalableDraweeView tmpScalableEventImage = widgetEventCoverSwapper.getBottomView();
        if (offset >= tmpMiddleOffsetPercent) {
            if (shouldUseImageScaleAndBlur) {
                if (tmpScalableEventImage != null) {
                    tmpScalableEventImage.setCurrentScale(1);
                }
                tmpScalableBlurringImageView.setAlpha(0f);
            }
        }
        else {
            float percent = offset / tmpMiddleOffsetPercent;
            if (shouldUseImageScaleAndBlur) {
                tmpScalableBlurringImageView.setAlpha(1 - percent);

                float percentImageScale = 1 + (1 - percent) * MAX_AVATAR_OVERSCALE;
                if (tmpScalableEventImage != null) {
                    tmpScalableEventImage.setCurrentScale(percentImageScale);
                }
                tmpScalableBlurringImageView.setCurrentScale(percentImageScale);
            }
        }
    }

    private boolean shouldOpenTranslation() {
        return false;
    }

    private void showTranslationIfRequired() {
        if (shouldOpenTranslation()) {
            openTranslation();
        }
    }

    private void openTranslation() {
        shouldDispatchTouches = false;

        bottomSheetBehavior.setState(EventDetailsBottomSheetBehavior.STATE_COLLAPSED);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(DURATION_OPEN_TRANSLATION);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (bottomSheet != null) {
                    bottomSheet.setVisibility(View.GONE);
                    bottomSheet.setTranslationY(0);
                }
                WidgetUtils.gone(widgetEventCoverSwapper);
                shouldDispatchTouches = true;
            }
        });
        set.start();
    }

    private void appearBottomSheet() {
        bottomSheet.setTranslationY(getResources().getDisplayMetrics().heightPixels - bottomSheet.getMeasuredHeight());
        bottomSheet.animate()
                .setStartDelay(APPEARING_START_DELAY)
                .alpha(1)
                .translationY(0)
                .setDuration(DURATION_COMMON_ALPHA)
                .setInterpolator(INTERPOLATOR_FAST_OUT_SLOW_IN)
                .withLayer()
                .withEndAction(() -> {
                    shouldDispatchTouches = true;
                });
    }

    public void setProfile(ResponseProfile profile) {
        if (profile == null) return;
        if (achievements != null) {
            profile.setAchievements(achievements);
        }
        loadCoverWithAlphaTransition(profile.getPhoto(PhotoSize.SIZE_BIG, getActivity()), true);
        this.profile = profile;
        if (profile.getPhoto(PhotoSize.SIZE_SMALL, getContext()) != null &&
                profile.getPhoto(PhotoSize.SIZE_SMALL, getContext()).equals(""))
            profile.setPhoto(null);
        profileReviewsAdapter.setProfile(profile);
        profileReviewsAdapter.notifyDataSetChanged();
    }

    public void setPhoto(String photoUrl) {
        profile = profileReviewsAdapter.getResponseProfile();
        profile.setPhoto(photoUrl);
        setProfile(profile);
        initRequiredBehaviorParams();
        appearBottomSheet();
    }

    public void setAchvimients(List<ResponseAchievement> achvimients) {
        this.achievements = (ArrayList<ResponseAchievement>) achvimients;
        if (this.achievements == null)
            Log.e("daspifhjdasof", "null get achivmients ");
        if (profile == null) {
            return;
        }
        profile.setAchievements((ArrayList<ResponseAchievement>) achvimients);
        if (profile.getPhoto(PhotoSize.SIZE_SMALL, getContext()) != null && profile.getPhoto(PhotoSize.SIZE_SMALL, getContext()).equals(""))
            profile.setPhoto(null);
        profileReviewsAdapter.setProfile(profile);
        profileReviewsAdapter.notifyDataSetChanged();
    }

    public void setReviews(ArrayList<ResponseReview> reviews) {
        this.reviews = reviews;
        profileReviewsAdapter.dismissViewHolder();
        profileReviewsAdapter.setReviews(reviews);
        profileReviewsAdapter.notifyDataSetChanged();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && last) {
                    visibleItemCount = verticalManager.getChildCount();
                    totalItemCount = verticalManager.getItemCount();
                    pastVisiblesItems = verticalManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        last = presenter.getNextUserReviews();
                    }
                }
            }
        });
    }

    public void setBitmap(Bitmap bitmap) {
        if (profileReviewsAdapter == null)
            profileReviewsAdapter = new ProfileReviewsAdapter(getContext(), new ArrayList<>());
        profileReviewsAdapter.notifyDataSetChanged();
        presenter.uploadPhoto(bitmap);
    }

    @Override
    public void onResume() {
        super.onResume();
        UserDataUtils userDataUtils = new UserDataUtils(getActivity());
        presenter.getUser(userDataUtils.getUserId());
        containerProfile.showProgress();
        profileReviewsAdapter.setReviews(new ArrayList<>());
        presenter.getReview();
        presenter.getAchivmient();
    }

    public void loadNextPage() {
        profileReviewsAdapter.showViewNextLoad();
    }

    public void setReviews(final List<ResponseReview> reviews) {
        this.reviews = reviews;
    }

    public void setProfileResponse(ResponseProfile profileResponse) {
        profile = profileResponse;
    }

    public ResponseProfile getProfile() {
        return profile;
    }

    public List<ResponseReview> getReviews() {
        return reviews;
    }

    private void loadCoverWithAlphaTransition(@NonNull String cover, boolean withAlpha) {
        if (cover == null) {
            cover = "";
            widgetEventCoverSwapper.getBottomView().setImageResource(R.drawable.profile_placeholder);
            return;
        }
        widgetEventCoverSwapper.showImageJustOnce(Uri.parse(cover), Uri.parse(cover), createCoverPostprocessor(),
                new FrescoControllerListener() {
                    @Override
                    public void onFinalImageSet(String id, com.facebook.imagepipeline.image.ImageInfo imageInfo, Animatable animatable) {
                        setBlurredBitmap();
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                    }
                }, withAlpha);
    }

    private void setBlurredBitmap() {
        if (blurredBitmap != null) {
            draweeBlurredEventImage.setImageBitmap(blurredBitmap);
            draweeBlurredEventImage.setAlpha(0f);
            draweeBlurredEventImage.setVisibility(View.VISIBLE);
        }
    }

    private Postprocessor createCoverPostprocessor() {
        return new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                if (shouldUseImageScaleAndBlur) {
                    blurredBitmap = WidgetUtils.blurBitmap(getActivity().getApplicationContext(), bitmap, 2, 25);
                }
                else {
                    blurredBitmap = null;
                }
            }
        };
    }


}
