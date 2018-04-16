package com.mgrmobi.brandbeat.ui.fragment.best_ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.mgrmobi.brandbeat.ui.activity.NewBrandActivity;
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
@SuppressLint("ValidFragment")
public class BrandPagerItemFragment extends Fragment implements ContainerReviewInBrand, ContainerFollowBrand, ContainerBrand {

    private static final float MIN_HEADER_SCALE = 0.0f;
    private static final long DURATION_DEFAULT = 350;
    private static final long DURATION_OPEN_TRANSLATION = DURATION_DEFAULT;
    private static final long DURATION_COMMON_ALPHA = DURATION_DEFAULT;
    private static final long APPEARING_START_DELAY = DURATION_DEFAULT;
    @FloatRange(from = 0f, to = 1f)
    private static final float MAX_AVATAR_OVERSCALE = 0.1f;
    @FloatRange(from = 0f, to = 1f)
    private static final float MAX_HEADER_TEXT_OVERSCALE = 0.2f;
    private static final TimeInterpolator INTERPOLATOR_FAST_OUT_SLOW_IN = new FastOutSlowInInterpolator();

    private ReviewInBrandRecyclerAdapter adapter;
    private EventDetailsBottomSheetBehavior bottomSheetBehavior;
    private Bitmap blurredBitmap;
    private int colorPrimary;
    private float middleOffsetPercent;
    private boolean shouldUseImageScaleAndBlur;
    private ValueAnimator scrimAnimatorIn;
    private ValueAnimator scrimAnimatorOut;
    private boolean scrimSet;
    protected TextView toolbarTitle;
    private PresenterBrandView presenter = new PresenterBrandView();
    private FollowPresenter followPresenter = new FollowPresenter();
    private String brandId;
    public ResponseBrand brand;
    private CustomProgressDialog progressDialog;
    private ArrayList<ResponseReview> responseReviews = new ArrayList<>();
    private String mNum;

    @Bind(R.id.id_appBarLayout)
    protected AppBarLayout appBarLayout;
    @Bind(R.id.id_widget_event_cover_swapper)
    protected WidgetCoverSwapper widgetEventCoverSwapper;
    @Bind(R.id.blurred_image)
    protected ScalableImageView draweeBlurredEventImage;
    @Bind(R.id.text_event_name)
    protected TextView textEventName;
    @Bind(R.id.bottom_sheet)
    protected LinearLayout bottomSheet;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    private NewBrandPagerFragment baseFragment;
    @Bind(R.id.recycler_huickler)
    protected EventDetailsRecyclerView recyclerView;

    public static BrandPagerItemFragment newInstance(String num) {
        BrandPagerItemFragment f = new BrandPagerItemFragment();
        Bundle args = new Bundle();
        args.putString("num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getString("num");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_brand_fragment, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initToolbar(view);
        presenter.setView(this, this);
        followPresenter.setView(this);
        presenter.getBrand(mNum);
        presenter.getReview(mNum);
        Log.i("M_CALL", "called on : " + this.toString());
        for(int i = 0; i < toolbar.getChildCount(); ++i) {
            View child = toolbar.getChildAt(i);
            if (child instanceof TextView) {
                toolbarTitle = (TextView) child;
                toolbarTitle.setShadowLayer(1, 0, 0, Color.BLACK);
                break;
            }
        }
        initView();
    }

    protected void initToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        getActivity().invalidateOptionsMenu();
        for(int i = 0; i < toolbar.getChildCount(); ++i) {
            View child = toolbar.getChildAt(i);
            if (child instanceof TextView) {
                toolbarTitle = (TextView) child;
                toolbarTitle.setShadowLayer(1, 0, 0, Color.BLACK);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            getActivity().finish();
        }
        return true;
    }

    private void initRequiredBehaviorParams() {
        bottomSheet.post(() -> {
            if (getActivity() == null) return;
            bottomSheetBehavior = (EventDetailsBottomSheetBehavior) EventDetailsBottomSheetBehavior.from(bottomSheet);
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            bottomSheetBehavior.setPeekHeight((int) (displayMetrics.heightPixels - 350 * displayMetrics.density));
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

    private void appearBottomSheet() {
        if(!isAdded()) return;
        bottomSheet.setTranslationY(getResources().getDisplayMetrics().heightPixels - bottomSheet.getMeasuredHeight());
        bottomSheet.animate()
                .setStartDelay(APPEARING_START_DELAY)
                .alpha(1)
                .translationY(0)
                .setDuration(DURATION_COMMON_ALPHA)
                .setInterpolator(INTERPOLATOR_FAST_OUT_SLOW_IN)
                .withLayer()
                .withEndAction(() -> {
                    if (getActivity() != null) {
                        ((NewBrandPagerActivtiy) getActivity()).setDispatchTouch(true);

                        if (toolbarTitle != null) {//                       toolbarTitle.setText();
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
                });
    }

    private void appearEventName() {
  /*      textEventName.animate()
                .setStartDelay(APPEARING_START_DELAY)
                .alpha(1)
                .setDuration(DURATION_COMMON_ALPHA)
                .setInterpolator(INTERPOLATOR_FAST_OUT_SLOW_IN)
                .withLayer();*/
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
                        if (throwable.getMessage() != null)
                            Log.e("asdas", throwable.getMessage());
                        else
                            Log.e("asdas", "unknowerror");
                    }
                }, withAlpha);
    }

    private Postprocessor createCoverPostprocessor() {
        return new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                //      if (shouldUseImageScaleAndBlur) {
                //          blurredBitmap = WidgetUtils.blurBitmap(getActivity().getApplicationContext(), bitmap, 0, 25);
                //      }
                //    else {
                //       blurredBitmap = null;
                //  }
            }
        };
    }

    private void setBlurredBitmap() {
        if (blurredBitmap != null) {
            draweeBlurredEventImage.setImageBitmap(blurredBitmap);
            draweeBlurredEventImage.setAlpha(1f);
            draweeBlurredEventImage.setVisibility(View.VISIBLE);
        }
    }

    public void onOffsetChanged(@SuppressWarnings("UnusedParameters") View bottomSheet, float offset) {
        float tmpMiddleOffsetPercent = 1 - middleOffsetPercent;
        View tmpTextEventName = textEventName;
        View tmpToolbarTitle = toolbarTitle;
        View tmpToolbar = toolbar;
        initToolbar(tmpToolbar);
        ScalableImageView tmpScalableBlurringImageView = draweeBlurredEventImage;
        ScalableDraweeView tmpScalableEventImage = widgetEventCoverSwapper.getBottomView();
        if (offset >= tmpMiddleOffsetPercent) {
            float percent = (offset - tmpMiddleOffsetPercent) / (1 - tmpMiddleOffsetPercent);
            float scale = 1 - percent * MIN_HEADER_SCALE;
            tmpTextEventName.setScaleX(scale);
            tmpTextEventName.setScaleY(scale);
            float percentAlpha = 1 - percent * 1.15f;
            if (percentAlpha >= 0) {
                tmpTextEventName.setAlpha(1);

                if (scrimAnimatorOut == null) {
                    scrimAnimatorOut = createToolbarScimOutAnimator(tmpToolbar, tmpToolbarTitle);
                }
                if (scrimSet) {
                    scrimSet = false;
                    AnimatorHelper.cancelAllAnimators(scrimAnimatorIn, scrimAnimatorOut);
                    scrimAnimatorOut.setFloatValues(tmpToolbarTitle.getAlpha(), 1);
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
                    scrimAnimatorIn.setFloatValues(tmpToolbarTitle != null ? tmpToolbarTitle.getAlpha() : 1, 1);
                    scrimAnimatorIn.start();
                    tmpTextEventName.setAlpha(0);
                }
            }
            if (shouldUseImageScaleAndBlur) {
                if (tmpScalableEventImage != null) {
                    tmpScalableEventImage.setCurrentScale(1);
                }
                tmpScalableBlurringImageView.setAlpha(1f);
            }
        }
        else {
            float percent = offset / tmpMiddleOffsetPercent;
            if (shouldUseImageScaleAndBlur) {
                tmpScalableBlurringImageView.setAlpha(1);

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
            // tmpToolbarTitle.setAlpha(currentFraction);
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
            //  tmpToolbarTitle.setAlpha(currentFraction);
        });
        return result;
    }

    private void showTranslationIfRequired() {
        if (shouldOpenTranslation()) {
            openTranslation();
        }
    }

    private void openTranslation() {
        ((NewBrandPagerActivtiy) getActivity()).setDispatchTouch(false);

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
                    //     toolbarTitle.setText("Toolbar title");
                    //           toolbarTitle.setAlpha(1f);
                }
                WidgetUtils.gone(widgetEventCoverSwapper);
                ((NewBrandPagerActivtiy) getActivity()).setDispatchTouch(true);
            }
        });
        set.start();

    }

    private boolean shouldOpenTranslation() {
        return false;
    }

    private boolean useImageScalingAndBlurring() {
        return true;
    }

    @Override
    public void getBrand(final ResponseBrand responseBrand) {
        if (responseBrand != null) {
            brand = responseBrand;
        }
        else {
        }
        if (responseReviews != null) {
            initData();
        }
        Log.i("M_CALL", "received brands on : " + this.toString());
    }

    private void initData() {
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
        if (brand != null) {
            toolbarTitle.setText(brand.getTitle());
            adapter.setHeader(brand);
        }
        adapter.setResponseReviewList(responseReviews);
        recyclerView.setAdapter(adapter);
        appearBottomSheet();
        adapter.notifyDataSetChanged();
        appearEventName();
        if (brand != null)
            loadCoverWithAlphaTransition(brand.getImage(PhotoSize.SIZE_BIG, getActivity()), true);        //loadCoverWithAlphaTransition(brand.getImage(PhotoSize.SIZE_BIG, getActivity()), true);
    }

    public void initView() {
        widgetEventCoverSwapper.setUseSaturationWhenSwapping(false);
        shouldUseImageScaleAndBlur = useImageScalingAndBlurring();
        colorPrimary = ColorUtils.colorPrimary(getActivity());
        initRequiredBehaviorParams();
    }

    @Override
    public void getReview(final ArrayList<ResponseReview> responseReviews) {
        this.responseReviews = responseReviews;
        if (responseReviews != null) {
            Log.e("brand", responseReviews.size() + "");
        }
        if (brand != null)
            initData();

        Log.i("M_CALL", "received reviews on : " + this.toString());
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
            //     progressDialog = new CustomProgressDialog(getActivity());
            //       progressDialog.show(getActivity().getSupportFragmentManager(), this.getClass().getName());
        }
    }

    @Override
    public void showError(final Throwable e) {
        ArrayList<String> strings =  ((NewBrandPagerActivtiy) getActivity()).brandsPagerAdapter.getIdsBrand();
        int i = 0;
        for(int j = 0; j <strings.size(); j++)
        {
            i = j;
            if(strings.get(j).equals(mNum))
                break;
        }
        strings.remove(mNum);
        ((NewBrandPagerActivtiy) getActivity()).brandsPagerAdapter.setIdsBrand(strings);
        if(i + 1 < strings.size())
            ((NewBrandPagerActivtiy) getActivity()).viewPager.setCurrentItem(i+1);
        else((NewBrandPagerActivtiy) getActivity()).viewPager.setCurrentItem(i-1);
    }

    @Override
    public void followReview(final BaseResponse responce) {
        brand.setiSubscriber(!brand.iSubscriber());
        adapter.setHeader(brand);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getReview(mNum);
    }

}
