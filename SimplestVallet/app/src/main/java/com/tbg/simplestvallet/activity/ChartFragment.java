package com.tbg.simplestvallet.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.app.SimplestValetApp;
import com.tbg.simplestvallet.model.active.abstr.IEntrySheet;


public class ChartFragment extends Fragment {

    private TextView mTvSpentAmount;

    public ChartFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    @Override
    public void onResume() {
        super.onResume();
        renderView();
    }

    private void setupView() {
        mTvSpentAmount = (TextView)getActivity().findViewById(R.id.tv_spent_amount);
    }

    private void renderView() {
        IEntrySheet entrySheet = SimplestValetApp.getEntryCollectionContainer().getEntrySheet();
        mTvSpentAmount.setText(String.valueOf(entrySheet.getCachedAllEntriesAmount()));
    }
}
