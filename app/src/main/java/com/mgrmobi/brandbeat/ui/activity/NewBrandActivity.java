package com.mgrmobi.brandbeat.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.Postprocessor;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.BaseResponse;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.presenter.FollowPresenter;
import com.mgrmobi.brandbeat.presenter.PresenterBrandView;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.mgrmobi.brandbeat.ui.adapters.ReviewInBrandRecyclerAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerBrand;
import com.mgrmobi.brandbeat.ui.base.ContainerFollowBrand;
import com.mgrmobi.brandbeat.ui.base.ContainerReviewInBrand;
import com.mgrmobi.brandbeat.ui.callbacks.FollowCallBack;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.widget.brand_view.AnimatorHelper;
import com.mgrmobi.brandbeat.ui.widget.brand_view.EventDetailsBottomSheetBehavior;
import com.mgrmobi.brandbeat.ui.widget.brand_view.EventDetailsRecyclerView;
import com.mgrmobi.brandbeat.ui.widget.brand_view.FrescoControllerListener;
import com.mgrmobi.brandbeat.ui.widget.brand_view.ScalableDraweeView;
import com.mgrmobi.brandbeat.ui.widget.brand_view.ScalableImageView;
import com.mgrmobi.brandbeat.ui.widget.brand_view.WidgetCoverSwapper;
import com.mgrmobi.brandbeat.ui.widget.brand_view.utils.ColorUtils;
import com.mgrmobi.brandbeat.ui.widget.brand_view.utils.WidgetUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NewBrandActivity extends AppCompatActivity implements ContainerReviewInBrand, ContainerBrand, ContainerFollowBrand {
    public static final String BRAND = "brand";

    public static final String EXTRA_CURRENT_COVER_IMAGE_URI = "EXTRA_CURRENT_COVER_IMAGE_URI";
    private static final float MIN_HEADER_SCALE = 0.2f;
    private static final long DURATION_DEFAULT = 350;
    private static final long DURATION_OPEN_TRANSLATION = DURATION_DEFAULT;
    private static final long DURATION_COMMON_ALPHA = DURATION_DEFAULT;
    private static final long APPEARING_START_DELAY = DURATION_DEFAULT;
    @FloatRange(from = 0f, to = 1f)
    private static final float MAX_AVATAR_OVERSCALE = 0.4f;
    @FloatRange(from = 0f, to = 1f)
    private static final float MAX_HEADER_TEXT_OVERSCALE = 0.2f;
    private static final TimeInterpolator INTERPOLATOR_FAST_OUT_SLOW_IN = new FastOutSlowInInterpolator();
    @Bind(R.id.id_appBarLayout)
    protected AppBarLayout appBarLayout;
    @Bind(R.id.id_widget_event_cover_swapper)
    protected WidgetCoverSwapper widgetEventCoverSwapper;
    @Bind(R.id.blurred_image)
    protected ScalableImageView draweeBlurredEventImage;
    @Bind(R.id.text_event_name)
    protected TextView textEventName;
    @Bind(R.id.recycler_view)
    protected EventDetailsRecyclerView recyclerView;
    @Bind(R.id.bottom_sheet)
    protected LinearLayout bottomSheet;
    private ReviewInBrandRecyclerAdapter adapter;
    private EventDetailsBottomSheetBehavior bottomSheetBehavior;
    private Bitmap blurredBitmap;
    private int colorPrimary;
    private float middleOffsetPercent;
    private boolean shouldDispatchTouches;
    private boolean shouldUseImageScaleAndBlur;
    private ValueAnimator scrimAnimatorIn;
    private ValueAnimator scrimAnimatorOut;
    private boolean scrimSet;
    protected Toolbar toolbar;
    protected TextView toolbarTitle;
    private PresenterBrandView presenter = new PresenterBrandView();
    private FollowPresenter followPresenter = new FollowPresenter();
    private String brandId;
    private ResponseBrand brand;
    private CustomProgressDialog progressDialog;

    public static Intent createIntent(String brandId, Context context) {
        Intent intent = new Intent(context, NewBrandActivity.class);
        intent.putExtra(BRAND, brandId);
        return intent;
    }


    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            for(int i = 0; i < toolbar.getChildCount(); ++i) {
                View child = toolbar.getChildAt(i);
                if (child instanceof TextView) {
                    toolbarTitle = (TextView) child;
                    toolbarTitle.setShadowLayer(1, 0, 0, Color.BLACK);
                    break;
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setWindowAnimations(R.style.NoAnimation);
        super.onCreate(savedInstanceState);
        brandId = (String) getIntent().getSerializableExtra(NewBrandActivity.BRAND);
        setContentView(R.layout.new_brand_fragment);
        ButterKnife.bind(this, this);
        initToolbar();
        showBrand();
        presenter.setView(this, this);
        presenter.getBrand(brandId);
        widgetEventCoverSwapper.setUseSaturationWhenSwapping(false);
        shouldUseImageScaleAndBlur = useImageScalingAndBlurring();
        colorPrimary = ColorUtils.colorPrimary(this);
        followPresenter.setView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (brandId != null)
            presenter.getReview(brandId);
    }

    private void initRequiredBehaviorParams() {
        bottomSheet.post(() -> {
            bottomSheetBehavior = (EventDetailsBottomSheetBehavior) EventDetailsBottomSheetBehavior.from(bottomSheet);
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
            bottomSheetBehavior.setPeekHeight((int) (displayMetrics.heightPixels -  350 * displayMetrics.density));
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

    private void showBrand() {
        initRequiredBehaviorParams();
        adapter = new ReviewInBrandRecyclerAdapter(new ArrayList<>(), new FollowCallBack() {
            @Override
            public void follow() {
                followPresenter.getFollowBrand(brand.getId());
            }

            @Override
            public void unFollow() {
                followPresenter.unFollowBrand(brand.getId());
            }
        });
        recyclerView.setAdapter(adapter);
        appearBottomSheet();
        appearEventName();
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
                    if (toolbarTitle != null) {//                       toolbarTitle.setText();
                    }
                });
    }

    private void appearEventName() {
        textEventName.animate()
                .setStartDelay(APPEARING_START_DELAY)
                .alpha(1)
                .setDuration(DURATION_COMMON_ALPHA)
                .setInterpolator(INTERPOLATOR_FAST_OUT_SLOW_IN)
                .withLayer();
    }

    private void loadCoverImage(String cover) {
        if (cover != null) {
            loadCoverWithAlphaTransition(cover, getIntent().getStringExtra(EXTRA_CURRENT_COVER_IMAGE_URI) != null);
        }
    }

    private void loadCoverWithAlphaTransition(@NonNull String cover, boolean withAlpha) {
        if (cover == null) return;
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

    private Postprocessor createCoverPostprocessor() {
        return new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                if (shouldUseImageScaleAndBlur) {
                    blurredBitmap = WidgetUtils.blurBitmap(getApplicationContext(), bitmap, 2, 25);
                }
                else {
                    blurredBitmap = null;
                }
            }
        };
    }

    private void setBlurredBitmap() {
        if (blurredBitmap != null) {
            draweeBlurredEventImage.setImageBitmap(blurredBitmap);
            draweeBlurredEventImage.setAlpha(0f);
            draweeBlurredEventImage.setVisibility(View.VISIBLE);
        }
    }

    public void onOffsetChanged(@SuppressWarnings("UnusedParameters") View bottomSheet, float offset) {
        float tmpMiddleOffsetPercent = 1 - middleOffsetPercent;
        View tmpTextEventName = textEventName;
        View tmpToolbarTitle = toolbarTitle;
        View tmpToolbar = toolbar;
        ScalableImageView tmpScalableBlurringImageView = draweeBlurredEventImage;
        ScalableDraweeView tmpScalableEventImage = widgetEventCoverSwapper.getBottomView();
        if (offset >= tmpMiddleOffsetPercent) {
            float percent = (offset - tmpMiddleOffsetPercent) / (1 - tmpMiddleOffsetPercent);
            float scale = 1 - percent * MIN_HEADER_SCALE;
            tmpTextEventName.setScaleX(scale);
            tmpTextEventName.setScaleY(scale);
            float percentAlpha = 1 - percent * 1.15f;
            if (percentAlpha >= 0) {
                tmpTextEventName.setAlpha(percentAlpha);

                if (scrimAnimatorOut == null) {
                    scrimAnimatorOut = createToolbarScimOutAnimator(tmpToolbar, tmpToolbarTitle);
                }
                if (scrimSet) {
                    scrimSet = false;
                    AnimatorHelper.cancelAllAnimators(scrimAnimatorIn, scrimAnimatorOut);
                    scrimAnimatorOut.setFloatValues(tmpToolbarTitle.getAlpha(), 0);
                    scrimAnimatorOut.start();
                }
            }
            else {
                if (scrimAnimatorIn == null) {
                    scrimAnimatorIn = createToolbarScrimInAnimator(tmpToolbar, tmpToolbarTitle);
                }
                if (!scrimSet) {
                    scrimSet = true;
                    AnimatorHelper.cancelAllAnimators(scrimAnimatorIn, scrimAnimatorOut);
                    scrimAnimatorIn.setFloatValues(tmpToolbarTitle != null ? tmpToolbarTitle.getAlpha() : 0, 1);
                    scrimAnimatorIn.start();

                    tmpTextEventName.setAlpha(0);
                }
            }
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
            float percentTextScale = 1 + (1 - percent) * MAX_HEADER_TEXT_OVERSCALE;
            tmpTextEventName.setScaleX(percentTextScale);
            tmpTextEventName.setScaleY(percentTextScale);
            tmpTextEventName.setAlpha(1);
        }
    }

    private ValueAnimator createToolbarScrimInAnimator(@NonNull View tmpToolbar, @NonNull View tmpToolbarTitle) {
        ValueAnimator result = new ValueAnimator();
        result.setDuration(DURATION_DEFAULT);
        result.setInterpolator(INTERPOLATOR_FAST_OUT_SLOW_IN);
        result.addUpdateListener(animation -> {
            float currentFraction = (float) animation.getAnimatedValue();
            tmpToolbar.setBackgroundColor(ColorUtils.getColor(colorPrimary, currentFraction));
            tmpToolbarTitle.setAlpha(currentFraction);
        });
        return result;
    }

    private ValueAnimator createToolbarScimOutAnimator(@NonNull View tmpToolbar, @NonNull View tmpToolbarTitle) {
        ValueAnimator result = new ValueAnimator();
        result.setDuration(DURATION_DEFAULT);
        result.setInterpolator(INTERPOLATOR_FAST_OUT_SLOW_IN);
        result.addUpdateListener(animation -> {
            float currentFraction = (float) animation.getAnimatedValue();
            tmpToolbar.setBackgroundColor(ColorUtils.getColor(colorPrimary, currentFraction));
            tmpToolbarTitle.setAlpha(currentFraction);
        });
        return result;
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
                if (appBarLayout != null) {
                    appBarLayout.setVisibility(View.GONE);
                    appBarLayout.setAlpha(1f);
                    appBarLayout.setTranslationY(0);
                }
                if (bottomSheet != null) {
                    bottomSheet.setVisibility(View.GONE);
                    bottomSheet.setTranslationY(0);
                }

                if (toolbarTitle != null) {
                    toolbarTitle.setText("Toolbar title");
                    toolbarTitle.setAlpha(1f);
                }
                WidgetUtils.gone(widgetEventCoverSwapper);
                shouldDispatchTouches = true;
            }
        });
        set.start();

        //  WidgetUtils.gone(toolbarBackButton);
    }

    private boolean shouldOpenTranslation() {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return shouldDispatchTouches && super.dispatchTouchEvent(ev);
    }

    private boolean useImageScalingAndBlurring() {
        return true;
    }

    @Override
    public void getBrand(final ResponseBrand responseBrand) {
        brand = responseBrand;
        adapter.setHeader(responseBrand);
        loadCoverWithAlphaTransition(responseBrand.getImage(PhotoSize.SIZE_BIG, this), true);
        toolbarTitle.setText(responseBrand.getTitle());
    }

    @Override
    public void followReview(final BaseResponse responce) {
        brand.setiSubscriber(!brand.iSubscriber());
        adapter.setHeader(brand);
    }

    @Override
    public void getReview(final ArrayList<ResponseReview> responseReviews) {
        adapter.setResponseReviewList(responseReviews);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void hideProgress() {
        if (progressDialog == null) {
            return;
        }
        else if (progressDialog.isVisible()) {
            progressDialog.dismissAllowingStateLoss();
            return;
        }
        if (progressDialog.getShowsDialog()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showProgress() {
        if (progressDialog == null || !progressDialog.isVisible()) {
            progressDialog = new CustomProgressDialog(this);
            progressDialog.show(getSupportFragmentManager(), this.getClass().getName());
        }
    }

    @Override
    public void showError(final Throwable e) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_brand, menu);
        //   this.menu = menu;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_next:
                presenter.getBrand(brand.getBrandNext());
                presenter.getReview(brand.getBrandNext());
                return true;
            case R.id.action_previus:
                presenter.getBrand(brand.getBrandPrevId());
                presenter.getReview(brand.getBrandPrevId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}