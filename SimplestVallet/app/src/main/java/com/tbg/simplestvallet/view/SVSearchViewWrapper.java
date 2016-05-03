package com.tbg.simplestvallet.view;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.adapter.SearchingEntryListAdapter;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wws2003 on 5/4/16.
 */
public class SVSearchViewWrapper {

    private Context mContext;
    private ListView mLvPendingEntries;
    private TextView mTvSearchSummary;

    private List<SVEntry> mPendingEntries = new ArrayList<>();
    private ArrayAdapter<SVEntry> mPendingEntriesAdapter;

    public SVSearchViewWrapper(Context context, ListView lvPendingEntries, TextView tvSearchSummary) {
        mContext = context;
        mLvPendingEntries = lvPendingEntries;
        mTvSearchSummary = tvSearchSummary;
        mPendingEntriesAdapter = new SearchingEntryListAdapter(lvPendingEntries.getContext(), mPendingEntries);
        mLvPendingEntries.setAdapter(mPendingEntriesAdapter);
    }

    public void hideSearchSummary() {
        mTvSearchSummary.setVisibility(View.GONE);
    }

    public void renderPendingEntryList(String query, List<SVEntry> matchedEntries) {
        String searchSummary = String.format(mContext.getResources().getString(R.string.tv_search_summary),
                matchedEntries.size(),
                query,
                getSumAmount(matchedEntries));
        mTvSearchSummary.setText(searchSummary);
        mTvSearchSummary.setVisibility(View.VISIBLE);

        mPendingEntries.clear();
        mPendingEntries.addAll(matchedEntries);
        mPendingEntriesAdapter.notifyDataSetChanged();
    }

    public void renderError(int errorCode) {
        String searchSummary = String.format(mContext.getResources().getString(R.string.tv_search_summary_error), errorCode);
        mTvSearchSummary.setText(searchSummary);
        mTvSearchSummary.setVisibility(View.VISIBLE);
    }

    private float getSumAmount(List<SVEntry> entries) {
        SVMoneyQuantity amount = new SVMoneyQuantity((float)0.0);
        for(SVEntry entry : entries) {
            amount = amount.add(entry.getMoneyQuantity());
        }
        return amount.getAmount();
    }
}
