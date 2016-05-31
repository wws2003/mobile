package com.tbg.simplestvallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.model.dto.SVMoneyQuantity;
import com.tbg.simplestvallet.model.dto.SVSearchResult;
import com.tbg.simplestvallet.util.DateUtil;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by wws2003 on 6/1/16.
 */
public class SVSearchExpandableEntriesListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private SVSearchResult mSearchResult;

    public SVSearchExpandableEntriesListAdapter(Context context) {
        mContext = context;
        mSearchResult = new SVSearchResult();
    }

    public void retrieveMatchedData(List<SVEntry> entries) {
        mSearchResult.clean();
        mSearchResult.consumeData(entries);
        notifyDataSetChanged();
    }

    public void cleanData() {
        mSearchResult.clean();
        notifyDataSetChanged();
    }

    //MARK: Implementing methods
    @Override
    public int getGroupCount() {
        return mSearchResult.getMonthCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<SVEntry> entriesInMonth = mSearchResult.getEntriesInPosition(groupPosition);
        return entriesInMonth == null ? 0 : entriesInMonth.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if(mSearchResult.isDataAvailableAtPosition(groupPosition)) {
            return null;
        }
        return mSearchResult.getEntriesInPosition(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(!mSearchResult.isDataAvailableAtPosition(groupPosition)) {
            return null;
        }
        List<SVEntry> entriesInMonth = mSearchResult.getEntriesInPosition(groupPosition);
        return entriesInMonth == null ? null : entriesInMonth.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_search_entries_header, parent, false);
        }
        renderHeaderView(convertView, groupPosition);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_search_entry_content, parent, false);
        }
        renderContentView(convertView, groupPosition, childPosition);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    //MARK: Private methods
    private void renderHeaderView(View headerView, int groupPosition) {
        if(mSearchResult.isDataAvailableAtPosition(groupPosition)) {

            int[] yearMonth = mSearchResult.getYearMonthInPosition(groupPosition);
            int entriesCount = mSearchResult.getEntriesCountInPosition(groupPosition);
            SVMoneyQuantity amount = mSearchResult.getAmountInPosition(groupPosition);

            String header = String.format(mContext.getResources().getString(R.string.tv_search_entries_header),
                    entriesCount,
                    yearMonth[0],
                    yearMonth[1],
                    amount.getAmount());

            TextView tvHeader = (TextView)headerView.findViewById(R.id.tv_search_entry_header);
            tvHeader.setText(header);

        }
    }

    private void renderContentView(View contentView, int groupPosition, int childPosition) {
        SVEntry entry = (SVEntry) getChild(groupPosition, childPosition);
        if(entry != null) {
            String content = String.format(mContext.getResources().getString(R.string.tv_search_entries_content),
                    getText(entry.getType()),
                    DateUtil.getYMDString(entry.getCreatedAt()),
                    entry.getMoneyQuantity().getAmount(),
                    getText(entry.getNote()));

            TextView tvContent = (TextView)contentView.findViewById(R.id.tv_search_entry_content);
            tvContent.setText(content);
        }
    }

    @Nonnull
    private String getText(String str) {
        return str != null ? str : "";
    }
}
