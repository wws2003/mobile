package com.tbg.simplestvallet.view;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.adapter.SVSearchExpandableEntriesListAdapter;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.util.List;

/**
 * Created by wws2003 on 6/1/16.
 */
public class SVExpSearchViewWrapper {

    private Context mContext;
    private SVSearchExpandableEntriesListAdapter mAdapter;

    private TextView mTvSearchSummary;

    public SVExpSearchViewWrapper(Context context,
                                  ExpandableListView expLvEntries,
                                  TextView tvSearchSummary) {
        mContext = context;
        mAdapter = new SVSearchExpandableEntriesListAdapter(mContext);
        expLvEntries.setAdapter(mAdapter);

        mTvSearchSummary = tvSearchSummary;
    }

    public void hideSearchSummary() {
        mTvSearchSummary.setVisibility(View.GONE);
    }

    public void renderEntriesList(String query, List<SVEntry> matchedEntries) {
        String searchSummary = String.format(mContext.getResources().getString(R.string.tv_search_summary),
                matchedEntries.size(),
                query,
                getSumAmount(matchedEntries));

        mTvSearchSummary.setText(searchSummary);
        mTvSearchSummary.setVisibility(View.VISIBLE);

        mAdapter.retrieveMatchedData(matchedEntries);
    }

    public void renderError(int errorCode) {
        String searchSummary = String.format(mContext.getResources().getString(R.string.tv_search_summary_error), errorCode);
        mTvSearchSummary.setText(searchSummary);
        mTvSearchSummary.setVisibility(View.VISIBLE);
    }

    //MARK: Private methods
    private float getSumAmount(List<SVEntry> entries) {
        SVMoneyQuantity amount = new SVMoneyQuantity((float)0.0);
        for(SVEntry entry : entries) {
            amount = amount.add(entry.getMoneyQuantity());
        }
        return amount.getAmount();
    }
}
