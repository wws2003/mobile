package com.tbg.simplestvallet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tbg.simplestvallet.R;
import com.tbg.simplestvallet.model.dto.SVEntry;
import com.tbg.simplestvallet.util.DateUtil;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Created by wws2003 on 10/29/15.
 */
public class SVSearchingEntryListAdapter extends ArrayAdapter<SVEntry> {

    private LayoutInflater mLayoutInflater;

    public SVSearchingEntryListAdapter(Context context, List<SVEntry> objects) {
        super(context, 0, objects);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if(itemView == null) {
            itemView = mLayoutInflater.inflate(R.layout.pending_entry, null);
        }
        SVEntry localEntry = getItem(position);
        renderLocalEntry(localEntry, itemView);
        return itemView;
    }

    private void renderLocalEntry(SVEntry entry, View itemView) {
        TextView tvEntryType = (TextView)itemView.findViewById(R.id.tv_item_pending_entry_type);
        tvEntryType.setText(getText(entry.getType()));

        TextView tvEntryDate = (TextView)itemView.findViewById(R.id.tv_item_pending_entry_date);
        tvEntryDate.setText(DateUtil.getYMDString(entry.getCreatedAt()));

        TextView tvEntryAmount = (TextView)itemView.findViewById(R.id.tv_item_pending_entry_amount);
        tvEntryAmount.setText(String.valueOf(entry.getMoneyQuantity().getAmount()));

        TextView tvEntryNote = (TextView)itemView.findViewById(R.id.tv_item_pending_entry_note);
        tvEntryNote.setText(getText(entry.getNote()));
    }

    @Nonnull
    private String getText(String str) {
        return str != null ? str : "";
    }
}
