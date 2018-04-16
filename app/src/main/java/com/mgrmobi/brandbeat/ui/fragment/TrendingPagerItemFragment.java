package com.mgrmobi.brandbeat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseTranding;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.ui.dialog.CustomProgressDialog;
import com.mgrmobi.brandbeat.utils.Utils;
import com.mgrmobi.sdk.social.android.AndroidBaseSocialNetwork;
import com.mgrmobi.sdk.social.base.SocialAuthorizationListener;
import com.mgrmobi.sdk.social.base.SocialCancelReason;
import com.mgrmobi.sdk.social.base.SocialNetwork;
import com.mgrmobi.sdk.social.base.SocialNetworkManager;
import com.mgrmobi.sdk.social.base.SocialShareListener;
import com.mgrmobi.sdk.social.base.SocialType;
import com.mgrmobi.sdk.social.facebook.FacebookSocialNetwork;
import com.mgrmobi.sdk.social.googleplus.GooglePlusSocialNetwork;
import com.mgrmobi.sdk.social.googleplus.utils.MGooglePlusUtils;
import com.mgrmobi.sdk.social.linkedin.LinkedinSocialNetwork;
import com.mgrmobi.sdk.social.twitter.TwitterSocialNetwork;
import com.mgrmobi.sdk.social.twitter.utils.TwitterUtils;
import com.squareup.okhttp.internal.Util;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class TrendingPagerItemFragment extends Fragment {

    @Bind(R.id.logo_brand)
    public ImageView logoBrand;
    @Bind(R.id.text_rating)
    public TextView textRating;
    @Bind(R.id.beat_rating)
    public RatingBar beats;
    @Bind(R.id.rated)
    public TextView rated;
    @Bind(R.id.reviewd)
    public TextView reviewd;
    @Bind(R.id.position_number)
    public TextView positionBrand;
    @Bind(R.id.brand_name)
    public TextView nameBrand;

    private CustomProgressDialog progressDialog;
    private ResponseBrand brand;
    private int position;
    private AndroidBaseSocialNetwork socialNetwork;

    public TrendingPagerItemFragment(ResponseBrand brand, int position) {
        super();
        this.brand = brand;
        this.position = position;
    }
    public TrendingPagerItemFragment()
    {}

    public static final Fragment newInstance(ResponseBrand brand, int position) {
        return new TrendingPagerItemFragment(brand, position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.trending_brand_item, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        setView();
    }

    private void setView() {
        Picasso.with(getContext()).load(brand.getImage(PhotoSize.SIZE_BIG, getContext())).into(logoBrand);
        textRating.setText(brand.getAvgRate());
        beats.setRating(Float.parseFloat(brand.getAvgRate()) / Utils.RATING_BAR_DELETE_VALUE);
        if (brand.getRatedSum() == null) {
          //  rated.setText("0 " + getString(R.string.rated));
        }
        else {
           // rated.setText(brand.getRatedSum() + " " + getString(R.string//.rated));
        }

        if (brand.getReviewsSum() == null)
            reviewd.setText("0 " + getString(R.string.reviewed));
        else
            reviewd.setText(brand.getReviewsSum() + " " + getString(R.string.reviewed));
        positionBrand.setText(position + 1 + "");
        nameBrand.setText(brand.getTitle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.facebook_button)
    protected void onFacebookClick() {
        if (SocialNetworkManager.isRegistered(SocialType.FACEBOOK)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.FACEBOOK);
            share();
        }
        else {
            socialNetwork = new FacebookSocialNetwork.Builder()
                    .setContext(getActivity())
                    .setPermission(new String[]{"public_profile", "email", "user_friends"})
                    .setPublishPermission(new String[]{"publish_actions"})
                    .create();
        }
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
    }

    @OnClick(R.id.twitter_button)
    protected void onTwitterClick() {
        if (SocialNetworkManager.isRegistered(SocialType.TWITTER)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.TWITTER);
        }
        else {
            socialNetwork = new TwitterSocialNetwork.Builder()
                    .setContext(getContext()).create();
        }
        share();
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
        //    socialNetwork.login(getActivity());
    }

    @OnClick(R.id.google_plus_button)
    protected void onGooglePlusClick() {
        if (SocialNetworkManager.isRegistered(SocialType.GOOGLE_PLUS)) {
            socialNetwork = (AndroidBaseSocialNetwork) SocialNetworkManager.getNetwork(SocialType.GOOGLE_PLUS);
        }
        socialNetwork = new GooglePlusSocialNetwork.Builder()
                .setContext(getActivity())
                .create();
        share();
        //    socialNetwork.setAuthorizationListener(socialAuthorizationListener);
//        socialNetwork.login(getActivity());
    }

    @OnClick(R.id.linked_in_button)
    protected void onLinkedInClick() {
        if (SocialNetworkManager.isRegistered(SocialType.LINKEDIN_WEB)) {
            socialNetwork = (LinkedinSocialNetwork) SocialNetworkManager.getNetwork(SocialType.LINKEDIN_WEB);
        }
        socialNetwork = new LinkedinSocialNetwork.Builder()
                .setContext(getActivity())
                .setConfig(getString(R.string.linked_in_client_id), getString(R.string.linked_in_client_secret),
                        getString(R.string.linked_in_redirect_url), getResources().getStringArray(R.array.linked_in_params))
                .create();
        socialNetwork.setAuthorizationListener(socialAuthorizationListener);
        socialNetwork.login(getActivity());
    }

    private SocialAuthorizationListener socialAuthorizationListener = new SocialAuthorizationListener() {
        @Override
        public void onAuthorizationSuccess(SocialNetwork network) {
            if (SocialType.FACEBOOK == network.getSocialType()) {
                if (!((FacebookSocialNetwork) network).checkPermissions()) {
                    ((FacebookSocialNetwork) network).login(getActivity());
                }
                ((FacebookSocialNetwork) network).profile(true);
            }
            share();
        }

        @Override
        public void onAuthorizationFail(SocialNetwork network, Throwable throwable) {
            if (socialNetwork.getSocialType() == SocialType.LINKEDIN_WEB) {
            }
            return;
        }

        @Override
        public void onAuthorizationCancel(SocialNetwork network, SocialCancelReason reason) {
            return;
        }
    };

    private void share() {
        if (socialNetwork.getSocialType() == SocialType.LINKEDIN_WEB) {
            progressDialog = new CustomProgressDialog(getActivity());
            try {
                progressDialog.show(getActivity().getSupportFragmentManager(), this.getClass().getName());
            }
            catch (IllegalStateException e)
            {}
        }
        if (socialNetwork == null) {
            return;
        }
        if (socialNetwork.getSocialType() == SocialType.TWITTER) {
            if (!TwitterUtils.isTwitterInstalled(getActivity())) {
                TwitterUtils.openMarket(getActivity());
                return;
            }
        }
        socialNetwork.share(getActivity(), getString(R.string.app_name), getString(R.string.check) + " " + brand.getTitle()
                + " " + getString(R.string.share_text), getString(R.string.url_share), null, new SocialShareListener() {
            @Override
            public void onShareSuccess(SocialNetwork network, String postId) {
                if (socialNetwork.getSocialType() == SocialType.LINKEDIN_WEB) {
                    progressDialog.dismiss();
                }
                Utils.showAlertDialog(getActivity(), (dialog, which) -> {}, (dialog1, which1) -> {}, "", getString(R.string.success), false, true);
                return;
            }

            @Override
            public void onShareCancel(SocialNetwork network, SocialCancelReason reason) {
                if (network.getSocialType() == SocialType.GOOGLE_PLUS) {
                    if (!MGooglePlusUtils.isGooglePlusAppInstalled(getActivity())) {
                        MGooglePlusUtils.openMarket(getActivity());
                    }
                }
                if (socialNetwork.getSocialType() == SocialType.LINKEDIN_WEB) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onShareFail(SocialNetwork network, Throwable e) {
                socialNetwork.logout();
                socialNetwork.login(getActivity());
                if (socialNetwork.getSocialType() == SocialType.LINKEDIN_WEB) {
                    progressDialog.dismiss();
                }

                return;
            }
        });
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        socialNetwork.onActivityResult(requestCode, resultCode, data);
    }
}
