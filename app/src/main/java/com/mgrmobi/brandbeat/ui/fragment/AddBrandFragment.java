package com.mgrmobi.brandbeat.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.request.RequestBrand;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.network.responce.ResponsePhotoUrl;
import com.mgrmobi.brandbeat.presenter.PresenterAddBrand;
import com.mgrmobi.brandbeat.ui.base.ContainerAddBrend;
import com.mgrmobi.brandbeat.utils.UriBuilder;
import com.mgrmobi.brandbeat.utils.UserDataUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
@SuppressLint("ValidFragment")
public class AddBrandFragment extends Fragment {
    @Bind(R.id.category_spinner)
    public Spinner category;
    @Bind(R.id.sub_category_spinner)
    public Spinner subCategory;
    @Bind(R.id.add_brand_picture)
    public SimpleDraweeView logoBrand;
    @Bind(R.id.input_name)
    public EditText name;
    @Bind(R.id.summary)
    public EditText description;
    @Bind(R.id.til_name)
    public TextInputLayout tilName;
    @Bind(R.id.till_summary)
    public TextInputLayout tillSummary;

    private ArrayList<ResponseCategories> responseCategories = new ArrayList<>();
    private ArrayList<ResponseCategories> subCategories = new ArrayList<>();
    private ContainerAddBrend containerAddBrend;
    private PresenterAddBrand presenterBrandView = new PresenterAddBrand();
    private String photoUrl = "";
    private RequestBrand requestBrand;
    private boolean needSave = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_brand_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        if (getActivity() instanceof ContainerAddBrend) {
            containerAddBrend = (ContainerAddBrend) getActivity();
            presenterBrandView.setView(containerAddBrend);
        }
        presenterBrandView.getCategories();
    }

    public void setSubCategory(ArrayList<ResponseCategories> subCategory) {
        subCategories = subCategory;
        ArrayList<String> strings = new ArrayList<>();
        for(ResponseCategories category : subCategory) {
            strings.add(category.getTitle());
        }
        strings.add(getString(R.string.select_subcategory));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, strings) {
            @Override
            public int getCount() {
                int count = super.getCount();
                if (strings.size() == 1) return 1;
                return count > 0 ? count - 1 : count;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        this.subCategory.setAdapter(adapter);
        this.subCategory.setSelection(strings.size() - 1);
        if (requestBrand != null && requestBrand.getSubcategory() != null) {
            for(int i = 0; i < responseCategories.size(); i++) {
                if (responseCategories.get(i).getId().equals(requestBrand.getSubcategory())) {
                    this.subCategory.setSelection(i);
                }
            }
        }
    }

    public void setCategory(ArrayList<ResponseCategories> categories) {
        responseCategories = categories;
        ArrayList<String> strings = new ArrayList<>();
        for(ResponseCategories category : responseCategories) {
            strings.add(category.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, strings) {
            @Override
            public int getCount() {
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        category.setAdapter(adapter);
        strings.add(getString(R.string.select_category));
        category.setSelection(strings.size() - 1);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (position == strings.size() - 1) return;
                presenterBrandView.getSubCategories(responseCategories.get(position).getId());
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                return;
            }
        });
        if (requestBrand != null && requestBrand.getCategory() != null)
            for(int i = 0; i < categories.size(); i++) {
                if (categories.get(i).equals(requestBrand.getCategory())) {
                    category.setSelection(i);
                }
            }
        else
            setSubCategory(new ArrayList<>());
    }

    @OnClick(R.id.add_brand_picture)
    public void onClick() {
        needSave = true;
        (getActivity()).startActivityForResult(makePhotoIntent(getResources().getString(R.string.image_chooser_title)
                , getActivity()), 1);
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

    public void setImage(Bitmap bitmap) {
        presenterBrandView.uploadPhoto(bitmap);
    }

    public void setResponseImage(ResponsePhotoUrl responseImage) {
        if (photoUrl != null) {
            photoUrl = responseImage.getResponsePhotoUrls().get(0).getPath();
            logoBrand.setImageURI(UriBuilder.createUri(Uri.parse(responseImage.getResponsePhotoUrls().get(0).getPath()), 400 + "", 400 + ""));
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        }
        else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @OnClick(R.id.submit)
    public void onCLick() {
        drawableToBitmap(logoBrand.getDrawable());
        if (requestBrand == null) requestBrand = new RequestBrand();
        boolean haveErorr = false;
        if (name.getText().toString().length() == 0) {
            tilName.setError(getString(R.string.required));
            haveErorr = true;
        }
        if (description.getText().toString().length() == 0) {
            tillSummary.setError(getString(R.string.required));
            haveErorr = true;
        }
        if (responseCategories.size() != category.getSelectedItemId()) {
            requestBrand.setCategory(responseCategories.get((int) category.getSelectedItemId()).getId());
            if (subCategory.getSelectedItemId() != subCategories.size())
                requestBrand.setSubcategory(subCategories.get((int) subCategory.getSelectedItemId()).getId());
            else {
                presenterBrandView.onError(new Throwable(getString(R.string.choose_sub_category)));
                return;
            }
        }
        else {
            presenterBrandView.onError(new Throwable(getString(R.string.choose_category)));
            return;
        }
        if (photoUrl != null) {
            requestBrand.setImage(photoUrl);
        }
        else {
            presenterBrandView.onError(new Throwable(getString(R.string.add_photo_error)));
            return;
        }
        if (haveErorr)
            return;
        requestBrand.setText(description.getText().toString());
        requestBrand.setTitle(name.getText().toString());
        presenterBrandView.createBrand(requestBrand);
    }

    public boolean isChange() {
        if (photoUrl != null) {
            return true;
        }
        if (name.getText().toString().length() > 0) {
            return true;
        }
        if (tillSummary.getEditText().getText().length() > 0) {
            return true;
        }
        if (category.getSelectedItemId() != 0 && responseCategories.size() != category.getSelectedItemId()) {
            return true;
        }
        if (subCategory.getSelectedItemId() == 0 && subCategories.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        UserDataUtils userDataUtils = new UserDataUtils(getContext());
        initView(userDataUtils.getCreateBrand());
        needSave = false;
    }

    @Override
    public void onPause() {
        if (needSave) {
            UserDataUtils userDataUtils = new UserDataUtils(getContext());
            RequestBrand requestBrand = new RequestBrand();
            requestBrand.setText(description.getText().toString());
            requestBrand.setTitle(name.getText().toString());
            if ((int) category.getSelectedItemId() != responseCategories.size())
                requestBrand.setCategory(responseCategories.get((int) category.getSelectedItemId() - 1).getId());
            userDataUtils.saveAddedBrand(requestBrand);
        }
        super.onPause();
    }

    private void initView(RequestBrand requestBrand) {
        name.setText(requestBrand.getTitle());
        description.setText(requestBrand.getText());
        if (responseCategories != null) {
            if (requestBrand.getCategory() != null) {
                for(int i = 0; i < responseCategories.size(); i++) {
                    if (responseCategories.get(i).getId().equals(requestBrand.getCategory())) {
                        category.setSelection(i);
                    }
                }
            }
        }
    }
}
