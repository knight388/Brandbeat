package com.mgrmobi.brandbeat.ui.base;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.github.clans.fab.FloatingActionMenu;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.activity.AddBrandActivity;
import com.mgrmobi.brandbeat.ui.activity.ChooseCategoryForReviewActivity;
import com.mgrmobi.brandbeat.utils.UriBuilder;
import com.mgrmobi.brandbeat.utils.UserDataUtils;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public abstract class BaseNavigationActivity extends AppCompatActivity {

    protected NavigationView navigationView;
    protected Toolbar toolbar;
    @Bind(R.id.container_frame)
    public View parentView;
    @Bind(R.id.flaoting_menu)
    public FloatingActionMenu menu;

    @Bind(R.id.tabs)
    public TabLayout tabLayout;
    private View root;

    protected abstract int getLayoutId();

    protected boolean isCollapse = true;

    protected void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment,
                fragment.getClass().getName()).commit();
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(getLayoutId());
        ButterKnife.bind(this, this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        ResponseProfile responseProfile = new ResponseProfile();
        UserDataUtils userDataUtils = new UserDataUtils(BrandBeatApplication.getInstance().getBaseContext());
        responseProfile = userDataUtils.getUserInfo();

        navigationView.inflateHeaderView(R.layout.nav_header_main);
        root = navigationView.getHeaderView(0).findViewById(R.id.root_header_view);
        if(root == null) return;
        if ((TextView) root.findViewById(R.id.name) != null && responseProfile != null && responseProfile.getUsername() != null)
            ((TextView) root.findViewById(R.id.name)).setText(responseProfile.getUsername());
        if ((TextView) root.findViewById(R.id.name) != null)
            ((TextView) root.findViewById(R.id.email_text)).setText(responseProfile.getEmail());
        if (responseProfile.getPhoto(PhotoSize.SIZE_SMALL, this) != null && !responseProfile.getPhoto(PhotoSize.SIZE_SMALL, this).equals("")) {
            int width = 50, height = 50;
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(responseProfile.getPhoto(PhotoSize.SIZE_SMALL, this)))
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setOldController(((SimpleDraweeView) root.findViewById(R.id.avatarImageView)).getController())
                    .setImageRequest(request)
                    .build();
            ((SimpleDraweeView) root.findViewById(R.id.avatarImageView)).setController(controller);
        }
        navigationView.getHeaderView(0).findViewById(R.id.relative_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (isCollapse) {

                    TypedValue typedValue = new TypedValue();
                    getResources().getValue(R.dimen.name_y_move, typedValue, true);
                    ObjectAnimator animX = ObjectAnimator.ofFloat(navigationView.findViewById(R.id.name), "y", dipToPixels(BaseNavigationActivity.this,
                            typedValue.getFloat()));
                    getResources().getValue(R.dimen.name_y_avatar_expand, typedValue, true);
                    ObjectAnimator animY = ObjectAnimator.ofFloat(navigationView.findViewById(R.id.avatarImageView), "y", dipToPixels(BaseNavigationActivity.this,
                            typedValue.getFloat()));
                    getResources().getValue(R.dimen.name_x_move_expand, typedValue, true);
                    ObjectAnimator animXText = ObjectAnimator.ofFloat(navigationView.findViewById(R.id.name), "x", dipToPixels(BaseNavigationActivity.this,
                            typedValue.getFloat()));
                    getResources().getValue(R.dimen.root_y_expand, typedValue, true);
                    ObjectAnimator animRotate = ObjectAnimator.ofFloat(navigationView.findViewById(R.id.relative_layout), "rotation", 0f, 180f);
                    ObjectAnimator animMove = ObjectAnimator.ofFloat(navigationView.findViewById(R.id.relative_layout), "y", dipToPixels(BaseNavigationActivity.this,
                            typedValue.getFloat()));
                    getResources().getValue(R.dimen.nav_header_expand_height, typedValue, true);
                    AnimatorSet animSetXY = new AnimatorSet();
                    animSetXY.playTogether(animX, animY, animXText, animRotate, animMove, expand(navigationView.getHeaderView(0), 500,
                            (int) dipToPixels(BaseNavigationActivity.this, typedValue.getFloat())));
                    animSetXY.start();
                    isCollapse = false;
                }
                else {
                    isCollapse = true;
                    TypedValue typedValue = new TypedValue();
                    getResources().getValue(R.dimen.name_y_collapse, typedValue, true);
                    ObjectAnimator animX = ObjectAnimator.ofFloat(navigationView.findViewById(R.id.name), "y", dipToPixels(BaseNavigationActivity.this,
                            typedValue.getFloat()));
                    getResources().getValue(R.dimen.avatar_y_collapse, typedValue, true);
                    ObjectAnimator animY = ObjectAnimator.ofFloat(navigationView.findViewById(R.id.avatarImageView), "y", dipToPixels(BaseNavigationActivity.this,
                            typedValue.getFloat()));
                    ObjectAnimator animXText = ObjectAnimator.ofFloat(navigationView.findViewById(R.id.name), "x", 0f);
                    ObjectAnimator animRotate = ObjectAnimator.ofFloat(navigationView.findViewById(R.id.relative_layout), "rotation", 3600f);
                    getResources().getValue(R.dimen.y_root_move_collapse, typedValue, true);
                    ObjectAnimator animMove = ObjectAnimator.ofFloat(navigationView.findViewById(R.id.relative_layout), "y", dipToPixels(BaseNavigationActivity.this,
                            typedValue.getFloat()));
                    AnimatorSet animSetXY = new AnimatorSet();
                    getResources().getValue(R.dimen.height_nav_header_collapse, typedValue, true);
                    animSetXY.playTogether(animX, animY, animXText, animRotate, animMove, expand(navigationView.getHeaderView(0), 500,
                            (int) dipToPixels(BaseNavigationActivity.this, typedValue.getFloat())));
                    animSetXY.start();
                }
            }
        });
        menu.setOnTouchListener((v, event) -> {
            if (menu.isOpened()) {
                menu.close(true);
            }
            return menu.isOpened();
        });

        menu.setMenuButtonHideAnimation(AnimationUtils.loadAnimation(this, R.anim.hide_to_bottom));
        menu.setMenuButtonShowAnimation(AnimationUtils.loadAnimation(this, R.anim.show_from_bottom));
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static Animator expand(final View v, int duration, int targetHeight) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(v.getHeight(), targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        return valueAnimator;
    }

    public static Animator collapse(final View v, int duration, int targetHeight) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        return valueAnimator;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public void setTabLayout(final TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    public void setPhoto(String photoUrl) {
        if (photoUrl == null || photoUrl.equals("")) {
            ((SimpleDraweeView) root.findViewById(R.id.avatarImageView)).setImageBitmap(null);
        }
        else ((SimpleDraweeView) root.findViewById(R.id.avatarImageView))
                .setImageURI(UriBuilder.createUri(Uri.parse(photoUrl), 50 + "", 50 + ""));
    }

    public void setNameEmail() {
        UserDataUtils userDataUtils = new UserDataUtils(this);
        ((TextView) root.findViewById(R.id.name)).setText(userDataUtils.getUserInfo().getUsername());
        ((TextView) root.findViewById(R.id.email_text)).setText(userDataUtils.getUserInfo().getEmail());
    }

    @OnClick(R.id.add_brand)
    public void onClicAddBrand() {
        int[] mass = new int[2];
        View menuView = findViewById(R.id.add_brand);
        menuView.getLocationOnScreen(mass);

        startActivity(AddBrandActivity.createIntent(this, mass[0], mass[1]));
    }

    @OnClick(R.id.add_review)
    public void onClickAddReview() {
        int[] mass = new int[2];
        View menuView = findViewById(R.id.add_review);
        menuView.getLocationOnScreen(mass);
        startActivity(ChooseCategoryForReviewActivity.createIntent(this, mass[0], mass[1]));
    }

    public FloatingActionMenu getMenu() {
        return menu;
    }

    public View getParentView() {
        return parentView;
    }
}