package com.mgrmobi.brandbeat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseAchievement;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.network.responce.ResponseFeed;
import com.mgrmobi.brandbeat.network.responce.ResponseLocalFeed;
import com.mgrmobi.brandbeat.network.responce.ResponseNotification;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.presenter.PresenterLogOutView;
import com.mgrmobi.brandbeat.presenter.PresenterProfileView;
import com.mgrmobi.brandbeat.ui.base.BaseNavigationActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerFeatureBrand;
import com.mgrmobi.brandbeat.ui.base.ContainerInterests;
import com.mgrmobi.brandbeat.ui.base.ContainerLocalFeed;
import com.mgrmobi.brandbeat.ui.base.ContainerLogOut;
import com.mgrmobi.brandbeat.ui.base.ContainerMyFeed;
import com.mgrmobi.brandbeat.ui.base.ContainerNotifications;
import com.mgrmobi.brandbeat.ui.base.ContainerProfile;
import com.mgrmobi.brandbeat.ui.base.ContainerRecentBrands;
import com.mgrmobi.brandbeat.ui.base.ContainerTrandingBrands;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.ui.fragment.AboutUsFragment;
import com.mgrmobi.brandbeat.ui.fragment.ChooseCategoryForCampareFragment;
import com.mgrmobi.brandbeat.ui.fragment.FeedFragment;
import com.mgrmobi.brandbeat.ui.fragment.InterestsFragment;
import com.mgrmobi.brandbeat.ui.fragment.LocalFeedFragment;
import com.mgrmobi.brandbeat.ui.fragment.BrandsFeedsFragment;
import com.mgrmobi.brandbeat.ui.fragment.NotificationsFragment;
import com.mgrmobi.brandbeat.ui.fragment.RecentBrandFragment;
import com.mgrmobi.brandbeat.ui.fragment.SendFeedBackFragment;
import com.mgrmobi.brandbeat.ui.fragment.TermsAndConditionsFragment;
import com.mgrmobi.brandbeat.ui.fragment.TrendingBrandFragment;
import com.mgrmobi.brandbeat.ui.fragment.TrendingPagerFragment;
import com.mgrmobi.brandbeat.ui.fragment.best_ui.NewProfileFragment;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;
import com.mgrmobi.sdk.social.base.SocialNetwork;
import com.mgrmobi.sdk.social.base.SocialNetworkManager;
import com.mgrmobi.sdk.social.base.SocialType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class NavigationActivity extends BaseNavigationActivity implements ContainerProfile,
        ContainerLogOut, NavigationView.OnNavigationItemSelectedListener, ContainerInterests,
        ContainerMyFeed, ContainerLocalFeed, ContainerRecentBrands, ContainerTrandingBrands,
        ContainerNotifications, ContainerFeatureBrand {
    private Bitmap imageBitmap;

    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String FRAGMENT = "fragment";
    private static final String PROFILE = "profile";
    private static final String RESPONSE_REVIEW = "response_reviews";
    private Fragment fragment;
    private CustomProgressDialog progressDialog;
    private Menu menu;
    private PresenterLogOutView presenterLogOutView = new PresenterLogOutView();
    PresenterProfileView presenterProfileView = new PresenterProfileView();

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.base_layout_for_fragment_with_navigation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationView.setNavigationItemSelectedListener(this);
        if (fragment == null)
            showFeed();
        presenterLogOutView.setView(this);
        presenterProfileView.setView(this);
        UserDataUtils userDataUtils = new UserDataUtils(this);
        presenterProfileView.getUser(userDataUtils.getUserId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    private void showSearchMenu() {
        menu.getItem(0).setVisible(true);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
    }

    private void showNotificationMenu() {
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(true);
        menu.getItem(2).setVisible(false);
    }

    private void showTrendingMenu() {
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(true);

    }

    private void noMenu() {
        if (menu == null) return;
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
    }

    private void showProfile() {
        fragment = new NewProfileFragment();
        showFragment(fragment);
        getSupportActionBar().setTitle(R.string.profile);
        tabLayout.setVisibility(View.GONE);
        noMenu();
        getMenu().setVisibility(View.GONE);
    }

    private void showInterests() {
        startActivity(EditInterestsFromMenuActivity.createIntent(this));
    }

    private void showRecentBrand() {
        fragment = new RecentBrandFragment();
        showFragment(fragment);
        getSupportActionBar().setTitle(R.string.recent);
        tabLayout.setVisibility(View.GONE);
        noMenu();
        getMenu().setVisibility(View.GONE);
    }

    private void showSendFeedBack() {
        fragment = new SendFeedBackFragment();
        showFragment(fragment);
        getSupportActionBar().setTitle(R.string.send_feedback);
        tabLayout.setVisibility(View.GONE);
        noMenu();
        getMenu().setVisibility(View.GONE);
    }

    private void showAboutUsFrargment() {
        fragment = new AboutUsFragment();
        showFragment(fragment);
        getSupportActionBar().setTitle(R.string.about_us);
        tabLayout.setVisibility(View.GONE);
        noMenu();
        getMenu().setVisibility(View.GONE);
    }

    private void showComapreFragment() {
        fragment = new ChooseCategoryForCampareFragment();
        showFragment(fragment);
        getSupportActionBar().setTitle(R.string.select_to_compare);
        tabLayout.setVisibility(View.GONE);
        noMenu();
        getMenu().setVisibility(View.GONE);
    }

    private void showTermsAndConditions() {
        fragment = new TermsAndConditionsFragment();
        showFragment(fragment);
        getSupportActionBar().setTitle(R.string.terms_and_conditions);
        tabLayout.setVisibility(View.GONE);
        noMenu();
        getMenu().setVisibility(View.GONE);
    }

    private void showAddReviewActivity() {
        startActivity(ChooseCategoryForReviewActivity.createIntent(this,0,0));
    }

    @Override
    public void getProfile(ResponseProfile responseProfile) {
        if (fragment != null && fragment instanceof NewProfileFragment) {
            ((NewProfileFragment) fragment).setProfile(responseProfile);
            setPhoto(responseProfile.getPhoto(PhotoSize.SIZE_SMALL, this));
        }
        if (getSupportActionBar() != null && fragment instanceof NewProfileFragment) {
            String str = "";
            if (responseProfile.getFirstName() != null)
                str += responseProfile.getFirstName() + " ";
            if (responseProfile.getLastName() != null)
                str += responseProfile.getLastName();
            if (str.equals(""))
                str += responseProfile.getUsername();
            getSupportActionBar().setTitle(str);
        }
    }

    @Override
    public void getUserReview(final ArrayList<ResponseReview> responseReviews) {
        if (fragment != null && fragment instanceof NewProfileFragment) {
            ((NewProfileFragment) fragment).setReviews(responseReviews);
        }
    }

    @Override
    public void getPhotoUrl(final String s) {
        if (fragment != null && fragment instanceof NewProfileFragment) {
            ((NewProfileFragment) fragment).setPhoto(s);
            setPhoto(s);
        }
    }

    @Override
    public void setAchivmients(final List<ResponseAchievement> achivmients) {
        if (fragment != null && fragment instanceof NewProfileFragment) {
            ((NewProfileFragment) fragment).setAchvimients(achivmients);
        }
    }

    @Override
    public void getInterest(final ArrayList<ResponseCategories> interests) {
        if (fragment != null && fragment instanceof InterestsFragment) {
            ((InterestsFragment) fragment).setInterests(interests);
        }
        else if(fragment != null && fragment instanceof ChooseCategoryForCampareFragment)
        {
            ((ChooseCategoryForCampareFragment) fragment).setIntersest(interests);
        }
    }

    @Override
    public void onComplete() {

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
    public void showProgress() {
        progressDialog = new CustomProgressDialog(this);
        try {
            progressDialog.show(getSupportFragmentManager(), this.getClass().getName());
        } catch (IllegalStateException e) {
        }
    }

    @Override
    public void showError(final Throwable e) {
        if (e != null && e.getMessage() != null && e.getMessage().equals("001-004")) {
            finish();
            startActivity(LoginActivity.createIntent(this));
        }
        Utils.showAlertDialog(this, (dialog, which) -> dialog.dismiss(), (dialog1, which1) -> dialog1.dismiss(),
                getString(R.string.error), e.getMessage(), true, false);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_interests: {
                if (!(fragment instanceof InterestsFragment)) {
                    showInterests();
                }
                break;
            }
            case R.id.profile: {
                if (!(fragment instanceof NewProfileFragment)) {
                    showProfile();
                }
                break;
            }
            case R.id.sign_out: {
                signOut();
                break;
            }
            case R.id.feed: {
                if (!(fragment instanceof FeedFragment)) {
                    showFeed();
                }
                break;
            }
            case R.id.recent: {
                if (!(fragment instanceof RecentBrandFragment)) {
                    showRecentBrand();
                }
                break;
            }
            case R.id.add_product: {
                int[] mass = new int[2];
                View menuView = findViewById(R.id.add_product);
                if (menuView != null)
                    menuView.getLocationOnScreen(mass);
                else {
                    mass = new int[2];
                    mass[0] = 0;
                    mass[1] = 0;
                }
                startActivity(AddBrandActivity.createIntent(this, mass[0], mass[1]));
                break;
            }
            case R.id.write_review: {
                showAddReviewActivity();
                break;
            }
            case R.id.trending: {
                if (!(fragment instanceof ChooseCategoryForCampareFragment)) {
                    showComapreFragment();
                }
                break;
            }
            case R.id.notifications: {
                if (!(fragment instanceof NotificationsFragment)) {
                    showNotifications();
                }
                break;
            }
            case R.id.send_feedback: {
                if (!(fragment instanceof SendFeedBackFragment))
                    showSendFeedBack();
                break;
            }
            case R.id.about: {
                if (!(fragment instanceof AboutUsFragment))
                    showAboutUsFrargment();
                break;
            }
            case R.id.terms_and_conditions: {
                if (!(fragment instanceof TermsAndConditionsFragment)) {
                    showTermsAndConditions();
                }
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void signOut() {
        presenterLogOutView.logOut();
    }

    @Override
    public void logOutSuccess() {
        this.finish();
        UserDataUtils userDataUtils = new UserDataUtils(this);
        userDataUtils.clear();
        SocialNetwork socialNetwork = SocialNetworkManager.getNetwork(SocialType.FACEBOOK);
        if (socialNetwork != null)
            socialNetwork.logout();
        socialNetwork = SocialNetworkManager.getNetwork(SocialType.TWITTER);
        if (socialNetwork != null)
            socialNetwork.logout();
        socialNetwork = SocialNetworkManager.getNetwork(SocialType.LINKEDIN_WEB);
        if (socialNetwork != null)
            socialNetwork.logout();
        socialNetwork = SocialNetworkManager.getNetwork(SocialType.GOOGLE_PLUS);
        if (socialNetwork != null)
            socialNetwork.logout();
        startActivity(new Intent(this, LogOutSuccessActivity.class));
    }

    @Override
    public void onError(final String message) {
        Snackbar.make(getParentView(), message, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.ok), v -> {
                })
                .show();
        if (fragment instanceof RecentBrandFragment) {
            ((RecentBrandFragment) fragment).showError();
        }
    }

    private void showFeed() {
        showFeedAfterGetPermissions();
    }

    private void showFeedAfterGetPermissions() {
        getSupportActionBar().setTitle(R.string.app_name);
        tabLayout.setVisibility(View.VISIBLE);
        fragment = new FeedFragment();
        showFragment(fragment);
        if (menu != null)
            showSearchMenu();
        getMenu().setVisibility(View.VISIBLE);
    }

    private void showNotifications() {
        getSupportActionBar().setTitle(R.string.notification);
        tabLayout.setVisibility(View.GONE);
        showNotificationMenu();
        fragment = new NotificationsFragment();
        showFragment(fragment);
        getMenu().setVisibility(View.GONE);
    }

    @Override
    public void getMyFeed(final ArrayList<ResponseFeed> responseFeed) {
        if (fragment != null && fragment instanceof FeedFragment) {
            Fragment page = fragment.getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + 0);
            if (page instanceof BrandsFeedsFragment) {
                ((BrandsFeedsFragment) page).setMyFeed(responseFeed);
            }
        }
    }

    @Override
    public void suggestedBrand(final ArrayList<ResponseBrand> responseBrands) {
        if (fragment != null && fragment instanceof FeedFragment) {
            Fragment page = fragment.getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + 0);
            if (page instanceof BrandsFeedsFragment) {
                ((BrandsFeedsFragment) page).setSuggestedBrand(responseBrands);
            }
        }
    }

    @Override
    public void getLocalFeed(final ArrayList<ResponseLocalFeed> responseFeed) {
        if (fragment != null && fragment instanceof FeedFragment) {
            Fragment page = fragment.getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + 1);
            if (page instanceof LocalFeedFragment) {
                ((LocalFeedFragment) page).setLocalFeed(responseFeed);
            }
        }
    }

    @Override
    public void getSuggetedBrand(final ArrayList<ResponseBrand> suggestedBrands) {
        if (fragment != null && fragment instanceof FeedFragment) {
            Fragment page = fragment.getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + 1);
            if (page instanceof LocalFeedFragment) {
                ((LocalFeedFragment) page).setSuggestedBrand(suggestedBrands);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNameEmail();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        if (data.getData() != null || data.getExtras().get("data") instanceof Bitmap)
            try {
                imageBitmap = Utils.loadImageFromResurce(data, this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (fragment != null && fragment instanceof NewProfileFragment) {
            if (imageBitmap != null) {
                double proportion = ((double) imageBitmap.getHeight() / (double) imageBitmap.getWidth());
                imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 500, (int) ((int) 500 * proportion), false);
                ((NewProfileFragment) fragment).setBitmap(imageBitmap);
            }
        }
        if (fragment != null && fragment instanceof TrendingPagerFragment) {
            ((TrendingPagerFragment) fragment).onResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void getBrands(final ArrayList<ResponseBrand> responseBrands) {
        if (fragment instanceof RecentBrandFragment) {
            ((RecentBrandFragment) fragment).setBrands(responseBrands);
        }
    }

    @Override
    public void getTrandingBrands(final List<ResponseBrand> responseBrands) {
        if (fragment instanceof TrendingBrandFragment) {
            ((TrendingBrandFragment) fragment).setBrand(responseBrands);
        }
    }

    @Override
    public void showPaginationProgress() {
        if (fragment instanceof NewProfileFragment) {
            ((NewProfileFragment) fragment).loadNextPage();
        }
    }

    @Override
    public void dismissPaginationProggress() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BITMAP_STORAGE_KEY, imageBitmap);
        if (fragment instanceof NewProfileFragment) {
            outState.putString(FRAGMENT, "fragment");
            outState.putSerializable(PROFILE, ((NewProfileFragment) fragment).getProfile());
            outState.putSerializable(RESPONSE_REVIEW, (Serializable) ((NewProfileFragment) fragment).getReviews());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null) return;
        imageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
        if (savedInstanceState.getString(FRAGMENT) != null && savedInstanceState.getString(FRAGMENT).equals("fragment")) {
            showProfile();
            if (fragment != null)
                if (fragment instanceof NewProfileFragment) {
                    ((NewProfileFragment) fragment).setProfileResponse((ResponseProfile) savedInstanceState.getSerializable(PROFILE));
                    ((NewProfileFragment) fragment).setReviews((List<ResponseReview>) savedInstanceState.getSerializable(RESPONSE_REVIEW));
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_context) {

            getWindow().setWindowAnimations(R.style.NoAnimation);
            //    startActivityForResult(ActivityEditProfile.createIntent(ActivityMainContent.this, profile, touchX, touchY),
            //            Constants.ID_ACTIVITY_EDIT_PROFILE);
            int[] mass = new int[2];
            View menuView = findViewById(R.id.action_context);
            menuView.getLocationOnScreen(mass);
            startActivity(SearchActivity.createIntent(NavigationActivity.this, mass[0], mass[1]));
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = NotificationsSettingsActivity.createIntent(NavigationActivity.this);
            Bundle b = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this, findViewById(R.id.action_settings), "asdasdas").toBundle();
            startActivity(intent, b);
            return true;
        }
        if (id == R.id.action_change_trending) {
            showComapreFragment();
      /*      if (!(fragment instanceof TrendingBrandFragment)) {
                showTrendingBrands();
            }
            else {
                showTrendingPagerFragment();
            }
            return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getNotifications(List<ResponseNotification> responseNatifications) {
        if (fragment instanceof NotificationsFragment) {
            ((NotificationsFragment) fragment).setNatifications(responseNatifications);
        }
    }

    @Override
    public void setFeatureBrand(final List<ResponseBrand> responseBrands) {
        if (fragment instanceof TrendingBrandFragment) {
            ((TrendingBrandFragment) fragment).setFeatureBrand(responseBrands);
        }
    }
}

