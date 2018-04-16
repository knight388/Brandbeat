package com.mgrmobi.brandbeat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mgrmobi.brandbeat.R;
import com.mgrmobi.brandbeat.network.responce.ResponseBrand;
import com.mgrmobi.brandbeat.presenter.PresenterSearchView;
import com.mgrmobi.brandbeat.ui.activity.AddBrandActivity;
import com.mgrmobi.brandbeat.ui.adapters.SearchAdapter;
import com.mgrmobi.brandbeat.ui.base.ContainerSearchView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Vasiliy Yakimov
 *         Developed by Magora Team (magora-systems.com). 2016.
 */
public class SearchFragment extends Fragment {
    @Bind(R.id.search_list1)
    public RecyclerView listSearchResults1;
    @Bind(R.id.search_list2)
    public RecyclerView listSearchResults2;
    @Bind(R.id.search_list3)
    public RecyclerView listSearchResults3;
    @Bind(R.id.search_1)
    public Button searchButton1;
    @Bind(R.id.search_2)
    public Button searchButton2;
    @Bind(R.id.search_3)
    public Button searchButton3;

    @Bind(R.id.progress_wheel)
    public View progress;
    @Bind(R.id.no_found_layout)
    public View noSearchView;

    @Bind(R.id.add_brand)
    public View addbrand;

    private SearchAdapter searchAdapter1;
    private SearchAdapter searchAdapter2;
    private SearchAdapter searchAdapter3;
    private PresenterSearchView presenterSearchView1 = new PresenterSearchView();
    private PresenterSearchView presenterSearchView2 = new PresenterSearchView();// add
    private PresenterSearchView presenterSearchView3 = new PresenterSearchView();// add
    public int currentSearchItem;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof ContainerSearchView) {
            presenterSearchView1.setView((ContainerSearchView) getActivity());
        }

        currentSearchItem = 1;
        ButterKnife.bind(this, view);
        presenterSearchView1.getRecentBrand();

        // add
        if (getActivity() instanceof ContainerSearchView) {
            presenterSearchView2.setView((ContainerSearchView) getActivity());
        }
        presenterSearchView2.getRecentBrand();

        if (getActivity() instanceof ContainerSearchView) {
            presenterSearchView3.setView((ContainerSearchView) getActivity());
        }
        presenterSearchView3.getRecentBrand();
        ///

        searchButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentSearchItem = 1;
                noSearchView.setVisibility(View.GONE);
                listSearchResults1.setVisibility(View.VISIBLE);
                listSearchResults2.setVisibility(View.GONE);
                listSearchResults3.setVisibility(View.GONE);
                //progress.setVisibility(View.VISIBLE);
            }
        });

        searchButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentSearchItem = 2;
                noSearchView.setVisibility(View.GONE);
                listSearchResults1.setVisibility(View.GONE);
                listSearchResults2.setVisibility(View.VISIBLE);
                listSearchResults3.setVisibility(View.GONE);
//                progress.setVisibility(View.VISIBLE);
            }
        });

        searchButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentSearchItem = 3;
                noSearchView.setVisibility(View.GONE);
                listSearchResults1.setVisibility(View.GONE);
                listSearchResults2.setVisibility(View.GONE);
                listSearchResults3.setVisibility(View.VISIBLE);
//                progress.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setCancel() {
        listSearchResults1.setVisibility(View.GONE);
        listSearchResults2.setVisibility(View.GONE);
        listSearchResults3.setVisibility(View.GONE);
        noSearchView.setVisibility(View.GONE);
    }

    private void initRecyclerView1(ArrayList<ResponseBrand> brands) {
        listSearchResults1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        listSearchResults1.setItemAnimator(itemAnimator);
        searchAdapter1 = new SearchAdapter(getActivity());
        listSearchResults1.setAdapter(searchAdapter1);
        searchAdapter1.notifyDataSetChanged();
    }
    public void setSearchResult1(ArrayList<ResponseBrand> searchResult) {
        listSearchResults1.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        if (searchResult.size() == 0) {
            listSearchResults1.setVisibility(View.GONE);
            noSearchView.setVisibility(View.VISIBLE);
        }
        initRecyclerView1(searchResult);
        searchAdapter1.setBrands(searchResult);
        searchAdapter1.notifyDataSetChanged();
    }
    public void setSearchString1(String searchString) {
        presenterSearchView1.getSearchResult(searchString);
        listSearchResults1.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        noSearchView.setVisibility(View.GONE);
    }

    @OnClick(R.id.add_brand)
    public void onAddBrandClick() {
        int[] mass = new int[2];
        addbrand.getLocationOnScreen(mass);

        getActivity().startActivity(AddBrandActivity.createIntent(getActivity(), mass[0], mass[1]));
    }

    private void initRecyclerView2(ArrayList<ResponseBrand> brands) {
        listSearchResults2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        listSearchResults2.setItemAnimator(itemAnimator);
        searchAdapter2 = new SearchAdapter(getActivity());
        listSearchResults2.setAdapter(searchAdapter2);
        searchAdapter2.notifyDataSetChanged();
    }
    public void setSearchResult2(ArrayList<ResponseBrand> searchResult) {
        listSearchResults2.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        if (searchResult.size() == 0) {
            listSearchResults2.setVisibility(View.GONE);
            noSearchView.setVisibility(View.VISIBLE);
        }
        initRecyclerView2(searchResult);
        searchAdapter2.setBrands(searchResult);
        searchAdapter2.notifyDataSetChanged();
    }
    public void setSearchString2(String searchString) {
        presenterSearchView2.getSearchResult(searchString);
        listSearchResults2.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        noSearchView.setVisibility(View.GONE);
    }

    private void initRecyclerView3(ArrayList<ResponseBrand> brands) {
        listSearchResults3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        listSearchResults3.setItemAnimator(itemAnimator);
        searchAdapter3 = new SearchAdapter(getActivity());
        listSearchResults3.setAdapter(searchAdapter3);
        searchAdapter3.notifyDataSetChanged();
    }
    public void setSearchResult3(ArrayList<ResponseBrand> searchResult) {
        listSearchResults3.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        if (searchResult.size() == 0) {
            listSearchResults3.setVisibility(View.GONE);
            noSearchView.setVisibility(View.VISIBLE);
        }
        initRecyclerView3(searchResult);
        searchAdapter3.setBrands(searchResult);
        searchAdapter3.notifyDataSetChanged();
    }
    public void setSearchString3(String searchString) {
        presenterSearchView3.getSearchResult(searchString);
        listSearchResults3.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        noSearchView.setVisibility(View.GONE);
    }
}
