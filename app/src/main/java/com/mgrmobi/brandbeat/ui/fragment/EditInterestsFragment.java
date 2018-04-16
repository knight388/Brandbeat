package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.entity.Interests;
import com.mgrmobi.brandbeat.network.responce.ResponseCategories;
import com.mgrmobi.brandbeat.presenter.PresenterInterestsView;
import com.mgrmobi.brandbeat.ui.adapters.EditInterestsAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerInterests;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class EditInterestsFragment extends Fragment {

    private RecyclerView gridView;
    private PresenterInterestsView presenter = new PresenterInterestsView();
    private ContainerInterests containerInterests;
    private EditInterestsAdapter interestsAdapter;
    private ArrayList<ResponseCategories> interests = new ArrayList<>();

    @Bind(R.id.progress_wheel)
    public View progressView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        View rootView = inflater.inflate(R.layout.recycler_with_progress_layout, container, false);
        gridView = (RecyclerView) rootView.findViewById(R.id.grid_interests);
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.getItem(R.id.action_context).setVisible(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerInterests) {
            containerInterests = (ContainerInterests) getActivity();
            presenter.setView(containerInterests);
        }
        ButterKnife.bind(this, view);
        containerInterests.showProgress();
        presenter.getInterests();
        progressView.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.GONE);
    }

    private void initGrid(ArrayList<ResponseCategories> interestses) {
        gridView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        gridView.setItemAnimator(itemAnimator);
        interestsAdapter = new EditInterestsAdapter(interestses, getActivity(), this);
        gridView.setAdapter(interestsAdapter);
        interestsAdapter.notifyDataSetChanged();
    }

    public void setInterests(ArrayList<ResponseCategories> interests) {
        progressView.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        this.interests = interests;
        initGrid(interests);
    }
    public void uploadNewInterest(ArrayList<Interests> interests) {
        if (interests != null) {
            presenter.setCategories(interests);
        }
        //presenter.setCategories((ArrayList<Interests>) interestsAdapter.getCheckedInterst());
    }
    public void uploadNewInterest(Interests interests) {
        if (interests != null) {
            List<Interests> interestsList = new ArrayList<>();
            interestsList.add(interests);
            presenter.setCategories((ArrayList<Interests>) interestsList);
        }
        //presenter.setCategories((ArrayList<Interests>) interestsAdapter.getCheckedInterst());
    }

    public boolean haveCheckedInterests() {
        if(interestsAdapter == null) return false;
        return interestsAdapter.isChecked();
    }

    public void deleteInterests(List<String> id) {
        presenter.deleteInterest(id);
    }

    public boolean uploadInterests() {
        return interestsAdapter.getCheckedInterst().size() > 0;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean isChange() {
        if(interestsAdapter == null) return false;
        return interestsAdapter.isChange();
    }
}
