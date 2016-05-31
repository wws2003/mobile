package com.tbg.simplestvallet.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.activity.delegate.SVEntrySearchDelegate;

public class SearchFragment extends Fragment {

    private SVEntrySearchDelegate mSearchDelegate = null;

    public SearchFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pendinglist, container, false);
    }

    //MARK: Private methods
    private void setupView() {
        ExpandableListView expLvSearchEntries = (ExpandableListView)getActivity().findViewById(R.id.explv_search_entries);
        TextView tvSearchSummary = (TextView)getActivity().findViewById(R.id.tv_search_summary);
        mSearchDelegate = new SVEntrySearchDelegate(getContext(), expLvSearchEntries, tvSearchSummary);

        SearchView searchView = (SearchView)getActivity().findViewById(R.id.srv_query);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchDelegate.search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
