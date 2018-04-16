package com.mgrmobi.brandbeat.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.location.bean.LocationBean;
import com.mgrmobi.brandbeat.location.impl.RxLocationBeanImpl;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.network.request.RequestReview;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.mgrmobi.brandbeat.network.responce.ResponseReview;
import com.mgrmobi.brandbeat.presenter.PresenterWriteReview;
import com.mgrmobi.brandbeat.ui.activity.WriteReviewActivity;
import com.mgrmobi.brandbeat.ui.adapters.WriteReviewAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerWriteReview;
import com.mgrmobi.brandbeat.ui.widget.RatingWidget;
import com.mgrmobi.brandbeat.ui.widget.RatingWidgetEditable;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class WriteReviewFragment extends Fragment {
    @Bind(R.id.rating_widjet)
    public RatingWidgetEditable seekBar;
    @Bind(R.id.review_text)
    public EditText reviewText;
    @Bind(R.id.complete)
    public Button complete;
    @Bind(R.id.cancel_button)
    public ImageView cancel;
    @Bind(R.id.title)
    public TextView title;
    @Bind(R.id.photo_view)
    public RecyclerView recyclerView;
 //   @Bind(R.id.rate_text)
   // public TextView mRateText;

    private List<Bitmap> bitmaps = new ArrayList<>();

    private WriteReviewAdapter photoReviewAdapter;
    private RequestLocation requestLocation = new RequestLocation();
    @Inject
    LocationBean locationBean = new RxLocationBeanImpl(BrandBeatApplication.getInstance().getApplicationContext());

    private ContainerWriteReview containerWriteReview;
    private PresenterWriteReview presenterWriteReview = new PresenterWriteReview();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.write_review_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initLocation();
        if (getActivity() instanceof ContainerWriteReview) {
            containerWriteReview = (ContainerWriteReview) getActivity();
            presenterWriteReview.setView(containerWriteReview);
        }
        ButterKnife.bind(this, view);
        if (getActivity().getIntent().getSerializableExtra(WriteReviewActivity.BRANDID) != null) {
            initNewReview();
        }
        else {
            initUpdateReview();
        }
        bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.camera));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        photoReviewAdapter = new WriteReviewAdapter(getContext(), null, bitmaps, (position) ->
        {
            if (bitmaps.size() > 1 && position != bitmaps.size() - 1) {
                Utils.showAlertDialog(getContext(), (dialog, which) -> {
                            bitmaps.remove(position);
                            photoReviewAdapter.setBitmaps(bitmaps);
                            photoReviewAdapter.notifyDataSetChanged();
                        }, (dialog1, which1) -> {
                            dialog1.dismiss();
                        },
                        getString(R.string.warning), getString(R.string.delete_photo_message), true, true);

            }
            else {
                getActivity().startActivityForResult(makePhotoIntent(getString(R.string.image_chooser_title), getActivity()), 1, null);
            }
        }, null);
        recyclerView.setAdapter(photoReviewAdapter);
        seekBar.init();
    }


    private void initNewReview() {
        complete.setOnClickListener(v -> {
            if (photoReviewAdapter.getBitmaps().size() > 1) {
                ArrayList<Bitmap> bitmaps = (ArrayList<Bitmap>) photoReviewAdapter.getBitmaps();
                List<Bitmap> bitmaps1 = new ArrayList<Bitmap>();
                for(int i = 0; i < bitmaps.size() - 1; i++) {
                    bitmaps1.add(bitmaps.get(i));
                }
                presenterWriteReview.uploadPhotoUrls((ArrayList<Bitmap>) bitmaps1);
            }
            else {
                uploadReview(null);
            }
        });
    }

    private void initUpdateReview() {
        ResponseReview responseReview = (ResponseReview) getActivity().getIntent().getSerializableExtra(WriteReviewActivity.REVIEW);
        title.setText(getResources().getString(R.string.edit_review));
        if (responseReview != null) {
            seekBar.setTextRating(Integer.parseInt(responseReview.getRate()));
            reviewText.setText(responseReview.getText());
            complete.setOnClickListener(v -> {
                RequestReview requestReview = new RequestReview();
                requestReview.setBrandId(responseReview.getBrandId());
                requestReview.setText(reviewText.getText().toString());
                requestReview.setRate(seekBar.getProgress() + "");
                requestReview.setReviewId(responseReview.getId());
                presenterWriteReview.updateReview(requestReview);
            });
        }
    }

    @OnClick(R.id.cancel_button)
    public void onCancelClick() {
        if (seekBar.getProgress() != 0 || reviewText.getText().length() > 0) {
            Utils.showAlertDialog(getActivity(), (dialog, which) -> {
                getActivity().finish();
            }, (dialog1, which1) -> {
                dialog1.dismiss();
            }, getString(R.string.warning), getString(R.string.review_cancel_text), true, true);
        }
        else {
            getActivity().finish();
        }
    }

    private void initLocation() {
        Observable<Location> brandBeatAddressObservable = locationBean.getCurrentLocation();
        brandBeatAddressObservable.map(loc -> new LatLng(loc.getLatitude(), loc.getLongitude()))
                .subscribe(loc ->
                {
                    locationBean.getAddressByLocation(loc)
                            .subscribe(address ->
                            {
                                initRequestLocationFromAddress(address);
                            }, error -> {
                                return;
                            });
                }, error -> {
                    return;
                });
    }

    public void initRequestLocationFromAddress(Address address) {
        requestLocation.setCountry(address.getCountryName());
        if (address.getLocality() != null) {
            requestLocation.setCity(address.getLocality());
        }
        else {
            if (address.getAdminArea() != null) {
                requestLocation.setCity(address.getAdminArea());
            }
        }
        requestLocation.setLat(address.getLatitude());
        requestLocation.setLng(address.getLongitude());
    }

    public void setImage(Bitmap bitmap) {
        if (bitmaps == null) return;
        bitmaps.add(0, bitmap);
        if (photoReviewAdapter == null) return;
        photoReviewAdapter.setBitmaps(bitmaps);
        photoReviewAdapter.notifyDataSetChanged();
    }

    public Intent makePhotoIntent(String title, Context ctx) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        Intent chooser = Intent.createChooser(galleryIntent, title);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent[] extraIntents = {takePictureIntent};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        return chooser;
    }

    public void uploadReview(ArrayList<ResponsePhotoUrl> responsePhotoUrls) {
        RequestReview requestReview = new RequestReview();
        List<String> strings = new ArrayList<>();
        if (responsePhotoUrls != null && responsePhotoUrls.size() > 0) {
            for(int i = 0; i < responsePhotoUrls.size(); i++) {
                if (responsePhotoUrls.get(i).getPath() != null) {
                    strings.add(responsePhotoUrls.get(i).getPath());
                }
            }
            requestReview.setImages(strings);
        }
        requestReview.setBrandId(getActivity().getIntent().getStringExtra(WriteReviewActivity.BRANDID));
        requestReview.setText(reviewText.getText().toString());
        requestReview.setRate(seekBar.getProgress() + "");
        if (requestReview.getImages() != null && requestReview.getImages().size() > 0) {
            if (requestReview.getText().equals("")) {
                Utils.showAlertDialog(getContext(), (dialog, which) -> {
                }, (dialog1, which1) -> {
                }, getString(R.string.error), getString(R.string.text_field_is_required), true, false);
                return;
            }
            else
                presenterWriteReview.getReview(requestReview);
        }
        else
            presenterWriteReview.getReview(requestReview);
    }
}