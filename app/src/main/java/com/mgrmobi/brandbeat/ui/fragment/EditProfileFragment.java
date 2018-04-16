package com.mgrmobi.brandbeat.ui.fragment;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mgrmobi.brandbeat.BuildConfig;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.application.BrandBeatApplication;
import com.mgrmobi.brandbeat.location.bean.LocationBean;
import com.mgrmobi.brandbeat.location.impl.RxLocationBeanImpl;
import com.mgrmobi.brandbeat.network.request.RequestLocation;
import com.mgrmobi.brandbeat.network.request.RequestUpdateProfile;
import com.mgrmobi.brandbeat.network.responce.ResponseAddress;
import com.mgrmobi.brandbeat.network.responce.ResponseIncomeRange;
import com.mgrmobi.brandbeat.network.responce.ResponseProfile;
import com.mgrmobi.brandbeat.network.responce.enums.PhotoSize;
import com.mgrmobi.brandbeat.presenter.PresenterEditProfile;
import com.mgrmobi.brandbeat.ui.activity.ChangePasswordActivity;
import com.mgrmobi.brandbeat.ui.activity.EditProfileActivity;
import com.mgrmobi.brandbeat.ui.base.ContainerEditProfile;
import com.mgrmobi.brandbeat.ui.dialog.ChooseDate;
import com.mgrmobi.brandbeat.ui.dialog.DateDialog;
import com.mgrmobi.brandbeat.utils.UserDataUtils;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import rx.Observable;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class EditProfileFragment extends Fragment {
    private ContainerEditProfile containerEditProfile;
    private PresenterEditProfile presenterEditProfile = new PresenterEditProfile();
    @Inject
    LocationBean locationBean = new RxLocationBeanImpl(BrandBeatApplication.getInstance().getApplicationContext());

    @Bind(R.id.input_name)
    public EditText name;
    @Bind(R.id.input_email)
    public EditText email;
    @Bind(R.id.input_location)
    public AutoCompleteTextView location;
    @Bind(R.id.input_gender)
    public Spinner gender;
    @Bind(R.id.input_dob)
    public TextView dob;
    @Bind(R.id.input_phone)
    public EditText addPhone;
    @Bind(R.id.input_income)
    public Spinner addIncome;
    @Bind(R.id.update_profile)
    public Button update;
    @Bind(R.id.till_name)
    public TextInputLayout tillName;
    @Bind(R.id.til_email)
    public TextInputLayout tillEmail;
    @Bind(R.id.password_layout)
    public View passwordLayout;
    @Bind(R.id.input_login)
    public EditText login;
    @Bind(R.id.til_login)
    public TextInputLayout tillLogin;
    @Bind(R.id.input_surname)
    public EditText surname;
    @Bind(R.id.til_surname)
    public TextInputLayout tillSurname;
    @Bind(R.id.delete_photo)
    public View deletePhoto;
    @Bind(R.id.scroll_view)
    public ScrollView scrollView;
    private List<ResponseIncomeRange> responseIncomeRanges;
    private ResponseProfile responseProfile;
    private boolean change = false;
    private boolean inComeZoneChange = false;
    private String locationName;

    Location currentLocation;

    @OnTextChanged(R.id.input_name)
    public void changeName() {
        if (responseProfile.getFirstName() != null && !name.getText().toString().equals(responseProfile.getFirstName())) {
            change = true;
        }
        if (responseProfile.getFirstName() == null && !name.getText().toString().equals("")) {
            change = true;
        }
    }

    @OnTextChanged(R.id.input_email)
    public void changeEmail() {
        if (responseProfile.getEmail() != null && !email.getText().toString().equals(responseProfile.getEmail()))
            change = true;
    }

    @OnTextChanged(R.id.input_phone)
    public void changePhone() {
        if (responseProfile.getPhone() == null && !addPhone.getText().toString().equals("")) {
            change = true;
        }
        if (responseProfile.getPhone() != null && !addPhone.getText().toString().equals(responseProfile.getPhone()))
            change = true;
    }

    @OnTextChanged(R.id.input_surname)
    public void changeSurname() {
        if (responseProfile.getLastName() != null && !surname.getText().toString().equals(responseProfile.getLastName())) {
            change = true;
        }
        if (responseProfile.getLastName() == null && !surname.getText().toString().equals("")) {
            change = true;
        }
    }

    @OnTextChanged(R.id.input_login)
    public void changeLogin() {
        if (!login.getText().toString().equals(responseProfile.getUsername())) {
            change = true;
        }
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(final boolean change) {
        this.change = change;
    }

    private Calendar dateInMill;
    private RequestLocation requestLocation = new RequestLocation();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View rootView = inflater.inflate(R.layout.edit_profile_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerEditProfile) {
            containerEditProfile = (ContainerEditProfile) getActivity();
            presenterEditProfile.setView(containerEditProfile);
        }
        ButterKnife.bind(this, view);
        ResponseProfile responseProfile = new ResponseProfile();
        responseProfile = (ResponseProfile) getActivity().getIntent().getSerializableExtra(EditProfileActivity.PROFILE);
        initViews(responseProfile);
        String address = "any";
        if (responseProfile.getResponseAddress() != null && responseProfile.getResponseAddress().getCountry() != null)
            address = responseProfile.getResponseAddress().getCountry();
        presenterEditProfile.getIncomeRangeCurrentLocation(address);
        initLocation();
    }

    private void initViews(ResponseProfile responseProfile) {
        this.responseProfile = responseProfile;
        name.setText(responseProfile.getFirstName());
        email.setText(responseProfile.getEmail());
        surname.setText(responseProfile.getLastName());
        login.setText(responseProfile.getUsername());
        locationName = responseProfile.getResponseAddress().getString();
        if (responseProfile.getResponseAddress() != null) {
            location.setText(responseProfile.getResponseAddress().getString());
            locationName = location.getText().toString();
        }
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for(int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                if (login.getText().length() > 34)
                    return "";
                return null;
            }
        };
        login.setFilters(new InputFilter[]{filter});
        String[] data = getActivity().getResources().getStringArray(R.array.genders);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data) {
            @Override
            public int getCount() {
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        Date date = new Date();
        if (responseProfile.getDob() != null) {
            date.setTime(Long.parseLong(responseProfile.getDob()));
            dob.setText(Utils.formatTextDate.format(date.getTime()));
        }
        if (responseProfile.getGender() != null && responseProfile.getGender().equals("1")) {
            gender.setSelection(0);
        }
        else {
            gender.setSelection(1);
        }
        gender.setOnTouchListener((v, event) -> {
            change = true;
            return false;
        });
        if (responseProfile.getPhone() != null && !responseProfile.getPhone().equals("")) {
            addPhone.setText("+" + responseProfile.getPhone());
        }
        update.setOnClickListener(v -> {
            if (!location.getText().equals(requestLocation.getAddressString()) && !location.getText().toString().equals("")) {
                locationBean.getAddressByName(location.getText().toString())
                        .subscribe(address -> {
                            initRequestLocationFromAddress(address);
                            updateUser(responseProfile);
                        }, error -> {
                            Utils.showAlertDialog(getContext(), (dialog, which) -> {
                                        updateUser(responseProfile);
                                        dialog.dismiss();
                                    },
                                    (dialog1, which1) -> {
                                        dialog1.dismiss();
                                    },
                                    getString(R.string.error), getString(R.string.dont_response_location),
                                    true, true);
                        });
            }
            else {
                updateUser(responseProfile);
            }
        });
        addPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (addPhone.getText().toString().length() != 0 &&
                        !addPhone.getText().toString().substring(0, 1).equals("+")) {
                    addPhone.setText("+" + s);
                    addPhone.setSelection(addPhone.getText().length());
                }
            }
        });
        if (responseProfile.getPhoto(PhotoSize.SIZE_SMALL, getContext()) == null || responseProfile.getPhoto(PhotoSize.SIZE_SMALL, getContext()).equals(""))
            deletePhoto.setVisibility(View.GONE);
    }

    @OnClick(R.id.delete_photo)
    public void deletePhotoClick() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.clearMemoryCaches();
        imagePipeline.clearDiskCaches();
        imagePipeline.clearCaches();
        if (!responseProfile.getPhotoSave().contains(BuildConfig.REST_API_ENDPOINT)) {
            RequestUpdateProfile r = new RequestUpdateProfile();
            r.setPhoto("");
            presenterEditProfile.updateProfile(r);
        }
        else if (responseProfile.getPhotoSave() != null && !responseProfile.getPhotoSave().equals(""))
            presenterEditProfile.deleteUserFile(responseProfile.getPhotoSave());
    }

    @OnClick(R.id.input_dob)
    public void onClick() {
        DateDialog dateDialog = new DateDialog(new ChooseDate() {
            @Override
            public void dateChoose(final Calendar date) {
                dob.setText(Utils.formatTextDate.format(date.getTime()));
                dateInMill = date;
                change = true;
            }
        });
        dateDialog.show(getFragmentManager(), dateDialog.getClass().getName());
    }

    public void setProfile() {
        change = false;
        UserDataUtils userDataUtils = new UserDataUtils(getActivity());
        userDataUtils.saveData(UserDataUtils.USER_EMAIL, String.valueOf(email.getText()));
        userDataUtils.saveData(UserDataUtils.USER_NAME, login.getText().toString());
        Utils.showAlertDialog(getActivity(), (dialog, which) -> {
            getActivity().finish();
        }, (dialog1, which1) -> {
        }, getString(R.string.success), getString(R.string.profile_change_suc), true, false);
    }

    @OnClick(R.id.password_layout)
    public void changePasswordClick() {
        startActivity(ChangePasswordActivity.createIntent(getActivity()));
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
        requestLocation.setCountryCode(address.getCountryCode());
        requestLocation.setLng(address.getLongitude());
    }

    private void updateUser(ResponseProfile responseProfile) {
        boolean isError = false;
        if (email.getText().toString().length() == 0) {
            tillEmail.setError(getString(R.string.required));
            isError = true;
        }

        if (name.getText().toString().length() == 0) {
            tillName.setError(getString(R.string.required));
            isError = true;
        }
        if (surname.getText().toString().length() == 0) {
            tillSurname.setError(getString(R.string.required));
            isError = true;
        }
        if (login.getText().length() == 0) {
            tillLogin.setError(getString(R.string.required));
            isError = true;
        }
        RequestUpdateProfile requestUpdateProfile = new RequestUpdateProfile();
        if (dateInMill != null)
            requestUpdateProfile.setDob(dateInMill.getTime().getTime() + "");
        if (addPhone.getText().toString().length() != 0)
            if (addPhone.getText().toString().length() < 7) {
                Utils.showAlertDialog(getContext(), (dialog, which) -> {
                }, (dialog1, which1) -> {
                }, getString(R.string.error), getString(R.string.length_phone_number), true, false);
                return;
            }
        requestUpdateProfile.setEmail(email.getText().toString());
        requestUpdateProfile.setGender(gender.getSelectedItemPosition() + 1 + "");
        if (addIncome.getSelectedItem() != null)
            requestUpdateProfile.setIncome(addIncome.getSelectedItem().toString());
        if (addPhone.getText().length() != 0)
            requestUpdateProfile.setPhone(addPhone.getText().toString().substring(1));
        else
            requestUpdateProfile.setPhone("");
        requestUpdateProfile.setUserName(login.getText().toString());
        requestUpdateProfile.setLastName(surname.getText().toString());
        requestUpdateProfile.setFirstName(name.getText().toString());
        //   requestUpdateProfile.setPhoto(responseProfile.getPhoto(PhotoSize.SIZE_SMALL, getContext()));
        ResponseAddress responseAddress = new ResponseAddress();
        responseAddress.setCity(requestLocation.getCity());
        responseAddress.setCountry(requestLocation.getCountry());
        responseAddress.setCountryCode(requestLocation.getCountryCode());
        responseAddress.setLat(requestLocation.getLat() + "");
        responseAddress.setLng(requestLocation.getLng() + "");
        requestUpdateProfile.setRequestLocation(responseAddress);
        if (isError) {
            scrollView.fullScroll(ScrollView.FOCUS_UP);
            return;
        }
        presenterEditProfile.updateProfile(requestUpdateProfile);
        change = false;

    }
    @OnTextChanged(R.id.input_location)
    public void locationChange() {
        if(location.getText().toString().length() < locationName.length())
        {
            locationName = location.getText().toString();
            return;
        }

        if (!inComeZoneChange) {
            inComeZoneChange = true;
            List<Integer> integers = new ArrayList<>();
            integers.add(AutocompleteFilter.TYPE_FILTER_CITIES);
            locationBean.getListAddress(location.getText().toString()).subscribe(addresses ->
            {
                List<String> strings = new ArrayList<String>();
                for(int i = 0; i < addresses.size(); i++) {
                    strings.add(addresses.get(i).getCountryName() + " " +addresses.get(i).getLocality());
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, strings);
                location.setAdapter(adapter);
                adapter.notifyDataSetChanged();
           //     presenterEditProfile.getIncomeRangeCurrentLocation(addresses.get(0).getCountryName().toLowerCase());
                inComeZoneChange = false;
            }, error -> {
                inComeZoneChange = false;
            });
        }
    }

    public void setIncomeRange(List<ResponseIncomeRange> incomeRange) {
        if (incomeRange.size() == 0) return;
        this.responseIncomeRanges = incomeRange;
        ArrayList<String> strings = new ArrayList<>();
        for(int i = 0; i < incomeRange.size(); i++) {
            if (responseProfile.getResponseAddress() == null || responseProfile.getResponseAddress().getCountry() == null)
                break;
            if (incomeRange.get(i).getCountry().equals(responseProfile.getResponseAddress().getCountry())) {
                strings = incomeRange.get(i).getRanges();
                break;
            }
        }
        if (strings.size() == 0) {
            strings = incomeRange.get(incomeRange.size() - 1).getRanges();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, strings) {
            @Override
            public int getCount() {
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }
        };
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        addIncome.setAdapter(adapter);
        strings.add(getString(R.string.add_income));
        addIncome.setSelection(strings.size() - 1);
        final ArrayList<String> finalStrings = strings;
        addIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (position == finalStrings.size() - 1) return;
                //     presenterBrandView.getSubCategories(responseCategories.get(position).getId());
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
                return;
            }
        });
        for(int i = 0; i < strings.size(); i++) {
            if (strings.get(i).equals(responseProfile.getIncomeRange())) {
                addIncome.setSelection(i);
                break;
            }
        }
        inComeZoneChange = false;
        //    setSubCategory(new ArrayList<>());
    }
}