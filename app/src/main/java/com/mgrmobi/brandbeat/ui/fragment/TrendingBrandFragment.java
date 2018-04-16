package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.presenter.PresenterFeatureBrand;
import com.mgrmobi.brandbeat.presenter.PresenterTrendingBrand;
import com.mgrmobi.brandbeat.ui.activity.NewBrandActivity;
import com.mgrmobi.brandbeat.ui.activity.best_ui.NewBrandPagerActivtiy;
import com.mgrmobi.brandbeat.ui.adapters.TrendingBrandAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerFeatureBrand;
import com.mgrmobi.brandbeat.ui.base.ContainerTrandingBrands;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class TrendingBrandFragment extends Fragment {

    @Bind(R.id.recycle)
    public RecyclerView recyclerView;
    @Bind(R.id.progress_wheel)
    public View progress;
    @Bind(R.id.root_view)
    public View rootView;
    @Bind(R.id.icon_brand)
    public ImageView iconBrand;
    @Bind(R.id.name_brand)
    public TextView nameBrand;
    @Bind(R.id.rating_text)
    public TextView ratingText;
    @Bind(R.id.beat_rating)
    public RatingBar beatRating;
    private List<ResponseBrand> brands;
    private ContainerTrandingBrands containerTrandingBrands;
    private PresenterFeatureBrand presenterFeatureBrand = new PresenterFeatureBrand();
    private PresenterTrendingBrand presenterTrandingBrand = new PresenterTrendingBrand();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tranding_brands_fragment, container, false);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(R.id.action_context).setVisible(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerTrandingBrands) {
            containerTrandingBrands = (ContainerTrandingBrands) getActivity();
            presenterTrandingBrand.setView(containerTrandingBrands);
        }
        if (getActivity() instanceof ContainerFeatureBrand) {
            presenterFeatureBrand.setView((ContainerFeatureBrand) getActivity());
            presenterFeatureBrand.getFeatureBrand();
        }
        ButterKnife.bind(this, view);
        presenterTrandingBrand.getTrandingBrands();
        progress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    public void setBrand(List<ResponseBrand> brand) {
        this.brands = brand;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        TrendingBrandAdapter trendingBrandAdapter = new TrendingBrandAdapter(getActivity(), brand);
        recyclerView.setAdapter(trendingBrandAdapter);
        progress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void setFeatureBrand(List<ResponseBrand> featureBrand) {
        if (featureBrand == null || featureBrand.size() == 0) {
            rootView.setVisibility(View.GONE);
            return;
        }
        ResponseBrand brand = featureBrand.get(0);
        rootView.setVisibility(View.VISIBLE);
        rootView.setOnClickListener(v -> startActivity(NewBrandPagerActivtiy.createIntent(brand.getId(), brand.getSubCategoryId(),getContext())));
        Picasso.with(getContext()).load(brand.getImage(PhotoSize.SIZE_BIG, getActivity())).into(iconBrand);

        nameBrand.setText(brand.getTitle());

        ratingText.setText(brand.getAvgRate());
        beatRating.setRating(Float.parseFloat(brand.getAvgRate())/5);
    }

    public List<ResponseBrand> getBrands() {
        return brands;
    }
}
