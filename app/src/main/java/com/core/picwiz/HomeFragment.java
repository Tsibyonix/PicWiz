package com.core.picwiz;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerViewHomeFragment;
    private HomeRecyclerViewAdapter homeRecyclerViewAdapter;
    private List<HomeRecyclerList> homeRecyclerLists;

    public static HomeFragment newInstance(int sectionNumber) {
        return new HomeFragment();
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeRecyclerLists = new ArrayList<>();
        homeRecyclerLists.add(new HomeRecyclerList("chronix", "To infinity and beyond", "2 hour", "#these #are #the #tags", R.drawable.im_header, "This is the universal caption xD"));
        homeRecyclerLists.add(new HomeRecyclerList("d4n73", "sigh its difficult to code", "3 hour", "#these #are #the #tags", R.drawable.im_header, "This is the universal caption xD"));
        homeRecyclerLists.add(new HomeRecyclerList("07", "bam bam", "4 hour", "#these #are #the #tags", R.drawable.im_header, "This is the universal caption xD"));
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerViewHomeFragment = (RecyclerView) view.findViewById(R.id.recycler_view_home_fragment);
        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(getActivity(), homeRecyclerLists);
        mRecyclerViewHomeFragment.setAdapter(homeRecyclerViewAdapter);
        mRecyclerViewHomeFragment.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}
