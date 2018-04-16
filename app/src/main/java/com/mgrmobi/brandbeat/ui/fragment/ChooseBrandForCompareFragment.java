package com.mgrmobi.brandbeat.ui.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.presenter.PresenterSubCategoryBrands;
import com.mgrmobi.brandbeat.ui.activity.ChooseBradsForCompareActivity;
import com.mgrmobi.brandbeat.ui.activity.CompareActivity;
import com.mgrmobi.brandbeat.ui.adapters.ChooseBrandsForCompareAdapter;
import com.mgrmobi.brandbeat.ui.adapters.CompareBottomAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerRecentBrands;
import com.mgrmobi.brandbeat.ui.callbacks.CompareBrandCallBack;
import com.mgrmobi.brandbeat.ui.callbacks.MoveViewCallback;
import com.mgrmobi.brandbeat.ui.widget.recycleViewSwipeButton.DragListener;
import com.mgrmobi.brandbeat.ui.widget.recycleViewSwipeButton.SimpleItemTouchHelperCallback;
import com.mgrmobi.brandbeat.ui.widget.recycleViewSwipeButton.TouchHelper;
import com.mgrmobi.brandbeat.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class ChooseBrandForCompareFragment extends Fragment implements DragListener {
    private TouchHelper mItemTouchHelper;

    @Bind(R.id.recycle_top)
    public RecyclerView recycleChooseBrand;
    @Bind(R.id.recycle_bottom)
    public RecyclerView bottomCompareRecyclerView;
    @Bind(R.id.progress_wheel)
    public View progress;
    @Bind(R.id.root_view)
    public RelativeLayout relativeLayout;

    private ArrayList<ResponseBrand> responseBrands;
    private CompareBottomAdapter compareBottomAdapter;
    private ChooseBrandsForCompareAdapter adapter;
    private PresenterSubCategoryBrands presenterSubCategoryBrands = new PresenterSubCategoryBrands();
    private String id;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_brand_for_compare_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        if (getActivity() instanceof ContainerRecentBrands) {
            presenterSubCategoryBrands.setView((ContainerRecentBrands) getActivity());
        }
        id = getActivity().getIntent().getStringExtra(ChooseBradsForCompareActivity.SUBCATEGORY_ID);
        presenterSubCategoryBrands.getSubCategoryBrand(id);
    }

    @Override
    public void onDismiss(final ResponseBrand responseBrand, int[] coordinats) {
        if (compareBottomAdapter.getItemCount() >= getResources().getInteger(R.integer.number_of_compare_brands)) {
            ResponseBrand responseBrand1 = compareBottomAdapter.getBrands().get(compareBottomAdapter.getItemCount() - 1);
            adapter.addBrand(responseBrand1, adapter.getItemCount());
            compareBottomAdapter.getBrands().remove(getResources().getInteger(R.integer.number_of_compare_brands) - 1);
            compareBottomAdapter.notifyItemRemoved(getResources().getInteger(R.integer.number_of_compare_brands));
            compareBottomAdapter.addBrand(responseBrand, currentBottomViewPosition + 1);
        }
        else {
            if (currentBottomViewPosition < 0) {
                compareBottomAdapter.addBrand(responseBrand, 1);
            }
            else {
                compareBottomAdapter.addBrand(responseBrand, currentBottomViewPosition + 1);
            }
        }
    }

    View view;
    private float viewx;
    private String idBrandCannotDeleted = "";
    private View itemView;

    public void setBrands(ArrayList<ResponseBrand> brands) {
        progress.setVisibility(View.GONE);
        if (brands.size() == 0) {
            Utils.showAlertDialog(getContext(), (dialog, which) -> {
                getActivity().finish();
            }, (dialog1, which1) -> {
            }, getString(R.string.warning), getString(R.string.no_brands_in_sub_category), true, false);
            return;
        }
        ResponseBrand selectedBrand = new ResponseBrand();
        idBrandCannotDeleted = getActivity().getIntent().getStringExtra(ChooseBradsForCompareActivity.BRAND_ID);
        if (idBrandCannotDeleted != null)
            for(int i = 0; i < brands.size(); i++) {
                if (brands.get(i).getId().equals(idBrandCannotDeleted)) {
                    selectedBrand = brands.get(i);
                    brands.remove(i);
                }
            }
        recycleChooseBrand.setVisibility(View.VISIBLE);
        if (responseBrands == null) {
            responseBrands = brands;
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            recycleChooseBrand.setItemAnimator(itemAnimator);
            adapter = new ChooseBrandsForCompareAdapter(getActivity(), this, responseBrands, new MoveViewCallback() {
                @Override
                public boolean callback(final int[] ints, View moveView, MotionEvent event, int position, RecyclerView.ViewHolder viewHolder) {
                    if (view != null) {
                        view.setVisibility(View.GONE);
                        viewHolder.itemView.setVisibility(View.VISIBLE);
                        view = null;
                    }
                    if (view == null) {
                        Log.e("go", "here");
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        view = inflater.inflate(R.layout.compare_brand_item, relativeLayout, false);

                        relativeLayout.addView(view);
                        ChooseBrandsForCompareAdapter.ItemViewHolder itemViewHolder = new ChooseBrandsForCompareAdapter.ItemViewHolder(view);
                        itemViewHolder.setValue(adapter.getBrands().get(position), getActivity());
                        viewHolder.itemView.setVisibility(View.GONE);
                    }
                    else {
                        recycleChooseBrand.onTouchEvent(event);
                        return true;
                    }

                    viewx = (int) view.getX();
                    recycleChooseBrand.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(final View v, final MotionEvent event) {
                            if (view == null) return false;
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_CANCEL:
                                    view.setVisibility(View.GONE);
                                    view = null;
                                    return true;
                                case MotionEvent.ACTION_SCROLL:
                                    return true;
                                case MotionEvent.ACTION_MOVE:
                                    Log.e("asdsa", "action_move");
                                    view.setY((int) event.getRawY() - view.getHeight());
                                    view.setX((int) event.getRawX() - (view.getWidth() / 2));
                                    if (viewx == 0) {
                                        viewx = Utils.convertDpToPixel(200, getContext());
                                    }
                                    else {
                                        if (Math.abs(viewx / view.getY() - 0.2) < 1 && Math.abs(viewx / view.getY() - 0.2) > 0.3) {
                                            view.setScaleX((float) Math.abs(viewx / view.getY() - 0.2));
                                            view.setScaleY((float) Math.abs(viewx / view.getY() - 0.2));
                                        }
                                    }
                                    addBottomPosition((int) event.getRawX());
                                    break;

                                case MotionEvent.ACTION_UP:
                                    Log.e("asdsa", "action_up");
                                    view.getLocationOnScreen(ints);
                                    if (ints[1] > Resources.getSystem().getDisplayMetrics().heightPixels - Utils.convertDpToPixel(300, getActivity())) {
                                        adapter.onItemDismiss(position, ints);
                                    }
                                    deleteAllNullBrands();
                                    view.destroyDrawingCache();
                                    view.setAlpha(0f);
                                    view.setVisibility(View.GONE);
                                    viewHolder.itemView.setVisibility(View.VISIBLE);
                                    view = null;
                                    break;
                                case MotionEvent.ACTION_DOWN:
                                    Log.e("asdsa", "action_down");
                                    if (view != null) {
                                        view.destroyDrawingCache();
                                        view.setAlpha(0f);
                                        view.setVisibility(View.GONE);
                                        viewHolder.itemView.setVisibility(View.VISIBLE);
                                        view = null;
                                    }
                                    break;
                                default:
                                    view = null;
                            }
                            return true;
                        }
                    });
                    return true;
                }
            });

            recycleChooseBrand.setAdapter(adapter);

            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recycleChooseBrand.setLayoutManager(layoutManager);

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
            mItemTouchHelper = new TouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(recycleChooseBrand);

            List<ResponseBrand> brands1 = new ArrayList<>();
            if (selectedBrand.getId() != null)
                brands1.add(selectedBrand);
            compareBottomAdapter = new CompareBottomAdapter(new CompareBrandCallBack() {
                @Override
                public void deleteFromButtom(final ResponseBrand brand, int position) {
                    if (idBrandCannotDeleted.equals(brand.getId())) return;
                    adapter.addBrand(brand, adapter.getItemCount());
                    compareBottomAdapter.getBrands().remove(position);
                    compareBottomAdapter.notifyItemRemoved(position);
                }
            }, brands1, getActivity());
            LinearLayoutManager layoutManager2
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            bottomCompareRecyclerView.setLayoutManager(layoutManager2);
            RecyclerView.ItemAnimator itemAnimator2 = new DefaultItemAnimator();
            bottomCompareRecyclerView.setItemAnimator(itemAnimator2);
            bottomCompareRecyclerView.setAdapter(compareBottomAdapter);
        }
        else {
            ArrayList<ResponseBrand> showBrands = (ArrayList<ResponseBrand>) compareBottomAdapter.getBrands();
            for(int i = 0; i < brands.size(); i++) {
                ResponseBrand brand = brands.get(i);
                for(ResponseBrand chossenBrand : showBrands) {
                    if (chossenBrand.getId().equals(brand.getId())) {
                        brands.remove(i);
                        i--;
                        break;
                    }
                }
            }
            responseBrands = brands;
            adapter.setBrands(responseBrands);
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.go_compare_view)
    public void onClick() {
        if (compareBottomAdapter.getBrands().size() > 1)
            startActivity(CompareActivity.callingIntent(getActivity(), (ArrayList<ResponseBrand>) compareBottomAdapter.getBrands()));
        else
            Utils.showAlertDialog(getContext(), (dialog, which) -> {
                    }, (dialog1, which1) -> {
                    }, getString(R.string.error),
                    getString(R.string.please_choose_two_brands), true, false);
    }

    private int currentBottomViewPosition = -1;

    private void addBottomPosition(int xPosition) {
        int width = (int) (getResources().getDimension(R.dimen.brad_image_for_adapter_width));
        int halfWidth = (int) (getResources().getDimension(R.dimen.brad_image_for_adapter_width) / 2);
        if (xPosition > 0 && xPosition < halfWidth && currentBottomViewPosition != 0) {
            for(int i = 0; i < compareBottomAdapter.getItemCount(); i++) {
                if (compareBottomAdapter.getBrands().get(i) == null) {
                    compareBottomAdapter.getBrands().remove(i);
                    compareBottomAdapter.notifyItemRemoved(i);
                }
            }
            compareBottomAdapter.addBrand(null, 2);
            currentBottomViewPosition = 1;
        }

        if (compareBottomAdapter.getItemCount() > 0 && xPosition > halfWidth && xPosition < width + halfWidth && currentBottomViewPosition != 1) {
            for(int i = 0; i < compareBottomAdapter.getItemCount(); i++) {
                if (compareBottomAdapter.getBrands().get(i) == null) {
                    compareBottomAdapter.getBrands().remove(i);
                    compareBottomAdapter.notifyItemRemoved(i);
                }
            }
            compareBottomAdapter.addBrand(null, 2);
            currentBottomViewPosition = 1;
        }

        if (compareBottomAdapter.getItemCount() > 1 && xPosition > width + halfWidth && xPosition < width * 2 + halfWidth && currentBottomViewPosition != 2) {
            for(int i = 0; i < compareBottomAdapter.getItemCount(); i++) {
                if (compareBottomAdapter.getBrands().get(i) == null) {
                    compareBottomAdapter.getBrands().remove(i);
                    compareBottomAdapter.notifyItemRemoved(i);
                }
            }
            compareBottomAdapter.addBrand(null, 3);
            currentBottomViewPosition = 2;
        }
        if (compareBottomAdapter.getItemCount() > 1 && xPosition > width + halfWidth * 2 && xPosition < width * 3 + halfWidth && currentBottomViewPosition != 3) {
            for(int i = 0; i < compareBottomAdapter.getItemCount(); i++) {
                if (compareBottomAdapter.getBrands().get(i) == null) {
                    compareBottomAdapter.getBrands().remove(i);
                    compareBottomAdapter.notifyItemRemoved(i);
                }
            }
            compareBottomAdapter.addBrand(null, 3);
            currentBottomViewPosition = 3;
        }
    }

    private void deleteAllNullBrands() {
        for(int i = 0; i < compareBottomAdapter.getItemCount(); i++) {
            if (compareBottomAdapter.getBrands().get(i) == null) {
                compareBottomAdapter.getBrands().remove(i);
                compareBottomAdapter.notifyItemRemoved(i);
            }
        }
    }

    public void setSearchString(String searchString) {
        recycleChooseBrand.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        presenterSubCategoryBrands.getSearchResult(searchString, id);
    }

    public void setCancelClick() {
        presenterSubCategoryBrands.getSubCategoryBrand(id);
    }
}
